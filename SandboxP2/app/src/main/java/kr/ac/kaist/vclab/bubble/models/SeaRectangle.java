package kr.ac.kaist.vclab.bubble.models;

import android.opengl.GLES20;
import android.opengl.GLUtils;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import kr.ac.kaist.vclab.bubble.MyGLRenderer;

/**
 * Created by avantgarde on 2016-11-07.
 */

public class SeaRectangle {
    private final int mProgram;
    private FloatBuffer mVertexBuffer;
    private FloatBuffer mNormalBuffer;
    private FloatBuffer mTextureCoorBuffer;

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

    private int mTextureHandle;
    private int mTextureCoorHandle;
    private int mTimeHandle;

    private static final int COORDS_PER_VERTEX = 3;
    private static final int VERTEX_STRIDE = COORDS_PER_VERTEX * 4;

    private static float[] vertices;
    private static float[] normals;
    private static float[] textureCoors;

    private float[] move = new float[]{10.0f ,2.0f};
    float[] color = {0.0f, 0.0f, 1.0f};


    public SeaRectangle(float sizeX, float sizeZ) {
        vertices = new float[]{
                0.0f, 0.0f, 0.0f,
                0.0f, 0.0f, sizeZ,
                sizeX, 0.0f, sizeZ,
                0.0f, 0.0f, 0.0f,
                sizeX, 0.0f, sizeZ,
                sizeX, 0.0f, 0.0f
        };

        normals = new float[]{
                0.0f, 1.0f, 0.0f,
                0.0f, 1.0f, 0.0f,
                0.0f, 1.0f, 0.0f,
                0.0f, 1.0f, 0.0f,
                0.0f, 1.0f, 0.0f,
                0.0f, 1.0f, 0.0f
        };

        textureCoors = new float[] {
                0.0f, 0.0f, 0.0f,
                0.0f, 1.0f, 0.0f,
                1.0f, 1.0f, 0.0f,
                0.0f, 0.0f, 0.0f,
                1.0f, 1.0f, 0.0f,
                1.0f, 0.0f, 0.0f
        };

        ByteBuffer byteBuf1 = ByteBuffer.allocateDirect(vertices.length * 4);
        byteBuf1.order(ByteOrder.nativeOrder());
        mVertexBuffer = byteBuf1.asFloatBuffer();
        mVertexBuffer.put(vertices);
        mVertexBuffer.position(0);

        ByteBuffer byteBuf2 = ByteBuffer.allocateDirect(normals.length * 4);
        byteBuf2.order(ByteOrder.nativeOrder());
        mNormalBuffer = byteBuf2.asFloatBuffer();
        mNormalBuffer.put(normals);
        mNormalBuffer.position(0);

        ByteBuffer byteBuf3 = ByteBuffer.allocateDirect(textureCoors.length * 4);
        byteBuf3.order(ByteOrder.nativeOrder());
        mTextureCoorBuffer = byteBuf3.asFloatBuffer();
        mTextureCoorBuffer.put(textureCoors);
        mTextureCoorBuffer.position(0);

        // prepare shaders and OpenGL program
        int vertexShader = MyGLRenderer.loadShaderFromFile(
                GLES20.GL_VERTEX_SHADER, "sea-vshader.glsl");
        int fragmentShader = MyGLRenderer.loadShaderFromFile(
                GLES20.GL_FRAGMENT_SHADER, "sea-fshader.glsl");

        mProgram = GLES20.glCreateProgram();             // create empty OpenGL Program
        GLES20.glAttachShader(mProgram, vertexShader);   // add the vertex shader to program
        GLES20.glAttachShader(mProgram, fragmentShader); // add the fragment shader to program
        GLES20.glLinkProgram(mProgram);                  // create OpenGL program executables

        // texture
        int[] textureHandles = new int[1];
        GLES20.glGenTextures(1, textureHandles, 0);

        GLES20.glActiveTexture(GLES20.GL_TEXTURE2);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureHandles[0]);
        GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, MyGLRenderer.loadImage("bluewater.jpg"), 0);
        GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_NEAREST);
        GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_NEAREST);
    }

    public void draw(float[] projMatrix,
                     float[] modelViewMatrix,
                     float[] normalMatrix,
                     float[] light,
                     float[] light2,
                     float curTime) {
        GLES20.glUseProgram(mProgram);
        float[] curMove = new float[]{move[0] * curTime, move[1] * curTime};
        // uniforms
        mProjMatrixHandle = GLES20.glGetUniformLocation(mProgram, "uProjMatrix");
        mModelViewMatrixHandle = GLES20.glGetUniformLocation(mProgram, "uModelViewMatrix");
        mNormalMatrixHandle = GLES20.glGetUniformLocation(mProgram, "uNormalMatrix");
        mColorHandle = GLES20.glGetUniformLocation(mProgram, "uColor");
        mLightHandle = GLES20.glGetUniformLocation(mProgram, "uLight");
        mLight2Handle = GLES20.glGetUniformLocation(mProgram, "uLight2");
        mTextureHandle = GLES20.glGetUniformLocation(mProgram, "uTextureUnit");
        mTimeHandle = GLES20.glGetUniformLocation(mProgram, "uTime");

        GLES20.glUniformMatrix4fv(mProjMatrixHandle, 1, false, projMatrix, 0);
        GLES20.glUniformMatrix4fv(mModelViewMatrixHandle, 1, false, modelViewMatrix, 0);
        GLES20.glUniformMatrix4fv(mNormalMatrixHandle, 1, false, normalMatrix, 0);

        GLES20.glUniform3fv(mColorHandle, 1, color, 0);
        GLES20.glUniform3fv(mLightHandle, 1, light, 0);
        GLES20.glUniform3fv(mLight2Handle, 1, light2, 0);
        GLES20.glUniform2fv(mTimeHandle, 1, curMove, 0);

        // attributes
        mPositionHandle = GLES20.glGetAttribLocation(mProgram, "aPosition");
        mNormalHandle = GLES20.glGetAttribLocation(mProgram, "aNormal");
        mTextureCoorHandle = GLES20.glGetAttribLocation(mProgram, "aTextureCoor");

        GLES20.glEnableVertexAttribArray(mPositionHandle);
        GLES20.glEnableVertexAttribArray(mNormalHandle);
        GLES20.glEnableVertexAttribArray(mTextureCoorHandle);

        GLES20.glVertexAttribPointer(
                mPositionHandle, COORDS_PER_VERTEX,
                GLES20.GL_FLOAT, false,
                VERTEX_STRIDE, mVertexBuffer);

        GLES20.glVertexAttribPointer(
                mNormalHandle, COORDS_PER_VERTEX,
                GLES20.GL_FLOAT, false,
                VERTEX_STRIDE, mNormalBuffer);

        GLES20.glVertexAttribPointer(
                mTextureCoorHandle, COORDS_PER_VERTEX,
                GLES20.GL_FLOAT, false,
                VERTEX_STRIDE, mTextureCoorBuffer
        );

        // set texture
        GLES20.glUniform1i(mTextureHandle, 2);

        // Draw the rectangle
        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, vertices.length / 3);

        GLES20.glDisableVertexAttribArray(mPositionHandle);
        GLES20.glDisableVertexAttribArray(mNormalHandle);
        GLES20.glDisableVertexAttribArray(mTextureCoorHandle);
    }
}