package kr.ac.kaist.vclab.bubble.utils;

/**
 * Created by mnswpr on 11/16/2016.
 */

public class SystemHelper {
    public static void printFloatArray(float[] floatArray){
        for(int i = 0; i < floatArray.length; i = i+3){
            System.out.println(floatArray[i] + ", " + floatArray[i+1] + ", " + floatArray[i+2]);
        }
    }
}
