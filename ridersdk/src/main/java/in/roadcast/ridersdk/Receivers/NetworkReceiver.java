package in.roadcast.ridersdk.Receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.CountDownTimer;

import in.roadcast.ridersdk.Managers.SessionManager;

public class NetworkReceiver extends BroadcastReceiver
{
    private SessionManager sessionManager;
    /**
     * CountDownTimer added to prevent frequent changes happening in network connection. timer Wait for 30 sec after
     * internet off event, in case on internet On event timer will not be fired.
     */
    private CountDownTimer timer;
    private boolean isTimerRunning;

    public NetworkReceiver(){
        timer = new CountDownTimer(30000, 30000) {
            public void onTick(long millisUntilFinished) {
                //nothing
            }
            public void onFinish() {
                isTimerRunning = false;
                //writeInRealm();
            }
        };
    }
    @Override
    public void onReceive(Context context, Intent intent) {

        ConnectivityManager conn = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = conn.getActiveNetworkInfo();
        sessionManager = new SessionManager(context);
        //HelperClient.getCommonMethods().getCurrentLocation(context, false);
        // HelperClient.getCurrentLocation(context).getSingleLocation();

        if (networkInfo != null ) {
            if (!sessionManager.isInternetAvailable()) {
                sessionManager.setInternetAvailable(true);
                if (isTimerRunning){
                    isTimerRunning=false;
                    timer.cancel();
                } else {
                    //writeInRealm();
                }
            }

        }  else {
            if (sessionManager.isInternetAvailable()) {
                sessionManager.setInternetAvailable(false);
                if (!isTimerRunning){
                    isTimerRunning=true;
                    timer.start();
                }
            }
        }
    }

//    public void writeInRealm(){
//        if(sessionManager.isPinPointed()) {
//            if (sessionManager.isInternetAvailable()) {
//                RealmManager.writeUserActivities("internet", "1", sessionManager.getLoginId());
//            } else {
//                RealmManager.writeUserActivities("internet", "0", sessionManager.getLoginId());
//            }
////            EventBus.getDefault().post(new ActivityToService(DefaultValues.RECEIVER_TO_SERVICE));
//            EventBus.getDefault().post(new ReceiverToActivity(ActivitiyName.ORDER_ACTIVITY));
//        }
//    }
}
