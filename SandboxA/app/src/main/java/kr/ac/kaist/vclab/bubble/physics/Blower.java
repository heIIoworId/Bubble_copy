package kr.ac.kaist.vclab.bubble.physics;

import java.util.ArrayList;

import kr.ac.kaist.vclab.bubble.events.SoundHandler;
import kr.ac.kaist.vclab.bubble.utils.GLHelper;
import kr.ac.kaist.vclab.bubble.utils.VecOperator;

/**
 * Created by mnswpr on 11/16/2016.
 */

public class Blower {
    SoundHandler soundHandler;
    float[] blowingDir;
    ArrayList<Particle> particles;

    public Blower(){
        soundHandler = new SoundHandler();
        soundHandler.start();
    }

    public void setBlowingDir(float[] viewMatrix){
        blowingDir = GLHelper.getViewVector(viewMatrix);
    }

    public void setParticles(ArrayList<Particle> _particles){
        particles = _particles;
    }

    public void applyForce(){
        float amplitude = (float) (soundHandler.getAmplitude()/200000f);
        float blowForce[] = VecOperator.scale(blowingDir, amplitude);
        for(int i = 0; i<particles.size(); i++){
            particles.get(i).applyForce(blowForce);
        }
    }
}
