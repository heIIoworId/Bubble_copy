package kr.ac.kaist.vclab.bubble.physics;

import java.util.ArrayList;

import kr.ac.kaist.vclab.bubble.utils.SystemHelper;

/**
 * Created by 84395 on 10/29/2016.
 */


// A fundamental class to apply forces to particle.
public class World {
    private float gravity[] = {0f, -0.0003f, 0f};
    private float damping = 1.0f;

    ArrayList<Particle> particles;
    ArrayList<Spring> springs;
    Particle bubbleCore;
    Blower blower;

    public World(){
        particles = new ArrayList<>();
        springs = new ArrayList<>();
        // FIXME BLOWER COMMENTED OUT
//        blower = new Blower();
    }

    public void applyForce(){

        // SET DAMPING RATIO
        for(int i = 0; i<particles.size(); i++){
            particles.get(i).setDamping(damping);
        }
        bubbleCore.setDamping(damping);

        // APPLY FORCE
        bubbleCore.applyForce(gravity);
        for (int i=0; i<springs.size(); i++){
            springs.get(i).applyForce();
        }

        // FIXME BLOWER COMMENTED OUT
//        blower.applyForce();
    }

    public ArrayList<Particle> getParticles(){
        return particles;
    }

    public void setParticles(ArrayList<Particle> _particles){
        particles = _particles;
    }

    public void setSprings(ArrayList<Spring> _springs){
        springs = _springs;
    }

    public void setBubbleCore(Particle _bubbleCore){
        bubbleCore = _bubbleCore;
    }

    public void setBlower(Blower _blower){
        blower = _blower;
    }

}
