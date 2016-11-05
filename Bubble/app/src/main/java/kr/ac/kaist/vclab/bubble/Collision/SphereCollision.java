package kr.ac.kaist.vclab.bubble.Collision;

/**
 * Created by Jongmin on 2016-10-23.
 */

public class SphereCollision extends Collision {
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

    public void scaleRadius(float scale){
        if(scale > 0) {
            radius *= scale;
        }
        System.out.println(radius + " scale");
    }

    boolean IsCollided(BoxCollision target){
        return Intersect.intersect(this, target);
    }



    boolean IsCollided(SphereCollision target){
        return Intersect.intersect(this, target);
    }
}
