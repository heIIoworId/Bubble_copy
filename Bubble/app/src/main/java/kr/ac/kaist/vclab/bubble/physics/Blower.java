package kr.ac.kaist.vclab.bubble.physics;

import android.opengl.Matrix;

import kr.ac.kaist.vclab.bubble.events.SoundHandler;
import kr.ac.kaist.vclab.bubble.utils.VecOperator;

/**
 * Created by mnswpr on 11/16/2016.
 */

public class Blower {
    static SoundHandler soundHandler;
    float[] blowingDir;
    Particle bubbleCore;

    public Blower() {
        soundHandler = SoundHandler.getInstance();
        soundHandler.start();
    }

    public static void start() {
        if (soundHandler != null)
            soundHandler.start();
    }

    public static void stop() {
        if (soundHandler != null)
            soundHandler.stop();
    }


    public void setBlowingDir(float[] viewRotationMatrix) {

        float negativeZ[] = {0f, 0f, -1f, 0f};
        float temp[] = new float[4];
        Matrix.multiplyMV(temp, 0, viewRotationMatrix, 0, negativeZ, 0);

        float viewVector[] = new float[3];
        viewVector[0] = -temp[0];
        viewVector[1] = temp[1];
        viewVector[2] = temp[2];

        blowingDir = viewVector;
    }

    public void setBlowingDirByVector(float[] vector) {
        blowingDir = vector;
    }

    public void setBubbleCore(Particle _bubbleCore) {
        bubbleCore = _bubbleCore;
    }

    public void applyForce() {
        float amplitude = (float) (soundHandler.getAmplitude() / 2000000f);
        float blowForce[] = VecOperator.scale(blowingDir, amplitude);
        bubbleCore.applyForce(blowForce);
    }

    public void reverseBlowingDir(){
        blowingDir[0] = - blowingDir[0];
        blowingDir[1] = - blowingDir[2];
        blowingDir[2] = - blowingDir[2];
    }
}
