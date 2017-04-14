package kr.ac.kaist.vclab.bubble.Collision;


import kr.ac.kaist.vclab.bubble.utils.VecOperator;

/**
 * Created by Jongmin on 2016-10-28.
 */

public class Intersect {
    public static boolean intersect(SphereCollision sphereCollision, BoxCollision boxCollision) {
        float[] axes = boxCollision.axes;
        float[] sphereCenter = sphereCollision.GetCenter();
        float[] distance = new float[3];
        float[] boxCenter = boxCollision.GetCenter();
        for (int i = 0; i < 3; i++) {
            distance[i] = sphereCenter[i] - boxCenter[i];
        }

        float[] result = new float[]{0, 0, 0};

        for (int i = 0; i < 3; i++) {
            float length;
            float[] axis = new float[3];
            axis[0] = axes[i];
            axis[1] = axes[4 + i];
            axis[2] = axes[8 + i];

            length = VecOperator.getMag(axis);
            axis = VecOperator.normalize(axis);

            float dot = VecOperator.dot(distance, axis);
            if (Math.abs(dot) > length && dot != 0) {
                dot *= length / Math.abs(dot);
            }
            for (int j = 0; j < 3; j++) {
                result[j] += axis[j] * dot;
            }

        }

        for (int i = 0; i < 3; i++) {
            result[i] += boxCenter[i] - sphereCenter[i];
        }
        return VecOperator.getMag(result) <= sphereCollision.getRadius();
    }

    public static boolean intersect(SphereCollision sphereCollision1, SphereCollision sphereCollision2) {
        float[] distance = new float[3];
        float[] sphereCollisionCenter1 = sphereCollision1.GetCenter();
        float[] sphereCollisionCenter2 = sphereCollision2.GetCenter();
        for (int i = 0; i < 3; i++) {
            distance[i] = sphereCollisionCenter1[i] - sphereCollisionCenter2[i];
        }

        return VecOperator.getMag(distance) <= Math.abs(sphereCollision2.getRadius() - sphereCollision1.getRadius());
    }

    public static boolean intersect(SphereCollision sphereCollision, TriangleCollision triangleCollision) {
        float[] vectors = triangleCollision.vectors;
        float[] sphereCenter = sphereCollision.GetCenter();


        float[] vec1 = new float[]{vectors[0], vectors[1], vectors[2]};
        float[] vec2 = new float[]{vectors[4], vectors[5], vectors[6]};
        float[] vec3 = new float[]{vectors[8], vectors[9], vectors[10]};

        float[] vec21 = VecOperator.sub(vec2, vec1);
        float[] vec31 = VecOperator.sub(vec3, vec1);

        float[] normal = new float[3];
        VecOperator.cross(vec21, vec31, normal);
        normal = VecOperator.normalize(normal);
        //ax+by+cz = d , plane : {a,b,c,d}
        float[] plane = new float[]{normal[0], normal[1], normal[2], VecOperator.dot(normal, vec1)};

        //distance between sphere and plane
        float distance = (VecOperator.dot(normal, sphereCenter) - plane[3]) / (VecOperator.dot(normal, normal));

        //too far
        if (distance > sphereCollision.getRadius()) {
            return false;
        }

        float[] nearest = VecOperator.scale(normal, -distance);
        nearest = VecOperator.add(sphereCenter, nearest);

        int first = 0;
        int second = 1;
        float denominator = vec21[first] * vec31[second] - vec21[second] * vec31[first];

        while (denominator == 0.0f && first < 2) {
            first = (first + 1);
            second = (second + 1) % 3;
            denominator = vec21[first] * vec31[second] - vec21[second] * vec31[first];
        }
        float[] nearesttov1 = VecOperator.sub(nearest, vec1);

        float a = (nearesttov1[first] * vec31[second] - nearesttov1[second] * vec31[first]) / denominator;
        float b = (nearesttov1[first] * vec21[second] - nearesttov1[second] * vec21[first]) / -denominator;

        //nearest point is out of the plane
        if (a < 0.0f || a > 1.0f || b < 0.0f || b > 1.0f || (a+b)>1.0f) {
            return (Intersectline(vec1, vec2, normal, nearest, sphereCollision)
                    || Intersectline(vec2, vec3, normal, nearest, sphereCollision)
                    || Intersectline(vec3, vec1, normal, nearest, sphereCollision));
        }
        //nearest point is in the plane
        else {
            return VecOperator.getDistance(nearest, sphereCenter) < sphereCollision.getRadius();
        }
    }

    static boolean Intersectline(float[] vec1, float[] vec2, float[] normal, float[] nearest, SphereCollision sphereCollision) {
        float[] vec21 = VecOperator.sub(vec2, vec1);
        float[] toNearest = new float[3];
        VecOperator.cross(vec21, normal, toNearest);
        float a = 0;
        boolean zero = false;
        for (int i = 0; i < 3; i++) {
            if (vec21[i] == 0) {
                a = (nearest[i] - vec1[i]) / toNearest[i];
                zero = true;

                float[] nearest2Line = VecOperator.scale(toNearest, a);
                float[] nearestProjectionToLine = VecOperator.sub(nearest, nearest2Line);

                //if nearestProjectionToLine is in the line
                if (IsinLine(vec1, vec2, nearestProjectionToLine)) {
                    if (VecOperator.getDistance(nearestProjectionToLine, sphereCollision.GetCenter()) < sphereCollision.getRadius()) {
                        return true;
                    }
                }
                //if nearestProjectionToLine is out of line
                else {
                    if (VecOperator.getDistance(vec1, sphereCollision.GetCenter()) < sphereCollision.getRadius()
                            || VecOperator.getDistance(vec2, sphereCollision.GetCenter()) < sphereCollision.getRadius()) {
                        return true;
                    }
                }
            }
        }

        if (zero) {
            return false;
        } else {
            a = (vec21[1] * (nearest[0] - vec1[0]) - vec21[0] * (nearest[1] - vec1[1])) / (vec21[0] * toNearest[1] - vec21[1] * toNearest[0]);

            float[] nearest2Line = VecOperator.scale(toNearest, a);
            float[] nearestProjectionToLine = VecOperator.sub(nearest, nearest2Line);
            if (IsinLine(vec1, vec2, nearestProjectionToLine)) {
                if (VecOperator.getDistance(nearestProjectionToLine, sphereCollision.GetCenter()) < sphereCollision.getRadius()) {
                    return true;
                }
            } else {
                if (VecOperator.getDistance(vec1, sphereCollision.GetCenter()) < sphereCollision.getRadius()
                        || VecOperator.getDistance(vec2, sphereCollision.GetCenter()) < sphereCollision.getRadius()) {
                    return true;
                }
            }
            return false;
        }
    }

    static boolean IsinLine(float[] vec1, float[] vec2, float[] x) {
        for (int i = 0; i < 3; i++) {
            float max = Math.max(vec1[i], vec2[i]);
            float min = Math.min(vec1[i], vec2[i]);
            if (x[i] > max || x[i] < min) {
                return false;
            }
        }
        return true;
    }


}
