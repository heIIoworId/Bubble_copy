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
import android.view.WindowManager;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import java.text.DecimalFormat;
import java.util.Arrays;

import kr.ac.kaist.vclab.bubble.environment.GameEnv;
import kr.ac.kaist.vclab.bubble.events.GyroHandler;
import kr.ac.kaist.vclab.bubble.events.SoundHandler;
import kr.ac.kaist.vclab.bubble.utils.MatOperator;
import kr.ac.kaist.vclab.bubble.utils.VecOperator;
import kr.ac.kaist.vclab.bubble.views.MyGLSurfaceView;

/**
 * Created by sjjeon on 16. 9. 20.
 */

public class MainActivity extends Activity implements SensorEventListener {
    public static Context context;
    private MyGLSurfaceView mGLView;
    private GyroHandler gyroHandler;
    private SensorManager mSensorManager;
    private static final float NS2S = 1.0f / 1000000000.0f;

    private long timestamp;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MainActivity.context = getApplicationContext();

        // Prevent android being dark
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        // Create a GLSurfaceView instance and set it
        // as the ContentView for this Activity
        mGLView = new MyGLSurfaceView(this);
        //gyroHandler = new GyroHandler();

        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);

        LinearLayout buttonLayout = new LinearLayout(this);
        buttonLayout.setOrientation(LinearLayout.HORIZONTAL);
        buttonLayout.setGravity(Gravity.CENTER);

        ToggleButton myButton1 = new ToggleButton(this);
        ToggleButton myButton2 = new ToggleButton(this);

        // FIXME NEEDED TO BE RUN VIA A THREAD
        String duration = "" + GameEnv.getInstance().getDuration();
        setButtonText(myButton1, duration);
        setButtonText(myButton2, "Button 2");

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


        mSensorManager = (SensorManager)context.getSystemService(Context.SENSOR_SERVICE);
        gyroHandler = new GyroHandler();
        gyroHandler.mGyroscope = mSensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);

        mSensorManager.unregisterListener(this);
        mSensorManager.registerListener(this, gyroHandler.mGyroscope, SensorManager.SENSOR_DELAY_FASTEST);

        SoundHandler.getInstance().start();
        timestamp = 0;
    }

    private void setButtonText(ToggleButton button, String text) {
        button.setText(text);
        button.setTextOn(text);
        button.setTextOff(text);
    }

    // reference : https://developer.android.com/guide/topics/sensors/sensors_motion.html
    private void getGyroVector(float[] gyroValues,
                                           float[] deltaVector,
                                           float dT) {
        gyroValues[1] = - gyroValues[1];
        // Calculate the angular speed of the sample
        float omegaMagnitude = VecOperator.getMag(gyroValues);

        // Normalize the rotation vector if it's big enough to get the axis
        if (omegaMagnitude > 0.000000001f) {
            gyroValues = VecOperator.normalize(gyroValues);
        }

        // Integrate around this axis with the angular speed by the timestep
        // in order to get a delta rotation from this sample over the timestep
        // We will convert this axis-angle representation of the delta rotation
        // into a quaternion before turning it into the rotation matrix.
        float thetaOverTwo = omegaMagnitude * dT / 2.0f;
        float sinThetaOverTwo = (float) Math.sin(thetaOverTwo);
        float cosThetaOverTwo = (float) Math.cos(thetaOverTwo);
        deltaVector[0] = sinThetaOverTwo * gyroValues[0];
        deltaVector[1] = sinThetaOverTwo * gyroValues[1];
        deltaVector[2] = sinThetaOverTwo * gyroValues[2];
        deltaVector[3] = cosThetaOverTwo;
    }
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // FIXME WHAT IS THIS?
    }

    public void onSensorChanged(SensorEvent event) {

        float[] deltaVector = new float[4];
        if(timestamp != 0){
            float dT = (event.timestamp - timestamp) * NS2S;
            getGyroVector(event.values, deltaVector, dT);
        }
        timestamp = event.timestamp;

        float[] deltaMat = new float[16];
        SensorManager.getRotationMatrixFromVector(deltaMat, deltaVector);
        float[] temp = new float[16];
        mGLView.doRotate(deltaMat);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(this);
        SoundHandler.getInstance().stop();

        // The following call pauses the rendering thread.
        // If your OpenGL application is memory intensive,
        // you should consider de-allocating objects that
        // consume significant memory here.
        mGLView.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mSensorManager.registerListener(this, gyroHandler.mGyroscope, SensorManager.SENSOR_DELAY_FASTEST);
        SoundHandler.getInstance().start();

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