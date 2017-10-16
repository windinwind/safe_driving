package lazydroid.safedriving;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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


public class LoginActivity extends AppCompatActivity {

    public static String loginURL = "http://34.210.113.123/login";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    /*
     * Do user authentication when login button clicked
     */
    public void loginButtonClicked(View v) {
        //System.out.println("button clicked");
        //get user name and password
        EditText userNameEdit = (EditText) findViewById(R.id.user_name);
        EditText passwordEdit = (EditText) findViewById(R.id.password);
        String username = userNameEdit.getText().toString();
        String password = passwordEdit.getText().toString();
        if(username == null || password == null || username.equals("") || password.equals("")){
            Log.d("login", "with empty string");
            return;
        }

        Log.d("username = ", username);
        Log.d("password = ", password);

        //send user info to server
        new NetworkConnection().execute(username, password);

    }


    private class NetworkConnection extends AsyncTask<String, Integer, Void> {

        private String username;
        private String hashedPassword;

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
                URL url = new URL(loginURL);
                //establish connection with the server login page
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

                //set user name and password into the get request
                Log.d("before encode name", username);
                Log.d("before encode password", password);

                //hash the password:
                //first character of the password + username + last character of the password + user password + lazyDroid
                String password_hash = Base64.encodeToString((password.charAt(0) + username +
                                password.charAt(password.length()-1) +
                                password + "lazyDroid").getBytes("UTF-8"), Base64.NO_WRAP);
                String encoded = "username:" + username + "\npassword:" + password_hash;
                Log.d("encoded String", encoded);

                urlConnection.setRequestMethod("POST");

                //write to output buffer
                OutputStream outputStream = urlConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream));
                bufferedWriter.write(encoded);
                bufferedWriter.flush();

                //get response from server
                int login_status = urlConnection.getResponseCode();
                System.out.println("server respond = " + login_status);

                urlConnection.disconnect();
                outputStream.close();
                bufferedWriter.close();

                if (login_status == 200) {
                    //success
                    this.username = username;
                    this.hashedPassword = password_hash;
                    publishProgress(1);
                } else {
                    //failed
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

            if (progress[0].equals(1)) {
                //success
                feedback.setText("Login success");
                UserInfo.setUsername(this.username);
                UserInfo.setPassword(this.hashedPassword);
                UserInfo.getSafepointFromServer();

                //finish this activity with success result
                setResult(RESULT_OK, null);
                finish();

            } else {
                feedback.setText("Invalid username or password");
            }
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