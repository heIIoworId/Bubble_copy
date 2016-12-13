package kr.ac.kaist.vclab.bubble;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.util.Log;

import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import kr.ac.kaist.vclab.bubble.activities.GameClearActivity;
import kr.ac.kaist.vclab.bubble.activities.GameOverActivity;
import kr.ac.kaist.vclab.bubble.activities.MainActivity;
import kr.ac.kaist.vclab.bubble.collision.Intersect;
import kr.ac.kaist.vclab.bubble.collision.TriangleCollision;
import kr.ac.kaist.vclab.bubble.environment.Env;
import kr.ac.kaist.vclab.bubble.environment.GameEnv;
import kr.ac.kaist.vclab.bubble.generators.ItemGenerator;
import kr.ac.kaist.vclab.bubble.models.BubbleCore;
import kr.ac.kaist.vclab.bubble.models.BubbleSphere;
import kr.ac.kaist.vclab.bubble.models.Item;
import kr.ac.kaist.vclab.bubble.models.ItemManager;
import kr.ac.kaist.vclab.bubble.models.LavaRectangle;
import kr.ac.kaist.vclab.bubble.models.MapCube;
import kr.ac.kaist.vclab.bubble.models.SeaRectangle;
import kr.ac.kaist.vclab.bubble.models.SkyBox;
import kr.ac.kaist.vclab.bubble.physics.Blower;
import kr.ac.kaist.vclab.bubble.physics.Particle;
import kr.ac.kaist.vclab.bubble.physics.Spring;
import kr.ac.kaist.vclab.bubble.physics.PhysicalWorld;
import kr.ac.kaist.vclab.bubble.utils.GeomOperator;
import kr.ac.kaist.vclab.bubble.utils.MatOperator;
import kr.ac.kaist.vclab.bubble.utils.VecOperator;

/**
 * Created by sjjeon on 16. 9. 20.
 */

public class MyGLRenderer implements GLSurfaceView.Renderer {
    // TAG
    private static final String TAG = "MyGLRenderer";

    // PRESETS OF MAP
    private float mapSizeX = GameEnv.getInstance().mapSizeX;
    private float mapSizeY = GameEnv.getInstance().mapSizeY;
    private float mapSizeZ = GameEnv.getInstance().mapSizeZ;
    private float mapUnitLength = GameEnv.getInstance().mapUnitLength;
    private float mapMaxHeight = GameEnv.getInstance().mapMaxHeight;
    private float mapMinHeight = GameEnv.getInstance().mapMinHeight;
    private float mapComplexity = GameEnv.getInstance().mapComplexity;

    private float lavaSizeX = GameEnv.getInstance().lavaSizeX;
    private float lavaSizeZ = GameEnv.getInstance().lavaSizeZ;
    private float lavaHeight = GameEnv.getInstance().lavaHeight;

    // FLAGS USED INSIDE RENDERER
    public boolean enableHintFlag = true; // false -> mapBlendFlag has no effect
    public boolean mapBlendFlag = false; // true -> map becomes transparent

    // DECLARE MODELS
    public MapCube mMap;
    public SeaRectangle mSea;
    public LavaRectangle mLava;
    public SkyBox mSkyBox;
    private BubbleSphere mBubble;
    private ArrayList<Item> mItems;

    // ITEM GENERATOR
    private ItemGenerator mItemGenerator;
    private ItemGenerator mBubbleGenerator;

    // DECLARE PHYSICAL ENTITIES
    private PhysicalWorld mPhysicalWorld;
    private ArrayList<Particle> mParticles;
    private ArrayList<Spring> mSprings;
    public static Blower mBlower;
    private BubbleCore mBubbleCore;

    // DECLARE LIGHTS
    private float[] mLight = new float[3];
    private float[] mLight2 = new float[3];

    // DECLARE POSITION OF CAMERA
    float[] mCamera = new float[3];

    // MATRICES FOR VIEW
    private float[] mViewMatrix = new float[16];
    public float[] mViewRotationMatrix = new float[16];
    public float[] mViewTranslationMatrix = new float[16];

    // MATRICES FOR mBubble
    public float[] mBubbleRotationMatrix = new float[16];
    public float[] mBubbleTranslationMatrix = new float[16];
    private float[] mBubbleModelMatrix = new float[16];
    private float[] mBubbleModelViewMatrix = new float[16];
    private float[] mBubbleNormalMatrix = new float[16];

    // MATRICES FOR mItems
    public float[][] mItemRotationMatrix = new float[GameEnv.getInstance().numOfTotalItems][16];
    public float[][] mItemTranslationMatrix = new float[GameEnv.getInstance().numOfTotalItems][16];
    private float[][] mItemModelMatrix = new float[GameEnv.getInstance().numOfTotalItems][16];
    private float[][] mItemModelViewMatrix = new float[GameEnv.getInstance().numOfTotalItems][16];
    private float[][] mItemNormalMatrix = new float[GameEnv.getInstance().numOfTotalItems][16];

    // MATRICES FOR mBubbleCore
    public float[] mBubbleCoreRotationMatrix = new float[16];
    public float[] mBubbleCoreTranslationMatrix = new float[16];
    private float[] mBubbleCoreModelMatrix = new float[16];
    private float[] mBubbleCoreModelViewMatrix = new float[16];
    private float[] mBubbleCoreNormalMatrix = new float[16];

    // MATRICES FOR mMap
    public float[] mMapRotationMatrix = new float[16];
    public float[] mMapTranslationMatrix = new float[16];
    private float[] mMapModelMatrix = new float[16];
    private float[] mMapModelViewMatrix = new float[16];
    private float[] mMapNormalMatrix = new float[16];

    // MATRICES FOR mLava
    public float[] mLavaRotationMatrix = new float[16];
    public float[] mLavaTranslationMatrix = new float[16];
    private float[] mLavaModelMatrix = new float[16];
    private float[] mLavaModelViewMatrix = new float[16];
    private float[] mLavaNormalMatrix = new float[16];

    // MATRICES FOR mSea
    public float[] mSeaRotationMatrix = new float[16];
    public float[] mSeaTranslationMatrix = new float[16];
    private float[] mSeaModelMatrix = new float[16];
    private float[] mSeaModelViewMatrix = new float[16];
    private float[] mSeaNormalMatrix = new float[16];

    // MATRICES FOR mSkybox
    public float[] mSkyboxRotationMatrix = new float[16];
    public float[] mSkyboxTranslationMatrix = new float[16];
    private float[] mSkyboxModelMatrix = new float[16];
    private float[] mSkyboxModelViewMatrix = new float[16];
    private float[] mSkyboxNormalMatrix = new float[16];

    // OTHER MATRICES
    private float[] mProjMatrix = new float[16];
    private float[] mTempMatrix = new float[16];

    private MainActivity activity;
    @Override
    // CALLED WHEN SURFACE IS CREATED AT FIRST.
    public void onSurfaceCreated(GL10 unused, EGLConfig config) {
        // SET BACKGROUND COLOR
        GLES20.glClearColor(0.7f, 0.8f, 0.9f, 1.0f); // skyblue

        // INIT BUBBLE
        mBubble = new BubbleSphere(
                GameEnv.getInstance().radiusOfBubble,
                GameEnv.getInstance().levelOfBubble);
        mBubble.color = GameEnv.getInstance().colorOfBubble;

        // INIT MAP
        mMap = new MapCube(
                mapSizeX, mapSizeY, mapSizeZ,
                mapUnitLength,
                mapMaxHeight, mapMinHeight,
                mapComplexity,
                1.0f, true
        );

        // INIT LAVA
        mLava = new LavaRectangle(lavaSizeX, lavaSizeZ);

        // INIT ITEMGEN
        mItemGenerator = new ItemGenerator(
                GameEnv.getInstance().numOfTotalItems,
                GameEnv.getInstance().radiusOfItem,
                GameEnv.getInstance().radiusOfBubble * 2,
                10.0f,
                1.0f / 8.0f,
                mMap.mGenerator
        );

        // INIT BUBBLEGEN
        mBubbleGenerator = new ItemGenerator(
                1,
                GameEnv.getInstance().radiusOfBubble,
                GameEnv.getInstance().radiusOfBubble * 2,
                7.0f,
                1.0f / 6.0f,
                mMap.mGenerator
        );

        // INIT ITEMS
        float[][] coors = mItemGenerator.getPositions();
        float[] positions = new float[coors.length * 3];

        for (int i = 0; i < coors.length; i++) {
            positions[3 * i] = coors[i][0];
            positions[3 * i + 1] = coors[i][1];
            positions[3 * i + 2] = coors[i][2];
        }

        mItems = new ArrayList<>();
        for (int i = 0; i < positions.length; i = i + 3) {
            float[] center = new float[3];
            center[0] = positions[i];
            center[1] = positions[i + 1];
            center[2] = positions[i + 2];
            Item tempItem = new Item(center);
            tempItem.color = GameEnv.getInstance().colorOfItem;
            mItems.add(tempItem);
        }
        for (int i = 0; i < GameEnv.getInstance().numOfTotalItems; i++) {
            ItemManager.getInstance().items[i] = mItems.get(i);
        }

        // INIT SEA (same x, z size as map)
        mSea = new SeaRectangle(mapSizeX, mapSizeZ);

        // INIT SKYBOX
        mSkyBox = new SkyBox(
                GameEnv.getInstance().imgFolder,
                GameEnv.getInstance().skySize
        );

        // INIT WORLD
        mParticles = GeomOperator.genParticles(mBubble.getVertices());
        mSprings = GeomOperator.genSprings(mParticles);
        //mBubbleCore = new BubbleCore(GameEnv.getInstance().initialLocationOfBubble);

        float[] bubbleCoor = mBubbleGenerator.getPositions()[0];
        mBubbleCore = new BubbleCore(
                new float[]{
                        bubbleCoor[0] - mapSizeX / 2.0f,
                        bubbleCoor[1],
                        bubbleCoor[2] - mapSizeZ / 2.0f
                }
        );

        if (Env.getInstance().micStatus == 1) {
            mBlower = new Blower();
            mBlower.setBubbleCore(mBubbleCore);
        }

        mPhysicalWorld = new PhysicalWorld();
        mPhysicalWorld.setParticles(mParticles);
        mPhysicalWorld.setSprings(mSprings);
        mPhysicalWorld.setBubbleCore(mBubbleCore);
        if (Env.getInstance().micStatus == 1) {
            mPhysicalWorld.setBlower(mBlower);
        }

        // INITIALIZE LIGHTS
        mLight = new float[]{2.0f, 3.0f, 14.0f};
        mLight2 = new float[]{-2.0f, -3.0f, -5.0f};

        // INITIALIZE MATRICES
        resetViewMatrix();
        // INIT VIEW MATRIX
        Matrix.setIdentityM(mViewRotationMatrix, 0);
        Matrix.setIdentityM(mViewTranslationMatrix, 0);
        Matrix.translateM(mViewTranslationMatrix, 0, 0, 0, -14.0f);

        // INIT BUBBLE MATRIX
        Matrix.setIdentityM(mBubbleRotationMatrix, 0);
        Matrix.setIdentityM(mBubbleTranslationMatrix, 0);

        // INIT ITEMS MARICES
        for (int i = 0; i < GameEnv.getInstance().numOfTotalItems; i++) {
            Matrix.setIdentityM(mItemRotationMatrix[i], 0);
            Matrix.setIdentityM(mItemTranslationMatrix[i], 0);
            float newCenter[] = new float[3];
            float center[] = mItems.get(i).getCenter();
            newCenter[0] = center[0] - (GameEnv.getInstance().mapSizeX / 2.0f);
            newCenter[1] = center[1];
            newCenter[2] = center[2] - (GameEnv.getInstance().mapSizeZ / 2.0f);
            mItems.get(i).setCenter(newCenter);
            Matrix.translateM(
                    mItemTranslationMatrix[i], 0,
                    newCenter[0], newCenter[1], newCenter[2]);
        }

        // INIT BUBBLECORE MATRIX
        Matrix.setIdentityM(mBubbleCoreRotationMatrix, 0);
        Matrix.setIdentityM(mBubbleCoreTranslationMatrix, 0);
        Matrix.translateM(mBubbleCoreTranslationMatrix, 0, 0, 0, 0);

        // INIT MAP MATRIX
        Matrix.setIdentityM(mMapRotationMatrix, 0);
        Matrix.setIdentityM(mMapTranslationMatrix, 0);
        Matrix.translateM(mMapTranslationMatrix, 0, -mapSizeX / 2.0f, 0, -mapSizeZ / 2.0f);

        // INIT LAVA MATRIX
        Matrix.setIdentityM(mLavaRotationMatrix, 0);
        Matrix.setIdentityM(mLavaTranslationMatrix, 0);
        Matrix.translateM(mLavaTranslationMatrix, 0, -lavaSizeX / 2.0f, lavaHeight, -lavaSizeZ / 2.0f);

        // INIT SEA MATRIX
        Matrix.setIdentityM(mSeaRotationMatrix, 0);
        Matrix.setIdentityM(mSeaTranslationMatrix, 0);
        Matrix.translateM(mSeaTranslationMatrix, 0, -mapSizeX / 2.0f, 0, -mapSizeZ / 2.0f);

        // INIT SKYBOX MATRIX
        Matrix.setIdentityM(mSkyboxRotationMatrix, 0);
        Matrix.setIdentityM(mSkyboxTranslationMatrix, 0);
    }

    @Override
    public void onDrawFrame(GL10 unused) {

        float curTime = (System.currentTimeMillis() - GameEnv.getInstance().startTime) / 1000000.0f;
        int status = GameEnv.getGameStatus();
        if(status == -1 ){
            Intent intent = new Intent(activity, GameOverActivity.class);
            activity.startActivity(intent);
            //Finish the MainActivity to prevent the return on the MainActivity by backpress.
            activity.finish();
        }
        else if (status == 1){
            Intent intent = new Intent(activity, GameClearActivity.class);
            activity.startActivity(intent);
            //Finish the MainActivity to prevent the return on the MainActivity by backpress.
            activity.finish();
        }

        // CLEAR COLOR & DEPTH BUFFERS
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);

        // UPDATE VIEW MATRIX TO FOLLOW BUBBLE
        if (GameEnv.getInstance().traceFlag) {
            updateView();
        }

        // CALC BUBBLE MATRIX
        Matrix.setIdentityM(mBubbleTranslationMatrix, 0);
        float curLocation[] = mBubbleCore.getLocation(); // FIND WHERE CORE IS
        mBubble.setCenter(curLocation); // UPDATE CENTER OF BUBBLE
        Matrix.translateM(
                mBubbleTranslationMatrix, 0,
                mBubble.getCenter()[0], mBubble.getCenter()[1], mBubble.getCenter()[2]);
        Matrix.setIdentityM(mBubbleModelMatrix, 0);
        Matrix.multiplyMM(mTempMatrix, 0, mBubbleRotationMatrix, 0, mBubbleModelMatrix, 0);
        System.arraycopy(mTempMatrix, 0, mBubbleModelMatrix, 0, 16);
        Matrix.multiplyMM(mTempMatrix, 0, mBubbleTranslationMatrix, 0, mBubbleModelMatrix, 0);
        System.arraycopy(mTempMatrix, 0, mBubbleModelMatrix, 0, 16);
        Matrix.scaleM(mBubbleModelMatrix, 0,
                GameEnv.getInstance().getScaleOfBubble(),
                GameEnv.getInstance().getScaleOfBubble(),
                GameEnv.getInstance().getScaleOfBubble());
//        mBubbleCore.getCollision().scaleRadius(GameEnv.getInstance().getScaleOfBubble());
        Matrix.multiplyMM(mBubbleModelViewMatrix, 0, mViewMatrix, 0, mBubbleModelMatrix, 0);
        normalMatrix(mBubbleNormalMatrix, 0, mBubbleModelViewMatrix, 0);

        // CALC mItems MATRICES
        for (int i = 0; i < GameEnv.getInstance().numOfTotalItems; i++) {
            Matrix.setIdentityM(mItemModelMatrix[i], 0);
            Matrix.multiplyMM(mTempMatrix, 0, mItemRotationMatrix[i], 0, mItemModelMatrix[i], 0);
            System.arraycopy(mTempMatrix, 0, mItemModelMatrix[i], 0, 16);
            Matrix.multiplyMM(mTempMatrix, 0, mItemTranslationMatrix[i], 0, mItemModelMatrix[i], 0);
            System.arraycopy(mTempMatrix, 0, mItemModelMatrix[i], 0, 16);
            Matrix.multiplyMM(mItemModelViewMatrix[i], 0, mViewMatrix, 0, mItemModelMatrix[i], 0);
            normalMatrix(mItemNormalMatrix[i], 0, mItemModelViewMatrix[i], 0);
        }

        // CALC mBubbleCore MATRIX
        Matrix.setIdentityM(mBubbleCoreModelMatrix, 0);
        Matrix.multiplyMM(mTempMatrix, 0, mBubbleCoreRotationMatrix, 0, mBubbleCoreModelMatrix, 0);
        System.arraycopy(mTempMatrix, 0, mBubbleCoreModelMatrix, 0, 16);
        Matrix.multiplyMM(mTempMatrix, 0, mBubbleCoreTranslationMatrix, 0, mBubbleCoreModelMatrix, 0);
        System.arraycopy(mTempMatrix, 0, mBubbleCoreModelMatrix, 0, 16);
        Matrix.multiplyMM(mBubbleCoreModelViewMatrix, 0, mViewMatrix, 0, mBubbleCoreModelMatrix, 0);
        normalMatrix(mBubbleCoreNormalMatrix, 0, mBubbleCoreModelViewMatrix, 0);

        // CALC MAP MODELMATRIX
        Matrix.setIdentityM(mMapModelMatrix, 0);
        Matrix.multiplyMM(mTempMatrix, 0, mMapRotationMatrix, 0, mMapModelMatrix, 0);
        System.arraycopy(mTempMatrix, 0, mMapModelMatrix, 0, 16);
        Matrix.multiplyMM(mTempMatrix, 0, mMapTranslationMatrix, 0, mMapModelMatrix, 0);
        System.arraycopy(mTempMatrix, 0, mMapModelMatrix, 0, 16);
        Matrix.multiplyMM(mMapModelViewMatrix, 0, mViewMatrix, 0, mMapModelMatrix, 0);
        normalMatrix(mMapNormalMatrix, 0, mMapModelViewMatrix, 0);

        // CALC LAVA MODELMATRIX
        Matrix.setIdentityM(mLavaModelMatrix, 0);
        Matrix.multiplyMM(mTempMatrix, 0, mLavaRotationMatrix, 0, mLavaModelMatrix, 0);
        System.arraycopy(mTempMatrix, 0, mLavaModelMatrix, 0, 16);
        Matrix.multiplyMM(mTempMatrix, 0, mLavaTranslationMatrix, 0, mLavaModelMatrix, 0);
        System.arraycopy(mTempMatrix, 0, mLavaModelMatrix, 0, 16);
        Matrix.multiplyMM(mLavaModelViewMatrix, 0, mViewMatrix, 0, mLavaModelMatrix, 0);
        normalMatrix(mLavaNormalMatrix, 0, mLavaModelViewMatrix, 0);

        // CALC SEA MODELMATRIX
        Matrix.setIdentityM(mSeaModelMatrix, 0);
        Matrix.multiplyMM(mTempMatrix, 0, mSeaRotationMatrix, 0, mSeaModelMatrix, 0);
        System.arraycopy(mTempMatrix, 0, mSeaModelMatrix, 0, 16);
        Matrix.multiplyMM(mTempMatrix, 0, mSeaTranslationMatrix, 0, mSeaModelMatrix, 0);
        System.arraycopy(mTempMatrix, 0, mSeaModelMatrix, 0, 16);
        Matrix.multiplyMM(mSeaModelViewMatrix, 0, mViewMatrix, 0, mSeaModelMatrix, 0);
        normalMatrix(mSeaNormalMatrix, 0, mSeaModelViewMatrix, 0);

        // CALC SKYBOX MODELMATRIX
        Matrix.setIdentityM(mSkyboxModelMatrix, 0);
        Matrix.multiplyMM(mTempMatrix, 0, mSkyboxRotationMatrix, 0, mSkyboxModelMatrix, 0);
        System.arraycopy(mTempMatrix, 0, mSkyboxModelMatrix, 0, 16);
        Matrix.multiplyMM(mTempMatrix, 0, mSkyboxTranslationMatrix, 0, mSkyboxModelMatrix, 0);
        System.arraycopy(mTempMatrix, 0, mSkyboxModelMatrix, 0, 16);
        Matrix.multiplyMM(mSkyboxModelViewMatrix, 0, mViewMatrix, 0, mSkyboxModelMatrix, 0);
        normalMatrix(mSkyboxNormalMatrix, 0, mSkyboxModelViewMatrix, 0);

        checkMove();
        mPhysicalWorld.applyForce();
        float updatedVertices[] = GeomOperator.genVertices(mPhysicalWorld.getParticles());
        mBubble.setVertices(updatedVertices);
        //FIXME SG (UPDATE NORMALS OF SPHERE)

        //FIXME SG TESTING...
        // DRAW
        // ... gl_depth_test (depth test)
        GLES20.glEnable(GLES20.GL_DEPTH_TEST);

        // ... gl_cull_face (culling)
        GLES20.glEnable(GLES20.GL_CULL_FACE);
        mSkyBox.draw(mProjMatrix, mSkyboxModelViewMatrix, mSkyboxNormalMatrix, mLight, mLight2);

        // MAP - NOT TRANSPARENT
        if ((!enableHintFlag) || (!mapBlendFlag)) {
            mMap.draw(mProjMatrix, mMapModelViewMatrix, mMapNormalMatrix, mMapModelMatrix, mLight, mLight2);
        }

        mLava.draw(mProjMatrix, mLavaModelViewMatrix, mLavaNormalMatrix, mLight, mLight2);

        mBubbleCore.updateTraceVertices();
        // CHECK COLLISION BETWEEN BUBBLE AND ITEMS
        mBubbleCore.itemCollisionDetect();
        //FIXME SG TEST OUT (IT CAUSES ERRORS)
//        mBubbleCore.drawTrace(mProjMatrix, mBubbleCoreModelViewMatrix, mBubbleCoreNormalMatrix,
//                mLight, mLight2);
        for (int i = 0; i < GameEnv.getInstance().numOfTotalItems; i++) {
            // DRAW ONLY UN-HITTED ITEM
            if (!mItems.get(i).isHitted) {
                mItems.get(i).draw(mProjMatrix, mItemModelViewMatrix[i], mItemNormalMatrix[i], mLight, mLight2);
            }
        }

        // ... gl_blend (alpha blending)
        GLES20.glEnable(GLES20.GL_BLEND);
        GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);

        // MAP - TRANSPARENT
        if (enableHintFlag && mapBlendFlag) {
            mMap.draw(mProjMatrix, mMapModelViewMatrix, mMapNormalMatrix, mMapModelMatrix, mLight, mLight2);
        }

        mSea.draw(mProjMatrix, mSeaModelViewMatrix, mSeaNormalMatrix, mLight, mLight2, curTime);

        mBubble.draw(mProjMatrix, mBubbleModelViewMatrix, mBubbleModelMatrix,
                mViewMatrix, mBubbleNormalMatrix, mLight, mLight2,
                mCamera, mSkyBox.getCubeTex());
        // FIXME CAN'T SEE SEA
        GLES20.glDisable(GLES20.GL_BLEND);
    }

    @Override
    public void onSurfaceChanged(GL10 unused, int width, int height) {
        GLES20.glViewport(0, 0, width, height);

        // Create a new perspective projection matrix. The height will stay the same
        // while the width will vary as per aspect ratio.
        final float ratio = (float) width / height;
        final float left = -ratio;
        final float right = ratio;
        final float bottom = -1.0f;
        final float top = 1.0f;
        final float near = GameEnv.getInstance().minViewDist;
        final float far = GameEnv.getInstance().maxViewDist;

        Matrix.frustumM(mProjMatrix, 0, left, right, bottom, top, near, far);
    }

    private void normalMatrix(float[] dst, int dstOffset, float[] src, int srcOffset) {
        Matrix.invertM(dst, dstOffset, src, srcOffset);
        dst[12] = 0;
        dst[13] = 0;
        dst[14] = 0;

        float[] temp = Arrays.copyOf(dst, 16);
        Matrix.transposeM(dst, dstOffset, temp, 0);
    }

    private void resetViewMatrix() {
        // Position the eye behind the origin.
        final float eyeX = 0.0f;
        final float eyeY = 0.0f;
        final float eyeZ = 4f;

        mCamera[0] = eyeX;
        mCamera[1] = eyeY;
        mCamera[2] = eyeZ;
        // We are looking toward the distance
        final float lookX = 0.0f;
        final float lookY = 0.0f;
        final float lookZ = -1.0f;

        // Set our up vector. This is where our head would be pointing were we holding the camera.
        final float upX = 0.0f;
        final float upY = 1.0f;
        final float upZ = 0.0f;

        // Set the view matrix. This matrix can be said to represent the camera position.
        // NOTE: In OpenGL 1, a ModelView matrix is used, which is a combination of a model and
        // view matrix. In OpenGL 2, we can keep track of these matrices separately if we choose.
        Matrix.setLookAtM(mViewMatrix, 0, eyeX, eyeY, eyeZ, lookX, lookY, lookZ, upX, upY, upZ);
    }

    // Utility method for compiling a OpenGL shader.
    public static int loadShader(int type, String shaderCode) {

        // create a vertex shader type (GLES20.GL_VERTEX_SHADER)
        // or a fragment shader type (GLES20.GL_FRAGMENT_SHADER)
        int shader = GLES20.glCreateShader(type);

        // add the source code to the shader and compile it
        GLES20.glShaderSource(shader, shaderCode);
        GLES20.glCompileShader(shader);

        return shader;
    }

    public static int loadShader(int type, InputStream shaderFile) {
        String shaderCode = null;
        try {
            shaderCode = IOUtils.toString(shaderFile, Charset.defaultCharset());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return loadShader(type, shaderCode);
    }

    public static int loadShaderFromFile(int type, String fileName) {
        try {
            return loadShader(type, MainActivity.context.getAssets().open(fileName));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public static Bitmap loadImage(String fileName) {
        try {
            Bitmap tmp = BitmapFactory.decodeStream(MainActivity.context.getAssets().open(fileName));
            android.graphics.Matrix matrix = new android.graphics.Matrix();
            matrix.preScale(1.0f, -1.0f);
            Bitmap image = Bitmap.createBitmap(tmp, 0, 0, tmp.getWidth(), tmp.getHeight(), matrix, true);
            tmp.recycle();
            return image;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Utility method for debugging OpenGL calls. Provide the name of the call
     * just after making it:
     * <p>
     * <pre>
     * mColorHandle = GLES20.glGetUniformLocation(mProgram, "vColor");
     * MyGLRenderer.checkGlError("glGetUniformLocation");</pre>
     * <p>
     * If the operation is not successful, the check throws an error.
     *
     * @param glOperation - Name of the OpenGL call to check.
     */
    public static void checkGlError(String glOperation) {
        int error;
        while ((error = GLES20.glGetError()) != GLES20.GL_NO_ERROR) {
            Log.e(TAG, glOperation + ": glError " + error);
            throw new RuntimeException(glOperation + ": glError " + error);
        }
    }

    // MAKING CAMERA FOLLOW BUBBLE
    public void updateView() {
        float[] eye = new float[]{
                mBubbleTranslationMatrix[12],
                mBubbleTranslationMatrix[13],
                mBubbleTranslationMatrix[14]};

        float[] translation = new float[]{0, 0, -1, 0};
        float[] change_translation = new float[4];
        Matrix.multiplyMV(change_translation, 0, mViewRotationMatrix, 0, translation, 0);
        for (int i = 0; i < 3; i++) {
            change_translation[i] *= GameEnv.getInstance().distOfBubbleAndCamera;
            eye[i] += change_translation[i];
        }

        float[] look = new float[]{
                mBubbleTranslationMatrix[12],
                mBubbleTranslationMatrix[13],
                mBubbleTranslationMatrix[14]};

        float[] up = new float[4];
        float[] temp = new float[]{0, 1, 0, 0};
        Matrix.multiplyMV(up, 0, mViewRotationMatrix, 0, temp, 0);
//        up = new float[]{0,1,0,0};
        Matrix.setLookAtM(mViewMatrix, 0,
                eye[0], eye[1], eye[2],
                look[0], look[1], look[2],
                up[0], up[1], up[2]);

        //UPDATE WORLD AND VERTICES OF SPHERE
        if (Env.getInstance().micStatus == 1) {
            mBlower.setBlowingDirByVector(new float[]{mBubbleTranslationMatrix[12] - eye[0],
                    mBubbleTranslationMatrix[13] - eye[1],
                    mBubbleTranslationMatrix[14] - eye[2]});
        }
        System.arraycopy(MatOperator.matLinear(mViewRotationMatrix), 0, mViewRotationMatrix, 0, 16);
    }

    void checkMove() {
        mBubbleCore.getCollision().move(mBubbleModelMatrix);
        mBubbleCore.getCollision().scaleRadius(GameEnv.getScaleOfBubble());

        float bubbleX = mBubbleTranslationMatrix[12];
        float bubbleZ = mBubbleTranslationMatrix[14];
        float bubbleR = mBubbleCore.getCollision().getRadius();
        TriangleCollision[] collisions = mMap.getCollisions();

        int dimX = (int) (mapSizeX / mapUnitLength);
        int dimZ = (int) (mapSizeZ / mapUnitLength);

        for (int i = (int) Math.max((bubbleX - bubbleR - mMapTranslationMatrix[12]) / mapUnitLength, 0); i < Math.min((bubbleX + bubbleR - mMapTranslationMatrix[12]) / mapUnitLength, dimX); i++) {
            for (int j = (int) Math.max((bubbleZ - bubbleR - mMapTranslationMatrix[14]) / mapUnitLength, 0); j < Math.min((bubbleZ + bubbleR - mMapTranslationMatrix[14]) / mapUnitLength, dimZ); j++) {
                TriangleCollision collision = collisions[i * dimZ * 2 + j * 2];
                collision.move(mMapModelMatrix);
                if (Intersect.intersect(mBubbleCore.getCollision(), collision)) {
                    System.out.println("Map Collide!");
                    GameEnv.getInstance().collisionFlag = 1;
                    return;
                }
                collision = collisions[i * dimZ * 2 + j * 2 + 1];
                collision.move(mMapModelMatrix);
                if (Intersect.intersect(mBubbleCore.getCollision(), collision)) {
                    System.out.println("Map Collide!");
                    GameEnv.getInstance().collisionFlag = 1;
                    return;
                }
            }
        }
        TriangleCollision[] seaCollsiions = mSea.getCollision();
        for (int i=0; i<2; i++){
            seaCollsiions[i].move(mSeaModelMatrix);
            if(Intersect.intersect(mBubbleCore.getCollision(), seaCollsiions[i])){
                System.out.println("Sea Collide!");
                GameEnv.getInstance().collisionFlag = 1;
                return;
            }
        }
    }
    public void setActivity(MainActivity activity){
        this.activity = activity;
    }
}