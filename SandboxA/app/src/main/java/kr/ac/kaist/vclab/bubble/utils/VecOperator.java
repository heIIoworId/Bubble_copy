package kr.ac.kaist.vclab.bubble.utils;

import android.opengl.Matrix;
import  java.lang.Math;
import java.util.Arrays;

/**
 * Created by 84395 on 10/29/2016.
 */

public class VecOperator {
    public static float[] addVectors(float[] vectorA, float[] vectorB){
        float result[] = new float[3];
        result[0] = vectorA[0] + vectorB[0];
        result[1] = vectorA[1] + vectorB[1];
        result[2] = vectorA[2] + vectorB[2];
        return result;
    }

    public static float[] subVectors(float[] vectorA, float[] vectorB){
        float result[] = new float[3];
        result[0] = vectorA[0] - vectorB[0];
        result[1] = vectorA[1] - vectorB[1];
        result[2] = vectorA[2] - vectorB[2];
        return result;
    };

    public static float distOfTwoPoints(float[] pointA, float[] pointB){
        float dist=0;

        float distXa = pointA[0];
        float distXb = pointB[0];
        float sqDistX = (distXa-distXb)*(distXa-distXb);

        float distYa = pointA[1];
        float distYb = pointB[1];
        float sqDistY = (distYa-distYb)*(distYa-distYb);

        float distZa = pointA[2];
        float distZb = pointB[2];
        float sqDistZ = (distZa-distZb)*(distZa-distZb);

        dist = (float)Math.sqrt(sqDistX + sqDistY + sqDistZ);
        return dist;
    };
}
