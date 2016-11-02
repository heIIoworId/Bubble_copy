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
        float forceA[] = new float[3];
        float forceB[] = new float[3];

        float[] pointA = particleA.getLocation();
        float[] pointB = particleB.getLocation();
        float stretch = VecOperator.distOfTwoPoints(pointA, pointB)-length;

        forceA[] =


    }
}
