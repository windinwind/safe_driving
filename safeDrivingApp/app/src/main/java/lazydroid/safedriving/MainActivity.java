package lazydroid.safedriving;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import android.os.AsyncTask;
import android.widget.TextView;


public class MainActivity extends AppCompatActivity {

    public static String loginURL = "http://34.210.113.123/login";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    protected void loginButtonClicked(View v) {
        //System.out.println("button clicked");
        EditText userNameEdit = (EditText) findViewById(R.id.user_name);
        EditText passwordEdit = (EditText) findViewById(R.id.password);
        String username = userNameEdit.getText().toString();
        String password = passwordEdit.getText().toString();

        Log.d("username = ", username);
        Log.d("password = ", password);
        new NetworkConnection().execute(username, password);
        //sendLoginInfo(username, password);

    }

    protected void sendLoginInfo(String username, String password) {
        //do network connection
        try {
            URL url = new URL(loginURL);
            //establish connection with the server login page
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

            //set user name and password into the get request
            Log.d("before encode name", username);
            Log.d("before encode password", password);
            String encoded = Base64.encodeToString((username + ":" + password).getBytes("UTF-8"), Base64.NO_WRAP);
            Log.d("encoded String", encoded);
            urlConnection.setRequestProperty("Authorization", "Basic " + encoded);
            urlConnection.setRequestMethod("GET");

            BufferedReader rd = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            //check the response from the server
            String login_status = rd.readLine();
            Log.d("login status is ", login_status);
            String login_status_pair[] = login_status.split(":");

            rd.close();
            urlConnection.disconnect();

            //TODO: if success, jump to start SD page
            //TODO: if fails, print response message
            if (login_status_pair.length != 2) {
                TextView feedback = (TextView) findViewById(R.id.feedback_box);
                feedback.setText("server error");
                return;
            }

            if (login_status_pair[1].equals("success")) {

            } else {
                TextView feedback = (TextView) findViewById(R.id.feedback_box);
                feedback.setText("Invalid username or password");
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    class NetworkConnection extends AsyncTask<String, Integer, Void> {


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
                //String encoded = Base64.encodeToString((username + ":" + password).getBytes("UTF-8"), Base64.NO_WRAP);

                //hash the password:
                //first character of the password + username + last character of the password + user password + lazyDroid
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


                int login_status = urlConnection.getResponseCode();
                System.out.println("server respond = " + login_status);

                /*
                BufferedReader rd = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                //check the response from the server
                String login_status = rd.readLine();
                Log.d("login status is ", login_status);
                String login_status_pair[] = login_status.split(":");

                rd.close();
                */
                outputStream.close();
                urlConnection.disconnect();

                //TODO: if success, jump to start SD page
                //TODO: if fails, print response message
                /*
                if (login_status != 202) {
                    //TextView feedback = (TextView)findViewById(R.id.feedback_box);
                    //feedback.setText("server error");
                    return null;
                }
                */
                if (login_status == 200) {
                    publishProgress(1);
                } else {
                    //TextView feedback = (TextView)findViewById(R.id.feedback_box);
                    //feedback.setText("Invalid username or password");
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

            if (progress[0].equals(1)) {
                TextView feedback = (TextView) findViewById(R.id.feedback_box);
                feedback.setText("Login success");

                Intent intent = new Intent(MainActivity.this, SafeDrivingActivity.class);
                intent.putExtra("login", "success");
                startActivity(intent);

            } else {
                TextView feedback = (TextView) findViewById(R.id.feedback_box);
                feedback.setText("Invalid username or password");
            }
        }

    }

    protected void registerButtonClicked(View v){
        Intent intent = new Intent(this, UserRegisterActivity.class);
        startActivity(intent);
    }
}