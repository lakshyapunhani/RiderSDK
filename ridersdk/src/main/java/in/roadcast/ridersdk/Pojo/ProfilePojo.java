package in.roadcast.ridersdk.Pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ProfilePojo
{
    @SerializedName("app_version")
    @Expose
    private String appVersion;
    @SerializedName("course")
    @Expose
    private String course;
    @SerializedName("current_duty")
    @Expose
    private String currentDuty;
    @SerializedName("device_details")
    @Expose
    private String deviceDetails;
    @SerializedName("device_token")
    @Expose
    private String deviceToken;
    @SerializedName("fixed_time")
    @Expose
    private String fixedTime;
    @SerializedName("id")
    @Expose
    private long id;
    @SerializedName("in_zone")
    @Expose
    private String inZone;
    @SerializedName("lat")
    @Expose
    private String lat;
    @SerializedName("lng")
    @Expose
    private String lng;
    @SerializedName("online")
    @Expose
    private String online;
    @SerializedName("speed")
    @Expose
    private String speed;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("total_amt_collect")
    @Expose
    private String totalAmtCollect;
    @SerializedName("total_amt_deposit")
    @Expose
    private String totalAmtDeposit;
    @SerializedName("unique_id")
    @Expose
    private long uniqueId;
    @SerializedName("user")
    @Expose
    private long user;
    @SerializedName("user_id")
    @Expose
    private long userId;

    public String getAppVersion() {
        return appVersion;
    }

    public void setAppVersion(String appVersion) {
        this.appVersion = appVersion;
    }

    public String getCourse() {
        return course;
    }

    public void setCourse(String course) {
        this.course = course;
    }

    public String getCurrentDuty() {
        return currentDuty;
    }

    public void setCurrentDuty(String currentDuty) {
        this.currentDuty = currentDuty;
    }

    public String getDeviceDetails() {
        return deviceDetails;
    }

    public void setDeviceDetails(String deviceDetails) {
        this.deviceDetails = deviceDetails;
    }

    public String getDeviceToken() {
        return deviceToken;
    }

    public void setDeviceToken(String deviceToken) {
        this.deviceToken = deviceToken;
    }

    public String getFixedTime() {
        return fixedTime;
    }

    public void setFixedTime(String fixedTime) {
        this.fixedTime = fixedTime;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getInZone() {
        return inZone;
    }

    public void setInZone(String inZone) {
        this.inZone = inZone;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }

    public String getOnline() {
        return online;
    }

    public void setOnline(String online) {
        this.online = online;
    }

    public String getSpeed() {
        return speed;
    }

    public void setSpeed(String speed) {
        this.speed = speed;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTotalAmtCollect() {
        return totalAmtCollect;
    }

    public void setTotalAmtCollect(String totalAmtCollect) {
        this.totalAmtCollect = totalAmtCollect;
    }

    public String getTotalAmtDeposit() {
        return totalAmtDeposit;
    }

    public void setTotalAmtDeposit(String totalAmtDeposit) {
        this.totalAmtDeposit = totalAmtDeposit;
    }

    public long getUniqueId() {
        return uniqueId;
    }

    public void setUniqueId(long uniqueId) {
        this.uniqueId = uniqueId;
    }

    public long getUser() {
        return user;
    }

    public void setUser(long user) {
        this.user = user;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }
}
