package in.roadcast.ridersdk.Receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;

import in.roadcast.ridersdk.BuildConfig;
import in.roadcast.ridersdk.Helper.HelperClient;
import in.roadcast.ridersdk.Managers.SessionManager;
import in.roadcast.ridersdk.Utils.MyConstants;
import in.roadcast.ridersdk.locationHelper.ForegroundServiceLauncher;


public class GpsReceiver extends BroadcastReceiver {

    public GpsReceiver() {

    }

    @Override
    public void onReceive(Context context, Intent intent) {

        SessionManager sessionManager = HelperClient.getSessionManager(context);
        String gpsStatus=null;

//        if(sessionManager.isAuthentified())
//        {
//            LocationManager lm = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
//
//            if(!lm.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
//                //No GPS.
//                intent = new Intent(context, ProfileActivity.class);
//                intent.putExtra("gpsStatus",false);
//                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK| Intent.FLAG_ACTIVITY_SINGLE_TOP);
//                gpsStatus="0";
//                sessionManager.setLastGpsState(MyConstants.GPS_STATE_OFF);
//            }
//            else if(lm.isProviderEnabled(LocationManager.GPS_PROVIDER))
//            {
//                intent = new Intent(context, ProfileActivity.class);
//                intent.putExtra("gpsStatus",true);
//                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK| Intent.FLAG_ACTIVITY_SINGLE_TOP);
//                sessionManager.setLastGpsState(MyConstants.GPS_STATE_ON);
//
//                if (sessionManager.get_duty_status().equals("true"))
//                {
//                    ForegroundServiceLauncher.getInstance().startService(context);
//                }
//                gpsStatus="1";
//            }
//
//            if (!BuildConfig.DEBUG)
//            {
//                context.startActivity(intent);
//                 HelperClient.getCurrentLocation(context).getSingleLocation();
////                RealmManager.getInstance().writeUserActivities("gps",gpsStatus, sessionManager.getUniqueId());
//            }
//
//
//        }
    }

}
