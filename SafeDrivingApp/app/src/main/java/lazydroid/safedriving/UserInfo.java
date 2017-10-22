package lazydroid.safedriving;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by helen on 2017-10-13.
 */

public class UserInfo {

    private static String username;
    private static String password;
    private static int safepoint = 0;

    private static String REMEMBER_USER_INFO = "userInfo";

    public static void updateStoredUserInfo(Context context) {
        SharedPreferences mSharedPreferences = context.getSharedPreferences(REMEMBER_USER_INFO, MODE_PRIVATE);
        SharedPreferences.Editor mEditor = mSharedPreferences.edit();
        mEditor.putString("username",username);
        mEditor.putString("password", password);
        mEditor.apply();
    }

    public static String getStoredUserInfo(Context context){
        SharedPreferences mSharedPreferences = context.getSharedPreferences(REMEMBER_USER_INFO, MODE_PRIVATE);
        username = mSharedPreferences.getString("username", "");
        password = mSharedPreferences.getString("password", "");
        return password;
    }

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
