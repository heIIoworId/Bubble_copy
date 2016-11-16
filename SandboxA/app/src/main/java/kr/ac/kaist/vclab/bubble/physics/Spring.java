package kr.ac.kaist.vclab.bubble.physics;

import kr.ac.kaist.vclab.bubble.utils.VecOperator;

/**
 * Created by mnswpr on 11/2/2016.
 */

public class Spring {
    Particle particleA;
    Particle particleB;
    float restLength = 1f; //DEFAULT SPRING LENGTH
    float minLength = 0.5f;
    float maxLength = 50.0f;
    float k = 0.00001f; // HOOKEAN COEFFICIENT

    public Spring(Particle _particleA, Particle _particleB){
        particleA = _particleA;
        particleB = _particleB;
    }

    public void applyForce(){
        float forceA[];
        float forceB[];

        float[] pointA = particleA.getLocation();
        float[] pointB = particleB.getLocation();
        float springLength = VecOperator.getDistance(pointA, pointB);

        if(springLength < minLength){
            springLength = minLength;
            float zeroVelocity[] = {0f,0f,0f};
            particleA.setVelocity(zeroVelocity);
            particleB.setVelocity(zeroVelocity);

        } else if(springLength > maxLength){
            springLength = maxLength;
            float zeroVelocity[] = {0f,0f,0f};
            particleA.setVelocity(zeroVelocity);
            particleB.setVelocity(zeroVelocity);
        } else {
            float stretch = springLength- restLength;

            forceA = VecOperator.sub(pointA, pointB);
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

    public float getSpringLength(){
        float length;
        float[] pointA = particleA.getLocation();
        float[] pointB = particleB.getLocation();
        length = VecOperator.getDistance(pointA, pointB);
        return length;
    }

    public Particle getParticleA(){
        return particleA;
    }
}
