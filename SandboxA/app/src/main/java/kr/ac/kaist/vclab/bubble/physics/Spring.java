package kr.ac.kaist.vclab.bubble.physics;

import kr.ac.kaist.vclab.bubble.utils.VecOperator;

/**
 * Created by mnswpr on 11/2/2016.
 */

public class Spring {
    Particle particleA;
    Particle particleB;
    float length;
    float k;

    public Spring(Particle _particleA, Particle _particleB, float _length, float _k){
        particleA = _particleA;
        particleB = _particleB;
        length = _length; // Default spring length;
        k = _k; // Hook's spring coefficient.
    }

    public void applyForce(){
        float forceA[];
        float forceB[];

        float[] pointA = particleA.getLocation();
        float[] pointB = particleB.getLocation();
        float stretch = VecOperator.getDistance(pointA, pointB)-length;

        forceA = VecOperator.sub(pointA, pointB);
        forceA = VecOperator.normalize(forceA);
        forceA = VecOperator.scale(forceA, k);
        forceA = VecOperator.scale(forceA, stretch);

        forceB = VecOperator.sub(pointB, pointA);
        forceB = VecOperator.normalize(forceB);
        forceB = VecOperator.scale(forceB, k);
        forceB = VecOperator.scale(forceB, stretch);

        particleA.applyForce(forceA);
        particleB.applyForce(forceB);
    }
}
