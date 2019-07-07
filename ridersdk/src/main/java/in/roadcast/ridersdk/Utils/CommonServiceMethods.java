package in.roadcast.ridersdk.Utils;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.telephony.TelephonyManager;

import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;

import in.roadcast.ridersdk.Helper.HelperClient;
import in.roadcast.ridersdk.Managers.SessionManager;
import in.roadcast.ridersdk.Networks.SendDataToServer;
import in.roadcast.ridersdk.R;

/**
 * Created by roadcast on 10/4/18.
 */

public class CommonServiceMethods
{

    public CommonServiceMethods() {}

    private AsyncTask isRunningSendDataToServer;

    public synchronized void sendDataToServer(Context mContext)
    {
        SessionManager sessionManager = HelperClient.getSessionManager(mContext);

        if (!sessionManager.isInternetAvailable()
                && isRunningSendDataToServer != null
                && !isRunningSendDataToServer.getStatus().equals(AsyncTask.Status.FINISHED))
        {
            return;
        }

        isRunningSendDataToServer = new SendDataToServer(mContext).execute();

    }

    public void checkInternetStatus(Context context)
    {
        SessionManager sessionManager = HelperClient.getSessionManager(context);
        ConnectivityManager connManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        NetworkInfo network = connManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

        if (mWifi.isConnected()) {
            // Do whatever
            sessionManager.setInternetAvailable(true);
        } else if (network.isConnected()) {
            TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            if (CommonMethods.getNetworkDataStatus(context, telephonyManager.getDataState()).equals("Connected")) {
                sessionManager.setInternetAvailable(true);
            }
        }
    }



    public boolean checkPermission(Context context) {
        return ActivityCompat.checkSelfPermission((Activity)context, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission((Activity)context, android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED;
    }


}
