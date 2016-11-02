package kr.ac.kaist.vclab.bubble.physics;

import java.util.ArrayList;

/**
 * Created by 84395 on 10/29/2016.
 */


// A fundamental class to apply forces to particle.
public class World {
    private float gravity[] = {0f, -0.1f, 0f};
    private float dragCoeff = 0.1f;

    ArrayList<Particle> particles;
    ArrayList<Spring> springs;

    public World(){
        particles = new ArrayList<Particle>();
        springs = new ArrayList<Spring>();
    };

    public void applyForce(){
        for(int i=0; i<particles.size(); i++){
            particles.get(i).applyForce(gravity);
        };
        for (int i=0; i<springs.size(); i++){
            springs.get(i).applyForce();
        }
    };
}
