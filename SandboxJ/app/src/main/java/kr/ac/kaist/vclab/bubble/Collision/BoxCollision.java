package kr.ac.kaist.vclab.bubble.Collision;

import android.opengl.Matrix;

import kr.ac.kaist.vclab.bubble.MatOperator;

import static android.R.transition.move;

/**
 * Created by Jongmin on 2016-10-23.
 */

public class BoxCollision extends Collision {
    float[] axis1;
    float[] axis2;
    float[] axis3;
    float[] originAxis = new float[16];
    float[] axes = new float[16];



    public BoxCollision(float[] axis1, float[] axis2, float[] axis3){
        originalCenter = new float[]{0,0,0,1};
        center = new float[]{0,0,0,1};
        //System.out.println("hiiii");
        /*
        for(int i=0; i<4; i++){
            for(int j=0; j<4; j++){
                if(i==3){
                    originAxis[i*4+j] = 0;
                }
                else {
                    switch (j % 4) {
                        case 0:
                            originAxis[i * 4 + j] = axis1[i];
                            break;
                        case 1:
                            originAxis[i * 4 + j] = axis2[i];
                            break;
                        case 2:
                            originAxis[i * 4 + j] = axis3[i];
                            break;
                        default:
                            originAxis[i * 4 + j] = 0;
                    }
                }
            }
        }
        */
        for(int i=0; i<4; i++){
            for(int j=0; j<4; j++){
                if(i==3){
                    originAxis[i + j*4] = 0;
                }
                else {
                    switch (j) {
                        case 0:
                            originAxis[i + j*4] = axis1[i];
                            break;
                        case 1:
                            originAxis[i + j*4] = axis2[i];
                            break;
                        case 2:
                            originAxis[i + j*4] = axis3[i];
                            break;
                        default:
                            originAxis[i + j*4] = 0;
                    }
                }
            }
        }
        this.axis1 = axis1;
        this.axis2 = axis2;
        this.axis3 = axis3;
    }

    @Override
    public void move(float[] transformation){
        float[] translation = MatOperator.matTranslation(transformation);
        float[] linear = MatOperator.matLinear(transformation);
        Matrix.multiplyMV(center, 0, translation, 0, originalCenter, 0);
        Matrix.multiplyMM(axes, 0, linear, 0, originAxis, 0 );

        for(int i=0; i<3; i++){
            for (int j=0; j<4; j++){
                switch (j){
                    case 0:
                        axis1[i] = axes[i+j*4];
                        break;
                    case 1:
                        axis2[i] = axes[i+j*4];
                        break;
                    case 2:
                        axis3[i] = axes[i+j*4];
                        break;
                    default:
                        break;
                }
            }
        }
    }

    public void scaleAxes(float scale){
        for(int i=0; i<16; i++){
            originAxis[i] *= scale;
        }
    }

    public void scaleAxes(float[] scale){
        for(int i=0; i<16; i++){
            if(i%4 != 3) {
                originAxis[i] *= scale[i % 4];
            }
        }
    }

}
