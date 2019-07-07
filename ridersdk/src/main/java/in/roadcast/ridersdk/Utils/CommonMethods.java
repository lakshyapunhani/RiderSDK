package in.roadcast.ridersdk.Utils;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.location.LocationManager;
import android.os.BatteryManager;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import in.roadcast.ridersdk.BuildConfig;
import in.roadcast.ridersdk.Helper.HelperClient;
import in.roadcast.ridersdk.Managers.SessionManager;


public class CommonMethods {

    private int counter = 0;

    public CommonMethods() {
    }


    public boolean hasGPSDevice(Context context) {
        final LocationManager mgr = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        if (mgr == null)
            return false;
        final List<String> providers = mgr.getAllProviders();
        return providers != null && providers.contains(LocationManager.GPS_PROVIDER);
    }

    public boolean isPhoneNumberValid(String phoneNo) {
        String expression = "^[0-9-+]{9,15}$";
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(phoneNo);
        return (matcher.matches());
    }

    public static boolean isServiceRunning(Context ctx, Class<?> serviceClass) {

        ActivityManager manager = (ActivityManager) ctx.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    public static void startService(Context context, Class<?> serviceClass) {

    }

    public String convertDate(long timestamp, String pattern, boolean toUtc) {

        if (timestamp <= 9999999999L) {
            timestamp = timestamp * 1000;
        }
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        sdf.setTimeZone(TimeZone.getDefault());

        if (toUtc) {
            sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
        }
        Date date = new Date(timestamp);
        return sdf.format(date);
    }

    public String convertInMeridiem(String timestamp) {
        long time = getUnix(timestamp, "yyyy-MM-dd HH:mm:ss");
        if (time == 0)
        {
            return "";
        }
        else
        {
            return convertInMeridiem(getUnix(timestamp, "yyyy-MM-dd HH:mm:ss"));
        }
    }

    public String convertDate(String date) {
        return convertDate(getUnix(date, "yyyy-MM-dd HH:mm:ss"), "yyyy-MM-dd HH:mm:ss", false);
    }

    public String convertDatePattern(String date)
    {
        return convertDate(getUnix(date, "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"), "yyyy-MM-dd", false);
    }

    public String convertUTCToISTDate(String date, String patternFrom, String patternTo) {
        return convertDate(getUnix(date, patternFrom), patternTo, false);
    }

    public String convertInMeridiem(long unix) {

        String timestamp = convertDate(unix, "yyyy-MM-dd HH:mm:ss", false);
        SimpleDateFormat originalFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
        originalFormat.setTimeZone(TimeZone.getDefault());
        SimpleDateFormat targetFormat = new SimpleDateFormat("hh:mm a");
        targetFormat.setTimeZone(TimeZone.getDefault());
        SimpleDateFormat finalFormat = new SimpleDateFormat("hh:mm");
        finalFormat.setTimeZone(TimeZone.getDefault());
        String correctTime = "";
        Date date;
        try {
            date = originalFormat.parse(timestamp);
            String time = targetFormat.format(date);
            if (time.contains("p.m.") || time.contains("PM") || time.contains("pm") || time.contains("P.M.")) {
                correctTime = finalFormat.format(date) + " PM";
            } else if (time.contains("a.m.") || time.contains("AM") || time.contains("am") || time.contains("A.M.")) {
                correctTime = finalFormat.format(date) + " AM";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return correctTime;
    }

    public Bundle getBatteryPercentage(Context context) {
        IntentFilter iFilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        Intent batteryStatus = context.registerReceiver(null, iFilter);

        int level = batteryStatus != null ? batteryStatus.getIntExtra(BatteryManager.EXTRA_LEVEL, -1) : -1;
        int scale = batteryStatus != null ? batteryStatus.getIntExtra(BatteryManager.EXTRA_SCALE, -1) : -1;

        float battery = level / (float) scale;

        int status = batteryStatus != null ? batteryStatus.getIntExtra(BatteryManager.EXTRA_STATUS, -1) : -1;
        boolean isCharging = status == BatteryManager.BATTERY_STATUS_CHARGING ||
                status == BatteryManager.BATTERY_STATUS_FULL;

        Bundle bundl = new Bundle();
        bundl.putFloat("battery", battery);
        bundl.putBoolean("isCharging", isCharging);
        return bundl;

    }

    public String convertDate(Date date) {
        Calendar c = Calendar.getInstance();
        if (date.getDay() == c.getTime().getDay()) {
            return convertDate(date.getTime(), "HH:mm", false);
        } else {
            return convertDate(date.getTime(), "yyyy-MM-dd", false);
        }
    }

    public static File getDirectory(Context context) {
        //Find the dir to save cached images
        File cacheDir;

        if (android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED))
            cacheDir = new File(android.os.Environment.getExternalStorageDirectory(), "RoadCast");
        else
            cacheDir = context.getCacheDir();

        if (!cacheDir.exists())
            cacheDir.mkdirs();

        return cacheDir;
    }

    public long getUnix(String dateString, String pattern) {
        try {
            DateFormat formatter = new SimpleDateFormat(pattern);
            formatter.setTimeZone(TimeZone.getTimeZone("UTC"));
            Date date = null;
            date = formatter.parse(dateString);
            return date.getTime();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static void writeBitmapToFile(Bitmap bitmap, File file) {
//		File newFile=new File(Environment.getExternalStorageDirectory().getPath()+"/placeholder.png");
//		Bitmap bitmap= BitmapFactory.decodeResource(getResources(),R.drawable.placeholder);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        try {
            FileOutputStream out = new FileOutputStream(file);
            out.write(byteArray);
            out.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String getNetworkTypeName(int type) {
        switch (type) {
            case 1:
                return "GPRS";
            case 2:
                return "EDGE";
            case 3:
                return "UMTS";
            case 8:
                return "HSDPA";
            case 9:
                return "HSUPA";
            case 10:
                return "HSPA";
            case 4:
                return "CDMA";
            case 5:
                return "CDMA - EvDo rev. 0";
            case 6:
                return "CDMA - EvDo rev. A";
            case 12:
                return "CDMA - EvDo rev. B";
            case 7:
                return "CDMA - 1xRTT";
            case 13:
                return "LTE";
            case 14:
                return "CDMA - eHRPD";
            case 11:
                return "iDEN";
            case 15:
                return "HSPA+";
            case 16:
                return "GSM";
            case 17:
                return "TD_SCDMA";
            case 18:
                return "IWLAN";
            case 19:
                return "LTE_CA";
            default:
                return "UNKNOWN";
        }
    }

    public static String getNetworkDataStatus(Context context, int type) {
        switch (type) {
            case 0:
                return "Connected";
            case 1:
                return "Connecting";
            case 2:
                return "Connected";
            default:
                return "Suspended";
        }
    }


    public HashMap<String, String> getRefreshTokenHashMap(Context context)
    {
        SessionManager sessionManager = HelperClient.getSessionManager(context);
        HashMap<String, String> hashMap = new HashMap<>();
//        hashMap.put("userId", sessionManager.getLoginId());
//        if (sessionManager.isOtpVerified()) {
//            String uniqueId = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
//            hashMap.put("userName", uniqueId);
//        }
//        else {
//            TelephonyManager mngr = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
//            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
//                hashMap.put("userName", "0");
//                return hashMap;
//            }
//            String imei = mngr != null ? mngr.getDeviceId() : null;
//            hashMap.put("userName", imei);
//        }
        return hashMap;
    }

    public void configTrackingModule(Context context) {
        SessionManager sessionManager = HelperClient.getSessionManager(context);
        switch (sessionManager.getTrackingModule()){
            case "1":   // Interval tracking
                HelperClient.getLiveTrackingPositionProvider(context).removeUpdates(false);
//                if (!checkDutyConfig(context)) {
//                    HelperClient.getIntervalTrackingPositionProvider(context).startIntervalUpdates(true);
//                } else {
//                    HelperClient.getIntervalTrackingPositionProvider(context).removeIntervalUpdates(true);
//                }
                break;
            case "2":   // No tracking
                if (sessionManager.isRequestingLocationUpdates()) {
                    HelperClient.getLiveTrackingPositionProvider(context).removeUpdates(false);
                    // HelperClient.getIntervalTrackingPositionProvider(context).removeIntervalUpdates(false);
                }
                break;
            case "0":
                //HelperClient.getIntervalTrackingPositionProvider(context).removeIntervalUpdates(false);
                if (!checkDutyConfig(context)) {
                    HelperClient.getLiveTrackingPositionProvider(context).startUpdates(true);
                } else {
                    HelperClient.getLiveTrackingPositionProvider(context).removeUpdates(true);
                }
                break;
            default:    // Live tracking
                //HelperClient.getIntervalTrackingPositionProvider(context).removeIntervalUpdates(false);
                if (!checkDutyConfig(context)) {
                    HelperClient.getLiveTrackingPositionProvider(context).startUpdates(true);
                } else {
                    HelperClient.getLiveTrackingPositionProvider(context).removeUpdates(true);
                }
                break;
        }
    }

    public boolean checkDutyConfig(Context context) {
        SessionManager sessionManager = HelperClient.getSessionManager(context);
        if (sessionManager.get_duty_status().matches("false"))
        {
            return true;//Stop Tracking
        } else {
            return false;
        }

    }


    public void isAutomaticTimeZoneEnabled(final Context context) {
        if (!BuildConfig.DEBUG) {
            AlertDialog.Builder builderTimeZone = new AlertDialog.Builder(context);
            builderTimeZone.setMessage("Your system date needs to be set automatically to function properly. Please change the date");
            builderTimeZone.setCancelable(false);
            builderTimeZone.setPositiveButton("OK",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            context.startActivity(new Intent(android.provider.Settings.ACTION_DATE_SETTINGS));
                            dialog.dismiss();
                        }
                    });

            AlertDialog alertTime = builderTimeZone.create();


            try {
                if (Settings.Global.getInt(context.getContentResolver(), Settings.Global.AUTO_TIME) == 1) {
                    // Enabled
                    if (alertTime.isShowing() && !((Activity) context).isDestroyed()) {
                        alertTime.dismiss();
                    }
                } else {
                    if (!alertTime.isShowing() && !((Activity) context).isDestroyed()) {
                        alertTime.show();
                    }
                    // Disabled
                }
            } catch (Settings.SettingNotFoundException e) {
                if (alertTime.isShowing() && !((Activity) context).isDestroyed()) {
                    alertTime.dismiss();
                }
                e.printStackTrace();

            }
        }
    }

    public void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }


}
