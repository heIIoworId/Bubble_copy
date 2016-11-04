package kr.ac.kaist.vclab.bubble.utils;

import  java.lang.Math;

/**
 * Created by 84395 on 10/29/2016.
 */

public class VecOperator {

    public static float getMag(float[] vector){
        float result;
        result = (float)Math.sqrt(
                (vector[0]*vector[0])+(vector[1]*vector[1])+(vector[2]*vector[2]));
        return result;
    }

    public static float[] normalize(float[] vector){
        float result[] = new float[3];
        float mag = getMag(vector);

        result[0] = vector[0];
        result[1] = vector[1];
        result[2] = vector[2];
        if(mag > 0 || mag < 0){
            result = scale(result, 1/mag);
        } else if(mag == 0){
            result[0] = 0f;
            result[1] = 0f;
            result[2] = 0f;
        }

        return result;
    }

    public static float[] scale(float[] vector, float scale){
        float result[] = new float[3];
        result[0] = vector[0]*scale;
        result[1] = vector[1]*scale;
        result[2] = vector[2]*scale;
        return result;
    }

    public static float[] addVectors(float[] vectorA, float[] vectorB){
        float result[] = new float[3];
        result[0] = vectorA[0] + vectorB[0];
        result[1] = vectorA[1] + vectorB[1];
        result[2] = vectorA[2] + vectorB[2];
        return result;
    }

    public static float[] sub(float[] vectorA, float[] vectorB){
        float result[] = new float[3];
        result[0] = vectorA[0] - vectorB[0];
        result[1] = vectorA[1] - vectorB[1];
        result[2] = vectorA[2] - vectorB[2];
        return result;
    };

    public static float getDistance(float[] pointA, float[] pointB){
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
