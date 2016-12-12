package kr.ac.kaist.vclab.bubble.events;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.opengl.Matrix;

import kr.ac.kaist.vclab.bubble.activities.MainActivity;
import kr.ac.kaist.vclab.bubble.utils.VecOperator;

/**
 * Created by Jongmin on 2016-12-12.
 */

public class SensorListener implements SensorEventListener {

    private GyroHandler gyroHandler;
    private final SensorManager mSensorManager;
    private static final float NS2S = 1.0f / 1000000000.0f;
    private long timestamp;
    private Context context;

    public SensorListener(Context context) {
        //Save context
        this.context = context;
        mSensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        gyroHandler = new GyroHandler();
        gyroHandler.mGyroscope = mSensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);

        mSensorManager.unregisterListener(this);
        mSensorManager.registerListener(this, gyroHandler.mGyroscope, SensorManager.SENSOR_DELAY_FASTEST);

        SoundHandler.getInstance().start();
        timestamp = 0;

    }

    private void getGyroVector(float[] gyroValues,
                               float[] deltaVector,
                               float dT) {
        gyroValues[1] = -gyroValues[1];
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
    }

    public void onSensorChanged(SensorEvent event) {

        float[] deltaVector = new float[4];
        if (timestamp != 0) {
            float dT = (event.timestamp - timestamp) * NS2S;
            getGyroVector(event.values, deltaVector, dT);
        }
        timestamp = event.timestamp;

        float[] deltaMat = new float[16];
        SensorManager.getRotationMatrixFromVector(deltaMat, deltaVector);
        float[] temp = new float[16];
        MainActivity.mGLView.doRotate(deltaMat);
    }

    public void onResume() {
        mSensorManager.registerListener(this, gyroHandler.mGyroscope, SensorManager.SENSOR_DELAY_FASTEST);
        SoundHandler.getInstance().start();
    }

    public void onPause() {
        mSensorManager.unregisterListener(this);
        SoundHandler.getInstance().stop();
    }
}