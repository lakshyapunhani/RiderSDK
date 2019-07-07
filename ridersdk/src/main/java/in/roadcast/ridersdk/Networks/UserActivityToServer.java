package in.roadcast.ridersdk.Networks;

import android.content.Context;
import android.os.Handler;

import java.util.ArrayList;
import java.util.HashMap;

import in.roadcast.ridersdk.Helper.HelperClient;
import in.roadcast.ridersdk.Managers.RcRequestManager;
import in.roadcast.ridersdk.Managers.RealmManager;
import in.roadcast.ridersdk.Managers.SessionManager;
import in.roadcast.ridersdk.Pojo.UserActivitiesRetro;
import in.roadcast.ridersdk.Requests.RequestCallBack;
import in.roadcast.ridersdk.Requests.RequestInterface;
import in.roadcast.ridersdk.Utils.MyConstants;

public class UserActivityToServer
{
    private SessionManager sessionManager;
    private Context context;
    private int counter = 0;
    private ArrayList<UserActivitiesRetro> listTosend;

    public UserActivityToServer(Context context) {
        this.context = context;
        sessionManager = HelperClient.getSessionManager(context);
    }

    private Handler handler = new Handler();
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            if (counter < listTosend.size()) {
                UserActivitiesRetro pojo = listTosend.get(counter);
                sendEventsToServer(pojo);
                counter++;
            } else {
                handler.removeCallbacks(runnable);
            }
        }
    };



    public synchronized void requestUserActivity() {

        listTosend = RealmManager.getInstance(context).getUserActivityListToSend();
        counter = 0;
        handler.postDelayed(runnable, 0);
    }

    private void sendEventsToServer(UserActivitiesRetro pojo) {
//        {
//            "event":"idle",
//                "latitude": 28.7040206,
//                "longitude": 77.1591246,
//                "user_id": "10",
//                "created_on":"2019-05-31 12:23:27"
//        }

        RealmManager.getInstance(context).updateUserActivityStatusToInProgress(pojo.getActivityId());

        HashMap<String,Object> hashMap = new HashMap<>();
        switch (pojo.getActionType()) {
            case MyConstants.ACTION_TYPE_DUTY :
                if (pojo.getActionStatus().equals(MyConstants.ACTION_STATUS_FALSE)) {
                    hashMap.put("event","off_duty");
                } else {
                    hashMap.put("event","on_duty");
                }
                break;
            case MyConstants.ACTION_TYPE_IDLE:
                hashMap.put("event","idle");
                break;
            case MyConstants.ACTION_TYPE_TRIP_COMPLETE :
                hashMap.put("event","tms");
                break;
        }
        hashMap.put("latitude",pojo.getLatitude());
        hashMap.put("longitude",pojo.getLongitude());
        hashMap.put("user_id",sessionManager.getLoginId());
        hashMap.put("created_on",HelperClient.getCommonMethods().convertDate(pojo.getTime(), "yyyy-MM-dd HH:mm:ss", true));

        RcRequestManager.getInstance()
                .updateIdleAndTripCompleteEvent(hashMap, sessionManager.getAccessToken(),
                        HelperClient.getCommonMethods().getRefreshTokenHashMap(context),
                        new RequestCallBack<String>() {
                            @Override
                            public void onSuccess(String response) {
                                RealmManager.getInstance(context).deleteUserActivity(pojo.getActivityId());
                                checkHandlerAndRestart();
                            }
                            @Override
                            public void onFailure(int failureCode) {
                                RealmManager.getInstance(context).updateUserActivityStatusToNotSent(pojo.getActivityId());
                                checkHandlerAndRestart();
                            }
                            @Override
                            public void onTokenRefresh(String token) {
                                if (token.contains(RequestInterface.UNAUTHORIZED_USER)) {
                                   // HelperClient.getCommonMethods().logout(context);
                                } else {
                                    RcRequestManager.getInstance().updateIdleAndTripCompleteEvent(hashMap, sessionManager.getAccessToken(),
                                            HelperClient.getCommonMethods().getRefreshTokenHashMap(context), this);
                                }
                            }
                        });
    }

    private void checkHandlerAndRestart() {
        if (counter < listTosend.size()) {
            handler.postDelayed(runnable, 0);
        } else {
            handler.removeCallbacks(runnable);
        }
    }
}
