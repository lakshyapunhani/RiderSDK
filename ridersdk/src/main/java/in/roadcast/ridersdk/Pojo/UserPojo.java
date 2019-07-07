package in.roadcast.ridersdk.Pojo;


import in.roadcast.ridersdk.RealmModels.UserRealm;

public class UserPojo {

    private String userId;
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

    public UserPojo(UserRealm realm) {
        setUserId(realm.getUniqueId());
        setLatitude(realm.getLatitude());
        setLongitude(realm.getLongitude());
        setProvider(realm.getProvider());
        //setAttendanceSessionStatus(realm.getAttendanceSessionStatus());
        setAttendenceTodayStatus(realm.getAttendenceSessionStatus());
        setLastAttendenceDate(realm.getLastAttendanceDate());
        setAttendMarkInTime(realm.getAttendMarkInTime());
        setAttendMarkOutTime(realm.getAttendMarkOutTime());
        setTimestamp(realm.getTimestamp());
        setBatteryStatus(realm.getBatteryStatus());
        setCourse(realm.getCourse());
        setAccuracy(realm.getAccuracy());
        setSpeed(realm.getSpeed());
        setCharging(realm.isCharging());
        setFullDayDistance(realm.getFullDayDistance());
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
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

    public String getProvider() {
        return provider;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }
//
//    public AttendanceStatus getAttendanceSessionStatus() {
//        return AttendanceStatus.getEnumConstant(attendenceSessionStatus);
//    }
//
//    public void setAttendanceSessionStatus(AttendanceStatus status) {
//        this.attendenceSessionStatus = status.ordinal();
//    }

    public int getAttendenceTodayStatus() {
        return attendenceTodayStatus;
    }

    public void setAttendenceTodayStatus(int attendenceTodayStatus) {
        this.attendenceTodayStatus = attendenceTodayStatus;
    }

    public String getLastAttendenceDate() {
        return lastAttendenceDate;
    }

    public void setLastAttendenceDate(String lastAttendenceDate) {
        this.lastAttendenceDate = lastAttendenceDate;
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

    public float getFullDayDistance() {
        return fullDayDistance;
    }

    public void setFullDayDistance(float fullDayDistance) {
        this.fullDayDistance = fullDayDistance;
    }
}
