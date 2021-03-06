package lazydroid.safedriving;

import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.gc.materialdesign.widgets.Dialog;

public class SafeDrivingActivity extends ActionBarActivity {

    private static TextView point;
    private static TextView username;
    private ImageView user_portrait_icon;
    private ImageView safepoint_icon;
    private Button login_button;
    private Button logout_button;

    private LocationManager mLocationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_safe_driving);

        //initialize textview, image, buttons
        point = (TextView)findViewById(R.id.after_login_point);
        username = (TextView) findViewById(R.id.after_login_username);
        user_portrait_icon = (ImageView)findViewById(R.id.user_portrait_icon);
        safepoint_icon = (ImageView)findViewById(R.id.safe_point_icon);
        login_button = (Button)findViewById(R.id.login_button);
        logout_button = (Button)findViewById(R.id.logout_button);

        mLocationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        setLogoutGUI();
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (!mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            showGpsDisabledDialog();
        }
    }

    /*
     * Start login page. Track if login is successful
     */
    public void loginButtonClicked(View v) {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivityForResult(intent, 1);
    }

    /*
     * Start LockScreen. Track bad behaviour number
     */
    public void startDrivingButtonClicked(View v) {
        Intent intent = new Intent(this, LockScreenActivity.class);
        badBehaviorCount.getCount(this);
        startActivity(intent);
    }

    /*
     * Reset UserInfo.
     */
    public void logoutButtonClicked(View v){
        //reset username and password
        UserInfo.setUsername(null);
        UserInfo.setPassword(null);

        setLogoutGUI();
    }

    /*
     * set GUI to login state
     */
    private void setLoginGUI(){
        //display icons and logout button
        user_portrait_icon.setVisibility(View.VISIBLE);
        safepoint_icon.setVisibility(View.VISIBLE);
        logout_button.setVisibility(View.VISIBLE);

        //hide login button
        login_button.setVisibility(View.GONE);

        //set username
        if(UserInfo.getUsername() != null) {
            username.setText(UserInfo.getUsername());
        }
    }

    /*
     * set GUI to logout state
     */
    private void setLogoutGUI(){
        //display login button
        login_button.setVisibility(View.VISIBLE);

        //hide images, texts and logout button
        user_portrait_icon.setVisibility(View.INVISIBLE);
        safepoint_icon.setVisibility(View.INVISIBLE);
        logout_button.setVisibility(View.GONE);
        point.setText("");
        username.setText("");
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //login success
        if(resultCode == RESULT_OK){
            setLoginGUI();
            updateSafePointonGUI();
        }

    }


    public static void updateSafePointonGUI(){
        point.setText(Integer.toString(UserInfo.getSafepoint()));
    }

    public void storeButtonClicked(View v){
        //Toast.makeText(this, "Store button clicked", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, StoreActivity.class);
        startActivityForResult(intent, 1);
    }

    public void showGpsDisabledDialog(){
        Dialog dialog = new Dialog(this, getResources().getString(R.string.gps_disabled), getResources().getString(R.string.please_enable_gps));

        dialog.setOnAcceptButtonClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent("android.settings.LOCATION_SOURCE_SETTINGS"));
            }
        });
        dialog.show();
    }
}