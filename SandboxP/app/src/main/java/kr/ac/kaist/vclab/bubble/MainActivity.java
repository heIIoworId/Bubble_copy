package kr.ac.kaist.vclab.bubble;

import android.app.Activity;
import android.content.Context;
// import android.hardware.Sensor;
// import android.hardware.SensorEvent;
// import android.hardware.SensorEventListener;
// import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.ToggleButton;

public class MainActivity extends Activity {
    public static Context context;
    private MyGLSurfaceView mGLView;
    // private GyroHandler gyroHandler;
    // private SensorManager mSensorManager;

    // private String[] labels = new String[]{"World", "Cube", "Map", "Bubble"};
    // private String[] modes = new String[]{"world", "cube", "map", "bubble"};
    private String[] labels = new String[]{"World", "Cube", "Map"};
    private String[] modes = new String[]{"world", "cube", "map"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MainActivity.context = getApplicationContext();

        // create mGLView and set it as the ContentView for this activity
        mGLView = new MyGLSurfaceView(this);
        // gyroHandler = new GyroHandler();

        // layouts
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);

        LinearLayout buttonLayout = new LinearLayout(this);
        buttonLayout.setOrientation(LinearLayout.HORIZONTAL);
        buttonLayout.setGravity(Gravity.CENTER);

        // buttons
        final ToggleButton[] buttons = new ToggleButton[labels.length];

        for (int i = 0; i < labels.length; i++) {
            buttons[i] = new ToggleButton(this);

            buttons[i].setText(labels[i]);
            buttons[i].setTextOn(labels[i]);
            buttons[i].setTextOff(labels[i]);
        }

        for (int i = 0; i < labels.length; i++) {
            buttonLayout.addView(buttons[i]);
        }

        // listener
        for (int i = 0; i < labels.length; i++) {
            final String modeChoosed = modes[i];

            buttons[i].setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        for (int j = 0; j < labels.length; j++) {
                            if (buttons[j] != buttonView) {
                                buttons[j].setChecked(false);
                            }

                            mGLView.mode = modeChoosed;
                        }
                    }
                }
            });
        }

        // default checked button
        buttons[0].setChecked(true);

        // layout params
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

        // mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        //자이로스코프 센서(회전)
        // gyroHandler.mGyroscope = mSensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
    }

    /*
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
    */

    /*
    public void onSensorChanged(SensorEvent event) {
        Sensor sensor = event.sensor;

        if (sensor.getType() == Sensor.TYPE_GYROSCOPE) {
            gyroHandler.onSensorChanged(event);
        }
        float[] values = new float[3];
        values = gyroHandler.getSensorValues();
        mGLView.rotateByGyroSensor(values[0], -values[1], -values[2]);
    }
    */

    @Override
    protected void onPause() {
        super.onPause();
        // mSensorManager.registerListener(this, gyroHandler.mGyroscope, SensorManager.SENSOR_DELAY_FASTEST);
        mGLView.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mGLView.onResume();
    }
}
