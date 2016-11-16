package kr.ac.kaist.vclab.bubble.utils;

import android.opengl.Matrix;

/**
 * Created by mnswpr on 11/16/2016.
 */

public class GLHelper {
    // FIXME TO BE IMPLEMENTED
    public static float[] getViewVector(float[] viewMatrix){

        float negativeZ[] = {0f,0f,-1f,0f};
        float temp[] = new float[4];
        Matrix.multiplyMV(temp, 0, viewMatrix, 0, negativeZ, 0);

        float viewVector[] = new float[3];
        viewVector[0] = temp[0];
        viewVector[1] = temp[1];
        viewVector[2] = temp[2];

        //FIXME LOG
        System.out.println("view vector: " +
                viewVector[0] + ", " + viewVector[1] + ", " + viewVector[2]);

        return viewVector;
    }
}
