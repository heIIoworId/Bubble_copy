package kr.ac.kaist.vclab.bubble.models;

import android.opengl.GLES20;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.ArrayList;

import kr.ac.kaist.vclab.bubble.MyGLRenderer;
import kr.ac.kaist.vclab.bubble.environment.GameEnv;
import kr.ac.kaist.vclab.bubble.physics.Particle;

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
    private int mModelMatrixHandle;
    private int mViewMatrixHandle;
    private int mLightHandle;
    private int mLight2Handle;
    private int mColorHandle;
    private int mEnvHandle; // CUBE MAP
    private int mCameraHandle;

    private static final int COORDS_PER_VERTEX = 3;
    private static final int VERTEX_STRIDE = COORDS_PER_VERTEX * 4;

    public float[] color;

    private float trajectory[];
    private ArrayList<Float> trajectoryArrayList;

    public BubbleCore(float[] _location) {
        super(_location);

        trajectory = new float[GameEnv.getInstance().lengthOfTrajectory];
        trajectoryArrayList = new ArrayList<>();

        initVertexBuffer();

        // PREPARE SHADER AND GL PROGRAM
        int vertexShader = MyGLRenderer.loadShaderFromFile(
                GLES20.GL_VERTEX_SHADER, "bubble-vshader.glsl");
        int fragmentShader = MyGLRenderer.loadShaderFromFile(
                GLES20.GL_FRAGMENT_SHADER, "bubble-fshader.glsl");

        mProgram = GLES20.glCreateProgram();             // create empty OpenGL Program
        GLES20.glAttachShader(mProgram, vertexShader);   // add the vertex shader to program
        GLES20.glAttachShader(mProgram, fragmentShader); // add the fragment shader to program
        GLES20.glLinkProgram(mProgram);                  // create OpenGL program executables
    }

    public void draw(float[] projMatrix,
                     float[] modelViewMatrix,
                     float[] modelMatrix,
                     float[] viewMatrix,
                     float[] normalMatrix,
                     float[] light,
                     float[] light2,
                     float[] camera,
                     int[] cubeTex) {

        GLES20.glUseProgram(mProgram);
        GLES20.glActiveTexture(GLES20.GL_TEXTURE3);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_CUBE_MAP, cubeTex[0]);

        // uniforms
        mProjMatrixHandle = GLES20.glGetUniformLocation(mProgram, "uProjMatrix");
        mModelViewMatrixHandle = GLES20.glGetUniformLocation(mProgram, "uModelViewMatrix");
        mModelMatrixHandle = GLES20.glGetUniformLocation(mProgram, "uModelMatrix");
        mViewMatrixHandle = GLES20.glGetUniformLocation(mProgram, "uViewMatrix");
        mNormalMatrixHandle = GLES20.glGetUniformLocation(mProgram, "uNormalMatrix");
        mColorHandle = GLES20.glGetUniformLocation(mProgram, "uColor");
        mLightHandle = GLES20.glGetUniformLocation(mProgram, "uLight");
        mLight2Handle = GLES20.glGetUniformLocation(mProgram, "uLight2");
        mCameraHandle = GLES20.glGetUniformLocation(mProgram, "campos");

        GLES20.glUniformMatrix4fv(mProjMatrixHandle, 1, false, projMatrix, 0);
        GLES20.glUniformMatrix4fv(mModelViewMatrixHandle, 1, false, modelViewMatrix, 0);
        GLES20.glUniformMatrix4fv(mModelMatrixHandle, 1, false, modelMatrix, 0);
        GLES20.glUniformMatrix4fv(mViewMatrixHandle, 1, false, viewMatrix, 0);
        GLES20.glUniformMatrix4fv(mNormalMatrixHandle, 1, false, normalMatrix, 0);

        GLES20.glUniform3fv(mColorHandle, 1, color, 0);
        GLES20.glUniform3fv(mLightHandle, 1, light, 0);
        GLES20.glUniform3fv(mLight2Handle, 1, light2, 0);
        GLES20.glUniform3fv(mCameraHandle, 1, camera, 0);
        GLES20.glUniform1i(mEnvHandle, 1);

        // attributes
        mPositionHandle = GLES20.glGetAttribLocation(mProgram, "aPosition");

        GLES20.glEnableVertexAttribArray(mPositionHandle);

        GLES20.glVertexAttribPointer(
                mPositionHandle, COORDS_PER_VERTEX,
                GLES20.GL_FLOAT, false,
                VERTEX_STRIDE, mVertexBuffer);

        // DRAW
        GLES20.glDrawArrays(GLES20.GL_LINE_STRIP, 0, trajectory.length / 3);
        GLES20.glLineWidth(5.0f);
        GLES20.glDisableVertexAttribArray(mPositionHandle);
    }

    public void initVertexBuffer(){
        ByteBuffer byteBuf = ByteBuffer.allocateDirect(trajectory.length * 4);
        byteBuf.order(ByteOrder.nativeOrder());
        mVertexBuffer = byteBuf.asFloatBuffer();
        mVertexBuffer.put(trajectory);
        mVertexBuffer.position(0);
    }
}
