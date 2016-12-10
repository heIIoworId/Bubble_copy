package kr.ac.kaist.vclab.bubble.models;

import android.graphics.Bitmap;
import android.opengl.GLES20;
import android.opengl.GLUtils;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import kr.ac.kaist.vclab.bubble.MyGLRenderer;
import kr.ac.kaist.vclab.bubble.collision.TriangleCollision;
import kr.ac.kaist.vclab.bubble.generators.MapGenerator;

/**
 * Created by avantgarde on 2016-11-02.
 */

public class MapCube {
    private final int mProgram;
    private FloatBuffer mVertexBuffer;
    private FloatBuffer mNormalBuffer;
    private FloatBuffer mTextureCoorBuffer;

    // bitmaps
    private Bitmap textureBitmap; // texture
    // private Bitmap textureNormalBitmap; // normal map of the texture

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
    private int mModelMatrixHandle;

    private int mTextureNormalHandle;

    private static final int COORDS_PER_VERTEX = 3;
    private static final int VERTEX_STRIDE = COORDS_PER_VERTEX * 4;

    public static MapGenerator mGenerator; // map generator instance

    public static float[] vertices;
    public static float[] normals;
    public static float[] textureCoors;
    private static int mode;
    public static TriangleCollision[] collisions;

    float color[] = {0.33f, 0.42f, 0.18f};

    public MapCube(float sizeX, float sizeY, float sizeZ, // map size
                   float unit, // dist. between points (=> resolution)
                   float maxHeight, // max height
                   float minHeight, // min height
                   float complexity, // complexity
                   float normalRate, // normal adjustment
                   boolean fill) { // false if you want to see the skeleton only
        mGenerator = new MapGenerator(
                sizeX, sizeY, sizeZ,
                unit,
                maxHeight,
                minHeight,
                complexity,
                normalRate,
                fill
        );

        vertices = mGenerator.getVertices();
        normals = mGenerator.getNormals();
        textureCoors = mGenerator.getTextureCoors();
        mode = mGenerator.getMode();
        collisions = mGenerator.getCollision();

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
                GLES20.GL_VERTEX_SHADER, "map-vshader5.glsl");
        int fragmentShader = MyGLRenderer.loadShaderFromFile(
                GLES20.GL_FRAGMENT_SHADER, "map-fshader5.glsl");

        mProgram = GLES20.glCreateProgram();             // create empty OpenGL Program
        GLES20.glAttachShader(mProgram, vertexShader);   // add the vertex shader to program
        GLES20.glAttachShader(mProgram, fragmentShader); // add the fragment shader to program
        GLES20.glLinkProgram(mProgram);                  // create OpenGL program executables

        // texture
        textureBitmap = MyGLRenderer.loadImage("rocky.jpg");
        // textureNormalBitmap = MyGLRenderer.loadImage("forest_normal.png");

        int[] textureHandles = new int[1];
        GLES20.glGenTextures(1, textureHandles, 0);

        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureHandles[0]);
        GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, textureBitmap, 0);
        GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_NEAREST);
        GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_NEAREST);
    }

    public void draw(float[] projMatrix,
                     float[] modelViewMatrix,
                     float[] normalMatrix,
                     float[] modelMatrix,
                     float[] light,
                     float[] light2) {
        GLES20.glUseProgram(mProgram);

        // uniforms
        mProjMatrixHandle = GLES20.glGetUniformLocation(mProgram, "uProjMatrix");
        mModelViewMatrixHandle = GLES20.glGetUniformLocation(mProgram, "uModelViewMatrix");
        mNormalMatrixHandle = GLES20.glGetUniformLocation(mProgram, "uNormalMatrix");
        mColorHandle = GLES20.glGetUniformLocation(mProgram, "uColor");
        mLightHandle = GLES20.glGetUniformLocation(mProgram, "uLight");
        mLight2Handle = GLES20.glGetUniformLocation(mProgram, "uLight2");
        mTextureHandle = GLES20.glGetUniformLocation(mProgram, "uTextureUnit");
        // mTextureNormalHandle = GLES20.glGetUniformLocation(mProgram, "uTextureNormalUnit");
        mModelMatrixHandle = GLES20.glGetUniformLocation(mProgram, "uModelMatrix");

        GLES20.glUniformMatrix4fv(mProjMatrixHandle, 1, false, projMatrix, 0);
        GLES20.glUniformMatrix4fv(mModelViewMatrixHandle, 1, false, modelViewMatrix, 0);
        GLES20.glUniformMatrix4fv(mNormalMatrixHandle, 1, false, normalMatrix, 0);
        GLES20.glUniformMatrix4fv(mModelMatrixHandle, 1, false, modelMatrix, 0);

        GLES20.glUniform3fv(mColorHandle, 1, color, 0);
        GLES20.glUniform3fv(mLightHandle, 1, light, 0);
        GLES20.glUniform3fv(mLight2Handle, 1, light2, 0);

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
        GLES20.glUniform1i(mTextureHandle, 0);

        // Draw the cube
        GLES20.glDrawArrays(mode, 0, vertices.length / 3);

        GLES20.glDisableVertexAttribArray(mPositionHandle);
        GLES20.glDisableVertexAttribArray(mNormalHandle);
        GLES20.glDisableVertexAttribArray(mTextureCoorHandle);
    }
    public TriangleCollision[] getCollisions(){
        return collisions;
    }

}