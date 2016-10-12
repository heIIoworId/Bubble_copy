package kr.ac.kaist.vclab.bubble.activities;

import android.app.Activity;
import android.content.Context;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.ToggleButton;

import java.io.IOException;

import kr.ac.kaist.vclab.bubble.SoundMeter;
import kr.ac.kaist.vclab.bubble.MyGLSurfaceView;
/**
 * Created by sjjeon on 16. 9. 20.
 */

public class MainActivity extends Activity {

    public static Context context;
    private MyGLSurfaceView mGLView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        MainActivity.context = getApplicationContext();

        // Create a GLSurfaceView instance and set it
        // as the ContentView for this Activity
        mGLView = new MyGLSurfaceView(this);

//        isBlowing();

        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);

        LinearLayout buttonLayout = new LinearLayout(this);
        buttonLayout.setOrientation(LinearLayout.HORIZONTAL);
        buttonLayout.setGravity(Gravity.CENTER);

        ToggleButton worldButton = new ToggleButton(this);
        ToggleButton cube1Button = new ToggleButton(this);
//        ToggleButton cube2Button = new ToggleButton(this);

        setButtonText(worldButton, "World");
        setButtonText(cube1Button, "Cube");
//        setButtonText(cube2Button, "Cube2");

        buttonLayout.addView(worldButton);
        buttonLayout.addView(cube1Button);
//        buttonLayout.addView(cube2Button);

//        final ToggleButton[] buttons = {worldButton, cube1Button, cube2Button};
        final ToggleButton[] buttons = {worldButton, cube1Button};

        for (int i = 0; i < buttons.length; i++) {
            final ToggleButton button = buttons[i];

            final int finalI = i;
            button.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        for (ToggleButton toggleButton : buttons) {
                            if (toggleButton != buttonView) {
                                toggleButton.setChecked(false);
                            }
                            mGLView.mode = finalI;
                        }
                    }
                }
            });
        }
        worldButton.setChecked(true);

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
        button.setTextOn(text);
        button.setTextOff(text);
    }


    public boolean isBlowing()
    {
        boolean recorder=true;
        int sampleRatio = 0;


        for (int rate : new int[] {8000, 11025, 16000, 22050, 44100}) {  // add the rates you wish to check against
            int bufferSize = AudioRecord.getMinBufferSize(rate, AudioFormat.CHANNEL_IN_MONO, AudioFormat.ENCODING_PCM_16BIT);
            if (bufferSize > 0) {
                // buffer size is valid, Sample rate supported
                sampleRatio = rate;
            }
        }

        int minSize = AudioRecord.getMinBufferSize(8000, AudioFormat.CHANNEL_IN_MONO, AudioFormat.ENCODING_PCM_16BIT);
        AudioRecord ar = new AudioRecord(MediaRecorder.AudioSource.MIC, 8000, AudioFormat.CHANNEL_IN_MONO, AudioFormat.ENCODING_PCM_16BIT, minSize);
        ar.release();

        short[] buffer = new short[minSize];

        ar.startRecording();
        while(recorder)
        {
            ar.read(buffer, 0, minSize);
            for (short s : buffer)
            {
                if (Math.abs(s) > 27000)   //DETECT VOLUME (IF I BLOW IN THE MIC)
                {
                    int blow_value=Math.abs(s);
                    System.out.println("Blow Value="+blow_value);
                    ar.stop();
                    recorder=false;

                    return true;

                }

            }
        }
        return false;
    }
//
//    private static int[] mSampleRates = new int[] { 8000, 11025, 22050, 44100 };
//    public AudioRecord findAudioRecord() {
//        for (int rate : mSampleRates) {
//            for (short audioFormat : new short[] { AudioFormat.ENCODING_PCM_8BIT, AudioFormat.ENCODING_PCM_16BIT }) {
//                for (short channelConfig : new short[] { AudioFormat.CHANNEL_IN_MONO, AudioFormat.CHANNEL_IN_STEREO }) {
//                    try {
//                        Log.d(C.TAG, "Attempting rate " + rate + "Hz, bits: " + audioFormat + ", channel: "
//                                + channelConfig);
//                        int bufferSize = AudioRecord.getMinBufferSize(rate, channelConfig, audioFormat);
//
//                        if (bufferSize != AudioRecord.ERROR_BAD_VALUE) {
//                            // check if we can instantiate and have a success
//                            AudioRecord recorder = new AudioRecord(MediaRecorder.AudioSource.DEFAULT, rate, channelConfig, audioFormat, bufferSize);
//
//                            if (recorder.getState() == AudioRecord.STATE_INITIALIZED)
//                                return recorder;
//                        }
//                    } catch (Exception e) {
//                        Log.e(C.TAG, rate + "Exception, keep trying.",e);
//                    }
//                }
//            }
//        }
//        return null;
//    }

//    public class SoundMeter {
//
//        private MediaRecorder mRecorder = null;
//
//        public void start() {
//            if (mRecorder == null) {
//                mRecorder = new MediaRecorder();
//                mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
//                mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
//                mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
//                mRecorder.setOutputFile("/dev/null");
//                try {
//                    mRecorder.prepare();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//                mRecorder.start();
//            }
//        }
//
//        public void stop() {
//            if (mRecorder != null) {
//                mRecorder.stop();
//                mRecorder.release();
//                mRecorder = null;
//            }
//        }
//
//        public double getAmplitude() {
//            if (mRecorder != null)
//                return  mRecorder.getMaxAmplitude();
//            else
//                return 0;
//
//        }
//    }

    @Override
    protected void onPause() {
        super.onPause();
        // The following call pauses the rendering thread.
        // If your OpenGL application is memory intensive,
        // you should consider de-allocating objects that
        // consume significant memory here.
        mGLView.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // The following call resumes a paused rendering thread.
        // If you de-allocated graphic objects for onPause()
        // this is a good place to re-allocate them.
        mGLView.onResume();
    }
}
