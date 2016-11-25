package kr.ac.kaist.vclab.bubble.Collision;

import android.opengl.Matrix;

/**
 * Created by Jongmin on 2016-10-23.
 */
public class Collision {
    protected float[] originalCenter;
    protected float[] center;

    /*
        Collision(float [] center){
            originalCenter = center;

        }
        */
    public void move(float[] transformation) {
        Matrix.multiplyMV(center, 0, transformation, 0, originalCenter, 0);
    }

    public float[] GetCenter() {
        return center;
    }

}