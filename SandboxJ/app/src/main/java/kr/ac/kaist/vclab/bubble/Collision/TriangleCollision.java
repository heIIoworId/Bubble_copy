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
                    originVectors[i + j*4] = 1;
                }
                else {
                    switch (j) {
                        case 0:
                            originVectors[i + j*4] = a[i];
                            break;
                        case 1:
                            originVectors[i + j*4] = b[i];
                            break;
                        case 2:
                            originVectors[i + j*4] = c[i];
                            break;
                        default:
                            originVectors[i + j*4] = 0;
                    }
                }
            }
        }
//        System.out.println("--------------------");
//        MatOperator.print(originVectors);
//        System.out.println("--------------------");

        this.vector1 = a;
        this.vector2 = b;
        this.vector3 = c;
    }

    public void move(float[] transformation){

//        System.out.println("--------------------");
//        System.out.println("trans");
//        MatOperator.print(transformation);
//        System.out.println("linear");
//        MatOperator.print(linear);

//        System.out.println("before");
//        MatOperator.print(vectors);

        Matrix.multiplyMM(vectors, 0, transformation, 0, originVectors, 0 );
//        System.out.println("after");
//        MatOperator.print(vectors);
//        System.out.println("--------------------");

        for(int i=0; i<3; i++){
            for (int j=0; j<4; j++){
                switch (j){
                    case 0:
                        vector1[i] = vectors[i+j*4];
                        break;
                    case 1:
                        vector2[i] = vectors[i+j*4];
                        break;
                    case 2:
                        vector3[i] = vectors[i+j*4];
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
