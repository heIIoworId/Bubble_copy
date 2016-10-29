package kr.ac.kaist.vclab.bubble.physics;

import java.util.ArrayList;

/**
 * Created by 84395 on 10/29/2016.
 */


// A fundamental class to apply forces to particle.
public class World {
    ArrayList<Particle> particles;

    public World(){

    };

    public ArrayList<Particle> getParticles() {
        return particles;
    }

    public void setParticles(ArrayList<Particle> particles) {
        this.particles = particles;
    }


}
