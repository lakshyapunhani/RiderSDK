package in.roadcast.ridersdk.Pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CheckUserProfileResponsePojo
{
    @SerializedName("app_version")
    @Expose
    private String appVersion;
    @SerializedName("course")
    @Expose
    private Object course;
    @SerializedName("current_duty")
    @Expose
    private Object currentDuty;
    @SerializedName("device_details")
    @Expose
    private UserDeviceDetailsPojo deviceDetails;
    @SerializedName("device_token")
    @Expose
    private String deviceToken;
    @SerializedName("device_type")
    @Expose
    private String deviceType;
    @SerializedName("external_id")
    @Expose
    private long externalId;
    @SerializedName("fixed_time")
    @Expose
    private Object fixedTime;
    @SerializedName("id")
    @Expose
    private long id;
    @SerializedName("in_zone")
    @Expose
    private String inZone;
    @SerializedName("lat")
    @Expose
    private Object lat;
    @SerializedName("lng")
    @Expose
    private Object lng;
    @SerializedName("on_duty")
    @Expose
    private String onDuty;
    @SerializedName("online")
    @Expose
    private String online;
    @SerializedName("speed")
    @Expose
    private Object speed;
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
    private String uniqueId;
    @SerializedName("user")
    @Expose
    private long user;

    public String getAppVersion() {
        return appVersion;
    }

    public void setAppVersion(String appVersion) {
        this.appVersion = appVersion;
    }

    public Object getCourse() {
        return course;
    }

    public void setCourse(Object course) {
        this.course = course;
    }

    public Object getCurrentDuty() {
        return currentDuty;
    }

    public void setCurrentDuty(Object currentDuty) {
        this.currentDuty = currentDuty;
    }

    public UserDeviceDetailsPojo getDeviceDetails() {
        return deviceDetails;
    }

    public void setDeviceDetails(UserDeviceDetailsPojo deviceDetails) {
        this.deviceDetails = deviceDetails;
    }

    public String getDeviceToken() {
        return deviceToken;
    }

    public void setDeviceToken(String deviceToken) {
        this.deviceToken = deviceToken;
    }

    public String getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }

    public long getExternalId() {
        return externalId;
    }

    public void setExternalId(long externalId) {
        this.externalId = externalId;
    }

    public Object getFixedTime() {
        return fixedTime;
    }

    public void setFixedTime(Object fixedTime) {
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

    public Object getLat() {
        return lat;
    }

    public void setLat(Object lat) {
        this.lat = lat;
    }

    public Object getLng() {
        return lng;
    }

    public void setLng(Object lng) {
        this.lng = lng;
    }

    public String getOnDuty() {
        return onDuty;
    }

    public void setOnDuty(String onDuty) {
        this.onDuty = onDuty;
    }

    public String getOnline() {
        return online;
    }

    public void setOnline(String online) {
        this.online = online;
    }

    public Object getSpeed() {
        return speed;
    }

    public void setSpeed(Object speed) {
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

    public String getUniqueId() {
        return uniqueId;
    }

    public void setUniqueId(String uniqueId) {
        this.uniqueId = uniqueId;
    }

    public long getUser() {
        return user;
    }

    public void setUser(long user) {
        this.user = user;
    }
}
