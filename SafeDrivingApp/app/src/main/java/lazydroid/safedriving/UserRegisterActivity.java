package lazydroid.safedriving;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_register);
    }

    protected void registerButtonClicked(View v){
        EditText userNameEdit = (EditText) findViewById(R.id.user_name);
        EditText passwordEdit = (EditText) findViewById(R.id.password);
        String username = userNameEdit.getText().toString();
        String password = passwordEdit.getText().toString();

        Log.d("username = ", username);
        Log.d("password = ", password);
        new UserRegisterActivity.NetworkConnection().execute(username, password);

    }

    class NetworkConnection extends AsyncTask<String, Integer, Void> {

        private String username;

        @Override
        protected Void doInBackground(String... params) {
            if (params.length != 2) {
                Log.d("doInBackground: ", "argument number wrong");
                return null;
            }
            String username = params[0];
            String password = params[1];

            //do network connection
            try {
                URL url = new URL(registerURL);
                //establish connection with the server login page
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

                //set user name and password into the get request
                Log.d("before encode name", username);
                Log.d("before encode password", password);
                String password_hash = Base64.encodeToString((password.charAt(0) + username +
                        password.charAt(password.length()-1) +
                        password + "lazyDroid").getBytes("UTF-8"), Base64.NO_WRAP);
                String encoded = "username:" + username + "\npassword:" + password_hash;
                Log.d("encoded String", encoded);
                urlConnection.setRequestMethod("POST");

                OutputStream outputStream = urlConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream));
                bufferedWriter.write(encoded);
                bufferedWriter.flush();

                //BufferedReader rd = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                //check the response from the server
                int register_status = urlConnection.getResponseCode();
                System.out.println("server respond = " + register_status);


                bufferedWriter.close();
                urlConnection.disconnect();

                //TODO: if success, jump to start SD page
                //TODO: if fails, print response message
                if(register_status == 200) {
                    this.username = username;
                    publishProgress(1);
                }else{
                    publishProgress(0);
                }

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... progress) {
            if(progress.length != 1) {
                return;
            }

            TextView feedback = (TextView) findViewById(R.id.feedback_box);
            if(progress[0].equals(1)) {
                feedback.setText("Registration success");
                //UserInfo.setUsername(this.username);
                finish();

            }else{
                feedback.setText("Registration failed");
            }
        }
    }
}
