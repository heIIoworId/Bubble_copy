package kr.ac.kaist.vclab.bubble.models;

import android.opengl.GLES20;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.ArrayList;

import kr.ac.kaist.vclab.bubble.collision.SphereCollision;
import kr.ac.kaist.vclab.bubble.MyGLRenderer;
import kr.ac.kaist.vclab.bubble.environment.GameEnv;
import kr.ac.kaist.vclab.bubble.utils.VecOperator;

/**
 * Created by mnswpr on 11/16/2016.
 */

public class BubbleSphere {

    // GL PROGRAM AND BUFFERS
    private final int mProgram;
    private FloatBuffer mVertexBuffer;
    private FloatBuffer mNormalBuffer;

    // ATTRIBUTE HANDLES
    private int mPositionHandle;
    private int mNormalHandle;

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

    private float[] center;

    private float[] vertexA;
    private float[] vertexB;
    private float[] vertexC;
    private float[] vertexD;
    private float[] vertexE;
    private float[] vertexF;

    private static float vertices[];
    private static ArrayList<Float> verticesList;
    private static float normals[];
    private static ArrayList<Float> normalsList;
    public float[] color;

    public BubbleSphere(float _radius, int _level) {

        center = new float[3];

        verticesList = new ArrayList<>();
        normalsList = new ArrayList<>();

        vertexA = new float[]{0f, 0f, 1f};
        vertexB = new float[]{1f, 0f, 0f};
        vertexC = new float[]{0f, 0f, -1f};
        vertexD = new float[]{-1f, 0f, 0f};
        vertexE = new float[]{0f, 1f, 0f};
        vertexF = new float[]{0f, -1f, 0f};

        subdivide(vertexA, vertexB, vertexE, _level, _radius);
        subdivide(vertexB, vertexC, vertexE, _level, _radius);
        subdivide(vertexC, vertexD, vertexE, _level, _radius);
        subdivide(vertexD, vertexA, vertexE, _level, _radius);
        subdivide(vertexF, vertexB, vertexA, _level, _radius);
        subdivide(vertexF, vertexC, vertexB, _level, _radius);
        subdivide(vertexF, vertexD, vertexC, _level, _radius);
        subdivide(vertexF, vertexA, vertexD, _level, _radius);

        vertices = new float[verticesList.size()];
        for (int i = 0; i < verticesList.size(); i++) {
            vertices[i] = verticesList.get(i);
        }
        initVertexBuffer();

        normals = new float[normalsList.size()];
        for (int i = 0; i < normalsList.size(); i++) {
            normals[i] = normalsList.get(i);
        }
        initNormalBuffer();

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
        mEnvHandle = GLES20.glGetUniformLocation(mProgram, "cubemap");
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
        mNormalHandle = GLES20.glGetAttribLocation(mProgram, "aNormal");

        GLES20.glEnableVertexAttribArray(mPositionHandle);
        GLES20.glEnableVertexAttribArray(mNormalHandle);

        GLES20.glVertexAttribPointer(
                mPositionHandle, COORDS_PER_VERTEX,
                GLES20.GL_FLOAT, false,
                VERTEX_STRIDE, mVertexBuffer);

        GLES20.glVertexAttribPointer(
                mNormalHandle, COORDS_PER_VERTEX,
                GLES20.GL_FLOAT, false,
                VERTEX_STRIDE, mNormalBuffer);

        // DRAW
        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, vertices.length / 3);
//        GLES20.glDrawArrays(GLES20.GL_LINES, 0, vertices.length / 3);
        GLES20.glLineWidth(3.0f);

        GLES20.glDisableVertexAttribArray(mPositionHandle);
        GLES20.glDisableVertexAttribArray(mNormalHandle);
    }

    private void subdivide(float[] _vertex1, float[] _vertex2, float[] _vertex3, int level, float radius) {
        if (level == 0) {
            normalsList.add(_vertex1[0]);
            normalsList.add(_vertex1[1]);
            normalsList.add(_vertex1[2]);
            normalsList.add(_vertex2[0]);
            normalsList.add(_vertex2[1]);
            normalsList.add(_vertex2[2]);
            normalsList.add(_vertex3[0]);
            normalsList.add(_vertex3[1]);
            normalsList.add(_vertex3[2]);

            _vertex1 = VecOperator.scale(_vertex1, radius);
            _vertex2 = VecOperator.scale(_vertex2, radius);
            _vertex3 = VecOperator.scale(_vertex3, radius);

            verticesList.add(_vertex1[0]);
            verticesList.add(_vertex1[1]);
            verticesList.add(_vertex1[2]);
            verticesList.add(_vertex2[0]);
            verticesList.add(_vertex2[1]);
            verticesList.add(_vertex2[2]);
            verticesList.add(_vertex3[0]);
            verticesList.add(_vertex3[1]);
            verticesList.add(_vertex3[2]);

        } else {
            float vertex1[] = new float[3];
            float vertex2[] = new float[3];
            float vertex3[] = new float[3];
            float vertex12[] = new float[3];
            float vertex23[] = new float[3];
            float vertex31[] = new float[3];

            vertex1 = _vertex1;
            vertex2 = _vertex2;
            vertex3 = _vertex3;

            vertex12 = VecOperator.add(vertex1, vertex2);
            vertex12 = VecOperator.scale(vertex12, 0.5f);
            vertex12 = VecOperator.normalize(vertex12);

            vertex23 = VecOperator.add(vertex2, vertex3);
            vertex23 = VecOperator.scale(vertex23, 0.5f);
            vertex23 = VecOperator.normalize(vertex23);

            vertex31 = VecOperator.add(vertex3, vertex1);
            vertex31 = VecOperator.scale(vertex31, 0.5f);
            vertex31 = VecOperator.normalize(vertex31);

            subdivide(vertex1, vertex12, vertex31, level - 1, radius);
            subdivide(vertex12, vertex2, vertex23, level - 1, radius);
            subdivide(vertex23, vertex3, vertex31, level - 1, radius);
            subdivide(vertex31, vertex12, vertex23, level - 1, radius);
        }
    }

    public void initVertexBuffer() {
        ByteBuffer byteBuf = ByteBuffer.allocateDirect(vertices.length * 4);
        byteBuf.order(ByteOrder.nativeOrder());
        mVertexBuffer = byteBuf.asFloatBuffer();
        mVertexBuffer.put(vertices);
        mVertexBuffer.position(0);
    }

    public void initNormalBuffer() {
        ByteBuffer byteBuf = ByteBuffer.allocateDirect(normals.length * 4);
        byteBuf.order(ByteOrder.nativeOrder());
        mNormalBuffer = byteBuf.asFloatBuffer();
        mNormalBuffer.put(normals);
        mNormalBuffer.position(0);
    }

    public float[] getVertices() {
        return vertices;
    }

    public void setVertices(float[] _vertices) {
        vertices = _vertices;
        initVertexBuffer();
    }

    public void setCenter(float[] _center) {
        center = _center;
    }

    public float[] getCenter() {
        return center;
    }

    //FIXME SG (UPDATE NORMALS ACCORDING TO VERTICES)
    public void updateNormals() {
    }
}
