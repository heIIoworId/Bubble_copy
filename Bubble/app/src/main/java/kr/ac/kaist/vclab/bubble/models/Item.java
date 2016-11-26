package kr.ac.kaist.vclab.bubble.models;

import kr.ac.kaist.vclab.bubble.environment.GameEnv;

/**
 * Created by mnswpr on 11/23/2016.
 */

// FIXME SG
public class Item {

    private float center[];
    private float radius;
    private boolean isHitted;
    private int itemType;

    public Item(float[] _center){
        center = _center;
        radius = GameEnv.getInstance().radiusOfItem;
        isHitted = false;
        itemType = 1; // 0: BAD, 1: GOOD
    }

    public void hitted(){
        isHitted = true;
    }


}
