package kr.ac.kaist.vclab.bubble.physics;

/**
 * Created by mnswpr on 11/2/2016.
 */

public class Spring {
    float particleA[];
    float particleB[];
    float length;
    float k;

    public Spring(float[] _anchor, float _length, float _k){
        anchor[0] = _anchor[0];
        anchor[1] = _anchor[1];
        anchor[2] = _anchor[2];

        length = _length;
        k = _k;
    }
}
