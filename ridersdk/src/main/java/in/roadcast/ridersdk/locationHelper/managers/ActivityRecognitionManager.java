package in.roadcast.ridersdk.locationHelper.managers;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.location.ActivityRecognitionClient;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import in.roadcast.ridersdk.Utils.MyConstants;
import in.roadcast.ridersdk.locationHelper.services.DetectedActivitiesIntentService;


public class ActivityRecognitionManager {
    private Context context;
    private Intent mIntentService;
    private PendingIntent mPendingIntent;
    private ActivityRecognitionClient mActivityRecognitionClient;

    public ActivityRecognitionManager(Context context)
    {
        this.context = context;
        mActivityRecognitionClient = new ActivityRecognitionClient(context);
        mIntentService = new Intent(context, DetectedActivitiesIntentService.class);
        mPendingIntent = PendingIntent.getService(context, 1, mIntentService, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    public void requestActivityUpdatesButtonHandler() {
        Task<Void> task = mActivityRecognitionClient.requestActivityUpdates(
                MyConstants.DETECTION_INTERVAL_IN_MILLISECONDS,
                mPendingIntent);

        task.addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void result) {
                Log.d("Activity Update request", "Success");
                //Toast.makeText(context, "Successfully requested activity updates", Toast.LENGTH_SHORT).show();
            }
        });

        task.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("Activity Update request", "Fail");
                //Toast.makeText(context, "Requesting activity updates failed to start", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void removeActivityUpdatesButtonHandler() {
        Task<Void> task = mActivityRecognitionClient.removeActivityUpdates(
                mPendingIntent);
        task.addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void result) {
                //Toast.makeText(context, "Removed activity updates successfully!", Toast.LENGTH_SHORT).show();
            }
        });

        task.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                //Toast.makeText(context, "Failed to remove activity updates!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
