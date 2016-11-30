package kr.ac.kaist.vclab.bubble.collision;

/**
 * Created by Jongmin on 2016-10-23.
 */

public class SphereCollision extends Collision {
    private float radius;

    public SphereCollision(float radius) {
        originalCenter = new float[]{0, 0, 0, 1};
        center = new float[]{0, 0, 0, 1};

        this.radius = radius;
        //this.originalCenter = pos;
    }

    public SphereCollision(float[] pos, float radius) {
        originalCenter = pos;
        center = pos;

        this.radius = radius;
    }

    public float getRadius() {
        return radius;
    }

    public void setRadius(float _radius){
        radius = _radius;
    }

    public void scaleRadius(float scale) {
        if (scale > 0) {
            radius *= scale;
        }
    }

    public boolean isCollided(BoxCollision target) {
        return Intersect.intersect(this, target);
    }

    public boolean isCollided(SphereCollision target) {
        return Intersect.intersect(this, target);
    }
}
