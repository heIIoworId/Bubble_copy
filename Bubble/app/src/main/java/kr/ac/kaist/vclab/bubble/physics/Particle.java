package kr.ac.kaist.vclab.bubble.physics;

import kr.ac.kaist.vclab.bubble.utils.VecOperator;

/**
 * Created by 84395 on 10/29/2016.
 */

// A basic element which has physical properties such as mass, location, velocity, and etc.
public class Particle {

    private float mass;
    private float location[];
    private float velocity[];
    private float acceleration[];
    private float damping = 1f;

    public Particle(float x, float y, float z){
        mass = 1.0f;
        location = new float[3];
        location[0] = x;
        location[1] = y;
        location[2] = z;
        velocity = new float[3];
        acceleration = new float[3];
    }

    public Particle(float[] _location){
        mass = 1.0f;
        location = _location;
        velocity = new float[3];
        acceleration = new float[3];
    }

    public void applyForce(float[] force){
        acceleration = VecOperator.scale(force, 1.0f/mass);
        velocity = VecOperator.add(velocity, acceleration);
        velocity = VecOperator.scale(velocity, damping);
        location = VecOperator.add(location, velocity);
        acceleration = VecOperator.scale(acceleration, 0f);
    }

    public float[] getLocation(){
        return location;
    }
    public void setLocation(float[] _location){
        location = _location;
    }

    public void setDamping(float _damping){
        damping = _damping;
    }

    public boolean isColocated(float[] _location) {
        boolean result = false;
        if(location[0]==_location[0] && location[1]==_location[1] && location[2]==_location[2]){
            result = true;
        }
        return result;
    }

    public float[] getVelocity(){
        return velocity;
    }
    public void setVelocity(float[] _velocity){
        velocity = _velocity;
    }

}
