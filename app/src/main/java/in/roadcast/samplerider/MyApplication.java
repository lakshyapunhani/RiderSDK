package in.roadcast.samplerider;

import android.app.Application;

import in.roadcast.ridersdk.RealmModules;
import io.realm.Realm;
import io.realm.RealmConfiguration;

public class MyApplication extends Application
{
    @Override
    public void onCreate()
    {
        Realm.init(this);

        RealmConfiguration config = new RealmConfiguration.Builder()
                .name("ridersdk_default.realm")
                .modules(new RealmModules())
                .build();

        Realm.setDefaultConfiguration(config);
        super.onCreate();
    }
}
