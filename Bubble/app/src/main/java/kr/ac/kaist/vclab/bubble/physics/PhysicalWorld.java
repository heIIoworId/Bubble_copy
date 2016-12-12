package kr.ac.kaist.vclab.bubble.physics;

import java.util.ArrayList;

import kr.ac.kaist.vclab.bubble.environment.Env;
import kr.ac.kaist.vclab.bubble.environment.GameEnv;

/**
 * Created by 84395 on 10/29/2016.
 */


// A fundamental class to apply forces to particle.
public class PhysicalWorld {

    ArrayList<Particle> particles;
    ArrayList<Spring> springs;
    Particle bubbleCore;
    Blower blower;

    public PhysicalWorld() {
        particles = new ArrayList<>();
        springs = new ArrayList<>();
    }

    public void applyForce() {
        // SET DAMPING RATIO
        for (int i = 0; i < particles.size(); i++) {
            particles.get(i).setDamping(GameEnv.getInstance().dampingOfInnerBubble);
        }
        bubbleCore.setDamping(GameEnv.getInstance().dampingOfBubbleCore);
        if (GameEnv.getInstance().collisionFlag == 1) {
            return;
        }

        // APPLY FORCE
        for (int i = 0; i < springs.size(); i++) {
            springs.get(i).applyForce();
        }
        bubbleCore.applyForce(GameEnv.getInstance().gravity);
        if (Env.getInstance().micStatus == 1) {
            blower.applyForce();
        }
    }

    public ArrayList<Particle> getParticles() {
        return particles;
    }

    public void setParticles(ArrayList<Particle> _particles) {
        particles = _particles;
    }

    public void setSprings(ArrayList<Spring> _springs) {
        springs = _springs;
    }

    public void setBubbleCore(Particle _bubbleCore) {
        bubbleCore = _bubbleCore;
    }

    public void setBlower(Blower _blower) {
        blower = _blower;
    }

}