package in.roadcast.ridersdk;

import android.Manifest;
import android.app.Activity;
import android.content.Context;

import androidx.annotation.Keep;
import androidx.annotation.RequiresPermission;

import in.roadcast.ridersdk.Pojo.UserPojo;

@Keep
public class Roadcast extends RoadcastEmpl
{
    private static volatile Roadcast riderEnable;

    public static Roadcast INSTANCE(Context context)
    {
        if (riderEnable == null)
        {
            synchronized (Roadcast.class)
            {
                if (riderEnable == null)
                {
                    riderEnable = new Roadcast(context);
                }
            }
        }
        return riderEnable;
    }

    public Roadcast(Context context)
    {
        //Prevent form the reflection api.
        super(context);
        if (riderEnable != null)
        {
            throw new RuntimeException("Use getInstance() method to get the single instance of this class.");
        }
    }


    /**
     * Function to login
     * @param mobileNumber - The number to login
     */
    @Override
    public void loginUser(String mobileNumber, RoadcastDelegate delegate) {
        super.loginUser(mobileNumber, delegate);
    }

    @Override
    @RequiresPermission(allOf = {Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION})
    public void confirmOtp(String otp, RoadcastDelegate delegate) {
        super.confirmOtp(otp, delegate);
    }

    @Override
    public void startLocationUpdates(RoadcastDelegate delegate) {
        super.startLocationUpdates(delegate);
    }

    @Override
    public void stopLocationUpdates() {
        super.stopLocationUpdates();
    }

    /**
     * Function to check if google play services available or not in device
     */
    @Override
    public boolean isGooglePlayServicesAvailable(Activity activity) {
        return super.isGooglePlayServicesAvailable(activity); }


    @Override
    public void setDutyStatus(String status,RoadcastDelegate delegate) {
        super.setDutyStatus(status,delegate);
    }

    @Override
    public void addNotificationIconsAndTitle(int smallIcon,int title) {
        super.addNotificationIconsAndTitle(smallIcon,title);
    }

    @Override
    public boolean isLocationUpdateStarted() {
        return super.isLocationUpdateStarted();
    }

    @Override
    public boolean getDutyStatus() {
        return super.getDutyStatus();
    }
}
