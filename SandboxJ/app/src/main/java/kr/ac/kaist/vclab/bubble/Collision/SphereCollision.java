package kr.ac.kaist.vclab.bubble.Collision;

/**
 * Created by Jongmin on 2016-10-23.
 */

import android.opengl.Matrix;

import kr.ac.kaist.vclab.bubble.MatOperator;

public class SphereCollision extends Collision{
    private float radius;

    public SphereCollision(float radius){
        originalCenter = new float[]{0,0,0,1};
        center = new float[]{0,0,0,1};

        this.radius = radius;
        //this.originalCenter = pos;
    }

    public SphereCollision(float[] pos, float radius){
        originalCenter = pos;
        center = pos;

        this.radius = radius;
    }

    public float GetRadius(){
        return radius;
    }

    @Override
    public void move(float[] transformation){
        float[] translation = MatOperator.matTranslation(transformation);
        Matrix.multiplyMV(center, 0, translation, 0, originalCenter, 0);
    }
    public void scaleRadius(float scale){
        if(scale > 0) {
            radius *= scale;
        }
        System.out.println(radius + " scale");
    }

}
