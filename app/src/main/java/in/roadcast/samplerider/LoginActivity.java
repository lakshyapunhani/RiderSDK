package in.roadcast.samplerider;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import in.roadcast.ridersdk.Roadcast;
import in.roadcast.ridersdk.RoadcastDelegate;

public class LoginActivity extends AppCompatActivity {

    EditText editText;
    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        editText = findViewById(R.id.edt_mobileNumber);
        button = findViewById(R.id.btn_submitMobile);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String mobileNum = editText.getText().toString().trim();
                if (mobileNum.isEmpty())
                {
                    Toast.makeText(LoginActivity.this, "Please enter mobile number", Toast.LENGTH_SHORT).show();
                    return;
                }

                Roadcast.INSTANCE(LoginActivity.this).loginUser(mobileNum, new RoadcastDelegate() {
                    @Override
                    public void success() {
                        startActivity(new Intent(LoginActivity.this,VerifyOTPActivity.class));
                        finish();
                    }

                    @Override
                    public void failure(String error) {
                        Toast.makeText(LoginActivity.this, "Issue", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }
}
