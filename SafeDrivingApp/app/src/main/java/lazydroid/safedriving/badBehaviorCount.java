package lazydroid.safedriving;

import android.content.Context;
import android.content.SharedPreferences;

import static android.content.Context.MODE_PRIVATE;

/**
 * Class that keeps track of the unlock times of the user
 * Use SharedPreferences so that the records will not be erased when the user exits the app
 */

public class badBehaviorCount {

    private SharedPreferences badBehaviorCount;
    private static String PREF_NAME = "badBehaviorCount";


    public badBehaviorCount() {
        //Blank
    }

    /* Use sharedPReferences to store the bad behavior count */
    //Update the badBehaviorCount value
    protected static void storeCount(Context context, int count) {
        SharedPreferences mSharedPreferences = context.getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        SharedPreferences.Editor mEditor = mSharedPreferences.edit();
        mEditor.putInt("count", count);
        mEditor.apply();
    }

    //Get the badBehaviorCount value
    protected static int getCount(Context context) {
        SharedPreferences mSharedPreferences = context.getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        int selectedCount = mSharedPreferences.getInt("count", 0);
        return selectedCount;
    }

    //Increment the badBehaviorCount value (When the user quits the lockscreen
    protected static void incCount(Context context) {
        SharedPreferences mSharedPreferences = context.getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        int oldCount = getCount(context);
        int newCount = oldCount + 1;
        storeCount(context, newCount);
    }
}
