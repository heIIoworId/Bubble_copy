package kr.ac.kaist.vclab.bubble;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;

/**
 * Created by Jongmin on 2016-11-05.
 */

public class GyroHandler implements SensorEventListener {
    Sensor mGyroscope;
    private float gyroX;
    private float gyroY;
    private float gyroZ;
    private float gyroScale = 0.2f;

    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    public void onSensorChanged(SensorEvent event) {
        gyroX = event.values[0] * gyroScale;
        gyroY = event.values[1] * gyroScale;
        gyroZ = event.values[2] * gyroScale;

    }

    public float[] getSensorValues() {
        float[] values = new float[3];
        values[0] = gyroX;
        values[1] = gyroY;
        values[2] = gyroZ;
        return values;
    }
}
