package lazydroid.safedriving;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


public class LoginActivity extends AppCompatActivity {

    public static String loginURL = "http://34.210.113.123/login";
    private static Activity activity = null;
    private static TextView feedback;

    private EditText userNameEdit;
    private EditText passwordEdit;

    private CheckBox remember;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        activity = this;
        feedback = (TextView) findViewById(R.id.feedback_box);

        remember = (CheckBox) findViewById(R.id.remember_user_info);
        String password = "";
        if(remember.isChecked()){
            password = UserInfo.getStoredUserInfo(this);
        }

        userNameEdit = (EditText) findViewById(R.id.user_name);
        passwordEdit = (EditText) findViewById(R.id.password);
        userNameEdit.setText(UserInfo.getUsername());
        passwordEdit.setText(password);

    }

    /*
     * Do user authentication when login button clicked
     */
    public void loginButtonClicked(View v) {
        //get user name and password
        String username = userNameEdit.getText().toString();
        String password = passwordEdit.getText().toString();
        if(username == null || password == null || username.equals("") || password.equals("")){
            Log.d("login", "with empty string");
            return;
        }

        if(remember.isChecked()){
            //store current userinfo
            UserInfo.setUsername(username);
            UserInfo.setPassword(password);
            UserInfo.updateStoredUserInfo(this);
        }else{
            //clear stored userinfo
            UserInfo.setUsername("");
            UserInfo.setPassword("");
            UserInfo.updateStoredUserInfo(this);
        }
        //send user info to server
        //new NetworkConnection().execute(username, password);
        new NetworkService().execute("login_post", username, password, "0");
    }

    public static void updateStatus(boolean success){

        if(success && UserInfo.getUsername() != null){
            feedback.setText("Login success");
            Toast.makeText(activity, "Login success", Toast.LENGTH_SHORT).show();
            //finish this activity with success result
            activity.setResult(RESULT_OK, null);
            activity.finish();

        }else{
            feedback.setText("Invalid username or password");
            Toast.makeText(activity, "Invalid username or password", Toast.LENGTH_SHORT).show();
        }
    }

    /*
     * Launch register page
     */
    public void registerButtonClicked(View v){
        Intent intent = new Intent(this, UserRegisterActivity.class);
        startActivity(intent);
    }
}