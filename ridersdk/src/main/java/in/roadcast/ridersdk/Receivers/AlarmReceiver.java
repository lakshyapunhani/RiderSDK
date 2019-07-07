package in.roadcast.ridersdk.Receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import in.roadcast.ridersdk.Helper.HelperClient;
import in.roadcast.ridersdk.Managers.SessionManager;
import in.roadcast.ridersdk.locationHelper.ForegroundServiceLauncher;


public class AlarmReceiver extends BroadcastReceiver
{
    @Override
    public void onReceive(Context context, Intent intent) {
        SessionManager sessionManager = HelperClient.getSessionManager(context);
        //HelperClient.getCurrentLocation(context).getSingleLocation();
//        if (!HelperClient.getCommonMethods().isServiceRunning(context, DeliveryService.class) && sessionManager.getIsEnable())
//        {
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//                context.startForegroundService(new Intent(context, DeliveryService.class));
//            }
//            else
//            {
//                context.startService(new Intent(context,DeliveryService.class));
//            }
//        }
//        if (sessionManager.isInternetAvailable())
//        {
//            new SendLocationsAsync(context).executeOnExecutor(AsyncTask.SERIAL_EXECUTOR);
//        }

//        if (sessionManager.isAuthentified()) {
//            ForegroundServiceLauncher.getInstance().startService(context);
//        }

    }

}
