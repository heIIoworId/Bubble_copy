package kr.ac.kaist.vclab.bubble.models;

import kr.ac.kaist.vclab.bubble.environment.GameEnv;

/**
 * Created by mnswpr on 11/23/2016.
 */

// FIXME SG
public class Item extends GeneralSphere {

    // FIXME SG (HOW TO MAKE IT GLOW?)
    // FIXME SG (HAVING TO CHANGE COLOR AS RANDOM)
    private float center[];
    private boolean isHitted;
    private int itemType;

    public Item(float[] _center){
        super(GameEnv.getInstance().radiusOfItem, GameEnv.getInstance().levelOfItem);
        center = _center;
        isHitted = false;
        // FIXME SG (NOT USED YET)
        itemType = 1; // 0: BAD, 1: GOOD
    }

    public boolean getHitstatus(){
        return isHitted;
    }
    public void updateHitStatus(){
        isHitted = true;
        makeItInvisible();
    }
    public float[] getCenter(){
        return center;
    }
    // FIXME SG (RE-IMPLEMENT IT NOT TO DRAW)
    private void makeItInvisible(){
        this.setVertices(new float[]{0f,0f,0f});
    }
}
