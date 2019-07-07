package in.roadcast.ridersdk.locationHelper.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.google.android.gms.location.DetectedActivity;

import in.roadcast.ridersdk.Helper.HelperClient;
import in.roadcast.ridersdk.Managers.SessionManager;
import in.roadcast.ridersdk.R;
import in.roadcast.ridersdk.Utils.MyConstants;
import in.roadcast.ridersdk.locationHelper.listener.GPSLiveTrackingPositionProvider;

public class ActivityRecognitionBroadcastReceiver extends BroadcastReceiver {
    private Context context;
    private GPSLiveTrackingPositionProvider positionProvider;

    public ActivityRecognitionBroadcastReceiver(Context context) {
        this.context = context;
        positionProvider = HelperClient.getLiveTrackingPositionProvider(context);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(MyConstants.BROADCAST_DETECTED_ACTIVITY)) {
            int type = intent.getIntExtra("type", -1);
            int confidence = intent.getIntExtra("confidence", 0);
            handleUserActivity(type, confidence);
        }
    }

    private void handleUserActivity(int type, int confidence) {
        String label = context.getString(R.string.activity_unknown);

        SessionManager sessionManager = HelperClient.getSessionManager(context);

        switch (type) {
            case DetectedActivity.IN_VEHICLE: {
                label = context.getString(R.string.activity_in_vehicle);
                break;
            }
            case DetectedActivity.ON_BICYCLE: {
                label = context.getString(R.string.activity_on_bicycle);
                break;
            }
            case DetectedActivity.ON_FOOT: {
                label = context.getString(R.string.activity_on_foot);
                break;
            }
            case DetectedActivity.TILTING: {
                label = context.getString(R.string.activity_tilting);
                break;
            }
            case DetectedActivity.RUNNING: {
                label = context.getString(R.string.activity_running);
                break;
            }
            case DetectedActivity.STILL: {
                label = context.getString(R.string.activity_still);
                break;
            }
            case DetectedActivity.WALKING: {
                label = context.getString(R.string.activity_walking);
                break;
            }
            case DetectedActivity.UNKNOWN: {
                label = context.getString(R.string.activity_unknown);
                break;
            }
        }

        Log.e("TAG", "User activity: " + label + ", Confidence: " + confidence);

        if (confidence > MyConstants.CONFIDENCE && !label.matches(context.getString(R.string.activity_tilting))) {
//            if (sessionManager.shouldSaveLogs()) {
//                RealmManager.saveLogs(this.getClass().getSimpleName(), HelperClient.getCommonMethods().convertDate(System.currentTimeMillis(), "HH:mm:ss", false) + " ", "Activity Detection", label);
//            }
            if (!label.matches(context.getString(R.string.activity_still)))
            {
                //Toast.makeText(context, label + " " + confidence, Toast.LENGTH_SHORT).show();
                if (sessionManager.getContinuousStillCountTime() != 0) {
                    sessionManager.setContinuousStillCountTime(0);
                }
                if (sessionManager.isAuthentified()) {
                    if (!sessionManager.isRequestingLocationUpdates()) {
                        HelperClient.getCommonMethods().configTrackingModule(context);
                    }
                }
            } else {
                if (sessionManager.isRequestingLocationUpdates()) {
                    //Toast.makeText(context, label + " " + confidence, Toast.LENGTH_SHORT).show();
                    if (sessionManager.getContinuousStillCountTime() == 0) {
                        sessionManager.setContinuousStillCountTime(System.currentTimeMillis());
                    }
//                    positionProvider.checkForGPSstatus();
                }
            }
        }
    }
}
