package in.roadcast.ridersdk;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;

import androidx.annotation.RequiresPermission;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.os.Build;
import android.provider.Settings;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;

import java.util.Date;

import in.roadcast.ridersdk.Helper.HelperClient;
import in.roadcast.ridersdk.Managers.RealmManager;
import in.roadcast.ridersdk.Managers.SessionManager;
import in.roadcast.ridersdk.Pojo.UserPojo;
import in.roadcast.ridersdk.Requests.LoginRequests;
import in.roadcast.ridersdk.Utils.MyConstants;
import in.roadcast.ridersdk.locationHelper.ForegroundServiceLauncher;
import in.roadcast.ridersdk.locationHelper.services.DeliveryService;

import static android.content.ContentValues.TAG;
import static android.content.Context.LOCATION_SERVICE;

class RoadcastEmpl {

    private Context context;
    protected LocationManager locationManager;
    LocationRequest mLocationRequest;
    private Location latestLocation;
    private FusedLocationProviderClient mFusedLocationClient;
    private LocationCallback mLocationCallback;
    double lt = 0.0;
    double ln = 0.0;
    private int noOfRetry;
    String uniqueId = "";
    private SessionManager sessionManager;
    RoadcastDelegate roadcastDelegate;

    public RoadcastEmpl() {
    }

    public RoadcastEmpl(Context context) {
        this.context = context;
        sessionManager = HelperClient.getSessionManager(context);
        locationManager = (LocationManager) context.getSystemService(LOCATION_SERVICE);
        enableFusedLocation();
    }

    private void enableFusedLocation() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(10000);
        mLocationRequest.setFastestInterval(5000);
        mLocationRequest.setSmallestDisplacement(0);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(context);

        mLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {

                for (final Location location : locationResult.getLocations()) {
                    if (location.getLatitude() == 0.0 || location.getLongitude() == 0.0) {
                        roadcastDelegate.failure("location_unavailable");
                        return;
                    }

                    latestLocation = location;

                    lt = location.getLatitude();
                    ln = location.getLongitude();

                    mFusedLocationClient.removeLocationUpdates(this);

                    startRoadcast();
                }
            }
        };
    }


    public void startLocationUpdates(RoadcastDelegate delegate)
    {
        if (sessionManager.isAuthentified())
        {
            ForegroundServiceLauncher.getInstance().startService(context, new RoadcastDelegate() {
                @Override
                public void success() {
                    delegate.success();
                }

                @Override
                public void failure(String error) {
                    delegate.failure("already_running");
                }
            });
        }
        else
        {
            delegate.failure("unauthorised");
        }
    }


    @RequiresPermission(allOf = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION})
    private void writeFirstUserRealm(RoadcastDelegate roadcastDelegate) {
        this.roadcastDelegate = roadcastDelegate;

        if (sessionManager.isInternetAvailable()) {
            //CHECK AVAILABILITY OF LOCATION AND INTERNET.
            //START ROADCAST.
            //FLAG PIN STATUS SHOULD BE FALSE HERE.

            //FIRST CHECK IF LOCATION IS AVAILABLE. IF NOT AVAILABLE.
            //checkUserExists(apiKey);

            if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                //Both GPS and Network Providers are NOT available.
                roadcastDelegate.failure("gps_stopped");
                return;
            }

            noOfRetry = 0;

            mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, null);

        } else {
            roadcastDelegate.failure("internet_unavailable");
        }
    }

    private void startRoadcast() {

        for (int i = 0; i < 20; i++) {
            noOfRetry++;
            //If Location is Available
            if (lt != 0.0 && ln != 0.0) {
                if (sessionManager.isInternetAvailable()) {
                    startSession();
                } else
                {
                    roadcastDelegate.failure("internet_unavailable");
                }
                break;
            }
            else
            {
                if (noOfRetry == 20)
                {
                    roadcastDelegate.failure("location_unavailable");
                }
            }
        }
    }

    private void startSession() {
        RealmManager.getInstance(context).writeFirstUserRealm(latestLocation, uniqueId, new RoadcastDelegate() {
            @Override
            public void success() {
                roadcastDelegate.success();
            }

            @Override
            public void failure(String error) {
                roadcastDelegate.failure("local_db_error");
            }
        });
    }

    private UserPojo getFirstRealm(String apiKey) {
        return RealmManager.getInstance(context).getUserData(apiKey);
    }

    public boolean isGooglePlayServicesAvailable(Activity activity) {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        int resultCode = apiAvailability.isGooglePlayServicesAvailable(activity);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (apiAvailability.isUserResolvableError(resultCode)) {
                apiAvailability.getErrorDialog(activity, resultCode, 2404)
                        .show();
            } else {
                Log.i(TAG, "This device is not supported.");
            }
            return false;
        } else {
            return true;
        }

    }

    public void loginUser(String mobileNumber, RoadcastDelegate delegate) {
        if (sessionManager.isInternetAvailable()) {
            LoginRequests.loginUser(mobileNumber, delegate, context);
        } else {
            delegate.failure("internet_unavailable");
        }
    }

    @RequiresPermission(allOf = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION})
    public void confirmOtp(String otp, RoadcastDelegate delegate) {
        uniqueId = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
        sessionManager.setApiKey(uniqueId);
        if (sessionManager.isInternetAvailable()) {
            LoginRequests.otpConfirm(otp, context, new RoadcastDelegate() {
                @Override
                public void success() {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (context.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED || context.checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                            delegate.failure("permission_not_granted");
                            return;
                        }
                    }
                    writeFirstUserRealm(delegate);
                }

                @Override
                public void failure(String error) {
                    delegate.failure("invalid_otp");
                }
            });
        }
        else
        {
            delegate.failure("internet_unavailable");
        }
    }

    public void stopLocationUpdates()
    {
        ForegroundServiceLauncher.getInstance().stopService(context);
    }

    public void addNotificationIconsAndTitle(int title,int smallIcon)
    {
        Intent intent = new Intent(MyConstants.BROADCAST_UPDATE_SERVICE_TITLE);
        intent.putExtra("title", title);
        intent.putExtra("smallIcon",smallIcon);
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
    }

    public boolean isLocationUpdateStarted()
    {
        if (ForegroundServiceLauncher.getInstance().isServiceRunning(context))
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    public void setDutyStatus(String status,RoadcastDelegate delegate)
    {
        LoginRequests.getUserProfileData(context, new RoadcastDelegate() {
            @Override
            public void success()
            {
                if (sessionManager.isAuthentified())
                {
                    if (status.matches(sessionManager.get_duty_status()))
                    {
                        delegate.failure("already_set");
                    }
                    else
                    {   String dutyStatus;
                        if (sessionManager.get_duty_status().equals("true")) {
                            dutyStatus = MyConstants.ACTION_STATUS_FALSE;
                            sessionManager.put_duty_status("false");
                        } else {
                            dutyStatus = MyConstants.ACTION_STATUS_TRUE;
                            sessionManager.put_duty_status("true");
                        }
                        RealmManager.getInstance(context).writeUserActivities(MyConstants.ACTION_TYPE_DUTY, dutyStatus, sessionManager.getApiKey());
                        HelperClient.getCommonMethods().configTrackingModule(context);
                        delegate.success();
                    }

                }
                else
                {
                    delegate.failure("unauthorised");
                }
            }

            @Override
            public void failure(String error) {
                delegate.failure(error);
            }
        },false);

    }

    public boolean getDutyStatus()
    {
        final boolean[] status = new boolean[1];
        LoginRequests.getUserProfileData(context, new RoadcastDelegate() {
            @Override
            public void success()
            {
                if (sessionManager.get_duty_status().equals("true"))
                {
                    status[0] = true;
                }
                else
                {
                    status[0] = false;
                }
            }

            @Override
            public void failure(String error)
            {
                    status[0] = false;
            }
        },false);

        return status[0];
    }



}
