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
    private FloatBuffer mTraceVertexBuffer;
    private FloatBuffer mTraceNormalBuffer;

    // ATTRIBUTE HANDLES
    private int mPositionHandle;
    private int mNormalHandle;

    // UNIFORM HANDLES
    private int mProjMatrixHandle;
    private int mModelViewMatrixHandle;
    private int mNormalMatrixHandle;
    private int mLightHandle;
    private int mLight2Handle;
    private int mColorHandle;

    private static final int COORDS_PER_VERTEX = 3;
    private static final int VERTEX_STRIDE = COORDS_PER_VERTEX * 4;

    private float traceVertices[];
    private ArrayList<Float> traceVerticesArrayList;
    private static float traceNormals[];
    private float[] traceColor;

    public BubbleCore(float[] _location) {
        super(_location);

        traceColor = GameEnv.getInstance().traceColor;
        traceVertices = new float[GameEnv.getInstance().lengthOfTrace];
        traceVerticesArrayList = new ArrayList<>();
        traceNormals = new float[GameEnv.getInstance().lengthOfTrace];

        initTraceVertexBuffer();
        initTraceNormalBuffer();

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

    // FIXME SG (SIMPLIFY IT ACCORDING TO FRAGMENT SHADER LATER)
    public void drawTrace(float[] projMatrix,
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
        GLES20.glUniform3fv(mColorHandle, 1, traceColor, 0);
        GLES20.glUniform3fv(mLightHandle, 1, light, 0);
        GLES20.glUniform3fv(mLight2Handle, 1, light2, 0);

        // ATTRIBUTES HANDLE
        mPositionHandle = GLES20.glGetAttribLocation(mProgram, "aPosition");
        mNormalHandle = GLES20.glGetAttribLocation(mProgram, "aNormal");

        GLES20.glEnableVertexAttribArray(mPositionHandle);
        GLES20.glEnableVertexAttribArray(mNormalHandle);

        GLES20.glVertexAttribPointer(
                mPositionHandle, COORDS_PER_VERTEX,
                GLES20.GL_FLOAT, false,
                VERTEX_STRIDE, mTraceVertexBuffer);

        GLES20.glVertexAttribPointer(
                mNormalHandle, COORDS_PER_VERTEX,
                GLES20.GL_FLOAT, false,
                VERTEX_STRIDE, mTraceNormalBuffer);

//        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, traceVertices.length / 3);
        GLES20.glDrawArrays(GLES20.GL_LINE_LOOP, 0, traceVertices.length / 3);
        // FIXME SG (ANOTHER WAY TO DRAW WIDE LINE?)
        GLES20.glLineWidth(5.0f);

        GLES20.glDisableVertexAttribArray(mPositionHandle);
    }

    // FIXME SG (HAVE TO SHORTEN TRACE ACCORDING TO TIME PASS)
    public void updateTraceVertices(){
        float currentLocation[] = this.getLocation();
        for(int i = 0; i < currentLocation.length; i++){
            traceVerticesArrayList.add(currentLocation[i]);
            if(traceVerticesArrayList.size() >
                    GameEnv.getInstance().lengthOfTrace + GameEnv.getInstance().traceOffset){
                traceVerticesArrayList.remove(0);
            }
        }
        for(int i = 0; i < traceVerticesArrayList.size() - GameEnv.getInstance().traceOffset; i++){
            traceVertices[i] = traceVerticesArrayList.get(i);
        }
        for(int i = 0; i < traceVerticesArrayList.size() - GameEnv.getInstance().traceOffset; i = i+3){
            traceNormals[i] = 0f;
            traceNormals[i+1] = 1.0f;
            traceNormals[i+2] = 0f;
        }
        initTraceVertexBuffer();
    }

    private void initTraceVertexBuffer(){
        ByteBuffer byteBuf = ByteBuffer.allocateDirect(traceVertices.length * 4);
        byteBuf.order(ByteOrder.nativeOrder());
        mTraceVertexBuffer = byteBuf.asFloatBuffer();
        mTraceVertexBuffer.put(traceVertices);
        mTraceVertexBuffer.position(0);
    }

    private void initTraceNormalBuffer(){
        ByteBuffer byteBuf = ByteBuffer.allocateDirect(traceNormals.length * 4);
        byteBuf.order(ByteOrder.nativeOrder());
        mTraceNormalBuffer = byteBuf.asFloatBuffer();
        mTraceNormalBuffer.put(traceNormals);
        mTraceNormalBuffer.position(0);
    }
}