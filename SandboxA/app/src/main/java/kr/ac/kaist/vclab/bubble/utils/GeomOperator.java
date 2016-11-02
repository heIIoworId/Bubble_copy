package kr.ac.kaist.vclab.bubble.utils;

import java.util.ArrayList;

import kr.ac.kaist.vclab.bubble.physics.Particle;

/**
 * Created by mnswpr on 11/2/2016.
 */

//FIXME
public class GeomOperator {
    public static ArrayList<Particle> genParticles(float[] vertices){
        ArrayList<Particle> particles = new ArrayList<>();
        for(int i = 0; i<vertices.length; i += 3){
            particles.add(new Particle(vertices[i], vertices[i+1], vertices[i+2]));
        }
        return particles;
    }
}
