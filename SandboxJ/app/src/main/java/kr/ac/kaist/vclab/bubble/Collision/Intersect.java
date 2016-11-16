package kr.ac.kaist.vclab.bubble.Collision;

import java.util.List;

import kr.ac.kaist.vclab.bubble.MatOperator;
import kr.ac.kaist.vclab.bubble.Sphere;
import kr.ac.kaist.vclab.bubble.VecOperator;

/**
 * Created by Jongmin on 2016-10-28.
 */

public class Intersect {
    public static boolean intersect(SphereCollision sphereCollision, BoxCollision boxCollision){

        //float[] axis1 = boxCollision.axis1;
        //float[] axis2 = boxCollision.axis2;
        //float[] axis3 = boxCollision.axis3;
        float[] axes = boxCollision.axes;
        float[] sphereCenter = sphereCollision.GetCenter();
        float[] distance = new float[3];
        float[] boxCenter = boxCollision.GetCenter();
        for(int i=0; i<3; i++){
            distance[i] = sphereCenter[i] - boxCenter[i];
        }

        float[] result = new float[]{0,0,0};

        for(int i=0; i<3; i++){
            float length;
            float[] axis = new float[3];
            axis[0] = axes[i*4];
            axis[1] = axes[i*4+1];
            axis[2] = axes[i*4+2];
//            System.out.println("dis : " + distance[0] + " " + distance[1] + " " + distance[2]);
//            System.out.println("axis : " + axis[0] + " " + axis[1] + " " + axis[2]);
            length = VecOperator.getMag(axis);
            axis = VecOperator.normalize(axis);

            float dot = VecOperator.dot(distance, axis);
//            System.out.println(dot);
            if (Math.abs(dot) > length && dot != 0){
                dot *= length/Math.abs(dot);
            }
//            System.out.println(dot);
            for(int j=0; j<3; j++){
                result[j]+=axis[j] * dot;
            }

        }
        /*
        float length;
        float[] result = new float[]{0,0,0};
        length = MatOperator.size(axis1);
        axis1 = MatOperator.normalize(axis1);
        float dot = MatOperator.dot(distance, axis1);
        if (Math.abs(dot) > length && dot != 0){
            dot = length/Math.abs(dot);
        }
        for(int i=0; i<3; i++){
            result[i]+=axis1[i] * dot;
        }

        length = MatOperator.size(axis2);
        axis2 = MatOperator.normalize(axis2);
        dot = MatOperator.dot(distance, axis2);
        if (Math.abs(dot) > length && dot != 0){
            dot = length/Math.abs(dot);
        }
        for(int i=0; i<3; i++){
            result[i]+=axis2[i] * dot;
        }

        length = MatOperator.size(axis3);
        axis3 = MatOperator.normalize(axis3);
        dot = MatOperator.dot(distance, axis3);
        if (Math.abs(dot) > length && dot != 0){
            dot = length/Math.abs(dot);
        }
        for(int i=0; i<3; i++) {
            result[i] += axis3[i] * dot;
        }
        */

//        System.out.println("beforevec "+result[0] +" " +result[1]+" "+result[2]);
        for(int i=0; i<3; i++) {
            result[i] += boxCenter[i] - sphereCenter[i];
        }
        /*
        System.out.println("aftervec "+result[0] +" " +result[1]+" "+result[2]);
        System.out.println("box "+boxCenter[0] +" " +boxCenter[1]+" "+boxCenter[2]);
        System.out.println("sphere "+sphereCenter[0] +" " +sphereCenter[1]+" "+sphereCenter[2]);
        System.out.println("radius " + sphereCollision.GetRadius());*/
        return VecOperator.getMag(result) <= sphereCollision.GetRadius();
    }


    public static boolean intersect(SphereCollision sphereCollision, TriangleCollision triangleCollision) {
        float[] vectors = triangleCollision.vectors;
        float[] sphereCenter = sphereCollision.GetCenter();


        float[] vec1 = new float[]{vectors[0], vectors[1], vectors[2]};
        System.out.println("v1 "+ vec1[0] + " " + vec1[1]+ " " +vec1[2]);
        float[] vec21 = new float[]{vectors[4] - vectors[0], vectors[5] - vectors[1], vectors[6] - vectors[2]};
        float[] vec31 = new float[]{vectors[8] - vectors[0], vectors[9] - vectors[1], vectors[10] - vectors[2]};
        float[] normal = new float[3];
        VecOperator.cross(vec21, vec31, normal);
        normal = VecOperator.normalize(normal);
        //ax+by+cz = d , plane : {a,b,c,d}-
        float[] plane = new float[]{normal[0], normal[1], normal[2], VecOperator.dot(normal, vec1)};

        float distance = (VecOperator.dot(normal, sphereCenter) - plane[3]) / (VecOperator.dot(normal, normal));
        if (distance > sphereCollision.GetRadius()) {
            return false;
        }

        float[] nearest = new float[3];
        nearest = VecOperator.scale(normal, -distance);
        nearest = VecOperator.sub(sphereCenter, nearest);

        float a = (nearest[0] * vec31[1] - nearest[1] * vec31[0]) / (vec21[0] * vec31[1] - vec21[1] * vec31[0]);
        float b = (nearest[0] * vec21[1] - nearest[1] * vec21[0]) / (vec31[0] * vec21[1] - vec31[1] * vec21[0]);
        System.out.println("nearest"+ " " + nearest[0]+ " " + nearest[1] + " " + nearest[2]);
        System.out.println("a " + a + " b " + b);

        return a >= 0 && a <= 1 && b >=0 && b <= 1;
    }
}
