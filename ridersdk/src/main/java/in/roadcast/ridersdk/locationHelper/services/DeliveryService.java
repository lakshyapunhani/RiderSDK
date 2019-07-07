package in.roadcast.ridersdk.locationHelper.services;

import android.Manifest;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.SystemClock;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.ActivityRecognition;


import java.util.concurrent.TimeUnit;

import in.roadcast.ridersdk.Helper.HelperClient;
import in.roadcast.ridersdk.Managers.RealmManager;
import in.roadcast.ridersdk.Managers.SessionManager;
import in.roadcast.ridersdk.R;
import in.roadcast.ridersdk.Receivers.AlarmReceiver;
import in.roadcast.ridersdk.Receivers.GpsReceiver;
import in.roadcast.ridersdk.Receivers.NetworkReceiver;
import in.roadcast.ridersdk.Utils.MyConstants;
import in.roadcast.ridersdk.locationHelper.managers.ActivityRecognitionManager;
import in.roadcast.ridersdk.locationHelper.receiver.ActivityRecognitionBroadcastReceiver;

public class DeliveryService extends Service implements
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, ResultCallback<Status> {

    Context mContext = this;
    SessionManager sessionManager;
    LocationManager lm = null;
    public static boolean isLocationChangedCalled = false;
    private GoogleApiClient mGoogleApiClient;

    private long timerTime;
    int internetCounter = 0;
    private String currentDate;
    private NetworkReceiver networkReceiver;
    private GpsReceiver gpsReceiver;

    private static final int NOTIF_ID=10345;

    ActivityRecognitionManager activityRecognitionManager;
    BroadcastReceiver activityRecognitionBroadcastReceiver;
    BroadcastReceiver updateNotificationBroadcastReceiver;


    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //HelperClient.getCommonServiceMethods().sendDataToServer(mContext);
        return START_STICKY;
    }

    private BroadcastReceiver updateNotificationReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent)
        {
            int title = intent.getIntExtra("title",0);
            int smallIcon = intent.getIntExtra("smallIcon",0);
            updateNotification(title,smallIcon);
        }
    };


    @Override
    public void onCreate()
    {
        sessionManager = HelperClient.getSessionManager(this);

        IntentFilter networkFilter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        networkReceiver = new NetworkReceiver();
        this.registerReceiver(networkReceiver, networkFilter);

        IntentFilter gpsFilter = new IntentFilter(LocationManager.PROVIDERS_CHANGED_ACTION);
        gpsReceiver = new GpsReceiver();
        this.registerReceiver(gpsReceiver, gpsFilter);

        IntentFilter filter = new IntentFilter(MyConstants.BROADCAST_UPDATE_SERVICE_TITLE);
        LocalBroadcastManager.getInstance(this).registerReceiver(updateNotificationReceiver, filter);

        activityRecognitionBroadcastReceiver = new ActivityRecognitionBroadcastReceiver(this);
        LocalBroadcastManager.getInstance(this).registerReceiver(activityRecognitionBroadcastReceiver,
                new IntentFilter(MyConstants.BROADCAST_DETECTED_ACTIVITY));

        updateNotificationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent)
            {
                String notiString = "Delivery";
                if (intent.getStringExtra(MyConstants.BROADCAST_UPDATE_NOTIFICATION_EXTRA) != null &&
                        intent.getStringExtra(MyConstants.BROADCAST_UPDATE_NOTIFICATION_EXTRA).equals(MyConstants.BROADCAST_UPDATE_NOTIFICATION_STILL_VALUE))
                {
                    timerTime = TimeUnit.SECONDS.toMillis(145);
                    notiString = "Still";
                }
                else
                {
                    timerTime = TimeUnit.SECONDS.toMillis(30);
                }
                timerHandler.removeCallbacks(timerRunnable);
                timerHandler.postDelayed(timerRunnable, 0);
                startForeground(NOTIF_ID, builtNotification(R.string.title_noti_delivery,android.R.drawable.sym_def_app_icon,notiString));
            }
        };

        this.registerReceiver(updateNotificationBroadcastReceiver, new IntentFilter(MyConstants.BROADCAST_UPDATE_NOTIFICATION));

        currentDate = HelperClient.getCommonMethods().convertDate(System.currentTimeMillis(), "yyyy/MM/dd", false);


        checkTrackingModuleAndStartUpdates();

        timerTime = 30000;

//        if (HelperClient.getCommonServiceMethods().isDateChanged(sessionManager, currentDate)) {
//            fullDayDistance = 0;
//            sessionManager.savePreviousDayTime(System.currentTimeMillis());
//        }

        activityRecognitionManager = new ActivityRecognitionManager(this);

        startAlarmReceiver();

        activityRecognitionManager.requestActivityUpdatesButtonHandler();

        buildGoogleApiClient();
        mGoogleApiClient.connect();

        if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        timerHandler.postDelayed(timerRunnable, 0);
    }

    Handler timerHandler = new Handler();
    Runnable timerRunnable = new Runnable() {
        @Override
        public void run() {
            try {
                long tempTimestamp = System.currentTimeMillis();
                internetCounter += timerTime;

                if (internetCounter > timerTime * 5)
                {
                    HelperClient.getCommonServiceMethods().checkInternetStatus(mContext);
                    internetCounter = 0;
                }

                if (sessionManager.getContinuousStillCountTime() != -1 && sessionManager.getContinuousStillCountTime() != 0) {
                    if ((tempTimestamp - sessionManager.getContinuousStillCountTime()) > TimeUnit.MINUTES.toMillis(5)) {
                        checkTrackingModuleAndStopUpdates();
                    }
                }

                if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }

                if (!mGoogleApiClient.isConnected()) {
                    mGoogleApiClient.connect();
                }

                if (lm != null && sessionManager.getLastLocationTime() != 0 && tempTimestamp - sessionManager.getLastLocationTime() > 60000) {
                    RealmManager.getInstance(DeliveryService.this).updateTimeStamp(sessionManager.getApiKey());
                }

                HelperClient.getCommonServiceMethods().sendDataToServer(mContext);

            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                timerHandler.postDelayed(this, timerTime);
            }
        }
    };

    private void checkTrackingModuleAndStopUpdates() {
        switch (sessionManager.getTrackingModule()) {
            case "1" :  // Interval Tracking
                break;
            case "2" :
                sessionManager.setRequestingLocationUpdates(false);
                sessionManager.setContinuousStillCountTime(-1);
                break;
            case "0" :
                HelperClient.getLiveTrackingPositionProvider(mContext).removeUpdates(true);
                break;
            default:
                HelperClient.getLiveTrackingPositionProvider(mContext).removeUpdates(true);
                break;
        }
    }

    private void checkTrackingModuleAndStartUpdates() {
        switch (sessionManager.getTrackingModule()) {
            case "1" :  // Interval Tracking
                startForeground(NOTIF_ID, builtNotification(R.string.title_noti_delivery,android.R.drawable.sym_def_app_icon,"Delivery"));
                break;
            case "2" :
                sessionManager.setRequestingLocationUpdates(false);
                sessionManager.setContinuousStillCountTime(-1);
                startForeground(NOTIF_ID, builtNotification(R.string.title_noti_delivery,android.R.drawable.sym_def_app_icon,"No-Track"));
                break;
            case "0" :
                startForeground(NOTIF_ID, builtNotification(R.string.title_noti_delivery,android.R.drawable.sym_def_app_icon,"Delivery"));
                HelperClient.getLiveTrackingPositionProvider(mContext).startUpdates(false);
                break;
            default :
                startForeground(NOTIF_ID, builtNotification(R.string.title_noti_delivery,android.R.drawable.sym_def_app_icon,"Delivery"));
                HelperClient.getLiveTrackingPositionProvider(mContext).startUpdates(false);
                break;
        }
    }

    protected synchronized void buildGoogleApiClient()
    {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(ActivityRecognition.API)
                .build();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle)
    {
        /*positionProvider.startUpdates();
        activityRecognitionManager.requestActivityUpdatesButtonHandler();*/
    }

    @Override
    public void onConnectionSuspended(int i) {
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) { }

    @Override
    public void onResult(@NonNull Status status) { }

    @Override
    public void onDestroy() {

        this.unregisterReceiver(networkReceiver);

        this.unregisterReceiver(gpsReceiver);

        this.unregisterReceiver(updateNotificationBroadcastReceiver);

        timerHandler.removeCallbacksAndMessages(null);
        LocalBroadcastManager.getInstance(this).unregisterReceiver(activityRecognitionBroadcastReceiver);
        LocalBroadcastManager.getInstance(this).unregisterReceiver(updateNotificationReceiver);

        super.onDestroy();
    }

    public void startAlarmReceiver()
    {
        Intent intent = new Intent(this, AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                this.getApplicationContext(), 234, intent, 0);
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            alarmManager.setExactAndAllowWhileIdle(AlarmManager.ELAPSED_REALTIME_WAKEUP,
                    SystemClock.elapsedRealtime() + TimeUnit.SECONDS.toMillis(295),pendingIntent);
        }
        else
        {
            alarmManager.setExact(AlarmManager.ELAPSED_REALTIME_WAKEUP,
                    SystemClock.elapsedRealtime() + TimeUnit.SECONDS.toMillis(295),pendingIntent);
        }
    }

    public Notification builtNotification(int title,int smallIcon,String delivery) {

        NotificationManager notificationManager = (NotificationManager) DeliveryService.this.getSystemService(Context.NOTIFICATION_SERVICE);
        assert notificationManager != null;

        NotificationCompat.Builder builder = null;

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O)
        {
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel notificationChannel = new
                    NotificationChannel("ID", "Name", importance);
            notificationManager.createNotificationChannel(notificationChannel);
            builder = new NotificationCompat.Builder(DeliveryService.this, notificationChannel.getId());
        } else {
            builder = new NotificationCompat.Builder(DeliveryService.this);
        }

        String message = "Service is active.";
        if (delivery.matches("Delivery"))
        {
            message = "Service is active.";
        }
        else if (delivery.matches("Fused"))
        {
            message = "Service is active.";
        }
        else if (delivery.matches("Still"))
        {
            message = "Service is idle";
        } else if (delivery.matches("No-Track"))
        {
            message = "Service is active, with No Tracking Mode.";
        }
        builder.setSmallIcon(smallIcon)
                .setAutoCancel(false)
                .setPriority(Notification.PRIORITY_MAX)
                .setVibrate(new long[]{0, 200})
                .setOngoing(true)
                .setContentTitle(getResources().getString(title))
                .setContentText(message);

        Intent launchIntent = DeliveryService.this.getPackageManager().
                getLaunchIntentForPackage(DeliveryService.this.getPackageName());
        launchIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                Intent.FLAG_ACTIVITY_NEW_TASK);

        PendingIntent contentIntent = PendingIntent.
                getActivity(DeliveryService.this, 0, launchIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(contentIntent);

        Notification notification = builder.build();
        return notification;

    }

    public void updateNotification(int text,int smallIcon)
    {
        Notification notification = builtNotification(text,smallIcon,"Delivery");

        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(NOTIF_ID, notification);
    }

}
