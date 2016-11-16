package kr.ac.kaist.vclab.bubble.physics;

/**
 * Created by 84395 on 10/29/2016.
 */

// A basic element which has physical properties such as mass, location, velocity, and etc.
public class Particle {

    private float mass;
    private float location[];
    private float velocity[];
    private float acceleration[];

    public Particle(float x, float y, float z) {
        mass = 1.0f;
        location = new float[3];
        location[0] = x;
        location[1] = y;
        location[2] = z;
        velocity = new float[3];
        acceleration = new float[3];
    }

    public Particle(float[] _location) {
        mass = 1.0f;
        location = _location;
        velocity = new float[3];
        acceleration = new float[3];
    }

    public void applyForce(float[] force) {
        acceleration[0] = force[0] / mass;
        acceleration[1] = force[1] / mass;
        acceleration[2] = force[2] / mass;

        velocity[0] += acceleration[0];
        velocity[1] += acceleration[1];
        velocity[2] += acceleration[2];

        location[0] += velocity[0];
        location[1] += velocity[1];
        location[2] += velocity[2];
    }

    public float[] getLocation() {
        return location;
    }

    public boolean isColocated(float[] _location) {
        boolean result = false;
        if (location[0] == _location[0] && location[1] == _location[1] && location[2] == _location[2]) {
            result = true;
        }
        return result;
    }
}
