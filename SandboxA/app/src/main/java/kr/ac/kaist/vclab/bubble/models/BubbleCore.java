package kr.ac.kaist.vclab.bubble.models;

import kr.ac.kaist.vclab.bubble.physics.Particle;

/**
 * Created by mnswpr on 11/17/2016.
 */

public class BubbleCore extends Particle{
    public BubbleCore(float x, float y, float z) {
        super(x, y, z);
    }

    public BubbleCore(float[] _location) {
        super(_location);
    }
}
