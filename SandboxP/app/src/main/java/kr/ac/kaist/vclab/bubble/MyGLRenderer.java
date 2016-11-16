package kr.ac.kaist.vclab.bubble;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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

    // presets
    private float mapSizeX = 30.0f;
    private float mapSizeY = 3.0f;
    private float mapSizeZ = 30.0f;
    private float mapUnitLength = 0.5f;
    private float mapMaxHeight = 15.0f;
    private float mapMinHeight = -2.0f;
    private float mapComplexity = 4.0f;
    private float skySizeX = 80.0f;
    private float skySizeY = 80.0f;
    private float skySizeZ = 80.0f;

    // objects
    private Cube mCube;
    private MapSquare mMap;
    private SeaRectangle mSea;
    private SkyBox mSky;
    private Sphere mSphere;


    // view matrix
    private float[] mViewMatrix = new float[16];

    // rotation matrix (changed by touch events)
    public float[] mViewRotationMatrix = new float[16];
    public float[] mCubeRotationMatrix = new float[16];
    public float[] mMapRotationMatrix = new float[16];
    public float[] mSeaRotationMatrix = new float[16];
    public float[] mSkyRotationMatrix = new float[16];
    public float [] mSphereRotationMatrix = new float[16];

    // translation matrix (changed by touch events)
    public float[] mViewTranslationMatrix = new float[16];
    public float[] mCubeTranslationMatrix = new float[16];
    public float[] mMapTranslationMatrix = new float[16];
    public float[] mSeaTranslationMatrix = new float[16];
    public float[] mSkyTranslationMatrix = new float[16];
    public float [] mSphereTranslationMatrix = new float[16];

    // projection matrix
    private float[] mProjMatrix = new float[16];

    // model matrix
    private float[] mCubeModelMatrix = new float[16];
    private float[] mMapModelMatrix = new float[16];
    private float[] mSeaModelMatrix = new float[16];
    private float[] mSkyModelMatrix = new float[16];
    private float[] mSphereModelMatrix = new float[16];

    // model-view matrix
    private float[] mCubeModelViewMatrix = new float[16];
    private float[] mMapModelViewMatrix = new float[16];
    private float[] mSeaModelViewMatrix = new float[16];
    private float[] mSkyModelViewMatrix = new float[16];
    private float[] mSphereModelViewMatrix = new float[16];

    // normal matrix
    private float[] mCubeNormalMatrix = new float[16];
    private float[] mMapNormalMatrix = new float[16];
    private float[] mSeaNormalMatrix = new float[16];
    private float[] mSkyNormalMatrix = new float[16];
    private float[] mSphereNormalMatrix = new float[16];

    // temporary matrix for calculation
    private float[] mTempMatrix = new float[16];

    // lights
    private float[] mLight = new float[3];
    private float[] mLight2 = new float[3];

    private float bubbleScale = 0.03f;
    private float[] bubbleStart = new float[]{0,0,0};
    private float cameraDistance = 2.0f;

    @Override
    public void onSurfaceCreated(GL10 unused, EGLConfig config) {
        // background frame color
        GLES20.glClearColor(bgColor[0], bgColor[1], bgColor[2], bgColor[3]);

        // lights
        mLight = new float[]{5.0f, 10.0f, 6.0f};
        mLight2 = new float[]{-4.0f, 7.0f, 8.0f};

        // objects
        mCube = new Cube();

        mMap = new MapSquare(
                mapSizeX, mapSizeY, mapSizeZ,
                mapUnitLength,
                mapMaxHeight, mapMinHeight,
                mapComplexity,
                1.0f, true
        );

        mSea = new SeaRectangle(mapSizeX, mapSizeZ);
        mSky = new SkyBox(skySizeX, skySizeY, skySizeZ);
        mSphere = new Sphere();

        // initialize rotation / translation matrix
        Matrix.setIdentityM(mViewRotationMatrix, 0);
        Matrix.setIdentityM(mViewTranslationMatrix, 0);
        Matrix.translateM(mViewTranslationMatrix, 0, 0, -2.5f, -14.0f);

        Matrix.setIdentityM(mCubeRotationMatrix, 0);
        Matrix.setIdentityM(mCubeTranslationMatrix, 0);
        Matrix.translateM(mCubeTranslationMatrix, 0, 3.0f, 3.0f, 2.0f);

        Matrix.setIdentityM(mMapRotationMatrix, 0);
        Matrix.setIdentityM(mMapTranslationMatrix, 0);
        Matrix.translateM(mMapTranslationMatrix, 0, -10.0f, -5.0f, -10.0f);

        Matrix.setIdentityM(mSeaRotationMatrix, 0);
        Matrix.setIdentityM(mSeaTranslationMatrix, 0);
        Matrix.translateM(mSeaTranslationMatrix, 0, -10.0f, -4.0f, -10.0f);

        Matrix.setIdentityM(mSkyRotationMatrix, 0);
        Matrix.setIdentityM(mSkyTranslationMatrix, 0);
        Matrix.translateM(mSkyTranslationMatrix, 0,
                -skySizeX / 2.0f, -skySizeY / 2.0f, -skySizeZ / 2.0f);


        Matrix.setIdentityM(mSphereRotationMatrix, 0);
        Matrix.setIdentityM(mSphereTranslationMatrix, 0);
        Matrix.translateM(mSphereTranslationMatrix, 0, bubbleStart[0], bubbleStart[1], bubbleStart[2]);
    }

    @Override
    public void onDrawFrame(GL10 unused) {
        // (re)draw background color
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);

        // calculate view matrix
        Matrix.setIdentityM(mViewMatrix, 0);
/*
        Matrix.multiplyMM(mTempMatrix, 0, mViewRotationMatrix, 0, mViewMatrix, 0);
        System.arraycopy(mTempMatrix, 0, mViewMatrix, 0, 16);


        float[] translation = new float[]{0,0,-1,0};
        Matrix.multiplyMV(translation, 0, mViewRotationMatrix, 0, translation, 0);
        for(int i=0; i<16; i++){
            System.out.print(mViewTranslationMatrix[i] + " ");
            if(i%4 == 3 )System.out.println();
        }
        //Matrix.translateM(mViewMatrix,0,translation[0],translation[1],translation[2]);


        System.arraycopy(mSphereTranslationMatrix, 0, mViewTranslationMatrix, 0, 16);
        Matrix.multiplyMM(mTempMatrix, 0, mViewTranslationMatrix, 0, mViewMatrix, 0);
        System.arraycopy(mTempMatrix, 0, mViewMatrix, 0, 16);
*/

        float[] eye = new float[]{mSphereTranslationMatrix[12], mSphereTranslationMatrix[13], mSphereTranslationMatrix[14]};

        float[] translation = new float[]{0,0,-1,0};
        float[] change_translation = new float[4];
        Matrix.multiplyMV(change_translation, 0, mViewRotationMatrix, 0, translation, 0);
        for (int i=0; i<3; i++){
            change_translation[i]*=cameraDistance;
            eye[i] += change_translation[i];
        }

        float[] look = new float[]{mSphereTranslationMatrix[12], mSphereTranslationMatrix[13], mSphereTranslationMatrix[14]};

        float[] up = new float[] {0,1,0,0};

        Matrix.setLookAtM(mViewMatrix, 0, eye[0], eye[1], eye[2], look[0], look[1], look[2], up[0], up[1], up[2]);



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

        // calculate rec model matrix
        Matrix.setIdentityM(mSeaModelMatrix, 0);

        Matrix.multiplyMM(mTempMatrix, 0, mSeaRotationMatrix, 0, mSeaModelMatrix, 0);
        System.arraycopy(mTempMatrix, 0, mSeaModelMatrix, 0, 16);

        Matrix.multiplyMM(mTempMatrix, 0, mSeaTranslationMatrix, 0, mSeaModelMatrix, 0);
        System.arraycopy(mTempMatrix, 0, mSeaModelMatrix, 0, 16);

        // calculate skybox model matrix
        Matrix.setIdentityM(mSkyModelMatrix, 0);

        Matrix.multiplyMM(mTempMatrix, 0, mSkyRotationMatrix, 0, mSkyModelMatrix, 0);
        System.arraycopy(mTempMatrix, 0, mSkyModelMatrix, 0, 16);

        Matrix.multiplyMM(mTempMatrix, 0, mSkyTranslationMatrix, 0, mSkyModelMatrix, 0);
        System.arraycopy(mTempMatrix, 0, mSkyModelMatrix, 0, 16);


        Matrix.setIdentityM(mSphereModelMatrix, 0);

        Matrix.multiplyMM(mTempMatrix, 0, mSphereRotationMatrix, 0, mSphereModelMatrix, 0);
        System.arraycopy(mTempMatrix, 0, mSphereModelMatrix, 0, 16);

        Matrix.multiplyMM(mTempMatrix, 0, mSphereTranslationMatrix, 0, mSphereModelMatrix, 0);
        System.arraycopy(mTempMatrix, 0, mSphereModelMatrix, 0, 16);
        Matrix.scaleM(mSphereModelMatrix, 0, bubbleScale, bubbleScale, bubbleScale);


        // calculate model-view matrix
        Matrix.multiplyMM(mCubeModelViewMatrix, 0, mViewMatrix, 0, mCubeModelMatrix, 0);
        Matrix.multiplyMM(mMapModelViewMatrix, 0, mViewMatrix, 0, mMapModelMatrix, 0);
        Matrix.multiplyMM(mSeaModelViewMatrix, 0, mViewMatrix, 0, mSeaModelMatrix, 0);
        Matrix.multiplyMM(mSkyModelViewMatrix, 0, mViewMatrix, 0, mSkyModelMatrix, 0);
        Matrix.multiplyMM(mSphereModelViewMatrix, 0, mViewMatrix, 0, mSphereModelMatrix, 0);

        // calculate normal matrix
        normalMatrix(mCubeNormalMatrix, 0, mCubeModelViewMatrix, 0);
        normalMatrix(mMapNormalMatrix, 0, mMapModelViewMatrix, 0);
        normalMatrix(mSeaNormalMatrix, 0, mSeaModelViewMatrix, 0);
        normalMatrix(mSkyNormalMatrix, 0, mSkyModelViewMatrix, 0);
        normalMatrix(mSphereNormalMatrix, 0, mSphereModelViewMatrix, 0);

        // draw the objects

        GLES20.glEnable(GLES20.GL_CULL_FACE);
        GLES20.glEnable(GLES20.GL_DEPTH_TEST);
        //mCube.draw(mProjMatrix, mCubeModelViewMatrix, mCubeNormalMatrix, mLight, mLight2);
        mMap.draw(mProjMatrix, mMapModelViewMatrix, mMapNormalMatrix, mLight, mLight2);
        mSky.draw(mProjMatrix, mSkyModelViewMatrix, mSkyNormalMatrix, mLight, mLight2);

        // mSea.draw(mProjMatrix, mSeaModelViewMatrix, mSeaNormalMatrix, mLight, mLight2);

        //GLES20.glDisable(GLES20.GL_DEPTH_TEST);

        GLES20.glEnable(GLES20.GL_BLEND);
        GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);
        mSphere.draw(mProjMatrix, mSphereModelViewMatrix, mSphereNormalMatrix, mLight, mLight2);
        mSea.draw(mProjMatrix, mSeaModelViewMatrix, mSeaNormalMatrix, mLight, mLight2);
        GLES20.glDisable(GLES20.GL_BLEND);

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
                1.0f, 100.0f // near, far
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

    /* Load image from the given filename. */
    public static Bitmap loadImage(String fileName) {
        try {
            Bitmap temp = BitmapFactory.decodeStream(MainActivity.context.getAssets().open(fileName));
            android.graphics.Matrix matrix = new android.graphics.Matrix();
            matrix.preScale(1.0f, -1.0f);
            Bitmap image = Bitmap.createBitmap(temp, 0, 0, temp.getWidth(), temp.getHeight(), matrix, true);
            temp.recycle();

            return image;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}
