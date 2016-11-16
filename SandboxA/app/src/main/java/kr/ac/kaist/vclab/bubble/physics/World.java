package kr.ac.kaist.vclab.bubble.physics;

import java.util.ArrayList;

/**
 * Created by 84395 on 10/29/2016.
 */


// A fundamental class to apply forces to particle.
public class World {
    //FIXME IMPLEMENTING GRAVITY FORCE APPLICATION
    private float gravity[] = {0f, -0.1f, 0f};
    private float damping = 1.0f;

    ArrayList<Particle> particles;
    ArrayList<Spring> springs;

    public World(){
        particles = new ArrayList<Particle>();
        springs = new ArrayList<Spring>();
    }

    public void applyForce(){
        //FIXME dragging force and gravity should be included.

//        for(int i=0; i<particles.size(); i++){
//            particles.get(i).applyForce(gravity);
//        }
        for (int i=0; i<springs.size(); i++){
            springs.get(i).applyForce();
        }
    }

    public ArrayList<Particle> getParticles(){
        return particles;
    }

    public void setParticles(ArrayList<Particle> _particles){
        particles = _particles;
        for(int i = 0; i<particles.size(); i++){
            particles.get(i).setDamping(damping);
        }
    }

    public void setSprings(ArrayList<Spring> _springs){
        springs = _springs;
    }
}
