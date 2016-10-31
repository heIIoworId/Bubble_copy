package kr.ac.kaist.vclab.bubble.models;

import java.nio.FloatBuffer;

/**
 * Created by mnswpr on 10/29/2016.
 */

public class BubbleSphere {

    private final int mProgram;
    private FloatBuffer mVertexBuffer;
    private FloatBuffer mNormalBuffer;

    // attribute handles
    private int mPositionHandle;
    private int mNormalHandle;

    // uniform handles
    private int mProjMatrixHandle;
    private int mModelViewMatrixHandle;
    private int mNormalMatrixHandle;

    private int mLightHandle;
    private int mLight2Handle;
    private int mColorHandle;

    private static final int COORDS_PER_VERTEX = 3;
    private static final int VERTEX_STRIDE = COORDS_PER_VERTEX * 4;

    public float[] color = { 0.2f, 0.709803922f, 0.898039216f };

    private float[] origin;
    private float radius;
    private int res;

    public BubbleSphere(float[] _origin, float _radius, int _res){
        this.origin = _origin;
        this.radius = _radius;
        this.res = _res;
    }

}
