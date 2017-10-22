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
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by helen on 2017-10-17.
 */

public class NetworkService extends AsyncTask<String, Boolean, Void> {

    private static String userURL = "http://34.210.113.123/user";
    private static String loginURL = "http://34.210.113.123/login";
    private static String registerURL = "http://34.210.113.123/register";

    private final int ERR = -1;
    private final boolean SUCCESS = true;
    private final boolean FAIL = false;

    private String valid_method = "";
    private String valid_username = "";
    private String valid_password = "";
    private int valid_point = 0;

    private boolean checkInputValid(String input){
        //check null pointer
        if(input == null || input.equals("")){
            return false;
        }
        return true;
    }


    @Override
    protected Void doInBackground(String... params) {
        //do checks before connect to server
        if(params.length != 4){
            Log.d("network service", "incorrect argument length");
            return null;
        }

        String method = params[0];
        String username = params[1];
        String password = params[2];
        String safepoint = params[3];

        //check argument validity
        if(!checkInputValid(method) ||
                !checkInputValid(username) ||
                !checkInputValid(password) ||
                !checkInputValid(safepoint)){
            return null;
        }

        try {

            if(method.equals("login_post") || method.equals("register_post")){

                boolean success = prepareForPost(method, username, password);
                if(success){
                    publishProgress(SUCCESS);
                }else{
                    publishProgress(FAIL);
                }
                return null;
            }

            if(method.equals("get")){
                this.valid_method = method;

                //Log.d("trying to get point", "username, password = " + username + " " + password);
                //set url based on username
                URL url = new URL(userURL + "?username=" + username);

                int point = getPointFromServer(url, password);

                if(point != ERR) {
                    //set safepoint to server response
                    UserInfo.setSafepointLocal(point);
                    //notify main thread
                    //Log.d("get success", "calling publish progress");
                    publishProgress(SUCCESS);
                }
                return null;
            }

            if(method.equals("put")){
                this.valid_method = method;

                URL url = new URL(userURL);
                String content = "username:" + username + "\npassword:" + password + "\nupdate:" + safepoint + "\n";
                //Log.d("updating points", content);
                //put user's point to server
                boolean success = putPointToServer(url, content);

                //notify main thread
                if(success){
                    publishProgress(SUCCESS);
                }
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return null;
    }

    /*
     * Post given content to given url.
     * Return 1 when success, 0 otherwise.
     */
    private boolean postToServer(URL url, String content){
        boolean success = false;
        try {
            //establish connection with the server login page
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

            //set user name and password into the get request
            urlConnection.setRequestMethod("POST");

            OutputStream outputStream = urlConnection.getOutputStream();
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream));
            bufferedWriter.write(content);
            bufferedWriter.flush();

            //BufferedReader rd = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            //check the response from the server
            int register_status = urlConnection.getResponseCode();
            System.out.println("server respond = " + register_status);

            bufferedWriter.close();
            urlConnection.disconnect();

            if (register_status == 200) {
                success = true;
            } else {
                success = false;
            }
        }catch (IOException e) {
            e.printStackTrace();
        }

        return success;
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

            urlConnection.disconnect();
            outputStream.close();

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
                return ERR;
            }

            String[] inputs = inputLine.split(":");

            //update safepoint according to response
            if(inputs.length != 2 || !inputs[0].equals("point")){
                return ERR;
            }

            point = Integer.parseInt(inputs[1]);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return point;
    }

    /*
     * Prepare for URL and content to post to server
     */
    private boolean prepareForPost(String method, String username, String password)
            throws MalformedURLException, UnsupportedEncodingException{
        this.valid_method = method;

        //Log.d("before encode name", username);
        //Log.d("before encode password", password);
        URL url;
        if(method.equals("login_post")) {
            url = new URL(loginURL);
        }else{
            url = new URL(registerURL);
        }
        String password_hash = Base64.encodeToString((password.charAt(0) + username +
                password.charAt(password.length() - 1) +
                password + "lazyDroid").getBytes("UTF-8"), Base64.NO_WRAP);
        String encoded = "username:" + username + "\npassword:" + password_hash;

        boolean result =  postToServer(url, encoded);
        if(result) {
            this.valid_username = username;
            this.valid_password = password_hash;

            if (method.equals("login_post")) {
                UserInfo.setUsername(this.valid_username);
                UserInfo.setPassword(this.valid_password);
                UserInfo.getSafepointFromServer();
            }
        }
        return result;
    }

    @Override
    protected void onProgressUpdate(Boolean... progress){
        //check argument length
        if(progress.length != 1){
            return;
        }

        //the update was success, needs to update GUI
        if((this.valid_method.equals("get") || this.valid_method.equals("put")) && progress[0] == SUCCESS){
            Log.d("updated point", Integer.toString(UserInfo.getSafepoint()));
            SafeDrivingActivity.updateSafePointonGUI();
            return;
        }

        if(this.valid_method.equals("login_post")){
            if(progress[0] == SUCCESS) {
                LoginActivity.updateStatus(SUCCESS);
            }else{
                LoginActivity.updateStatus(FAIL);
            }
            return;
        }

        if(this.valid_method.equals("register_post")){
            if(progress[0] == SUCCESS) {
                UserRegisterActivity.updateStatus(SUCCESS);
            }else{
                UserRegisterActivity.updateStatus(FAIL);
            }
        }
    }
}
