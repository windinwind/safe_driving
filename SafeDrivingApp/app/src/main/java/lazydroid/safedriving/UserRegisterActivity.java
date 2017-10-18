package lazydroid.safedriving;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.io.BufferedWriter;
import java.io.IOException;

import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class UserRegisterActivity extends AppCompatActivity {

    public static String registerURL = "http://34.210.113.123/register";
    private static Activity activity = null;
    private static TextView feedback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_register);

        activity = this;
        feedback = (TextView) findViewById(R.id.feedback_box);
    }

    protected void registerButtonClicked(View v){
        EditText userNameEdit = (EditText) findViewById(R.id.user_name);
        EditText passwordEdit = (EditText) findViewById(R.id.password);
        String username = userNameEdit.getText().toString();
        String password = passwordEdit.getText().toString();

        if(username == null || password == null || username.equals("") || password.equals("")){
            Log.d("register", "with empty string");
            return;
        }

        Log.d("username = ", username);
        Log.d("password = ", password);
        //new UserRegisterActivity.NetworkConnection().execute(username, password);
        new NetworkService().execute("register_post", username, password, "0");
    }

    public static void updateStatus(boolean success){

        if(success){
            feedback.setText("Login success");
            //finish this activity with success result
            activity.setResult(RESULT_OK, null);
            activity.finish();

        }else{
            feedback.setText("Invalid username or password");
        }
    }

}
