package in.roadcast.ridersdk.Receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import in.roadcast.ridersdk.Helper.HelperClient;
import in.roadcast.ridersdk.Managers.SessionManager;


public class BootUpReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.
        Log.d(this.getClass().getSimpleName(),"In On Receive method.");

        SessionManager sessionManager = HelperClient.getSessionManager(context);

//        if(sessionManager.isAuthentified())
//        {
//            Intent i = new Intent(context, SplashActivity.class);
//            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            context.startActivity(i);
//        }
    }

}
