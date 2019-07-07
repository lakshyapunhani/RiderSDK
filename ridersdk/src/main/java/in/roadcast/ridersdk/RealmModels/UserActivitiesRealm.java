package in.roadcast.ridersdk.RealmModels;


import androidx.annotation.Keep;

import java.util.UUID;

import in.roadcast.ridersdk.Utils.MyConstants;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

@Keep
public class UserActivitiesRealm extends RealmObject {

    @PrimaryKey
    private String activityId;
    private long time;
    private String actionType;
    private String actionStatus;
    private double latitude;
    private double longitude;
    private String sentToServerStatus;

    public UserActivitiesRealm () {

    }

    public UserActivitiesRealm( String actionType, String actionStatus, UserRealm userRealm){
        this.actionType=actionType;
        this.actionStatus=actionStatus;
        this.time=System.currentTimeMillis();
        this.latitude=userRealm.getLatitude();
        this.longitude=userRealm.getLongitude();
        this.activityId= UUID.randomUUID().toString();
        this.sentToServerStatus = MyConstants.SENT_TO_SERVER_NOT;
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

    public String getActivityId() {
        return activityId;
    }

    public void setActivityId(String activityId) {
        this.activityId = activityId;
    }

    public String getActionStatus() {
        return actionStatus;
    }

    public void setActionStatus(String actionStatus) {
        this.actionStatus = actionStatus;
    }

    public String getSentToServerStatus() {
        return sentToServerStatus;
    }

    public void setSentToServerStatus(String sentToServerStatus) {
        this.sentToServerStatus = sentToServerStatus;
    }
}

