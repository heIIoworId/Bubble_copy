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
    private String[] labels = new String[]{"World", "Map"};
    private String[] modes = new String[]{"world", "map"};

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

        // buttons - for changing the mode
        final ToggleButton[] modeButtons = new ToggleButton[labels.length];

        for (int i = 0; i < labels.length; i++) {
            modeButtons[i] = new ToggleButton(this);

            modeButtons[i].setText(labels[i]);
            modeButtons[i].setTextOn(labels[i]);
            modeButtons[i].setTextOff(labels[i]);
        }

        for (int i = 0; i < labels.length; i++) {
            buttonLayout.addView(modeButtons[i]);
        }

        // button - for set hint on / off
        final ToggleButton hintButton = new ToggleButton(this);

        hintButton.setText("Hint");
        hintButton.setTextOn("HintOn");
        hintButton.setTextOff("HintOff");

        buttonLayout.addView(hintButton);

        // listener
        for (int i = 0; i < labels.length; i++) {
            final String modeChoosed = modes[i];

            modeButtons[i].setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        for (int j = 0; j < labels.length; j++) {
                            if (modeButtons[j] != buttonView) {
                                modeButtons[j].setChecked(false);
                            }

                            mGLView.mode = modeChoosed;
                        }
                    }
                }
            });
        }

        hintButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    mGLView.mRenderer.mapBlendFlag = true;
                    mGLView.requestRender();
                } else {
                    mGLView.mRenderer.mapBlendFlag = false;
                    mGLView.requestRender();
                }
            }
        });

        // default checked button
        modeButtons[0].setChecked(true);
        hintButton.setChecked(false);

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
