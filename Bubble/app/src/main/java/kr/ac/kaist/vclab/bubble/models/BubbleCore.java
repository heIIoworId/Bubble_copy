package kr.ac.kaist.vclab.bubble.models;

import android.opengl.GLES20;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.ArrayList;

import kr.ac.kaist.vclab.bubble.MyGLRenderer;
import kr.ac.kaist.vclab.bubble.environment.GameEnv;
import kr.ac.kaist.vclab.bubble.physics.Particle;
import kr.ac.kaist.vclab.bubble.utils.SystemHelper;

/**
 * Created by 84395 on 11/27/2016.
 */

public class BubbleCore extends Particle {

    // GL PROGRAM AND BUFFERS
    private final int mProgram;
    private FloatBuffer mVertexBuffer;

    // ATTRIBUTE HANDLES
    private int mPositionHandle;

    // UNIFORM HANDLES
    private int mProjMatrixHandle;
    private int mModelViewMatrixHandle;
    private int mNormalMatrixHandle;
    private int mLightHandle;
    private int mLight2Handle;
    private int mColorHandle;

    private static final int COORDS_PER_VERTEX = 3;
    private static final int VERTEX_STRIDE = COORDS_PER_VERTEX * 4;

    private float[] color = {0.2f, 0.709803922f, 0.898039216f};

    private float trajectory[];
    private ArrayList<Float> trajectoryArrayList;

    public BubbleCore(float[] _location) {
        super(_location);

        trajectory = new float[GameEnv.getInstance().lengthOfTrajectory];
        trajectoryArrayList = new ArrayList<>();

        initVertexBuffer();

        // PREPARE SHADER AND GL PROGRAM
        int vertexShader = MyGLRenderer.loadShaderFromFile(
                GLES20.GL_VERTEX_SHADER, "bubblecore-vshader.glsl");
        int fragmentShader = MyGLRenderer.loadShaderFromFile(
                GLES20.GL_FRAGMENT_SHADER, "bubblecore-fshader.glsl");

        // INITIATE PROGRAM, ATTACH SHADERS, AND LINK PROGRAM
        mProgram = GLES20.glCreateProgram();
        GLES20.glAttachShader(mProgram, vertexShader);
        GLES20.glAttachShader(mProgram, fragmentShader);
        GLES20.glLinkProgram(mProgram);
    }

    // FIXME SG (working on)
    public void drawTrajectory(float[] projMatrix,
                               float[] modelViewMatrix,
                               float[] normalMatrix,
                               float[] light,
                               float[] light2) {

        GLES20.glUseProgram(mProgram);

        // UNIFORM HANDLES
        mProjMatrixHandle = GLES20.glGetUniformLocation(mProgram, "uProjMatrix");
        mModelViewMatrixHandle = GLES20.glGetUniformLocation(mProgram, "uModelViewMatrix");
        mNormalMatrixHandle = GLES20.glGetUniformLocation(mProgram, "uNormalMatrix");
        mColorHandle = GLES20.glGetUniformLocation(mProgram, "uColor");
        mLightHandle = GLES20.glGetUniformLocation(mProgram, "uLight");
        mLight2Handle = GLES20.glGetUniformLocation(mProgram, "uLight2");

        // SPECIFY THE VALUE OF UNIFORM HANDLES
        GLES20.glUniformMatrix4fv(mProjMatrixHandle, 1, false, projMatrix, 0);
        GLES20.glUniformMatrix4fv(mModelViewMatrixHandle, 1, false, modelViewMatrix, 0);
        GLES20.glUniformMatrix4fv(mNormalMatrixHandle, 1, false, normalMatrix, 0);
        GLES20.glUniform3fv(mColorHandle, 1, color, 0);
        GLES20.glUniform3fv(mLightHandle, 1, light, 0);
        GLES20.glUniform3fv(mLight2Handle, 1, light2, 0);

        // ATTRIBUTES HANDLE
        mPositionHandle = GLES20.glGetAttribLocation(mProgram, "aPosition");

        GLES20.glEnableVertexAttribArray(mPositionHandle);

        GLES20.glVertexAttribPointer(
                mPositionHandle, COORDS_PER_VERTEX,
                GLES20.GL_FLOAT, false,
                VERTEX_STRIDE, mVertexBuffer);

        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, trajectory.length / 3);
//        GLES20.glDrawArrays(GLES20.GL_LINE_LOOP, 0, vertices.length / 3);

        GLES20.glDisableVertexAttribArray(mPositionHandle);
    }

    //FIXME SG (working on)
    public void updateTrajectory(){
        float currentLocation[] = this.getLocation();
        for(int i = 0; i < currentLocation.length; i++){
            trajectoryArrayList.add(currentLocation[i]);
            if(trajectoryArrayList.size() > GameEnv.getInstance().lengthOfTrajectory){
                trajectoryArrayList.remove(0);
            }
        }
        for(int i = 0; i < trajectoryArrayList.size(); i++){
            trajectory[i] = trajectoryArrayList.get(i);
        }

        // FIXME TESTING
//        System.out.println("test");
//        SystemHelper.printFloatArray(trajectory);
    }

    public void initVertexBuffer(){
        ByteBuffer byteBuf = ByteBuffer.allocateDirect(trajectory.length * 4);
        byteBuf.order(ByteOrder.nativeOrder());
        mVertexBuffer = byteBuf.asFloatBuffer();
        mVertexBuffer.put(trajectory);
        mVertexBuffer.position(0);
    }
}