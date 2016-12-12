package kr.ac.kaist.vclab.bubble.events;

import android.media.MediaRecorder;

import java.io.IOException;

/**
 * Created by 84395 on 10/8/2016.
 */

public class SoundHandler {
    private static MediaRecorder mRecorder = null;
    private static SoundHandler ourInstance = new SoundHandler();

    public static SoundHandler getInstance() {
        return ourInstance;
    }

    private SoundHandler() {
    }

    public static void start() {
        if (mRecorder == null) {
            mRecorder = new MediaRecorder();
            mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
            mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
            mRecorder.setOutputFile("/dev/null");
            try {
                mRecorder.prepare();
            } catch (IOException e) {
                e.printStackTrace();
            }
            mRecorder.start();
        }
    }

    public static void stop() {
        if (mRecorder != null) {
            mRecorder.stop();
            mRecorder.release();
            mRecorder = null;
        }
    }

    public static double getAmplitude() {
        if (mRecorder != null)
            return mRecorder.getMaxAmplitude();
        else
            return 0;

    }
}
