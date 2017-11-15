package lazydroid.safedriving;

import android.content.Context;
import android.content.SharedPreferences;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by fly on 17/04/15.
 */
public class GpsData {
    private boolean isRunning;
    private long time;
    private long timeStopped;
    private boolean isFirstTime;

    private double distanceM;
    private double curSpeed;
    private double maxSpeed;

    private onGpsServiceUpdate onGpsServiceUpdate;

    private static String PREF_NAME = "curSpeed";

    public interface onGpsServiceUpdate{
        public void update();
    }

    public void setOnGpsServiceUpdate(onGpsServiceUpdate onGpsServiceUpdate){
        this.onGpsServiceUpdate = onGpsServiceUpdate;
    }

    public void update(){
        onGpsServiceUpdate.update();
    }

    public GpsData() {
        isRunning = false;
        distanceM = 0;
        curSpeed = 0;
        maxSpeed = 0;
        timeStopped = 0;
    }

    public GpsData(onGpsServiceUpdate onGpsServiceUpdate){
        this();
        setOnGpsServiceUpdate(onGpsServiceUpdate);
    }

    public void addDistance(double distance){
        distanceM = distanceM + distance;
    }

    public double getDistance(){
        return distanceM;
    }

    public double getMaxSpeed() {
        return maxSpeed;
    }

    public double getAverageSpeed(){
        double average;
        String units;
        if (time <= 0) {
            average = 0.0;
        } else {
            average = (distanceM / (time / 1000)) * 3.6;
        }
        return average;
    }

    public double getAverageSpeedMotion(){
        double motionTime = time - timeStopped;
        double average;
        String units;
        if (motionTime <= 0){
            average = 0.0;
        } else {
            average = (distanceM / (motionTime / 1000)) * 3.6;
        }
        return average;
    }

    public void setCurSpeed(double curSpeed) {
        this.curSpeed = curSpeed;
        if (curSpeed > maxSpeed){
            maxSpeed = curSpeed;
        }
    }

    public boolean isFirstTime() {
        return isFirstTime;
    }

    public void setFirstTime(boolean isFirstTime) {
        this.isFirstTime = isFirstTime;
    }

    public boolean isRunning() {
        return isRunning;
    }

    public void setRunning(boolean isRunning) {
        this.isRunning = isRunning;
    }

    public void setTimeStopped(long timeStopped) {
        this.timeStopped += timeStopped;
    }

    public double getCurSpeed() {
        return curSpeed;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    protected static void storeSpeed(Context context, int speed) {
        SharedPreferences mSharedPreferences = context.getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        SharedPreferences.Editor mEditor = mSharedPreferences.edit();
        mEditor.putInt("speed", speed);
        mEditor.apply();
    }

    protected static int getSpeed(Context context) {
        SharedPreferences mSharedPreferences = context.getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        int selectedSpeed = mSharedPreferences.getInt("speed", 0);
        return selectedSpeed;
    }
}

