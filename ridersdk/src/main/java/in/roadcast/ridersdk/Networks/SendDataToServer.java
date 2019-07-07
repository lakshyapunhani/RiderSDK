package in.roadcast.ridersdk.Networks;

import android.content.Context;
import android.os.AsyncTask;

import in.roadcast.ridersdk.Helper.HelperClient;
import in.roadcast.ridersdk.Managers.SessionManager;
import in.roadcast.ridersdk.RealmModels.LocationRealm;
import in.roadcast.ridersdk.RealmModels.UserActivitiesRealm;
import in.roadcast.ridersdk.RealmModels.UserRealm;
import in.roadcast.ridersdk.Utils.MyConstants;
import io.realm.Realm;

public class SendDataToServer extends AsyncTask<Void, Void, Void> {

    private Context mContext;

    public SendDataToServer(Context mContext) {
        this.mContext = mContext;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        sendDataToServer(mContext);
    }

    @Override
    protected Void doInBackground(Void... params) {

        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);

    }

    public synchronized void sendDataToServer(Context mContext)
    {
        SessionManager sessionManager = HelperClient.getSessionManager(mContext);

        if (sessionManager.isInternetAvailable())
        {
            try (Realm realm = Realm.getDefaultInstance())
            {
                if ((realm.where(LocationRealm.class).count() > 0
                        || realm.where(UserRealm.class).equalTo("uniqueId",
                                sessionManager.getApiKey()).count() > 0)
                        && !HelperClient.getCommonMethods().checkDutyConfig(mContext)
                        && !sessionManager.getTrackingModule().matches("2"))
                {
                    new SendLocationsAsync(mContext).executeOnExecutor(AsyncTask.SERIAL_EXECUTOR);
                }

                if (realm.where(UserActivitiesRealm.class).equalTo("sentToServerStatus",
                        MyConstants.SENT_TO_SERVER_NOT).count() > 0)
                {
                    HelperClient.getUserActivityToServer(mContext).requestUserActivity();
                }
            }



            }
        }

}
