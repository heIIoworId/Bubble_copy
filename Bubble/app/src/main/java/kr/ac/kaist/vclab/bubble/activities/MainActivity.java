package kr.ac.kaist.vclab.bubble.activities;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.net.Uri;
import android.opengl.Matrix;
import android.os.Bundle;
import android.view.Gravity;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import java.text.DecimalFormat;
import java.util.Arrays;

import kr.ac.kaist.vclab.bubble.environment.GameEnv;
import kr.ac.kaist.vclab.bubble.events.GyroHandler;
import kr.ac.kaist.vclab.bubble.events.SensorListener;
import kr.ac.kaist.vclab.bubble.events.SoundHandler;
import kr.ac.kaist.vclab.bubble.utils.MatOperator;
import kr.ac.kaist.vclab.bubble.utils.VecOperator;
import kr.ac.kaist.vclab.bubble.views.MyGLSurfaceView;

/**
 * Created by sjjeon on 16. 9. 20.
 */

public class MainActivity extends Activity {
    public static Context context;
    public static MyGLSurfaceView mGLView;
    public static SensorListener mSensorListener;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MainActivity.context = getApplicationContext();
        GameEnv.getInstance().init();
        // Prevent android being dark
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        // Make it as full screen
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        // Create a GLSurfaceView instance and set it
        // as the ContentView for this Activity
        mGLView = new MyGLSurfaceView(this);
        mSensorListener = new SensorListener(this);
        mGLView.setActivity(MainActivity.this);

        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);

        LinearLayout buttonLayout = new LinearLayout(this);
        buttonLayout.setOrientation(LinearLayout.HORIZONTAL);
        buttonLayout.setGravity(Gravity.CENTER);
        buttonLayout.setAlpha(0.8f);

        final ToggleButton myButton1 = new ToggleButton(this);
        myButton1.setBackgroundColor(0);
        final ToggleButton myButton2 = new ToggleButton(this);
        myButton2.setBackgroundColor(0);

        // FIXME NEEDED TO BE RUN VIA A THREAD
        Thread t = new Thread() {
            @Override
            public void run() {
                try {
                    while (!isInterrupted()) {
                        Thread.sleep(1000);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                // update TextView here!
                                String duration = "" + GameEnv.getInstance().getDuration() + " secs";
                                setButtonText(myButton1, duration);
                                String itemNum = "" + GameEnv.getInstance().numOfAchievedItems + "/" +
                                        GameEnv.getInstance().numOfTotalItems;
                                setButtonText(myButton2, itemNum);
                            }
                        });
                    }
                } catch (InterruptedException e) {
                }
            }
        };
        t.start();

        buttonLayout.addView(myButton1);
        buttonLayout.addView(myButton2);

        LinearLayout.LayoutParams glParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
        glParams.weight = 1;
        LinearLayout.LayoutParams buttonParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
        layout.addView(buttonLayout, buttonParams);
        layout.addView(mGLView, glParams);
        setContentView(layout);
    }

    private void setButtonText(ToggleButton button, String text) {
        button.setText(text);
        button.setTextSize(17f);
        button.setTextOn(text);
        button.setTextOff(text);
    }

    // reference : https://developer.android.com/guide/topics/sensors/sensors_motion.html


    @Override
    protected void onPause() {
        super.onPause();
        mSensorListener.onPause();
        // The following call pauses the rendering thread.
        // If your OpenGL application is memory intensive,
        // you should consider de-allocating objects that
        // consume significant memory here.
        mGLView.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mSensorListener.onResume();
        // The following call resumes a paused rendering thread.
        // If you de-allocated graphic objects for onPause()
        // this is a good place to re-allocate them.
        mGLView.onResume();
    }


    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
    }
}