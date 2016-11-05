package kr.ac.kaist.vclab.bubble.Collision;


import kr.ac.kaist.vclab.bubble.utils.VecOperator;

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
                axis[0] = axes[i];
                axis[1] = axes[4+i];
                axis[2] = axes[8+i];
                System.out.println("dis : " + distance[0] + " " + distance[1] + " " + distance[2]);
                System.out.println("axis : " + axis[0] + " " + axis[1] + " " + axis[2]);
                length = VecOperator.getMag(axis);
                axis = VecOperator.normalize(axis);

                float dot = VecOperator.dot(distance, axis);
                if (Math.abs(dot) > length && dot != 0){
                    dot *= length/Math.abs(dot);
                }
                System.out.println(dot);
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

        System.out.println("beforevec "+result[0] +" " +result[1]+" "+result[2]);
        for(int i=0; i<3; i++) {
            result[i] += boxCenter[i] - sphereCenter[i];
        }
        System.out.println("aftervec "+result[0] +" " +result[1]+" "+result[2]);
        System.out.println("box "+boxCenter[0] +" " +boxCenter[1]+" "+boxCenter[2]);
        System.out.println("sphere "+sphereCenter[0] +" " +sphereCenter[1]+" "+sphereCenter[2]);
        System.out.println("radius " + sphereCollision.GetRadius());
        return VecOperator.getMag(result) <= sphereCollision.GetRadius();
    }

    public static boolean intersect(SphereCollision sphereCollision1, SphereCollision sphereCollision2){
        float[] distance = new float[3];
        float[] sphereCollisionCenter1 = sphereCollision1.GetCenter();
        float[] sphereCollisionCenter2 = sphereCollision2.GetCenter();
        for(int i=0; i<3; i++){
            distance[i] = sphereCollisionCenter1[i] - sphereCollisionCenter2[i];
        }

        return VecOperator.getMag(distance) <= Math.abs(sphereCollision2.GetRadius() - sphereCollision1.GetRadius());
    }
/*
    public static boolean intersect(SphereCollision sphereCollision, List<Collision> collisionList){
        for (Collision c: collisionList) {
            if()
            if(intersect(sphereCollision, c))

        }
    }*/


}
