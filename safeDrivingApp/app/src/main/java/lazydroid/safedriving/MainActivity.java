package lazydroid.safedriving;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;

import android.os.AsyncTask;

public class MainActivity extends AppCompatActivity {

    EditText userNameEdit;
    EditText passwordEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        userNameEdit = (EditText) findViewById(R.id.user_name);
        passwordEdit = (EditText) findViewById(R.id.password);

    }

    protected void loginButtonClicked(View v) {
        //System.out.println("button clicked");
        String username = userNameEdit.getText().toString();
        String password = passwordEdit.getText().toString();

        Log.d("username = ", username);
        Log.d("password = ", password);

        new NetworkConnection().execute(username, password);

    }
}
    class NetworkConnection extends AsyncTask<String, Object, Void> {

        @Override
        protected Void doInBackground(String... params) {
            if(params.length != 2) {
                Log.d( "doInBackground: ", "argument number wrong");
                return null;
            }
            String username = params[0];
            String password = params[1];
            //do network connection in async task
            try {
                URL url = new URL("http://192.168.31.147/login");
                //establish connection with the server login page
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

                //set user name and password into the get request
                Log.d("before encode name", username);
                Log.d("before encode password", password);
                String encoded = Base64.encodeToString((username + ":" + password).getBytes("UTF-8"),Base64.NO_WRAP);
                Log.d("encoded String", encoded);
                urlConnection.setRequestProperty("Authorization", "Basic "+encoded);
                urlConnection.setRequestMethod("GET");

                BufferedReader rd = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                //check the response from the server
                String login_status = rd.readLine();
                Log.d("login status is ", login_status);
                //TODO: if success, jump to start SD page
                //TODO: if fails, print response message

                rd.close();
                urlConnection.disconnect();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }
}
