package kr.ac.kaist.vclab.bubble.physics;

import kr.ac.kaist.vclab.bubble.utils.VecOperator;

/**
 * Created by mnswpr on 11/2/2016.
 */

public class Spring {
    Particle particleA;
    Particle particleB;
    float length = 3.0f; //DEFAULT SPRING LENGTH
    float k = 0.000001f; // HOOKEAN COEFFICIENT

    public Spring(Particle _particleA, Particle _particleB){
        particleA = _particleA;
        particleB = _particleB;
    }

    public void applyForce(){
        float forceA[];
        float forceB[];

        float[] pointA = particleA.getLocation();
//        System.out.println("pointA: " + pointA[0] + ", " + pointA[1] + ", " + pointA[2]);
        float[] pointB = particleB.getLocation();
        float stretch = VecOperator.getDistance(pointA, pointB)-length;

        forceA = VecOperator.sub(pointA, pointB);
//        System.out.println("pointA: " + pointA[0] + ", " + pointA[1] + ", " + pointA[2]);
        System.out.println("forceA: " + forceA[0] + ", " + forceA[1] + ", " + forceA[2]);

        forceA = VecOperator.normalize(forceA);
        forceA = VecOperator.scale(forceA, k*(-1));
        forceA = VecOperator.scale(forceA, stretch);

        forceB = VecOperator.sub(pointB, pointA);
        forceB = VecOperator.normalize(forceB);
        forceB = VecOperator.scale(forceB, k*(-1));
        forceB = VecOperator.scale(forceB, stretch);

        particleA.applyForce(forceA);
        particleB.applyForce(forceB);
    }

    public boolean isColocated(float[] locationA, float[] locationB) {
        boolean result = false;
            if(particleA.isColocated(locationA) && particleB.isColocated(locationB)){
                result = true;
            } else if(particleA.isColocated(locationB) && particleB.isColocated(locationA)){
                result = true;
            }
        return result;
    }

    public Particle getParticleA(){
        return particleA;
    }
}
