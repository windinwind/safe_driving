package lazydroid.safedriving;

import android.os.AsyncTask;
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

    /*
     * Get safepoint from local result
     */
    public static int getSafepoint() {
        return safepoint;
    }

    public static void setLocalSafepoint(int safepoint) {
        UserInfo.safepoint = safepoint;
    }

    /*
     * Set safepoint to a new value. Update to server simultaneously
     */
    public static void setSafepoint(int safepoint) {
        UserInfo.safepoint = safepoint;
        new NetworkConnection().execute("put");
    }

    public static void setPassword(String password) {
        UserInfo.password = password;
    }

    /*
     * Get safepoint from server record. Must be called when first login
     */
    public static void getSafepointFromServer(){
        new NetworkConnection().execute("get");
    }

    private static class NetworkConnection extends AsyncTask<String, Boolean, Void> {

        private final int ERR = -1;
        private final boolean SUCCESS = true;
        @Override
        protected Void doInBackground(String... params) {
            //do checks before connect to server
            if(params.length != 1){
                Log.d("get/update safe point", "incorrect argument length");
                return null;
            }

            if(username == null || password == null){
                //username or password doesn't exist
                Log.d("get/update safe point", "username/password doesn't exist");
                return null;
            }
                String method = params[0];

            try {

                if(method.equals("get")){
                    Log.d("trying to get point", "username, password = " + username + " " + password);
                    //set url based on username
                    URL url = new URL(userURL + "?username=" + username);

                    int point = getPointFromServer(url, password);

                    if(point != ERR) {
                        //set safepoint to server response
                        safepoint = point;
                        //notify main thread
                        publishProgress(SUCCESS);
                    }

                }else if(method.equals("put")){
                    URL url = new URL(userURL);
                    String content = "username:" + username + "\npassword:" + password + "\nupdate:" + safepoint + "\n";

                    //put user's point to server
                    boolean success = putPointToServer(url, content);

                    //notify main thread
                    if(success){
                        publishProgress(SUCCESS);
                    }
                }

            } catch (MalformedURLException e) {
                e.printStackTrace();
            }

            return null;
        }

        /*
         * Update the given content to the given url
         * return 1 if success, 0 otherwise
         */
        private boolean putPointToServer(URL url, String content){
            boolean success = false;
            //establish connection with the server login page
            try {
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("PUT");

                OutputStream outputStream = urlConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream));
                bufferedWriter.write(content);
                bufferedWriter.flush();

                //get response from server
                int update_status = urlConnection.getResponseCode();
                System.out.println("update respond = " + update_status);

                if (update_status == 200) {
                    Log.d("point update", "success");
                    success = true;

                } else {
                    Log.d("point update", "failed");
                    success = false;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return success;
        }


        /*
         * Establish connection with the url, send get store request with authentication
         * Return the server reponse point
         */
        private int getPointFromServer(URL url, String authentication){
            int point = 0;

            try {
                //establish connection
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.setRequestProperty("Authorization",authentication);

                //get response from the server
                BufferedReader response = new BufferedReader(
                        new InputStreamReader(urlConnection.getInputStream()));
                String inputLine = response.readLine();

                //close connection
                urlConnection.disconnect();
                response.close();

                //check server response format
                if(inputLine == null || !inputLine.contains(":")){
                    Log.d("response illegal", "input line null or no :");
                    return ERR;
                }

                String[] inputs = inputLine.split(":");

                Log.d("get point", inputLine);

                //update safepoint according to response
                if(inputs.length != 2){
                    return ERR;
                }

                if(inputs[0].equals("point")){
                    point = Integer.parseInt(inputs[1]);
                    Log.d("point from server", inputs[1]);

                }else{
                    return ERR;
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
            return point;
        }

        @Override
        protected void onProgressUpdate(Boolean... progress){
            //check argument length
            if(progress.length != 1){
                return;
            }

            //the update was success, needs to update GUI
            if(progress[0] == SUCCESS){
                SafeDrivingActivity.updateSafePointonGUI();
            }
        }
    }
}
