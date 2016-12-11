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
    DecimalFormat form = new DecimalFormat("0.2f");
    public static Context context;
    private MyGLSurfaceView mGLView;
    private GyroHandler gyroHandler;
    private SensorManager mSensorManager;
    private static final float NS2S = 1.0f / 1000000000.0f;
    private final float[] deltaRotationVector = new float[4];
    private final float[] mInitRotationMatrix = new float[16];
    private int initFlag = 1;

    private Sensor accSensor;
    private Sensor magSensor;

    private final float[] mAccelerometerReading = new float[3];
    private final float[] mMagnetometerReading = new float[3];

    private final float[] mRotationMatrix = new float[9];
    private final float[] mOrientationAngles = new float[3];
    private final float[] mOriginOrientationAngles = new float[3];


    private Sensor rotationVectorSensor;
    private Sensor gyroSensor;
    private Sensor orientationSensor;

    private long timestamp;

    private float[]rotationOrientation = new float[3];
    private float[]rotationMatrix = new float[16];
    private float[]initMatrix = new float[16];
    private int initialize = 0;
    private long prevInit = 0;
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

        //자이로스코프 센서(회전)
        /*
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        gyroHandler.mGyroscope = mSensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        mSensorManager.registerListener(
                this, gyroHandler.mGyroscope, SensorManager.SENSOR_DELAY_FASTEST);
        accSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        magSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        mSensorManager.registerListener(
                this, accSensor, SensorManager.SENSOR_DELAY_FASTEST);
        mSensorManager.registerListener(
                this, magSensor, SensorManager.SENSOR_DELAY_FASTEST);
        /*/
        mSensorManager = (SensorManager)context.getSystemService(Context.SENSOR_SERVICE);

        rotationVectorSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR);

        gyroSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        mSensorManager.unregisterListener(this);
        mSensorManager.registerListener(this, rotationVectorSensor, SensorManager.SENSOR_DELAY_FASTEST);
        Matrix.setIdentityM(rotationMatrix, 0);
        SoundHandler.getInstance().start();

        initialize = 1;
        timestamp = 0;
    }

    private void setButtonText(ToggleButton button, String text) {
        button.setText(text);
        button.setTextOn(text);
        button.setTextOff(text);
    }
    public void updateOrientationAngles() {
        // Update rotation matrix, which is needed to update orientation angles.
        mSensorManager.getRotationMatrix(mRotationMatrix, null,
                mAccelerometerReading, mMagnetometerReading);

        // "mRotationMatrix" now has up-to-date information.

        mSensorManager.getOrientation(mRotationMatrix, mOrientationAngles);
        for (int i = 0; i < 3; i++) {
            mOrientationAngles[i] = (float) Math.toDegrees(mOrientationAngles[i]);
        }
//        System.out.println("orientation : " + form.format(mOrientationAngles[0]) + " "
//                + form.format(mOrientationAngles[1]) + " "
//                + form.format(mOrientationAngles[2]) + " \n\n\n\n");

        // "mOrientationAngles" now has up-to-date information.
    }

    public float[] getRotationFromOrientationAngle(){
        float[] rot = new float[16];
        Matrix.setIdentityM(rot, 0);
        float[] diff = new float[3];
        diff = VecOperator.sub(mOrientationAngles, mOriginOrientationAngles);
//        System.out.println("orientation : " + form.format(diff[0]) + " "
//                + form.format(diff[1]) + " "
//                + form.format(diff[2]) + " \n\n\n\n");

        diff[1] *= 2;
        diff = VecOperator.scale(diff, 0.3f);
//        System.out.println(VecOperator.getMag(diff));
        //System.out.println(mOriginOrientationAngles[0] + " " + mOriginOrientationAngles[1] + " " + mOriginOrientationAngles[2]);
        //System.out.println(mOrientationAngles[0] + " " + mOrientationAngles[1] + " " + mOrientationAngles[2]);

        //  System.out.println(diff[0] + " " + diff[1] + " " + diff[2]);
//
        Matrix.rotateM(rot, 0, diff[0], 1, 0 ,0);
        Matrix.rotateM(rot, 0, diff[1], 0, 1 ,0);
        Matrix.rotateM(rot, 0, diff[2], 0, 0 ,1);
        return rot;
    }


    private void getRotationVectorFromGyro(float[] gyroValues,
                                           float[] deltaRotationVector,
                                           float timeFactor) {
        float[] normValues = new float[3];
        gyroValues[1] = - gyroValues[1];

        // Calculate the angular speed of the sample
        float omegaMagnitude =
                (float) Math.sqrt(gyroValues[0] * gyroValues[0] +
                        gyroValues[1] * gyroValues[1] +
                        gyroValues[2] * gyroValues[2]);

        // Normalize the rotation vector if it's big enough to get the axis
        if (omegaMagnitude > 0.000000001f) {
            normValues[0] = gyroValues[0] / omegaMagnitude;
            normValues[1] = gyroValues[1] / omegaMagnitude;
            normValues[2] = gyroValues[2] / omegaMagnitude;
        }

        // Integrate around this axis with the angular speed by the timestep
        // in order to get a delta rotation from this sample over the timestep
        // We will convert this axis-angle representation of the delta rotation
        // into a quaternion before turning it into the rotation matrix.
        float thetaOverTwo = omegaMagnitude * timeFactor;
        float sinThetaOverTwo = (float) Math.sin(thetaOverTwo);
        float cosThetaOverTwo = (float) Math.cos(thetaOverTwo);
        deltaRotationVector[0] = sinThetaOverTwo * normValues[0];
        deltaRotationVector[1] = sinThetaOverTwo * normValues[1];
        deltaRotationVector[2] = sinThetaOverTwo * normValues[2];
        deltaRotationVector[3] = cosThetaOverTwo;
    }
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // FIXME WHAT IS THIS?
    }

    public void onSensorChanged(SensorEvent event) {
        if(initialize == 1) {
            System.arraycopy(event.values, 0, rotationOrientation, 0, 3);
            SensorManager.getRotationMatrixFromVector(rotationMatrix, event.values);
            System.out.printf("rotation Ori : %f | %f | %f\n", rotationOrientation[0], rotationOrientation[1], rotationOrientation[2]);
            mGLView.doRotate(rotationMatrix);

            System.arraycopy(rotationMatrix,0,initMatrix,0,16);

            initialize = 0;
            mSensorManager.unregisterListener(this, rotationVectorSensor);
            mSensorManager.registerListener(this, gyroSensor, SensorManager.SENSOR_DELAY_FASTEST);

        }else{
            float[] deltaVector = new float[4];
            if(timestamp != 0){
                float dT = (event.timestamp - timestamp) / 1000000000.0f;
                System.arraycopy(event.values, 0, rotationOrientation, 0, 3);
                getRotationVectorFromGyro(rotationOrientation, deltaVector, dT / 2.0f);
            }
            timestamp = event.timestamp;

            float[] deltaMat = new float[16];
            SensorManager.getRotationMatrixFromVector(deltaMat, deltaVector);
            float[] temp = new float[16];
            Matrix.multiplyMM(temp, 0, deltaMat, 0, rotationMatrix, 0);
            rotationMatrix = Arrays.copyOf(temp, 16);
            mGLView.doRotate(deltaMat);

        }

    }

    @Override
    protected void onPause() {
        super.onPause();
        SoundHandler.getInstance().stop();
        mSensorManager.unregisterListener(this);

        // The following call pauses the rendering thread.
        // If your OpenGL application is memory intensive,
        // you should consider de-allocating objects that
        // consume significant memory here.
        mGLView.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        SoundHandler.getInstance().start();
        mSensorManager.registerListener(this, rotationVectorSensor, SensorManager.SENSOR_DELAY_NORMAL);
        initialize = 1;
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

