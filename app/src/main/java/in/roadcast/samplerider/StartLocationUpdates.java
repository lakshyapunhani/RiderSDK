package in.roadcast.samplerider;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;


public class StartLocationUpdates extends AppCompatActivity {

    Button button;
    boolean updatesStarted;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_location_updates);
        button = findViewById(R.id.startUpdates);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (updatesStarted)
                {
                    //ForegroundServiceLauncher.getInstance().startService(StartLocationUpdates.this);
                    updatesStarted = true;
                }
                else
                {
                   // ForegroundServiceLauncher.getInstance().stopService(StartLocationUpdates.this);
                    updatesStarted = false;
                }
                updateUI();
            }
        });
    }

    private void updateUI()
    {
        if (updatesStarted)
        {
            button.setText("Stop updates");
        }
        else
        {
            button.setText("Start updates");
        }
    }
}
