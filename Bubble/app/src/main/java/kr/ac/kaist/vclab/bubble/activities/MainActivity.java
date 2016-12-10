package kr.ac.kaist.vclab.bubble.activities;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import kr.ac.kaist.vclab.bubble.MyGLRenderer;
import kr.ac.kaist.vclab.bubble.environment.GameEnv;
import kr.ac.kaist.vclab.bubble.events.GyroHandler;
import kr.ac.kaist.vclab.bubble.physics.Blower;
import kr.ac.kaist.vclab.bubble.views.MyGLSurfaceView;

/**
 * Created by sjjeon on 16. 9. 20.
 */

public class MainActivity extends Activity implements SensorEventListener {

    public static Context context;
    private MyGLSurfaceView mGLView;
    private GyroHandler gyroHandler;
    private SensorManager mSensorManager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MainActivity.context = getApplicationContext();

        // Create a GLSurfaceView instance and set it
        // as the ContentView for this Activity
        mGLView = new MyGLSurfaceView(this);
        gyroHandler = new GyroHandler();

        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);

        LinearLayout buttonLayout = new LinearLayout(this);
        buttonLayout.setOrientation(LinearLayout.HORIZONTAL);
        buttonLayout.setGravity(Gravity.CENTER);

        ToggleButton myButton1 = new ToggleButton(this);
        ToggleButton myButton2 = new ToggleButton(this);

        // FIXME SG (CHECK OVERLAYVIEW OF ANDROID)
        String duration = "" + GameEnv.getInstance().getDuration();
        setButtonText(myButton1, duration);
        setButtonText(myButton2, "Button 2");

        buttonLayout.addView(myButton1);
        buttonLayout.addView(myButton2);

        // COMMENT OUT FUNCTIONALITY OF BUTTONS
//        final ToggleButton[] buttons = {myButton1, myButton2};
//
//        for (int i = 0; i < buttons.length; i++) {
//            final ToggleButton button = buttons[i];
//
//            final int finalI = i;
//            button.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//                @Override
//                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                    if (isChecked) {
//                        for (ToggleButton toggleButton : buttons) {
//                            if (toggleButton != buttonView) {
//                                toggleButton.setChecked(false);
//                            }
//                            mGLView.mode = finalI;
//                        }
//                    }
//                }
//            });
//        }
//        myButton1.setChecked(true);

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

        //자이로스코프 센서(회전)
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        gyroHandler.mGyroscope = mSensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        mSensorManager.registerListener(
                this, gyroHandler.mGyroscope, SensorManager.SENSOR_DELAY_FASTEST);
    }

    private void setButtonText(ToggleButton button, String text) {
        button.setText(text);
        button.setTextOn(text);
        button.setTextOff(text);
    }

    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // FIXME WHAT IS THIS?
    }

    public void onSensorChanged(SensorEvent event) {
        Sensor sensor = event.sensor;
        if (sensor.getType() == Sensor.TYPE_GYROSCOPE) {
            gyroHandler.onSensorChanged(event);
        }
        float values[] = new float[3];
        values = gyroHandler.getSensorValues();
        mGLView.rotateByGyroSensor(values[0], -values[1], -values[2]);
    }

    @Override
    protected void onPause() {
        super.onPause();
        // The following call pauses the rendering thread.
        // If your OpenGL application is memory intensive,
        // you should consider de-allocating objects that
        // consume significant memory here.
        System.out.println("pause");
        MyGLRenderer.mBlower.stop();
        mGLView.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // The following call resumes a paused rendering thread.
        // If you de-allocated graphic objects for onPause()
        // this is a good place to re-allocate them.
        //MyGLRenderer.mBlower.start();


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