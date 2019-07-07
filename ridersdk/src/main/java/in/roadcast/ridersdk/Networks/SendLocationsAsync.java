package in.roadcast.ridersdk.Networks;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;

import java.io.IOException;
import java.util.List;

import in.roadcast.ridersdk.Helper.HelperClient;
import in.roadcast.ridersdk.Managers.RealmManager;
import in.roadcast.ridersdk.Managers.SessionManager;
import in.roadcast.ridersdk.RealmModels.LocationRealm;
import in.roadcast.ridersdk.RealmModels.UserRealm;
import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;

public class SendLocationsAsync extends AsyncTask<Void, Void, Void> {

    private Context mContext;

    private SessionManager sessionManager;

    private StringBuilder stringBuilder;

    private boolean shouldDeleteLocations;

    private int locationIndex = 0;


    public SendLocationsAsync(Context mContext) {
        this.mContext = mContext;
//        locRetroList = new ArrayList<>();
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        sessionManager = HelperClient.getSessionManager(mContext);
        shouldDeleteLocations = false;
        stringBuilder = new StringBuilder();
    }

    @Override
    protected Void doInBackground(Void... params) {
        initialiseData();
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
    }

    private void initialiseData() {
        if (sessionManager.isAuthentified()) {
            stringBuilder = new StringBuilder();
            try (Realm realm = Realm.getDefaultInstance()) {

                final Bundle bundle = HelperClient.getCommonMethods().getBatteryPercentage(mContext);        // get battery status fields
                final float battery = bundle.getFloat("battery", 0);
                final boolean isCharging = bundle.getBoolean("isCharging", false);
                UserRealm userRealm = realm.where(UserRealm.class).equalTo("uniqueId", sessionManager.getApiKey()).findFirst();
                if (userRealm != null) {

                    appendDataInStringBuilder(userRealm.getLatitude(), userRealm.getLongitude(), System.currentTimeMillis(),
                            battery, userRealm.getCourse(), userRealm.getAccuracy(), userRealm.getSpeed(),
                            userRealm.getProvider(), isCharging, "0", userRealm.getFullDayDistance());

                }
                shouldDeleteLocations = false;

                RealmResults<LocationRealm> tempLocList = realm.where(LocationRealm.class).findAllSorted("timestamp", Sort.ASCENDING);
                List<LocationRealm> realmLocationList = realm.copyFromRealm(tempLocList);

                if (realmLocationList.size() > 0) {
                    stringBuilder = new StringBuilder();
                    for (LocationRealm loc : realmLocationList) {

                        appendDataInStringBuilder(loc.getLatitude(), loc.getLongitude(), loc.getTimestamp(),
                                loc.getBatteryStatus(), loc.getCourse(), loc.getAccuracy(), loc.getSpeed(),
                                loc.getProvider(), loc.isCharging(), "1", loc.getFullDayDistance());

//                        locRetroList.add(new LocationRetro(loc, "1"));
                    }
                    locationIndex = realmLocationList.size();
                    shouldDeleteLocations = true;
                }
            }
            sendLocationRequest();
        }
    }

    private void appendDataInStringBuilder(double lat, double lng, long timestamp, float battery,
                                           float course, float accuracy, float speed, String locationProvider,
                                           boolean isCharging, String timeChange, float fullDayDistance) {
        String isChargingString = "0";
        String provider;

        String uniqueId = Settings.Secure.getString(mContext.getContentResolver(), Settings.Secure.ANDROID_ID);

        stringBuilder.append(uniqueId);
        stringBuilder.append(",");
        stringBuilder.append(lat);
        stringBuilder.append(",");
        stringBuilder.append(lng);
        stringBuilder.append(",");

        if (String.valueOf(timestamp).length() > 10) {
            long fixedTimestamp = (long) timestamp/1000;
            stringBuilder.append(fixedTimestamp);
        } else {
            stringBuilder.append(timestamp);
        }
        stringBuilder.append(",");
        stringBuilder.append(battery);
        stringBuilder.append(",");
        stringBuilder.append(course);
        stringBuilder.append(",");
        stringBuilder.append(accuracy);
        stringBuilder.append(",");
        stringBuilder.append(speed);
        stringBuilder.append(",");

        if (locationProvider.equalsIgnoreCase("gps")) {
            provider = "G";
        } else {
            provider = "F";
        }
        stringBuilder.append(provider);
        stringBuilder.append(",");

        if (isCharging) {
            isChargingString = "1";
        } else {
            isChargingString = "0";
        }
//        9,6979,28.70351,77.13579,1556803801,0.99,100,10,24,G,1,0,24577,1;
        stringBuilder.append(isChargingString);
        stringBuilder.append(",");
        stringBuilder.append(timeChange);
        stringBuilder.append(",");
        stringBuilder.append(fullDayDistance);
        stringBuilder.append(",");
        stringBuilder.append("1");
//        if (sessionManager.isInsideGeofence()) {
//
//        } else {
//            stringBuilder.append("0");
//        }
        stringBuilder.append(",");
        stringBuilder.append(sessionManager.getLoginId());
        stringBuilder.append(",");
        stringBuilder.append(sessionManager.getCompanyId());
        stringBuilder.append(";");

    }

    private void sendLocationRequest() {
        String locations = stringBuilder.toString();
        // Log.d("LOCATIONS", locations);

        RequestBody body =
                RequestBody.create(MediaType.parse("text/plain"), locations);
        Call<ResponseBody> call1 = HelperClient.getTestRequestInterface().locationToService(sessionManager.getAccessToken(), "text/plain", body);


        try {
            Response response = call1.execute();
            if (response.body() == null)
            {
//                if (HelperClient.getCommonMethods().shouldSaveLogs(sessionManager))
//                {
//                    RealmManager.saveLogs(this.getClass().getSimpleName(), "locationToServer", "ResponseJson",
//                            "ResponseBodyNull");
//                }
            }

            if (response.code() == 200)
            {
                if (shouldDeleteLocations)
                {
                    RealmManager.getInstance(mContext).cascadeDeleteLocations(locationIndex);
                    locationIndex = 0;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
