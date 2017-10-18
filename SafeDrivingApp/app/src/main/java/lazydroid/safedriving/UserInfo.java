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
        return UserInfo.username;
    }

    public static void setUsername(String username) {
        UserInfo.username = username;
    }

    /*
     * Get safepoint from local result
     */
    public static int getSafepoint() {
        Log.d("current safe point", Integer.toString(UserInfo.safepoint));
        return UserInfo.safepoint;
    }

    public static void setLocalSafepoint(int safepoint) {
        UserInfo.safepoint = safepoint;
    }

    /*
     * Set safepoint to a new value. Update to server simultaneously
     */
    public static void setSafepoint(int safepoint) {
        //Log.d("new safe point", Integer.toString(safepoint));
        UserInfo.safepoint = safepoint;
        new NetworkService().execute("put", username, password, Integer.toString(safepoint));
        //new NetworkConnection().execute("put");
    }

    public static void setPassword(String password) {
        UserInfo.password = password;
    }

    /*
     * Get safepoint from server record. Must be called when first login
     */
    public static void getSafepointFromServer(){
        new NetworkService().execute("get", username, password, Integer.toString(safepoint));
        //new NetworkConnection().execute("get");
    }

    /*
     * Set safepoint locally. Must be called when first login
     */
    public static void setSafepointLocal(int safepoint){
        UserInfo.safepoint = safepoint;
    }
    
}
