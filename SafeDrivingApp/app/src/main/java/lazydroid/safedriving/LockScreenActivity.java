package lazydroid.safedriving;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Chronometer;
import android.widget.Chronometer.OnChronometerTickListener;
import android.widget.TextView;

/**
 * The class that makes the screenlock
 */
public class LockScreenActivity extends AppCompatActivity {
    TextView textCount;
    TextView textTimer;
    long startTime;
    long countUp;
    private String message = "Unlock Times: ";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Set up the lockscreen
        makeFullScreen();


        setContentView(R.layout.activity_lock_screen);


        //Make a textbox that displays the time since entering the lockscreen
        Chronometer stopWatch = (Chronometer) findViewById(R.id.chrono);
        startTime = SystemClock.elapsedRealtime();
        textTimer = (TextView) findViewById(R.id.Timer);
        stopWatch.setOnChronometerTickListener(new OnChronometerTickListener() {
            @Override
            public void onChronometerTick(Chronometer arg0) {
                countUp = (SystemClock.elapsedRealtime() - arg0.getBase()) / 1000;
                String asText = (countUp / 60) + ":" + (countUp % 60);
                textTimer.setText(asText);
            }
        });
        badBehaviorCount.resetCount(this);
        stopWatch.start();

        //Start the lockscreen service to disable the keys
        startService(new Intent(this,LockScreenService.class));
    }

    @Override
    protected void onResume() {
        super.onResume();
        int unlockTimes = badBehaviorCount.getCount(this);
        textCount = (TextView) findViewById(R.id.badBehavCountText);
        textCount.setText(message + unlockTimes);
    }

    @Override
    protected void onPause() {
        super.onPause();

        badBehaviorCount.incCount(this);
        UserInfo.setSafepoint(UserInfo.getSafepoint() - 1);
    }

    /**
     * The method that sets the screen to fullscreen.  It removes the Notifications bar,
     * the Actionbar and the virtual keys (if they are on the phone)
     */
    public void makeFullScreen() {
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        if(Build.VERSION.SDK_INT < 19) { //View.SYSTEM_UI_FLAG_IMMERSIVE is only on API 19+
            this.getWindow().getDecorView()
                    .setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        } else {
            this.getWindow().getDecorView()
                    .setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE);
        }
    }

    public void unlockScreen(View view) {
        //Increment the bad behavior count everytime the user quits


        UserInfo.setSafepoint(UserInfo.getSafepoint() + 1);

        finish();
    }
}



