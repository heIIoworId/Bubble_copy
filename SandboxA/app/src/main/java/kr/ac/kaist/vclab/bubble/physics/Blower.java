package kr.ac.kaist.vclab.bubble.physics;

import kr.ac.kaist.vclab.bubble.events.SoundHandler;
import kr.ac.kaist.vclab.bubble.utils.GLHelper;
import kr.ac.kaist.vclab.bubble.utils.VecOperator;

/**
 * Created by mnswpr on 11/16/2016.
 */

public class Blower {
    SoundHandler soundHandler;
    float[] blowingDir;
    Particle bubbleCore;

    public Blower(){
        soundHandler = new SoundHandler();
        soundHandler.start();
    }

    public void setBlowingDir(float[] viewMatrix){
        blowingDir = GLHelper.getViewVector(viewMatrix);
    }

    public void setBubbleCore(Particle _bubbleCore){
        bubbleCore = _bubbleCore;
    }

    public void applyForce(){
        float amplitude = (float) (soundHandler.getAmplitude()/200000f);
        float blowForce[] = VecOperator.scale(blowingDir, amplitude);
        bubbleCore.applyForce(blowForce);
    }
}
