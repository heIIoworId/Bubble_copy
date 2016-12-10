package kr.ac.kaist.vclab.bubble.models;

import kr.ac.kaist.vclab.bubble.environment.GameEnv;

/**
 * Created by mnswpr on 11/23/2016.
 */

public class Item extends GeneralSphere {

    // FIXME SG (HOW TO MAKE IT GLOW?)
    private float center[];
    public boolean isHitted;
    private int itemType;

    public Item(float[] _center){
        super(GameEnv.getInstance().radiusOfItem, GameEnv.getInstance().levelOfItem);
        center = _center;
        isHitted = false;
        // FIXME SG (NOT USED YET)
        itemType = 1; // 0: BAD, 1: GOOD
    }

    public void markAsHitted(){
        isHitted = true;
    }
    public float[] getCenter(){
        return center;
    }
    public void setCenter(float[] _center){
        center = _center;
    }
}
