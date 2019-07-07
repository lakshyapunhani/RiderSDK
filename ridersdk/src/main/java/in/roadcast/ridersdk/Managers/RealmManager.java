package in.roadcast.ridersdk.Managers;

import android.content.Context;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Keep;

import java.util.ArrayList;

import in.roadcast.ridersdk.Helper.HelperClient;
import in.roadcast.ridersdk.Pojo.UserActivitiesRetro;
import in.roadcast.ridersdk.Pojo.UserPojo;
import in.roadcast.ridersdk.RealmModels.LocationRealm;
import in.roadcast.ridersdk.RealmModels.UserActivitiesRealm;
import in.roadcast.ridersdk.RealmModels.UserRealm;
import in.roadcast.ridersdk.RealmModules;
import in.roadcast.ridersdk.RoadcastDelegate;
import in.roadcast.ridersdk.Utils.MyConstants;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmList;
import io.realm.RealmResults;
import io.realm.Sort;

@Keep
public class RealmManager
{
    private static volatile RealmManager sRealmManager;

    public static RealmManager getInstance(Context context)
    {
        if (sRealmManager==null)
        {
            synchronized (RealmManager.class)
            {
                if (sRealmManager == null)
                {
                    sRealmManager=new RealmManager(context);
                }
            }
        }
        return sRealmManager;
    }

    private RealmManager(Context context)
    {
        if (sRealmManager != null)
        {
            throw new RuntimeException("Use getInstance() method to get the single instance of this class.");
        }
//        else
//        {
//            Realm.init(context);
//
//            RealmConfiguration config = new RealmConfiguration.Builder()
//                    .name("ridersdk_default.realm")
//                    .modules(new RealmModules())
//                    .build();
//
//            Realm.setDefaultConfiguration(config);
//        }
    }

    public void writeFirstUserRealm(final Location location, final String uniqueId, RoadcastDelegate roadcastDelegate) {

        try (Realm realm = Realm.getDefaultInstance())
        {
            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm)
                {
                    UserRealm userRealm = new UserRealm();
                    userRealm.setUniqueId(uniqueId);
                    userRealm.setLatitude(location.getLatitude());
                    userRealm.setLongitude(location.getLongitude());

                    userRealm.setProvider(location.getProvider());
                    userRealm.setAccuracy(location.getAccuracy());
                    userRealm.setCourse(location.getBearing());
                    userRealm.setSpeed(location.getSpeed());
                    userRealm.setTimestamp(System.currentTimeMillis());

                    realm.insertOrUpdate(userRealm);
                    roadcastDelegate.success();
                }
            });
        }
    }

    public void writeLocationInRealm(final Context context, final Location location, final boolean addNew, final float distance) {
        try (Realm realm = Realm.getDefaultInstance())
        {
            Bundle bundle = HelperClient.getCommonMethods().getBatteryPercentage(context);        // get battery status fields
            final float battery = bundle.getFloat("battery", 0);
            final boolean isCharging = bundle.getBoolean("isCharging", false);
            realm.executeTransactionAsync(new Realm.Transaction()
            {
                @Override
                public void execute(Realm bgRealm)
                {
                    SessionManager sessionManager = HelperClient.getSessionManager(context);

                    UserRealm userRealm = bgRealm.where(UserRealm.class).equalTo("uniqueId", sessionManager.getApiKey()).findFirst();
                    //UserRealm userRealm = bgRealm.where(UserRealm.class).equalTo("userId", sessionManager.getLoginId()).findFirst();
                    if (userRealm == null)
                    {
                        userRealm = new UserRealm();
                        userRealm.setUniqueId(HelperClient.getSessionManager(context).getApiKey());
                        //userRealm.setAttendanceSessionStatus(AttendanceStatus.NOT_STARTED);
                    }

                    //location table
                    if (addNew) {
                        if (sessionManager.getLastUpdatedLatitude() != location.getLatitude() &&
                                sessionManager.getLastUpdatedLongitude() != location.getLongitude()) {

                            userRealm.setLatitude(location.getLatitude());
                            userRealm.setLongitude(location.getLongitude());
                            userRealm.setFullDayDistance(distance);

                            LocationRealm newLocation = new LocationRealm(location,battery,isCharging);
                            newLocation.setFullDayDistance(distance);

                            sessionManager.saveLastUpdatedLatitudeAndLongitude(location);

                            bgRealm.insert(newLocation);                                        // add new one
                        }
                    }


                    // for updating the previous one only
                    userRealm.setProvider(location.getProvider());
                    userRealm.setAccuracy(location.getAccuracy());
                    userRealm.setCourse(location.getBearing());
                    userRealm.setSpeed(location.getSpeed());
                    userRealm.setTimestamp(System.currentTimeMillis());

                    bgRealm.insertOrUpdate(userRealm);

                }
            }, new Realm.Transaction.OnSuccess() {
                @Override
                public void onSuccess() {
                    Log.i(this.getClass().getSimpleName(), "transaction successful");
                }
            }, new Realm.Transaction.OnError() {
                @Override
                public void onError(Throwable error) {
                    Log.i(this.getClass().getSimpleName(), "transaction failed");
                }
            });
        }
    }

    public void updateTimeStamp(final String userId) {
        try (Realm realm = Realm.getDefaultInstance()) {
            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    UserRealm userRealm = realm.where(UserRealm.class).equalTo("userId", userId).findFirst();
                    if (userRealm != null) {
                        userRealm.setTimestamp(System.currentTimeMillis());
                    }
                }
            });
        }
    }

    public void cascadeDeleteLocations(final int index) {
        try (Realm realm = Realm.getDefaultInstance()) {
            final RealmResults<LocationRealm> realmLocationList = realm.where(LocationRealm.class).findAllSorted("timestamp", Sort.ASCENDING);
            if (realmLocationList.size() > 0 && index > 0 && realmLocationList.size() >= index) {
                realm.executeTransaction(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        RealmList<LocationRealm> locationList = new RealmList<>();

                        if (realmLocationList.size() >= index) {
                            for (int i = 0; i < index; i++) {
                                locationList.add(realmLocationList.get(i));
                            }
                        }

//                        locationList.addAll(realmLocationList.subList(0, index));
                        cascadeDelete(locationList);
                    }
                });
            }
        }
    }

    private void cascadeDelete(final RealmList<LocationRealm> list) {
        for (final LocationRealm loc : list) {
            if (loc.isValid()) {

                loc.deleteFromRealm();
            } else {
                Log.d("TAG", "Location not valid");
            }
        }
    }

    public UserPojo getUserData (String userId) {
        try (Realm realm = Realm.getDefaultInstance()){
            return new UserPojo(realm.where(UserRealm.class).equalTo("uniqueId", userId).findFirst());
        }
    }

    public void writeUserActivities(final String actionType, final String actionStatus, final String uniqueId) {

        if (isRedundantEvent(actionType, actionStatus))
        {
            return;
        }

        try (Realm realm = Realm.getDefaultInstance()) {
            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    UserRealm userRealm = realm.where(UserRealm.class).equalTo("uniqueId", uniqueId).findFirst();
                    if (userRealm != null) {
                        UserActivitiesRealm activity = new UserActivitiesRealm(actionType, actionStatus, userRealm);
                        realm.insertOrUpdate(activity);
                    }
                }
            });
        }
    }

    public boolean isRedundantEvent(final String actionType, final String actionStatus) {
        try (Realm realm = Realm.getDefaultInstance()) {
            RealmResults<UserActivitiesRealm> events = realm.where(UserActivitiesRealm.class).equalTo("actionType", actionType).findAll();

            if (events.size() != 0 && events.last().getActionStatus().equals(actionStatus)) {
                return true;
            } else {
                return false;
            }
        }
    }

    public void updateUserActivityStatusToInProgress(String activityId) {
        try (Realm realm = Realm.getDefaultInstance()){
            realm.executeTransactionAsync(new Realm.Transaction() {
                @Override
                public void execute(Realm bgRealm) {
                    UserActivitiesRealm userActivitiesRealm = bgRealm.where(UserActivitiesRealm.class).equalTo("activityId",activityId).findFirst();
                    if (userActivitiesRealm != null && userActivitiesRealm.isValid()) {
                        userActivitiesRealm.setSentToServerStatus(MyConstants.SENT_TO_SERVER_INPROGRESS);
                        bgRealm.insertOrUpdate(userActivitiesRealm);
                    }
                }
            });
        }
    }

    public ArrayList<UserActivitiesRetro> getUserActivityListToSend() {
        ArrayList<UserActivitiesRetro> arrayList = new ArrayList<>();
        try (Realm realm = Realm.getDefaultInstance()){
            RealmResults<UserActivitiesRealm> mList = realm.where(UserActivitiesRealm.class).findAllSorted("time", Sort.ASCENDING);
            for (UserActivitiesRealm userActivitiesRealm : mList) {
                if (userActivitiesRealm != null && userActivitiesRealm.isValid()) {
                    arrayList.add(new UserActivitiesRetro(userActivitiesRealm));
                }
            }
        }
        return arrayList;
    }

    public void deleteUserActivity(final String activityId) {   // to delete only activities which were sent to server

        try (Realm realm = Realm.getDefaultInstance()) {
            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    UserActivitiesRealm userActivitiesRealm = realm.where(UserActivitiesRealm.class).equalTo("activityId", activityId).findFirst();
                    if (userActivitiesRealm != null && userActivitiesRealm.isValid()) {
                        userActivitiesRealm.deleteFromRealm();
                    }

                }
            });
        }
    }

    public void updateUserActivityStatusToNotSent(String activityId) {
        try (Realm realm = Realm.getDefaultInstance()){
            realm.executeTransactionAsync(new Realm.Transaction() {
                @Override
                public void execute(Realm bgRealm) {
                    UserActivitiesRealm userActivitiesRealm = bgRealm.where(UserActivitiesRealm.class).equalTo("activityId",activityId).findFirst();
                    if (userActivitiesRealm != null && userActivitiesRealm.isValid()) {
                        userActivitiesRealm.setSentToServerStatus(MyConstants.SENT_TO_SERVER_NOT);
                        bgRealm.insertOrUpdate(userActivitiesRealm);
                    }
                }
            });
        }
    }
}
