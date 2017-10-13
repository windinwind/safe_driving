package lazydroid.safedriving;

/**
 * Created by helen on 2017-10-13.
 */

public class UserInfo {

    private static String username;
    private static int safepoint;

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
    }
}
