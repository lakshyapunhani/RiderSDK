package in.roadcast.ridersdk.locationHelper.listener;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Looper;

import androidx.core.app.ActivityCompat;

import java.util.concurrent.TimeUnit;

import in.roadcast.ridersdk.BuildConfig;
import in.roadcast.ridersdk.Helper.HelperClient;
import in.roadcast.ridersdk.Managers.RealmManager;
import in.roadcast.ridersdk.Managers.SessionManager;
import in.roadcast.ridersdk.RealmModels.UserRealm;
import in.roadcast.ridersdk.Utils.MyConstants;
import in.roadcast.ridersdk.locationHelper.managers.ActivityRecognitionManager;
import in.roadcast.ridersdk.locationHelper.services.DeliveryService;
import io.realm.Realm;

public class GPSLiveTrackingPositionProvider implements LocationListener
{
    private final Context context;
    private LocationManager locationManager;
    private SessionManager sessionManager;

    float fullDayDistance;

    private String currentDate;

    private Location lastUpdatedLocation;

    private float speedLimit;

    boolean limitUp = false;

    public GPSLiveTrackingPositionProvider(Context context) {
        this.context = context;

        locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);

        sessionManager = HelperClient.getSessionManager(context);

        //speed got in kmph and converted to m/s
        //speedLimit = (sessionManager.getSpeedForSpeedAlert() * 5) / 18;

        currentDate = HelperClient.getCommonMethods().convertDate(System.currentTimeMillis(), "yyyy/MM/dd", false);
    }

    @SuppressLint("MissingPermission")
    public void startUpdates(boolean shouldUpdateNotification)
    {

        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        if (locationManager == null)
        {
            locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        }

        locationManager.requestLocationUpdates(
                LocationManager.GPS_PROVIDER, TimeUnit.SECONDS.toMillis(10), 0,
                this, Looper.myLooper());

        locationManager.requestLocationUpdates(
                LocationManager.NETWORK_PROVIDER, TimeUnit.SECONDS.toMillis(35), 0,
                this, Looper.myLooper());

//        if (sessionManager.shouldSaveLogs()) {
//            RealmManager.saveLogs(this.getClass().getSimpleName(), HelperClient.getCommonMethods().convertDate(System.currentTimeMillis(), "dd-MMM-yyyy HH:mm:ss", false) + " ", "Live Location Updates", "Started");
//        }

        sessionManager.setRequestingLocationUpdates(true);

        if (sessionManager.getContinuousStillCountTime() == -1) {
            sessionManager.setContinuousStillCountTime(0);
        }

        if (shouldUpdateNotification) {
            Intent intent = new Intent(MyConstants.BROADCAST_UPDATE_NOTIFICATION);
            intent.putExtra(MyConstants.BROADCAST_UPDATE_NOTIFICATION_EXTRA, MyConstants.BROADCAST_UPDATE_NOTIFICATION_MOVING_VALUE);
            context.sendBroadcast(intent);
        }

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

    private synchronized void criteriaForUpdate(Location location) {

        float distance = location.distanceTo(lastUpdatedLocation);
        if (location.getSpeed() == 0 && distance > 0f)
        {
            float timeBTWLoc = (float) (location.getTime() - lastUpdatedLocation.getTime()) / 1000;

            float speed = distance / timeBTWLoc;
            //Speed is in m/s
            if (speed >= 0.5f)
            {
                location.setSpeed(speed);
            }
        }


        if (significantChange(location, distance))
        {
            if (location.getSpeed() > speedLimit && !limitUp)
            {
                limitUp = true;
//                RealmManager.getInstance().writeUserActivities("speed_limit", "1", sessionManager.getUniqueId());
            }
            else if (location.getSpeed() < speedLimit && limitUp)
            {
                limitUp = false;
//                RealmManager.getInstance().writeUserActivities("speed_limit", "0", sessionManager.getUniqueId());
            }
            //RealmManager.getInstance(context).addDistance(currentDate, distance,sessionManager.getReimbursementPerKm());
            lastUpdatedLocation = location;
            RealmManager.getInstance(context).writeLocationInRealm(context, location, true, fullDayDistance);
        }
        else
        {
            lastUpdatedLocation.setTime(location.getTime());
            RealmManager.getInstance(context).writeLocationInRealm(context, location, false, fullDayDistance);
        }
    }

    private boolean significantChange(Location location, float distance) {
        //Distance is in meters.

        return location.getAccuracy() < 100f &&
                distance > 16.1f &&
                location.getSpeed() > 0.5f &&
                location.getSpeed() < 50f;
    }

    @Override
    public void onLocationChanged(Location location)
    {
        if (lastUpdatedLocation == null)
        {
            lastUpdatedLocation = location;
        }
        if (location.getLatitude() == 0.0 || location.getLongitude() == 0.0)
        {
            return;
        }

        if (!BuildConfig.DEBUG) {
            if (location.isFromMockProvider()) {
                if (!sessionManager.isMockLocationActivated()) {
                    sessionManager.setMockLocationActivated(true);
//                    RealmManager.getInstance().writeUserActivities("mock_location", "1", sessionManager.getUniqueId());
                }
                return;
            } else {
                if (sessionManager.isMockLocationActivated()) {
                    sessionManager.setMockLocationActivated(false);
//                    RealmManager.getInstance().writeUserActivities("mock_location", "0", sessionManager.getUniqueId());
                }
            }
        }

        if (location.getProvider().equals("gps")) {
            criteriaForUpdate(location);
            sessionManager.setLastLocationTime(System.currentTimeMillis());

        } else {
            criteriaForUpdate(lastUpdatedLocation);
        }

        DeliveryService.isLocationChangedCalled = true;

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    public void removeUpdates(boolean shouldUpdateNotification)
    {
        if (locationManager != null)
        {
            locationManager.removeUpdates(this);
//            if (sessionManager.shouldSaveLogs()) {
//                RealmManager.saveLogs(this.getClass().getSimpleName(), HelperClient.getCommonMethods().convertDate(System.currentTimeMillis(), "HH:mm:ss", false) + " ", "Live Location Updates", "Removed");
//            }
            sessionManager.setRequestingLocationUpdates(false);
            sessionManager.setContinuousStillCountTime(-1);
            new ActivityRecognitionManager(context).requestActivityUpdatesButtonHandler();
            locationManager = null;
            if (shouldUpdateNotification) {
                Intent intent = new Intent(MyConstants.BROADCAST_UPDATE_NOTIFICATION);
                intent.putExtra(MyConstants.BROADCAST_UPDATE_NOTIFICATION_EXTRA, MyConstants.BROADCAST_UPDATE_NOTIFICATION_STILL_VALUE);
                context.sendBroadcast(intent);
            }
        }
    }

}
