package kr.ac.kaist.vclab.bubble.physics;

import kr.ac.kaist.vclab.bubble.utils.VecOperator;

/**
 * Created by mnswpr on 11/2/2016.
 */

public class Spring {
    Particle particleA;
    Particle particleB;
    float restLength; //DEFAULT SPRING LENGTH
    float minLength;
    float maxLength;
    float k = 0.08f; // HOOKEAN COEFFICIENT

    public Spring(Particle _particleA, Particle _particleB) {
        particleA = _particleA;
        particleB = _particleB;
    }

    public void applyForce() {
        float forceA[];
        float forceB[];

        float[] pointA = particleA.getLocation();
        float[] pointB = particleB.getLocation();
        float springLength = VecOperator.getDistance(pointA, pointB);

        if (springLength < minLength) {
            springLength = minLength;
            float zeroVelocity[] = {0f, 0f, 0f};
            particleA.setVelocity(zeroVelocity);
            particleB.setVelocity(zeroVelocity);

        } else if (springLength > maxLength) {
            springLength = maxLength;
            float zeroVelocity[] = {0f, 0f, 0f};
            particleA.setVelocity(zeroVelocity);
            particleB.setVelocity(zeroVelocity);
        } else {
            float stretch = springLength - restLength;

            forceA = VecOperator.sub(pointA, pointB);
            forceA = VecOperator.normalize(forceA);
            forceA = VecOperator.scale(forceA, k);
            forceA = VecOperator.scale(forceA, (-1f) * stretch);

            forceB = VecOperator.sub(pointB, pointA);
            forceB = VecOperator.normalize(forceB);
            forceB = VecOperator.scale(forceB, k);
            forceB = VecOperator.scale(forceB, (-1f) * stretch);

            particleA.applyForce(forceA);
            particleB.applyForce(forceB);
        }
    }

    public boolean isColocated(float[] locationA, float[] locationB) {
        boolean result = false;
        if (particleA.isColocated(locationA) && particleB.isColocated(locationB)) {
            result = true;
        } else if (particleA.isColocated(locationB) && particleB.isColocated(locationA)) {
            result = true;
        }
        return result;
    }

    public float getSpringLength() {
        float length;
        float[] pointA = particleA.getLocation();
        float[] pointB = particleB.getLocation();
        length = VecOperator.getDistance(pointA, pointB);
        return length;
    }

    public void setRestLength(float _restLength) {
        restLength = _restLength;
    }

    public void setMinLength(float _minLength) {
        minLength = _minLength;
    }

    public void setMaxLength(float _maxLength) {
        maxLength = _maxLength;
    }

    public Particle getParticleA() {
        return particleA;
    }
}
