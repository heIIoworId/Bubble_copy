package kr.ac.kaist.vclab.bubble.utils;

import java.util.ArrayList;

import kr.ac.kaist.vclab.bubble.physics.Particle;
import kr.ac.kaist.vclab.bubble.physics.Spring;

/**
 * Created by mnswpr on 11/2/2016.
 */

public class GeomOperator {
    public static ArrayList<Particle> genParticles(float[] vertices){
        ArrayList<Particle> particles = new ArrayList<>();
        for(int i = 0; i<vertices.length; i += 3){
            boolean isUnique = true;
            float location[] = {vertices[i], vertices[i+1], vertices[i+2]};

            for(int j=0; j<particles.size(); j++){
                Particle currParticle = particles.get(j);
                if(isUnique){
                    isUnique = !currParticle.isColocated(location);
                    if(!isUnique) { // WHEN isUnique IS FALSE,
                        particles.add(currParticle);
                    }
                }
            }

            if(isUnique){
                particles.add(new Particle(location));
            }
        }
        return particles;
    }
    public static ArrayList<Spring> genSprings(ArrayList<Particle> particles){
        ArrayList<Spring> springs = new ArrayList<>();
        for(int i=0; i<particles.size(); i += 3){
            boolean isUniqueAB = true;
            boolean isUniqueBC = true;
            boolean isUniqueCA = true;
            Particle a = particles.get(i);
            Particle b = particles.get(i+1);
            Particle c = particles.get(i+2);

            for(int j=0; j<springs.size(); j++){
                Spring currSpring = springs.get(j);
                if(isUniqueAB){
                    isUniqueAB = !currSpring.isColocated(a.getLocation(), b.getLocation());
                }
                if(isUniqueBC){
                    isUniqueBC = !currSpring.isColocated(b.getLocation(), c.getLocation());
                }
                if(isUniqueCA){
                    isUniqueCA = !currSpring.isColocated(c.getLocation(), a.getLocation());
                }
            }

            if(isUniqueAB){
                springs.add(new Spring(a, b));
            }
            if(isUniqueBC){
                springs.add(new Spring(b, c));
            }
            if(isUniqueCA){
                springs.add(new Spring(c, a));
            }
        }
        return springs;
    }
}
