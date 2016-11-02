package kr.ac.kaist.vclab.bubble;

import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;

import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.Arrays;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * Created by avantgarde on 2016-11-02.
 */

public class MyGLRenderer implements GLSurfaceView.Renderer {
    // background color
    private float[] bgColor = new float[]{0.7f, 0.8f, 0.9f, 1.0f};

    // objects
    private Cube mCube;
    private MapSquare mMap;

    // view matrix
    private float[] mViewMatrix = new float[16];

    // rotation matrix (changed by touch events)
    public float[] mViewRotationMatrix = new float[16];
    public float[] mCubeRotationMatrix = new float[16];
    public float[] mMapRotationMatrix = new float[16];

    // translation matrix (changed by touch events)
    public float[] mViewTranslationMatrix = new float[16];
    public float[] mCubeTranslationMatrix = new float[16];
    public float[] mMapTranslationMatrix = new float[16];

    // projection matrix
    private float[] mProjMatrix = new float[16];

    // model matrix
    private float[] mCubeModelMatrix = new float[16];
    private float[] mMapModelMatrix = new float[16];

    // model-view matrix
    private float[] mCubeModelViewMatrix = new float[16];
    private float[] mMapModelViewMatrix = new float[16];

    // normal matrix
    private float[] mCubeNormalMatrix = new float[16];
    private float[] mMapNormalMatrix = new float[16];

    // temporary matrix for calculation
    private float[] mTempMatrix = new float[16];

    // lights
    private float[] mLight = new float[3];
    private float[] mLight2 = new float[3];

    @Override
    public void onSurfaceCreated(GL10 unused, EGLConfig config) {
        // background frame color
        GLES20.glClearColor(bgColor[0], bgColor[1], bgColor[2], bgColor[3]);
        GLES20.glEnable(GLES20.GL_CULL_FACE);
        GLES20.glEnable(GLES20.GL_DEPTH_TEST);

        // (re)set view matrix
        resetViewMatrix();

        // lights
        mLight = new float[]{2.0f, 3.0f, 14.0f};
        mLight2 = new float[]{-2.0f, -3.0f, -5.0f};

        // objects
        mCube = new Cube();
        mMap = new MapSquare();

        // initialize rotation / translation matrix
        Matrix.setIdentityM(mViewRotationMatrix, 0);
        Matrix.setIdentityM(mViewTranslationMatrix, 0);
        Matrix.translateM(mViewTranslationMatrix, 0, 0, 0, -9.3f);

        Matrix.setIdentityM(mCubeRotationMatrix, 0);
        Matrix.setIdentityM(mCubeTranslationMatrix, 0);
        Matrix.translateM(mCubeTranslationMatrix, 0, 0, 3.0f, 2.0f);

        Matrix.setIdentityM(mMapRotationMatrix, 0);
        Matrix.setIdentityM(mMapTranslationMatrix, 0);
        Matrix.translateM(mMapTranslationMatrix, 0, 0, 0, 0);
    }

    @Override
    public void onDrawFrame(GL10 unused) {
        // (re)draw background color
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);

        // calculate view matrix
        Matrix.setIdentityM(mViewMatrix, 0);

        Matrix.multiplyMM(mTempMatrix, 0, mViewRotationMatrix, 0, mViewMatrix, 0);
        System.arraycopy(mTempMatrix, 0, mViewMatrix, 0, 16);

        Matrix.multiplyMM(mTempMatrix, 0, mViewTranslationMatrix, 0, mViewMatrix, 0);
        System.arraycopy(mTempMatrix, 0, mViewMatrix, 0, 16);

        // calculate cube model matrix
        Matrix.setIdentityM(mCubeModelMatrix, 0);

        Matrix.multiplyMM(mTempMatrix, 0, mCubeRotationMatrix, 0, mCubeModelMatrix, 0);
        System.arraycopy(mTempMatrix, 0, mCubeModelMatrix, 0, 16);

        Matrix.multiplyMM(mTempMatrix, 0, mCubeTranslationMatrix, 0, mCubeModelMatrix, 0);
        System.arraycopy(mTempMatrix, 0, mCubeModelMatrix, 0, 16);

        Matrix.scaleM(mCubeModelMatrix, 0, 0.4f, 0.4f, 0.4f);

        // calculate map model matrix
        Matrix.setIdentityM(mMapModelMatrix, 0);

        Matrix.multiplyMM(mTempMatrix, 0, mMapRotationMatrix, 0, mMapModelMatrix, 0);
        System.arraycopy(mTempMatrix, 0, mMapModelMatrix, 0, 16);

        Matrix.multiplyMM(mTempMatrix, 0, mMapTranslationMatrix, 0, mMapModelMatrix, 0);
        System.arraycopy(mTempMatrix, 0, mMapModelMatrix, 0, 16);

        Matrix.translateM(
                mMapModelMatrix, 0,
                -mMap.sizeX / 2.0f, mMap.sizeY / 2.0f - 3.0f, -mMap.sizeZ / 2.0f
        );

        // calculate model-view matrix
        Matrix.multiplyMM(mCubeModelViewMatrix, 0, mViewMatrix, 0, mCubeModelMatrix, 0);
        Matrix.multiplyMM(mMapModelViewMatrix, 0, mViewMatrix, 0, mMapModelMatrix, 0);

        // calculate normal matrix
        normalMatrix(mCubeNormalMatrix, 0, mCubeModelViewMatrix, 0);
        normalMatrix(mMapNormalMatrix, 0, mMapModelViewMatrix, 0);

        // draw the objects
        mCube.draw(mProjMatrix, mCubeModelViewMatrix, mCubeNormalMatrix, mLight, mLight2);
        mMap.draw(mProjMatrix, mMapModelViewMatrix, mMapNormalMatrix, mLight, mLight2);
    }

    @Override
    public void onSurfaceChanged(GL10 unused, int width, int height) {
        // set viewport
        GLES20.glViewport(0, 0, width, height);

        // calculate projection matrix
        float ratio = width / (float) height;

        Matrix.frustumM(
                mProjMatrix, 0,
                -ratio, ratio, // left, right
                -1.0f, 1.0f, // bottom, top
                1.0f, 20.0f // near, far
        );
    }

    /* Extension of Matrix - Calculate normal matrix. */
    private void normalMatrix(float[] dst, int dstOffset, float[] src, int srcOffset) {
        Matrix.invertM(dst, dstOffset, src, srcOffset);

        dst[12] = 0;
        dst[13] = 0;
        dst[14] = 0;

        float[] temp = Arrays.copyOf(dst, 16);
        Matrix.transposeM(dst, dstOffset, temp, 0);
    }

    /* (Re)set the view to specific values. */
    private void resetViewMatrix() {
        Matrix.setLookAtM(
                mViewMatrix, 0,
                0.0f, 0.0f, 4.0f, // eye position
                0.0f, 0.0f, -1.0f, // looking direction
                0.0f, 1.0f, 0.0f // up vector
        );
    }

    /* Load shader from the given code. */
    public static int loadShader(int type, String shaderCode) {
        int shader = GLES20.glCreateShader(type);

        // add the code to the shader
        GLES20.glShaderSource(shader, shaderCode);

        // compile the shader
        GLES20.glCompileShader(shader);

        return shader;
    }

    /* Load shader from the given stream. */
    public static int loadShader(int type, InputStream shaderFile) {
        String shaderCode = null;

        try {
            shaderCode = IOUtils.toString(shaderFile, Charset.defaultCharset());
        } catch (IOException e) {
            e.printStackTrace();
        }

        return loadShader(type, shaderCode);
    }

    /* Load shader from the given filename. */
    public static int loadShaderFromFile(int type, String fileName) {
        try {
            return loadShader(type, MainActivity.context.getAssets().open(fileName));
        } catch (IOException e) {
            e.printStackTrace();
        }

        return -1;
    }
}
