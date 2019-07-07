package in.roadcast.ridersdk.Pojo;

import in.roadcast.ridersdk.RealmModels.UserActivitiesRealm;

public class UserActivitiesRetro {
    private long time;
    private String actionType;              // "duty"
    private String actionStatus;            // "0" for offDuty & "1" for onDuty
    private double latitude;
    private double longitude;
    private String activityId;

    public UserActivitiesRetro(){

    }

    public UserActivitiesRetro(UserActivitiesRealm userActivitiesRealm){
        actionType=userActivitiesRealm.getActionType();
        time=userActivitiesRealm.getTime();
        latitude=userActivitiesRealm.getLatitude();
        longitude=userActivitiesRealm.getLongitude();
        actionStatus=userActivitiesRealm.getActionStatus();
        activityId = userActivitiesRealm.getActivityId();
    }

    public String getActionType() {
        return actionType;
    }

    public void setActionType(String actionType) {
        this.actionType = actionType;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
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

    public String getActionStatus() {
        return actionStatus;
    }

    public void setActionStatus(String actionStatus) {
        this.actionStatus = actionStatus;
    }

    public String getActivityId() {
        return activityId;
    }

    public void setActivityId(String activityId) {
        this.activityId = activityId;
    }
}
