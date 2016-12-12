package kr.ac.kaist.vclab.bubble.collision;


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
            // System.out.println("dis : " + distance[0] + " " + distance[1] + " " + distance[2]);
            // System.out.println("axis : " + axis[0] + " " + axis[1] + " " + axis[2]);
            length = VecOperator.getMag(axis);
            axis = VecOperator.normalize(axis);

            float dot = VecOperator.dot(distance, axis);
            if (Math.abs(dot) > length && dot != 0) {
                dot *= length / Math.abs(dot);
            }
            // System.out.println(dot);
            for (int j = 0; j < 3; j++) {
                result[j] += axis[j] * dot;
            }

        }

        // System.out.println("beforevec "+result[0] +" " +result[1]+" "+result[2]);
        for (int i = 0; i < 3; i++) {
            result[i] += boxCenter[i] - sphereCenter[i];
        }
        // System.out.println("aftervec "+result[0] +" " +result[1]+" "+result[2]);
        // System.out.println("box "+boxCenter[0] +" " +boxCenter[1]+" "+boxCenter[2]);
        // System.out.println("sphere "+sphereCenter[0] +" " +sphereCenter[1]+" "+sphereCenter[2]);
        // System.out.println("radius " + sphereCollision.GetRadius());
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
//        //System.out.println("v1 "+ vec1[0] + " " + vec1[1]+ " " +vec1[2]);
        float[] vec21 = new float[]{vectors[4] - vectors[0], vectors[5] - vectors[1], vectors[6] - vectors[2]};
//        //System.out.println("v21 "+ vec21[0] + " " + vec21[1]+ " " +vec21[2]);
        float[] vec31 = new float[]{vectors[8] - vectors[0], vectors[9] - vectors[1], vectors[10] - vectors[2]};
//        //System.out.println("v31 "+ vec31[0] + " " + vec31[1]+ " " +vec31[2]);
        float[] normal = new float[3];
        VecOperator.cross(vec21, vec31, normal);
//        //System.out.println("normal "+ normal[0] + " " + normal[1]+ " " +normal[2]);
        normal = VecOperator.normalize(normal);
//        //System.out.println("normal "+ normal[0] + " " + normal[1]+ " " +normal[2]);
        //ax+by+cz = d , plane : {a,b,c,d}
        float[] plane = new float[]{normal[0], normal[1], normal[2], VecOperator.dot(normal, vec1)};
        //System.out.println("plane" + +plane[0]+ " "+plane[1]+ " " +plane[2]+ " " +plane[3]);

        float distance = (VecOperator.dot(normal, sphereCenter) - plane[3]) / (VecOperator.dot(normal, normal));
        //fix : 절대값문제
        //System.out.println("distance" +  distance);
        if (distance > sphereCollision.getRadius()) {
            return false;
        }

        float[] nearest = new float[3];
        nearest = VecOperator.scale(normal, -distance);
//        //System.out.println("nearest"+ " " + nearest[0]+ " " + nearest[1] + " " + nearest[2]);
        //System.out.println("sphere"+ " " + sphereCenter[0]+ " " + sphereCenter[1] + " " + sphereCenter[2]);

        nearest = VecOperator.add(sphereCenter, nearest);
        //System.out.println("nearest in plane"+ " " + nearest[0]+ " " + nearest[1] + " " + nearest[2]);

        int first = 0;
        int second = 1;
        float denominator = vec21[first] * vec31[second] - vec21[second] * vec31[first];

        while (denominator == 0.0f && first < 2) {
            first = (first + 1);
            second = (second + 1) % 3;
            denominator = vec21[first] * vec31[second] - vec21[second] * vec31[first];
        }
        float[] nearesttov1 = new float[3];
        nearesttov1 = VecOperator.sub(nearest, vec1);

        float a = (nearest[first] * vec31[second] - nearesttov1[second] * vec31[first]) / denominator;
        float b = (nearest[first] * vec21[second] - nearesttov1[second] * vec21[first]) / -denominator;

        //System.out.print("v1 "+ vec1[0] + " " + vec1[1]+ " " +vec1[2]);
        //System.out.print("// v2 "+ vec2[0] + " " + vec2[1] + " " +vec2[2]);
        //System.out.println("// v3 "+ vec3[0] + " " + vec3[1] + " " +vec3[2]);
        //System.out.println("sphere"+ " " + sphereCenter[0]+ " " + sphereCenter[1] + " " + sphereCenter[2] + "r " + sphereCollision.getRadius());

        //System.out.println("first "+ first + " second" + second + " bde "+denominator);
        //System.out.println("a " + a + " b " + b);

        //nearest point is out of the plane
        if (a < 0 || a > 1 || b < 0 || b > 1) {
            //System.out.println("out of plane!!!");
            return (Intersectline(vec1, vec2, normal, nearest, sphereCollision)
                    || Intersectline(vec2, vec3, normal, nearest, sphereCollision)
                    || Intersectline(vec3, vec1, normal, nearest, sphereCollision));
        }
        //nearest point is in the plane
        else {
            //System.out.println("in the  plane!!!");

            //System.out.println("nearest"+ " " + nearest[0]+ " " + nearest[1] + " " + nearest[2]);//

            //System.out.println("distance to sphere "+ VecOperator.getDistance(nearest, sphereCenter));
            return VecOperator.getDistance(nearest, sphereCenter) < sphereCollision.getRadius();
        }
    }

    static boolean Intersectline(float[] vec1, float[] vec2, float[] normal, float[] nearest, SphereCollision sphereCollision) {
        float[] vec21 = new float[3];
        vec21 = VecOperator.sub(vec2, vec1);
        float[] tonearest = new float[3];
        VecOperator.cross(vec21, normal, tonearest);
        float a = 0;
        boolean zero = false;
        for (int i = 0; i < 3; i++) {
            if (vec21[i] == 0) {
                a = (nearest[i] - vec1[i]) / tonearest[i];
                zero = true;

                float[] toline = new float[3];
                toline = VecOperator.scale(tonearest, a);
                float[] inline = new float[3];
                inline = VecOperator.sub(nearest, toline);
                //System.out.println();
                if (IsinLine(vec1, vec2, inline)) {
                    if (VecOperator.getDistance(inline, sphereCollision.GetCenter()) < sphereCollision.getRadius()) {
                        //System.out.println("aaaa");
                        return true;
                    }
                } else {
                    if (VecOperator.getDistance(vec1, sphereCollision.GetCenter()) < sphereCollision.getRadius()
                            || VecOperator.getDistance(vec2, sphereCollision.GetCenter()) < sphereCollision.getRadius()) {
                        if (VecOperator.getDistance(vec1, sphereCollision.GetCenter()) < sphereCollision.getRadius()) {
                            //System.out.println(vec1[0] + " " + vec1[1] + " " + vec1[2] + "\n " + sphereCollision.GetCenter()[0] + " " + sphereCollision.GetCenter()[1] + " " + sphereCollision.GetCenter()[2] + " " + sphereCollision.getRadius() + " " + VecOperator.getDistance(vec1, sphereCollision.GetCenter()));
                        } else //System.out.println(vec1[0] +" " + vec1[1] +" " + vec1[2] +"\n "+sphereCollision.GetCenter()[0]+" "+sphereCollision.GetCenter()[1] + " " + sphereCollision.GetCenter()[2]+" "+sphereCollision.getRadius() + " " + VecOperator.getDistance(vec2, sphereCollision.GetCenter()));
                            //System.out.println("bbbb");

                            return true;
                    }
                }
            }
        }

        if (zero) {
            return false;
        } else {
            a = (vec21[1] * (nearest[0] - vec1[0]) - vec21[0] * (nearest[1] - vec1[1])) / (vec21[0] * tonearest[1] - vec21[1] * tonearest[0]);
            float[] toline = new float[3];
            toline = VecOperator.scale(tonearest, a);
            float[] inline = new float[3];
            inline = VecOperator.sub(nearest, toline);
            if (IsinLine(vec1, vec2, inline)) {
                if (VecOperator.getDistance(inline, sphereCollision.GetCenter()) < sphereCollision.getRadius()) {
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
