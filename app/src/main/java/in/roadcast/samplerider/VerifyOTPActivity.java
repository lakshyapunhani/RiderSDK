package in.roadcast.samplerider;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import in.roadcast.ridersdk.Roadcast;
import in.roadcast.ridersdk.RoadcastDelegate;

public class VerifyOTPActivity extends AppCompatActivity {

    EditText editText;
    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_otp);

        editText = findViewById(R.id.edt_otp);
        button = findViewById(R.id.btn_submitOtp);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String otp = editText.getText().toString().trim();
                if (otp.isEmpty()) {
                    Toast.makeText(VerifyOTPActivity.this, "Please enter otp", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED || checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        return;
                    }
                }
                Roadcast.INSTANCE(VerifyOTPActivity.this).confirmOtp(otp, new RoadcastDelegate() {
                    @Override
                    public void success() {
                        startActivity(new Intent(VerifyOTPActivity.this, MainActivity.class));
                        finish();
                    }

                    @Override
                    public void failure(String error) {
                        Toast.makeText(VerifyOTPActivity.this, "Issue", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

    }
}
