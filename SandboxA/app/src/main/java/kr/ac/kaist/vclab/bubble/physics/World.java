package kr.ac.kaist.vclab.bubble.physics;

import java.util.ArrayList;

import kr.ac.kaist.vclab.bubble.utils.GLHelper;

/**
 * Created by 84395 on 10/29/2016.
 */


// A fundamental class to apply forces to particle.
public class World {
    private float gravity[] = {0f, -0.1f, 0f};
    private float damping = 1.0f;

    ArrayList<Particle> particles;
    ArrayList<Spring> springs;
    Blower blower;

    public World(){
        particles = new ArrayList<Particle>();
        springs = new ArrayList<Spring>();
        blower = new Blower();
    }

    public void applyForce(){
        // FIXME IMPLEMENT GRAVITY APPLICATION
//        for(int i=0; i<particles.size(); i++){
//            particles.get(i).applyForce(gravity);
//        }
        for (int i=0; i<springs.size(); i++){
            springs.get(i).applyForce();
        }

        // FIXME
        blower.applyForce();
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

    public void setBlower(Blower _blower){
        blower = _blower;
    }
}
