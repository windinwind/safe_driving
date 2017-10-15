package lazydroid.safedriving;

import android.os.AsyncTask;
import android.util.Base64;
import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by helen on 2017-10-13.
 */

public class UserInfo {

    private static String username;
    private static String password;
    private static int safepoint = 0;

    private static String userURL = "http://34.210.113.123/user";

    public static String getUsername() {
        return username;
    }

    public static void setUsername(String username) {
        UserInfo.username = username;
    }

    public static int getSafepoint() {
        return safepoint;
    }

    public static void setSafepoint(int safepoint) {
        UserInfo.safepoint = safepoint;
        new NetworkConnection().execute("put");
    }

    public static void setPassword(String password) {
        UserInfo.password = password;
    }

    public static void getSafepointFromServer(){
        new NetworkConnection().execute("get");
    }

    private static class NetworkConnection extends AsyncTask<String, Integer, Void> {

        @Override
        protected Void doInBackground(String... params) {
            try {
                if(params.length != 1){
                    Log.d("get/update safe point", "incorrect argument length");
                    return null;
                }

                if(username == null || password == null){
                    //username or password doesn't exist
                    Log.d("get/update safe point", "username/password doesn't exist");
                    return null;
                }

                if(params[0].equals("get")){
                    Log.d("trying to get point", "username, password = " + username + " " + password);
                    //set url based on username
                    String getPointURL = userURL + "?username=" + username;
                    URL url = new URL(getPointURL);

                    //establish connection with the server username page
                    HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

                    //set header
                    urlConnection.setRequestMethod("GET");
                    urlConnection.setRequestProperty("Authorization",password);

                    //get response from the server
                    BufferedReader response = new BufferedReader(
                            new InputStreamReader(urlConnection.getInputStream()));
                    String inputLine = response.readLine();
                    if(inputLine == null || !inputLine.contains(":")){
                        Log.d("response illegal", "input line null or no :");
                        return null;
                    }
                    String[] inputs = inputLine.split(":");

                    Log.d("get point", inputLine);

                    //update safepoint according to response
                    if(inputs.length != 2){
                        return null;
                    }

                    if(inputs[0].equals("point")){
                        int point = Integer.parseInt(inputs[1]);
                        Log.d("point from server", inputs[1]);
                        Log.d("point from server - int", Integer.toString(point));
                        safepoint = point;
                    }

                    urlConnection.disconnect();
                    response.close();

                }else if(params[0].equals("put")){
                    URL url = new URL(userURL);
                    //establish connection with the server login page
                    HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                    urlConnection.setRequestMethod("PUT");

                    OutputStream outputStream = urlConnection.getOutputStream();
                    BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream));
                    bufferedWriter.write("username:"+ username + "\npassword:" + password + "\nupdate:" + safepoint + "\n");
                    bufferedWriter.flush();

                    //get response from server
                    int update_status = urlConnection.getResponseCode();
                    System.out.println("update respond = " + update_status);

                    if(update_status == 200){
                        Log.d("point update", "success");
                    }else{
                        Log.d("point update", "failed");
                    }

                }else{
                    //undefined method
                    return null;
                }

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }
    }
}
