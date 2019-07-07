package in.roadcast.ridersdk.RealmModels;

import android.location.Location;


import androidx.annotation.Keep;

import java.util.UUID;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

@Keep
public class LocationRealm extends RealmObject {

    @PrimaryKey
    private String locationId;
    private double latitude;
    private double longitude;
    private long timestamp;
    private float batteryStatus;
    private float course;
    private float accuracy;
    private float speed;
    private boolean isCharging;
    private String provider;
    private float fullDayDistance;

    public LocationRealm(){

    }

    public LocationRealm(Location location, float batteryStatus, boolean isCharging){
        latitude=location.getLatitude();
        longitude=location.getLongitude();
        timestamp= System.currentTimeMillis();
        course=location.getBearing();
        speed=location.getSpeed();
        accuracy=location.getAccuracy();
        this.batteryStatus=batteryStatus;
        this.isCharging=isCharging;
        this.locationId= UUID.randomUUID().toString();
        this.provider=location.getProvider();
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public float getBatteryStatus() {
        return batteryStatus;
    }

    public void setBatteryStatus(float batteryStatus) {
        this.batteryStatus = batteryStatus;
    }


    public float getCourse() {
        return course;
    }

    public void setCourse(float course) {
        this.course = course;
    }

    public float getAccuracy() {
        return accuracy;
    }

    public void setAccuracy(float accuracy) {
        this.accuracy = accuracy;
    }

    public float getSpeed() {
        return speed;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }

    public boolean isCharging() {
        return isCharging;
    }

    public void setCharging(boolean charging) {
        isCharging = charging;
    }

    public String getLocationId() {
        return locationId;
    }

    public void setLocationId(String locationId) {
        this.locationId = locationId;
    }

    public String getProvider() {
        return provider;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }

    public float getFullDayDistance() {
        return fullDayDistance;
    }

    public void setFullDayDistance(float fullDayDistance) {
        this.fullDayDistance = fullDayDistance;
    }

}
