package in.roadcast.ridersdk.Managers;

import android.content.Context;
import android.content.SharedPreferences;
import android.location.Location;

import androidx.annotation.Keep;

@Keep
public class SessionManager
{
    static SharedPreferences pref;
    static SharedPreferences.Editor editor;
    Context _context;

    private static final String PREF_NAME = "in.roadcast.ridersdk";
    int PRIVATE_MODE = 0;

    public SessionManager(Context context)
    {
        this._context = context.getApplicationContext();
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    private String ISINTERNETAVAILABLE="isinternetavailable";
    private static final String ACCESS_TOKEN = "accessToken";
    private static final String USER_ID = "USER_ID";
    private static final String COMPANY_ID = "COMPANY_ID";
    private static final String IS_AUTHENTIFIED = "is_authenticated";
    private static final String API_KEY= "api_key";
    private static final String STILL_COUNT = "still_count";
    private static final String REQUESTING_LOCATION_UPDATES = "requesting_location_updates";
    private static final String LAST_LOCATION_LATITUDE = "last_location_latitude";
    private static final String LAST_LOCATION_LONGITUDE = "last_location_longitude";
    private static final String MOCK_LOCATION_ACTIVATED = "mock_location_activated";
    private static final String LASTLOCATION_TIME ="last_location_time" ;
    private static final String TRACKING_MODULE = "trackingModule";
    private static final String ON_DUTY = "on_duty";
    private static final String MOBILE_NUMBER = "mobile_number";
    private static final String USER_NAME = "user_name";
    private static final String PROFILE_ID = "profileId";

    public void setContinuousStillCountTime(long stillCountTime)
    {
        editor.putLong(STILL_COUNT,stillCountTime).commit();
    }

    public long getContinuousStillCountTime()
    {
        return pref.getLong(STILL_COUNT,0);
    }

    public void setRequestingLocationUpdates(boolean isRequestingUpdates) {
        editor.putBoolean(REQUESTING_LOCATION_UPDATES, isRequestingUpdates).commit();
    }

    public boolean isRequestingLocationUpdates() {
        return pref.getBoolean(REQUESTING_LOCATION_UPDATES, false);
    }

    public void setInternetAvailable(boolean b) {
        editor.putBoolean(ISINTERNETAVAILABLE,b);
        editor.commit();
    }

    public Boolean isInternetAvailable() {
        return pref.getBoolean(ISINTERNETAVAILABLE,true);
    }

    public void setAccessToken(String accessToken)
    {
        editor.putString(ACCESS_TOKEN,accessToken).commit();
    }

    public String getAccessToken()
    {
        return pref.getString(ACCESS_TOKEN,"");
    }

    public void setApiKey(String apiKey)
    {
        editor.putString(API_KEY,apiKey).commit();
    }

    public String getApiKey()
    {
        return pref.getString(API_KEY,"");
    }

    public void setLoginId(long loginId)
    {
        editor.putLong(USER_ID, loginId);
        editor.commit();
    }

    public long getLoginId() {
        return pref.getLong(USER_ID, 3);
    }

    public void setIsAuthentified(boolean isAuthentified)
    {
        editor.putBoolean(IS_AUTHENTIFIED, isAuthentified);
        editor.commit();
    }

    public boolean isAuthentified() {
        return pref.getBoolean(IS_AUTHENTIFIED, false);
    }

    public void saveLastUpdatedLatitudeAndLongitude(Location location) {
        editor.putString(LAST_LOCATION_LATITUDE, String.valueOf(location.getLatitude()));
        editor.putString(LAST_LOCATION_LONGITUDE, String.valueOf(location.getLongitude()));
        editor.commit();

    }

    public double getLastUpdatedLatitude() {
        return Double.parseDouble(pref.getString(LAST_LOCATION_LATITUDE, "0"));
    }

    public double getLastUpdatedLongitude() {
        return Double.parseDouble(pref.getString(LAST_LOCATION_LONGITUDE, "0"));
    }

    public void setMockLocationActivated(boolean isMock) {
        editor.putBoolean(MOCK_LOCATION_ACTIVATED, isMock).commit();
    }

    public boolean isMockLocationActivated() {
        return pref.getBoolean(MOCK_LOCATION_ACTIVATED, false);
    }

    public void setLastLocationTime(long time) {
        editor.putLong(LASTLOCATION_TIME,time);
        editor.commit();
    }

    public long getLastLocationTime(){
        return  pref.getLong(LASTLOCATION_TIME,0);
    }

    public void setTrackingModule(String trackingModule)
    {
        editor.putString(TRACKING_MODULE,trackingModule).commit();
    }

    public String getTrackingModule()
    {
        return pref.getString(TRACKING_MODULE,"0");
    }

    public void put_duty_status(String duty_status_1)
    {
        // Storing login value as TRUE
        editor.putString(String.valueOf(ON_DUTY), duty_status_1);
        editor.commit();
    }

    public String get_duty_status() {
        // Storing login value as TRUE
        return pref.getString(ON_DUTY, "true");

    }

    public void setCompanyId(long companyId)
    {
        editor.putLong(COMPANY_ID, companyId);
        editor.commit();
    }

    public long getCompanyId()
    {
        return pref.getLong(COMPANY_ID, 2);
    }

    public void saveMobileNumber (String mobileNumber) {
        editor.putString(MOBILE_NUMBER, mobileNumber).commit();
    }

    public String getMobileNumber () {
        return pref.getString(MOBILE_NUMBER, "0000000000");
    }

    public void saveUsername (String username) {
        if (username == null) {
            return;
        }
        editor.putString(USER_NAME, username).commit();
    }

    public String getUserName () {
        return pref.getString(USER_NAME, "Not Available");
    }

    public void saveUserProfileId(String profileId) {
        editor.putString(PROFILE_ID, profileId).commit();
    }

    public String getUserProfileId() {
        return pref.getString(PROFILE_ID,"0");
    }


}


