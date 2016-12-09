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
    private float[] bgColor = new float[]{0.3f, 0.3f, 1.0f, 1.0f};

    // presets
    // ... map (& sea)
    private float mapSizeX = 90.0f;
    private float mapSizeY = 25.0f;
    private float mapSizeZ = 90.0f;
    private float mapUnitLength = 1.5f;
    private float mapMaxHeight = 26.5f;
    private float mapMinHeight = -11.5f;
    private float mapComplexity = 5.5f;

    // ... skybox
    private float skySizeX = 350.0f;
    private float skySizeY = 350.0f;
    private float skySizeZ = 350.0f;

    // ... lava
    private float lavaSizeX = 350.0f;
    private float lavaSizeZ = 350.0f;
    private float lavaHeight = -45.0f;

    // ... items
    public int itemCount = 15;
    private float itemRadius = 1.5f;
    private float itemMinDist = 5.0f;
    private float itemHeightOffset = 5.0f;

    // objects
    private MapCube mMap;
    private SeaRectangle mSea;
    private SkyBox mSky;
    private LavaRectangle mLava;
    private ItemSphere[] mItem;

    // item handlers
    private ItemGenerator mItemGenerator = null;

    // flags
    public boolean[] itemDrawFlag = new boolean[itemCount]; // true = draw ith item
    public boolean mapBlendFlag = false;

    // view matrix
    private float[] mViewMatrix = new float[16];

    // rotation matrix (changed by touch events)
    public float[] mViewRotationMatrix = new float[16];
    public float[] mCubeRotationMatrix = new float[16];
    public float[] mMapRotationMatrix = new float[16];
    public float[] mSeaRotationMatrix = new float[16];
    public float[] mSkyRotationMatrix = new float[16];
    public float[] mSphereRotationMatrix = new float[16];
    public float[] mLavaRotationMatrix = new float[16];
    public float[][] mItemRotationMatrix = new float[itemCount][16];

    // translation matrix (changed by touch events)
    public float[] mViewTranslationMatrix = new float[16];
    public float[] mCubeTranslationMatrix = new float[16];
    public float[] mMapTranslationMatrix = new float[16];
    public float[] mSeaTranslationMatrix = new float[16];
    public float[] mSkyTranslationMatrix = new float[16];
    public float[] mSphereTranslationMatrix = new float[16];
    public float[] mLavaTranslationMatrix = new float[16];
    public float[][] mItemTranslationMatrix = new float[itemCount][16];

    // projection matrix
    private float[] mProjMatrix = new float[16];

    // model matrix
    private float[] mCubeModelMatrix = new float[16];
    private float[] mMapModelMatrix = new float[16];
    private float[] mSeaModelMatrix = new float[16];
    private float[] mSkyModelMatrix = new float[16];
    private float[] mSphereModelMatrix = new float[16];
    private float[] mLavaModelMatrix = new float[16];
    private float[][] mItemModelMatrix = new float[itemCount][16];

    // model-view matrix
    private float[] mCubeModelViewMatrix = new float[16];
    private float[] mMapModelViewMatrix = new float[16];
    private float[] mSeaModelViewMatrix = new float[16];
    private float[] mSkyModelViewMatrix = new float[16];
    private float[] mSphereModelViewMatrix = new float[16];
    private float[] mLavaModelViewMatrix = new float[16];
    private float[][] mItemModelViewMatrix = new float[itemCount][16];

    // normal matrix
    private float[] mCubeNormalMatrix = new float[16];
    private float[] mMapNormalMatrix = new float[16];
    private float[] mSeaNormalMatrix = new float[16];
    private float[] mSkyNormalMatrix = new float[16];
    private float[] mSphereNormalMatrix = new float[16];
    private float[] mLavaNormalMatrix = new float[16];
    private float[][] mItemNormalMatrix = new float[itemCount][16];

    // temporary matrix for calculation
    private float[] mTempMatrix = new float[16];

    // lights
    private float[] mLight = new float[3];
    private float[] mLight2 = new float[3];

    private float bubbleScale = 0.03f;
    private float[] bubbleStart = new float[]{0, 7.0f, -13.0f};
    // private float cameraDistance = 2.0f;

    @Override
    public void onSurfaceCreated(GL10 unused, EGLConfig config) {
        // background frame color
        GLES20.glClearColor(bgColor[0], bgColor[1], bgColor[2], bgColor[3]);

        // lights
        mLight = new float[]{5.0f, 10.0f, 6.0f};
        mLight2 = mLight;

        // objects
        mMap = new MapCube(
                mapSizeX, mapSizeY, mapSizeZ,
                mapUnitLength,
                mapMaxHeight, mapMinHeight,
                mapComplexity,
                1.0f, true
        );

        mSea = new SeaRectangle(mapSizeX, mapSizeZ);
        mSky = new SkyBox(skySizeX, skySizeY, skySizeZ);
        mLava = new LavaRectangle(lavaSizeX, lavaSizeZ);

        // Since ItemGenerator need MapGenerator instance, we need to pass mMap to mItemGenerator
        // after we initialize mMap.
        mItemGenerator = new ItemGenerator(
                itemCount,
                itemRadius,
                itemMinDist,
                itemHeightOffset,
                mMap.mGenerator // This is already initialized, so we can safely pass this to mItemGenerator.
        );

        mItem = new ItemSphere[itemCount];

        for (int i = 0; i < itemCount; i++) {
            mItem[i] = new ItemSphere(itemRadius);
            itemDrawFlag[i] = true;
        }

        // initialize rotation / translation matrix
        Matrix.setIdentityM(mViewRotationMatrix, 0);
        Matrix.setIdentityM(mViewTranslationMatrix, 0);
        Matrix.translateM(mViewTranslationMatrix, 0, 0, -7.5f, -45.0f);

        Matrix.setIdentityM(mCubeRotationMatrix, 0);
        Matrix.setIdentityM(mCubeTranslationMatrix, 0);
        Matrix.translateM(mCubeTranslationMatrix, 0, 3.0f, 3.0f, 2.0f);

        Matrix.setIdentityM(mMapRotationMatrix, 0);
        Matrix.setIdentityM(mMapTranslationMatrix, 0);
        Matrix.translateM(mMapTranslationMatrix, 0, -mapSizeZ / 2.0f, 0, -mapSizeZ / 2.0f);

        Matrix.setIdentityM(mSeaRotationMatrix, 0);
        Matrix.setIdentityM(mSeaTranslationMatrix, 0);
        Matrix.translateM(mSeaTranslationMatrix, 0, -mapSizeX / 2.0f, 0, -mapSizeZ / 2.0F);

        Matrix.setIdentityM(mSkyRotationMatrix, 0);
        Matrix.setIdentityM(mSkyTranslationMatrix, 0);
        Matrix.translateM(mSkyTranslationMatrix, 0,
                -skySizeX / 2.0f, -skySizeY / 2.0f, -skySizeZ / 2.0f);

        Matrix.setIdentityM(mSphereRotationMatrix, 0);
        Matrix.setIdentityM(mSphereTranslationMatrix, 0);
        Matrix.translateM(mSphereTranslationMatrix, 0, bubbleStart[0], bubbleStart[1], bubbleStart[2]);

        Matrix.setIdentityM(mLavaRotationMatrix, 0);
        Matrix.setIdentityM(mLavaTranslationMatrix, 0);
        Matrix.translateM(mLavaTranslationMatrix, 0, -lavaSizeX / 2.0f, lavaHeight, -lavaSizeZ / 2.0f);

        float[][] itemPositions = mItemGenerator.getPositions();

        for (int i = 0; i < itemCount; i++) {
            Matrix.setIdentityM(mItemRotationMatrix[i], 0);
            Matrix.setIdentityM(mItemTranslationMatrix[i], 0);
            Matrix.translateM(mItemTranslationMatrix[i], 0, -mapSizeX / 2.0f, 0, -mapSizeZ / 2.0f);
            Matrix.translateM(mItemTranslationMatrix[i], 0, itemPositions[i][0], itemPositions[i][1], itemPositions[i][2]);
        }
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

        // calculate sphere model matrix
        Matrix.setIdentityM(mSphereModelMatrix, 0);
        Matrix.multiplyMM(mTempMatrix, 0, mSphereRotationMatrix, 0, mSphereModelMatrix, 0);
        System.arraycopy(mTempMatrix, 0, mSphereModelMatrix, 0, 16);
        Matrix.multiplyMM(mTempMatrix, 0, mSphereTranslationMatrix, 0, mSphereModelMatrix, 0);
        System.arraycopy(mTempMatrix, 0, mSphereModelMatrix, 0, 16);
        Matrix.scaleM(mSphereModelMatrix, 0, bubbleScale, bubbleScale, bubbleScale);

        // calculate lava model matrix
        Matrix.setIdentityM(mLavaModelMatrix, 0);
        Matrix.multiplyMM(mTempMatrix, 0, mLavaRotationMatrix, 0, mLavaModelMatrix, 0);
        System.arraycopy(mTempMatrix, 0, mLavaModelMatrix, 0, 16);
        Matrix.multiplyMM(mTempMatrix, 0, mLavaTranslationMatrix, 0, mLavaModelMatrix, 0);
        System.arraycopy(mTempMatrix, 0, mLavaModelMatrix, 0, 16);

        // calculate items' model matrix
        for (int i = 0; i < itemCount; i++) {
            Matrix.setIdentityM(mItemModelMatrix[i], 0);
            Matrix.multiplyMM(mTempMatrix, 0, mItemRotationMatrix[i], 0, mItemModelMatrix[i], 0);
            System.arraycopy(mTempMatrix, 0, mItemModelMatrix[i], 0, 16);
            Matrix.multiplyMM(mTempMatrix, 0, mItemTranslationMatrix[i], 0, mItemModelMatrix[i], 0);
            System.arraycopy(mTempMatrix, 0, mItemModelMatrix[i], 0, 16);
        }

        // calculate model-view matrix
        Matrix.multiplyMM(mCubeModelViewMatrix, 0, mViewMatrix, 0, mCubeModelMatrix, 0);
        Matrix.multiplyMM(mMapModelViewMatrix, 0, mViewMatrix, 0, mMapModelMatrix, 0);
        Matrix.multiplyMM(mSeaModelViewMatrix, 0, mViewMatrix, 0, mSeaModelMatrix, 0);
        Matrix.multiplyMM(mSkyModelViewMatrix, 0, mViewMatrix, 0, mSkyModelMatrix, 0);
        Matrix.multiplyMM(mSphereModelViewMatrix, 0, mViewMatrix, 0, mSphereModelMatrix, 0);
        Matrix.multiplyMM(mLavaModelViewMatrix, 0, mViewMatrix, 0, mLavaModelMatrix, 0);

        for (int i = 0; i < itemCount; i++) {
            Matrix.multiplyMM(mItemModelViewMatrix[i], 0, mViewMatrix, 0, mItemModelMatrix[i], 0);
        }

        // calculate normal matrix
        normalMatrix(mCubeNormalMatrix, 0, mCubeModelViewMatrix, 0);
        normalMatrix(mMapNormalMatrix, 0, mMapModelViewMatrix, 0);
        normalMatrix(mSeaNormalMatrix, 0, mSeaModelViewMatrix, 0);
        normalMatrix(mSkyNormalMatrix, 0, mSkyModelViewMatrix, 0);
        normalMatrix(mSphereNormalMatrix, 0, mSphereModelViewMatrix, 0);
        normalMatrix(mLavaNormalMatrix, 0, mLavaModelViewMatrix, 0);

        for (int i = 0; i < itemCount; i++) {
            normalMatrix(mItemNormalMatrix[i], 0, mItemModelViewMatrix[i], 0);
        }

        // draw the objects
        GLES20.glEnable(GLES20.GL_CULL_FACE);
        GLES20.glEnable(GLES20.GL_DEPTH_TEST);
        mSky.draw(mProjMatrix, mSkyModelViewMatrix, mSkyNormalMatrix, mLight, mLight2);
        mLava.draw(mProjMatrix, mLavaModelViewMatrix, mLavaNormalMatrix, mLight, mLight2);

        if (!mapBlendFlag) {
            mMap.draw(mProjMatrix, mMapModelViewMatrix, mMapNormalMatrix, mMapModelMatrix, mLight, mLight2);
        }

        for (int i = 0; i < itemCount; i++) {
            if (itemDrawFlag[i]) {
                mItem[i].draw(mProjMatrix, mItemModelViewMatrix[i], mItemNormalMatrix[i], mLight, mLight2);
            }
        }

        // GLES20.glDisable(GLES20.GL_CULL_FACE);

        GLES20.glEnable(GLES20.GL_BLEND);
        GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);

        if (mapBlendFlag) {
            mMap.draw(mProjMatrix, mMapModelViewMatrix, mMapNormalMatrix, mMapModelMatrix, mLight, mLight2);
        }

        // mSphere.draw(mProjMatrix, mSphereModelViewMatrix, mSphereNormalMatrix, mLight, mLight2);
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
                1.0f, 370.0f // near, far
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
