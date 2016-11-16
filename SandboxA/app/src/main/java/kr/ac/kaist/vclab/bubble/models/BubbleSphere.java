package kr.ac.kaist.vclab.bubble.models;

import android.opengl.GLES20;
import android.util.FloatMath;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;
import java.util.ArrayList;

import kr.ac.kaist.vclab.bubble.MyGLRenderer;

/**
 * Created by mnswpr on 10/29/2016.
 */

public class BubbleSphere {

    private final int mProgram;

    //BUFFER
    private FloatBuffer mVertexBuffer;
    private FloatBuffer mNormalBuffer;
    private FloatBuffer mTextureCoordBuffer;
    private ShortBuffer mIndexBuffer;

    // ATTRIBUTE HANDLES
    private int mPositionHandle;
    private int mNormalHandle;

    // UNIFORM HANDLES
    private int mProjMatrixHandle;
    private int mModelViewMatrixHandle;
    // MATRIX FOR AFFINE TRANSFORMATION OF NORMAL VECTOR FROM MODEL FRAME --> EYE FRAME
    private int mNormalMatrixHandle;
    private int mLightHandle;
    private int mLight2Handle;
    private int mColorHandle;

    private static final int COORDS_PER_VERTEX = 3;
    private static final int VERTEX_STRIDE = COORDS_PER_VERTEX * 4; // MEANING 4 BYTES PER VERTEX

    private static float vertices[];
    private static float normals[];
    private static short indexes[];
    private static float textureCoords[];
    public float[] color = { 0.2f, 0.709803922f, 0.898039216f };

    public BubbleSphere(float radius, int stacks, int slices){

        int vertexCount = (stacks + 1) * (slices + 1);
        vertices = new float[vertexCount * COORDS_PER_VERTEX];
        normals = new float[vertexCount * COORDS_PER_VERTEX];
        indexes = new short[vertexCount];
        textureCoords = new float[vertexCount * 2];

        calcVertices(radius, stacks, slices);
        calcNormals();

        initVertexBuffer();
        initNormalBuffer();
        initTextureCoordBuffer();
        initIndexBuffer();

        // INITIATE VERTEX SHADER AND FRAGMENT SHADER
        int vertexShader = MyGLRenderer.loadShaderFromFile(
                GLES20.GL_VERTEX_SHADER, "basic-gl2.vshader");
        int fragmentShader = MyGLRenderer.loadShaderFromFile(
                GLES20.GL_FRAGMENT_SHADER, "diffuse-gl2.fshader");

        // INITIATE OpenGL PROGRAM
        mProgram = GLES20.glCreateProgram();             // create empty OpenGL Program
        GLES20.glAttachShader(mProgram, vertexShader);   // add the vertex shader to program
        GLES20.glAttachShader(mProgram, fragmentShader); // add the fragment shader to program
        GLES20.glLinkProgram(mProgram);                  // create OpenGL program executables
    }

    public void draw(float[] projMatrix,
                     float[] modelViewMatrix,
                     float[] normalMatrix,
                     float[] light,
                     float[] light2) {

        GLES20.glUseProgram(mProgram);

        // INITIATE UNIFORM HANDLE
        mProjMatrixHandle = GLES20.glGetUniformLocation(mProgram, "uProjMatrix");
        mModelViewMatrixHandle = GLES20.glGetUniformLocation(mProgram, "uModelViewMatrix");
        mNormalMatrixHandle = GLES20.glGetUniformLocation(mProgram, "uNormalMatrix");
        mColorHandle = GLES20.glGetUniformLocation(mProgram, "uColor");
        mLightHandle = GLES20.glGetUniformLocation(mProgram, "uLight");
        mLight2Handle = GLES20.glGetUniformLocation(mProgram, "uLight2");

        // SET UNIFORMS
        GLES20.glUniformMatrix4fv(mProjMatrixHandle, 1, false, projMatrix, 0);
        GLES20.glUniformMatrix4fv(mModelViewMatrixHandle, 1, false, modelViewMatrix, 0);
        GLES20.glUniformMatrix4fv(mNormalMatrixHandle, 1, false, normalMatrix, 0);
        GLES20.glUniform3fv(mColorHandle, 1, color, 0);
        GLES20.glUniform3fv(mLightHandle, 1, light, 0);
        GLES20.glUniform3fv(mLight2Handle, 1, light2, 0);

        // INITIATE ATTRIBUTE HANDLES
        mPositionHandle = GLES20.glGetAttribLocation(mProgram, "aPosition");
        mNormalHandle = GLES20.glGetAttribLocation(mProgram, "aNormal");

        // ENABLE CERTAIN VERTEX ATTRIBUTE
        GLES20.glEnableVertexAttribArray(mPositionHandle);
        GLES20.glEnableVertexAttribArray(mNormalHandle);

        // SET ATTRIBUTES
        GLES20.glVertexAttribPointer(
                mPositionHandle, COORDS_PER_VERTEX,
                GLES20.GL_FLOAT, false,
                VERTEX_STRIDE, mVertexBuffer);
        GLES20.glVertexAttribPointer(
                mNormalHandle, COORDS_PER_VERTEX,
                GLES20.GL_FLOAT, false,
                VERTEX_STRIDE, mNormalBuffer);

        // DRAW THE SPHERE
//        GLES20.glDrawArrays(GLES20.GL_LINES, 0, vertices.length / 3);
        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0 , vertices.length/3);
        GLES20.glLineWidth(4.0f);

        // DISABLE CERTAIN VERTEX ATTRIBUTE
        GLES20.glDisableVertexAttribArray(mPositionHandle);
        GLES20.glDisableVertexAttribArray(mNormalHandle);
    }

    private void calcVertices(float radius, int stacks, int slices){

        ArrayList<Float> verticesAL = new ArrayList<>();
        ArrayList<Float> normalsAL = new ArrayList<>();
        ArrayList<Float> textureCoordsAL = new ArrayList<>();
        ArrayList<Short> indexesAl = new ArrayList<>();

        for (int stackNum = 0; stackNum < stacks + 1; stackNum++){
            for (int sliceNum = 0; sliceNum < slices + 1; sliceNum++){

                // STACK-WISE ANGLE
                float theta = (float) (stackNum*Math.PI / stacks);
                // SLICE-WISE ANGLE
                float phi= (float) (sliceNum * 2 * Math.PI / slices);

                float sinTheta = (float) Math.sin((double) theta);
                float sinPhi = (float) Math.sin((double) phi);
                float cosTheta = (float) Math.cos((double) theta);
                float cosPhi = (float) Math.cos((double) phi);

                float normalX = cosPhi * sinTheta;
                float normalY = cosTheta;
                float normalZ = sinPhi * sinTheta;

                float x = radius * normalX;
                float y = radius * normalY;
                float z = radius * normalZ;

                //FIXME U?
                float u = 1f - ((float) sliceNum / (float) slices);
                float v = (float) stackNum / (float) stacks;

                verticesAL.add(x);
                verticesAL.add(y);
                verticesAL.add(z);

                normalsAL.add(normalX);
                normalsAL.add(normalY);
                normalsAL.add(normalZ);

                textureCoordsAL.add(u);
                textureCoordsAL.add(v);
            }
        }

        //FIXME ?
        for(int stackNum = 0; stackNum < stacks; stackNum++){
            for (int sliceNum = 0; sliceNum < slices; sliceNum++){

                int second = (sliceNum * (stacks + 1)) + stackNum;
                int first = second + stacks + 1;

                indexesAl.add((short) first);
                indexesAl.add((short) second);
                indexesAl.add((short) (first + 1));

                indexesAl.add((short) second);
                indexesAl.add((short) (second + 1));
                indexesAl.add((short) (first + 1));
            }
        }

        // CONVERT ARRAY LIST TO ARRAY
        int i = 0;
        for (Float f : verticesAL){
            vertices[i++] = f;
        }

        i = 0;
        for (Float f : normalsAL){
            normals[i++] = f;
        }

        i = 0;
        for (Short s : indexesAl){
            indexes[i++] = s;
        }

        i = 0;
        for (Float f: textureCoordsAL){
            textureCoords[i++] = f;
        }
    }

    // FIXME DON'T KNOW HOW
    private void calcNormals(){

    }

    private void initVertexBuffer(){
        ByteBuffer byteBuf = ByteBuffer.allocateDirect(vertices.length * 4);
        byteBuf.order(ByteOrder.nativeOrder());
        mVertexBuffer = byteBuf.asFloatBuffer();
        mVertexBuffer.put(vertices);
        mVertexBuffer.position(0);
    }

    private void initNormalBuffer(){
        ByteBuffer byteBuf = ByteBuffer.allocateDirect(normals.length * 4);
        byteBuf.order(ByteOrder.nativeOrder());
        mNormalBuffer = byteBuf.asFloatBuffer();
        mNormalBuffer.put(normals);
        mNormalBuffer.position(0);
    }

    private void initTextureCoordBuffer(){
        ByteBuffer byteBuf = ByteBuffer.allocateDirect(textureCoords.length * 4);
        byteBuf.order(ByteOrder.nativeOrder());
        mTextureCoordBuffer = byteBuf.asFloatBuffer();
        mTextureCoordBuffer.put(textureCoords);
        mTextureCoordBuffer.position(0);
    }

    private void initIndexBuffer(){
        ByteBuffer byteBuf = ByteBuffer.allocateDirect(indexes.length * 2);
        byteBuf.order(ByteOrder.nativeOrder());
        mIndexBuffer = byteBuf.asShortBuffer();
        mIndexBuffer.put(indexes);
        mIndexBuffer.position(0);
    }
}
