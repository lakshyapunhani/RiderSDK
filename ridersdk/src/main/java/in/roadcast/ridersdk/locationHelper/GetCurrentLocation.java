package in.roadcast.ridersdk.locationHelper;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.CountDownTimer;
import android.os.Looper;

import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;

import in.roadcast.ridersdk.BuildConfig;
import in.roadcast.ridersdk.Helper.HelperClient;
import in.roadcast.ridersdk.Managers.RealmManager;
import in.roadcast.ridersdk.Managers.SessionManager;
import in.roadcast.ridersdk.RealmModels.UserRealm;
import io.realm.Realm;

public class GetCurrentLocation
{
    SessionManager sessionManager;
    int counter = 0;
    LocationRequest mLocationRequest;
    LocationCallback mLocationCallback;
    FusedLocationProviderClient mFusedLocationClient;
    public Context context;

    private Location lastUpdatedLocation;

    public GetCurrentLocation(Context context) {
        this.context = context;
        sessionManager = HelperClient.getSessionManager(context);
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(context);
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(5000);
        mLocationRequest.setFastestInterval(5000);
        mLocationRequest.setSmallestDisplacement(0);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        try (Realm realm = Realm.getDefaultInstance())
        {
            UserRealm userRealm = realm.where(UserRealm.class).equalTo("uniqueId", sessionManager.getApiKey()).findFirst();
            if (userRealm != null)
            {
                lastUpdatedLocation = new Location("gps");
                lastUpdatedLocation.setLatitude(userRealm.getLatitude());
                lastUpdatedLocation.setLongitude(userRealm.getLongitude());
                lastUpdatedLocation.setTime(System.currentTimeMillis());
                lastUpdatedLocation.setProvider(userRealm.getProvider());
            }
        }
    }

    public void getSingleLocation()
    {
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult == null) {
                    return;
                }
//                if (sessionManager.isInstantLocationNotiReceived()) {
//                    if (counter == 3)
//                    {
//                        mFusedLocationClient.removeLocationUpdates(this);
//                        counter = 0;
//                    }
//                    else
//                    {
//                        counter++;
//                    }
//                }
//                else
//                {
//                    mFusedLocationClient.removeLocationUpdates(this);
//                }

                mFusedLocationClient.removeLocationUpdates(this);

                final Location tempLocation = locationResult.getLastLocation();

                if (tempLocation != null) {
                    if (!BuildConfig.DEBUG) {
                        if (tempLocation.isFromMockProvider()) {
                            if (!sessionManager.isMockLocationActivated()) {
                                sessionManager.setMockLocationActivated(true);
//                                RealmManager.getInstance().writeUserActivities("mock_location", "1", sessionManager.getUniqueId());
                            }
                            return;
                        } else {
                            if (sessionManager.isMockLocationActivated()) {
                                sessionManager.setMockLocationActivated(false);
//                                RealmManager.getInstance().writeUserActivities("mock_location", "0", sessionManager.getUniqueId());
                            }
                        }
                    }
                    sessionManager.setLastLocationTime(System.currentTimeMillis());
                    RealmManager.getInstance(context).writeLocationInRealm(context, tempLocation, true, 0);
                    new CountDownTimer(4000, 4000) {

                        @Override
                        public void onTick(long millisUntilFinished) {

                        }

                        @Override
                        public void onFinish() {
                        }
                    }.start();

                }
            }
        };

        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
    }




}
