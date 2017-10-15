package lazydroid.safedriving;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class SafeDrivingActivity extends AppCompatActivity {

    private static TextView point;
    private ImageView user_portrait_icon;
    private ImageView safepoint_icon;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_safe_driving);

        //initialize textview for safepoint
        point = (TextView)findViewById(R.id.after_login_point);

        user_portrait_icon = (ImageView)findViewById(R.id.user_portrait_icon);
        safepoint_icon = (ImageView)findViewById(R.id.safe_point_icon);
        user_portrait_icon.setVisibility(View.INVISIBLE);
        safepoint_icon.setVisibility(View.INVISIBLE);
    }

    /*
     * Start login page. Track if login is successful
     */
    protected void loginButtonClicked(View v) {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivityForResult(intent, 1);
    }

    /*
     * Start LockScreen. Track bad behaviour number
     */
    protected void startDrivingButtonClicked(View v) {
        Intent intent = new Intent(this, LockScreenActivity.class);
        badBehaviorCount.getCount(this);
        startActivity(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //login success
        if(resultCode == RESULT_OK){
            user_portrait_icon.setVisibility(View.VISIBLE);
            safepoint_icon.setVisibility(View.VISIBLE);
            //set username
            TextView username = (TextView) findViewById(R.id.after_login_username);
            if(UserInfo.getUsername() != null) {
                username.setText(UserInfo.getUsername());
            }

            updateSafePointonGUI();

            //hide login button
            View loginbutton = findViewById(R.id.login_button);
            loginbutton.setVisibility(View.GONE);

            //UserInfo.setSafepoint(6666);
        }

    }


    public static void updateSafePointonGUI(){
        point.setText(Integer.toString(UserInfo.getSafepoint()));
    }
}