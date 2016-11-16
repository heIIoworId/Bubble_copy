package kr.ac.kaist.vclab.bubble.Collision;

import android.opengl.Matrix;

import kr.ac.kaist.vclab.bubble.MatOperator;

/**
 * Created by Jongmin on 2016-11-15.
 */

public class TriangleCollision extends Collision{
    private float radius;
    float[] originVectors = new float[16];
    float[] vectors = new float[16];
    private float[] vector1;
    private float[] vector2;
    private float[] vector3;


    public TriangleCollision(float []a, float[] b, float[]c){
        originalCenter = new float[]{0,0,0,1};
        center = new float[]{0,0,0,1};
        //System.out.println("hiiii");
        for(int i=0; i<4; i++){
            for(int j=0; j<4; j++){
                if(i==3){
                    originVectors[i*4+j] = 0;
                }
                else {
                    switch (j % 4) {
                        case 0:
                            originVectors[i * 4 + j] = a[i];
                            break;
                        case 1:
                            originVectors[i * 4 + j] = b[i];
                            break;
                        case 2:
                            originVectors[i * 4 + j] = c[i];
                            break;
                        default:
                            originVectors[i * 4 + j] = 0;
                    }
                }
            }
        }
        this.vector1 = a;
        this.vector2 = b;
        this.vector3 = c;
    }

    public void move(float[] transformation){
        float[] linear = MatOperator.matLinear(transformation);
        Matrix.multiplyMV(center, 0, transformation, 0, originalCenter, 0);
        Matrix.multiplyMM(vectors, 0, linear, 0, originVectors, 0 );

        for(int i=0; i<3; i++){
            for (int j=0; j<4; j++){
                switch (j%4){
                    case 0:
                        vector1[i] = vectors[i*4+j];
                        break;
                    case 1:
                        vector2[i] = vectors[i*4+j];
                        break;
                    case 2:
                        vector3[i] = vectors[i*4+j];
                        break;
                    default:
                        break;
                }
            }
        }
    }

    public void scalevectors(float scale){
        for(int i=0; i<16; i++){
            originVectors[i] *= scale;
        }
    }


}
