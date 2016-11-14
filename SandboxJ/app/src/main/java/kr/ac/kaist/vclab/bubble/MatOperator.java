package kr.ac.kaist.vclab.bubble;

import android.opengl.Matrix;

import java.util.Arrays;
/**
 * Created by Jongmin on 2016-10-23.
 */

import android.opengl.Matrix;
import java.util.Arrays;

/**
 * Created by PCPC on 2016-10-04.
 */
public class MatOperator {
    public static void cross(float[] p1, float[] p2, float[] result) {
        result[0] = p1[1] * p2[2] - p2[1] * p1[2];
        result[1] = p1[2] * p2[0] - p2[2] * p1[0];
        result[2] = p1[0] * p2[1] - p2[0] * p1[1];
    }

    public static float dot(float[] p1, float[] p2) {
        return p1[0] * p2[0] + p1[1] * p2[1] + p1[2] * p2[2];
    }

    public static float size(float[] vector) {
        float sum = 0;
        for (float v : vector) {
            sum += v * v;
        }
        return (float) Math.sqrt(sum);
    }

    public static  void normalMatrix(float[] dst, int dstOffset, float[] src, int srcOffset) {
        Matrix.invertM(dst, dstOffset, src, srcOffset);
        dst[12] = 0;
        dst[13] = 0;
        dst[14] = 0;

        float[] temp = Arrays.copyOf(dst, 16);

        Matrix.transposeM(dst, dstOffset, temp, 0);
    }
    public static float[] normalize(float[] vector) {
        float length = size(vector);
        for (int i = 0; i < vector.length; i++) {
            vector[i] /= length;
        }
        return vector;
    }


    public static float[] multiply(float[] mat, float a) {
        for(int i =0; i< 16; i++){
            mat[i] *= a;
        }
        return mat;
    }

    public static float[] matLinear(float[] mat){
        float[] transformation = new float[16];
        System.arraycopy(mat, 0, transformation, 0, 16);
        transformation[12]=0;
        transformation[13]=0;
        transformation[14]=0;
        return transformation;
    }
    public static float[] matTranslation(float[] mat){
        float[] transformation = new float[16];
        Matrix.setIdentityM(transformation, 0);
        transformation[12]=mat[12];
        transformation[13]=mat[13];
        transformation[14]=mat[14];
        return transformation;
    }
}
