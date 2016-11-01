package kr.ac.kaist.vclab.bubble;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.ToggleButton;

public class MainActivity extends Activity {
    public static Context context;
    private MyGLSurfaceView mGLView;
    private String[] labels = new String[]{"World", "Cube", "Map"};
    private String[] modes = new String[]{"world", "cube", "map"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MainActivity.context = getApplicationContext();

        // create mGLView and set it as the ContentView for this activity
        mGLView = new MyGLSurfaceView(this);

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
    }

    @Override
    protected void onPause() {
        super.onPause();
        mGLView.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mGLView.onResume();
    }
}
