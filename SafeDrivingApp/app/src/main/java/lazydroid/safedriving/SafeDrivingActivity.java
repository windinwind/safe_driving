package lazydroid.safedriving;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

public class SafeDrivingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_safe_driving);

    }

    protected void loginButtonClicked(View v) {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivityForResult(intent, 1);
        //startActivity(intent);
    }

    protected void startDrivingButtonClicked(View v) {
        Intent intent = new Intent(this, LockScreenActivity.class);
        badBehaviorCount.getCount(this);
        startActivity(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //login success
        if(resultCode == RESULT_OK){
            //set username
            TextView username = (TextView) findViewById(R.id.after_login_username);
            if(UserInfo.getUsername() != null) {
                username.setText(UserInfo.getUsername());
            }

            //hide login button
            View loginbutton = findViewById(R.id.login_button);
            loginbutton.setVisibility(View.GONE);
        }

    }
}