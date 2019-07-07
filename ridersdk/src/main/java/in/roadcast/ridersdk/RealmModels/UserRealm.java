package in.roadcast.ridersdk.RealmModels;


import androidx.annotation.Keep;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;


@Keep
public class UserRealm extends RealmObject
{
    @PrimaryKey
    private String uniqueId;
    private double latitude;
    private double longitude;
    private String provider;
    private int attendenceSessionStatus;           // to check if attendance session is active or not
    private int attendenceTodayStatus;            // to check if attendance is done today or not
    private String lastAttendenceDate;
    private String attendMarkInTime;
    private String attendMarkOutTime;
    private long timestamp;
    private float batteryStatus;
    private float course;
    private float accuracy;
    private float speed;
    private boolean isCharging;
    private float fullDayDistance;

    public UserRealm() { }

    public int getAttendenceSessionStatus() {
        return attendenceSessionStatus;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
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

    public String getAttendMarkInTime() {
        return attendMarkInTime;
    }

    public void setAttendMarkInTime(String attendMarkInTime) {
        this.attendMarkInTime = attendMarkInTime;
    }

    public String getAttendMarkOutTime() {
        return attendMarkOutTime;
    }

    public void setAttendMarkOutTime(String attendMarkOutTime) {
        this.attendMarkOutTime = attendMarkOutTime;
    }

//    public AttendanceStatus getAttendanceSessionStatus() {
//        return AttendanceStatus.getEnumConstant(attendenceSessionStatus);
//    }
//
//    public void setAttendanceSessionStatus(AttendanceStatus status) {
//        this.attendenceSessionStatus = status.ordinal();
//    }

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

    public String getProvider() {
        return provider;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }

    public String getLastAttendanceDate() {
        return lastAttendenceDate;
    }

    public void setLastAttendanceDate(String attendanceDate) {
        this.lastAttendenceDate = attendanceDate;
    }

    public float getFullDayDistance() {
        return fullDayDistance;
    }

    public void setFullDayDistance(float fullDayDistance) {
        this.fullDayDistance = fullDayDistance;
    }

    public String getUniqueId() {
        return uniqueId;
    }

    public void setUniqueId(String uniqueId) {
        this.uniqueId = uniqueId;
    }
}
