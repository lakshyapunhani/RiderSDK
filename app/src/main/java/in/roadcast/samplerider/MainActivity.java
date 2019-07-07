package in.roadcast.samplerider;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import in.roadcast.ridersdk.Roadcast;
import in.roadcast.ridersdk.Pojo.UserPojo;
import in.roadcast.ridersdk.RoadcastDelegate;

public class MainActivity extends AppCompatActivity
{
    Button startUpdatesButton;
    Button stopUpdatesButton;
    Button onDuty;
    Button offDuty;
    Button changeTitleButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (!Roadcast.INSTANCE(this).isGooglePlayServicesAvailable(MainActivity.this))
        {
            Toast.makeText(this, "No google play services available", Toast.LENGTH_SHORT).show();
        }

        startUpdatesButton = findViewById(R.id.startUpdatesButton);
        stopUpdatesButton = findViewById(R.id.stopUpdatesButton);
        onDuty = findViewById(R.id.btn_onDuty);
        offDuty = findViewById(R.id.btn_offDuty);
        changeTitleButton = findViewById(R.id.changeTitleButton);
        //startUpdatesButton.setEnabled(true);
        // stopUpdatesButton.setEnabled(false);

        startUpdatesButton.setOnClickListener(view -> {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                        || checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "Please grant all permissions", Toast.LENGTH_SHORT).show();
                    return;
                }
            }

            Roadcast.INSTANCE(MainActivity.this).startLocationUpdates(new RoadcastDelegate() {
                @Override
                public void success() {
                    Toast.makeText(MainActivity.this, "Updates started", Toast.LENGTH_SHORT).show();
                    //startUpdatesButton.setEnabled(false);
                   // stopUpdatesButton.setEnabled(true);
                }

                @Override
                public void failure(String error) {
                    Toast.makeText(MainActivity.this, "Issue", Toast.LENGTH_SHORT).show();
                }
            });
        });

        stopUpdatesButton.setOnClickListener(view -> {
            Roadcast.INSTANCE(MainActivity.this).stopLocationUpdates();
            Toast.makeText(MainActivity.this, "Updates Stopped", Toast.LENGTH_SHORT).show();
            //startUpdatesButton.setEnabled(true);
            //stopUpdatesButton.setEnabled(false);
        });

        onDuty.setOnClickListener(view ->
        {
            Roadcast.INSTANCE(MainActivity.this).setDutyStatus("true", new RoadcastDelegate() {
                @Override
                public void success() {
                    Toast.makeText(MainActivity.this, "Duty enabled", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void failure(String error) {
                    Toast.makeText(MainActivity.this, error, Toast.LENGTH_SHORT).show();
                }
            });
        });

        offDuty.setOnClickListener(view ->
        {
            Roadcast.INSTANCE(MainActivity.this).setDutyStatus("false", new RoadcastDelegate() {
                @Override
                public void success() {
                    Toast.makeText(MainActivity.this, "Duty disabled", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void failure(String error) {
                    Toast.makeText(MainActivity.this, error, Toast.LENGTH_SHORT).show();
                }
            });
        });

        changeTitleButton.setOnClickListener(view -> Roadcast.INSTANCE(MainActivity.this).addNotificationIconsAndTitle(R.string.app_name,R.drawable.ic_launcher));

    }

}
