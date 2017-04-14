package kr.ac.kaist.vclab.bubble.Collision;

import android.opengl.Matrix;

import kr.ac.kaist.vclab.bubble.models.Item;
import kr.ac.kaist.vclab.bubble.utils.MatOperator;
import kr.ac.kaist.vclab.bubble.utils.VecOperator;

/**
 * Created by Jongmin on 2016-10-23.
 */

public class SphereCollision extends Collision {
    private float originalRadius;
    private float radius;


    public SphereCollision(float radius) {
        originalCenter = new float[]{0, 0, 0, 1};
        center = new float[]{0, 0, 0, 1};

        this.originalRadius = radius;
        this.radius = radius;
        //this.originalCenter = pos;
    }

    public SphereCollision() {
    }

    public SphereCollision(float[] pos, float radius) {
        originalCenter = pos;
        center = pos;

        this.radius = radius;
    }

    public float getRadius() {
        return radius;
    }

    public void setRadius(float _radius) {
        radius = _radius;
    }

    public void move(float[] transformation) {
        float[] translation = MatOperator.matTranslation(transformation);
        Matrix.multiplyMV(center, 0, translation, 0, originalCenter, 0);
    }


    public void scaleRadius(float scale) {
        radius = originalRadius * scale;
    }

    public boolean isCollided(BoxCollision target) {
        return Intersect.intersect(this, target);
    }

    public boolean isCollided(SphereCollision target) {
        return Intersect.intersect(this, target);
    }

    public boolean isCollided(float[] center1, float radius1, float[] center2, float radius2) {
        boolean bool = false;
        float dist = VecOperator.getDistance(center1, center2);
        if (dist <= radius1 + radius2) {
            bool = true;
        }
        return bool;
    }
}
