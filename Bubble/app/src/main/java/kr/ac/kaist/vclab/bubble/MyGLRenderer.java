package kr.ac.kaist.vclab.bubble;

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

import kr.ac.kaist.vclab.bubble.activities.MainActivity;
import kr.ac.kaist.vclab.bubble.events.SoundHandler;
import kr.ac.kaist.vclab.bubble.models.Cube;
import kr.ac.kaist.vclab.bubble.models.MapCube;
import kr.ac.kaist.vclab.bubble.models.SeaRectangle;
import kr.ac.kaist.vclab.bubble.models.SkyBox;
import kr.ac.kaist.vclab.bubble.models.Sphere;
import kr.ac.kaist.vclab.bubble.models.Square;
import kr.ac.kaist.vclab.bubble.physics.Particle;
import kr.ac.kaist.vclab.bubble.physics.Spring;
import kr.ac.kaist.vclab.bubble.physics.World;
import kr.ac.kaist.vclab.bubble.utils.GeomOperator;

/**
 * Created by sjjeon on 16. 9. 20.
 */

public class MyGLRenderer implements GLSurfaceView.Renderer {
    // TAG
    private static final String TAG = "MyGLRenderer";

    // PRESETS
    private float mapSizeX = 30.0f; // X-size (widthX) of map
    private float mapSizeY = 3.0f; // Y-size (thickness) of map
    private float mapSizeZ = 30.0f; // Z-size (widthZ) of map
    private float mapUnitLength = 0.5f; // length of the side of a triangle
    private float mapMaxHeight = 12.0f; // maximum height
    private float mapMinHeight = -2.0f; // minimum height (>= -mapSizeY)
    private float mapComplexity = 3.6f; // complexity (bigger complexity -> more & steeper mountains)

    // DECLARE MODELS
    public Cube mCube;
    public Sphere mSphere;
    public Square mSquare;
    public MapCube mMap;
    public SeaRectangle mSea;
    public SkyBox mSkyBox;

    // DECLARE OTHERS
    private World mWorld;
    private SoundHandler soundHandler;

    //DECLARE LIGHTS
    private float[] mLight = new float[3];
    private float[] mLight2 = new float[3];

    // DECLARE MATRICES FOR 3D GRAPHICS
    private float[] mViewMatrix = new float[16];

    public float[] mViewRotationMatrix = new float[16];
    public float[] mCubeRotationMatrix = new float[16];
    public float[] mSphereRotationMatrix = new float[16];
    public float[] mMapRotationMatrix = new float[16];
    public float[] mSeaRotationMatrix = new float[16];
    public float[] mSkyboxRotationMatrix = new float[16];

    public float[] mViewTranslationMatrix = new float[16];
    public float[] mCubeTranslationMatrix = new float[16];
    public float[] mSphereTranslationMatrix = new float[16];
    public float[] mMapTranslationMatrix = new float[16];
    public float[] mSeaTranslationMatrix = new float[16];
    public float[] mSkyboxTranslationMatrix = new float[16];

    private float[] mCubeModelMatrix = new float[16];
    private float[] mSphereModelMatrix = new float[16];
    private float[] mSquareModelMatrix = new float[16];
    private float[] mMapModelMatrix = new float[16];
    private float[] mSeaModelMatrix = new float[16];
    private float[] mSkyboxModelMatrix = new float[16];

    private float[] mCubeModelViewMatrix = new float[16];
    private float[] mSphereModelViewMatrix = new float[16];
    private float[] mSquareModelViewMatrix = new float[16];
    private float[] mMapModelViewMatrix = new float[16];
    private float[] mSeaModelViewMatrix = new float[16];
    private float[] mSkyboxModelViewMatrix = new float[16];

    private float[] mCubeNormalMatrix = new float[16];
    private float[] mSphereNormalMatrix = new float[16];
    private float[] mSquareNormalMatrix = new float[16];
    private float[] mMapNormalMatrix = new float[16];
    private float[] mSeaNormalMatrix = new float[16];
    private float[] mSkyboxNormalMatrix = new float[16];

    private float[] mProjMatrix = new float[16];

    private float[] mTempMatrix = new float[16];

    float scale = 0.4f;
    float[] mCamera = new float[3];

    private float bubbleScale = 0.03f;
    private float[] bubbleStart = new float[]{0,0,0};
    private float cameraDistance = 1.5f;

    @Override
    // CALLED WHEN SURFACE IS CREATED AT FIRST.
    public void onSurfaceCreated(GL10 unused, EGLConfig config) {

        //FIXME DISABLE THIS WHEN TRYING TO RUN ON DESKTOP.
//        soundHandler = new SoundHandler();
//        soundHandler.start();

        // SET BACKGROUND COLOR
        GLES20.glClearColor(0.7f, 0.8f, 0.9f, 1.0f); // skyblue

        // INITIALIZE MODELS
        mSquare = new Square();
        mSquare.color = new float[]{0.1f, 0.95f, 0.1f};

        mCube = new Cube();
        mCube.getCollision().scaleAxes(scale);
        mCube.color = new float[]{0.2f, 0.7f, 0.9f};

        mSphere = new Sphere();
        mSphere.getCollision().scaleRadius(scale);
        mSphere.color = new float[]{0.7f, 0.7f, 0.7f};

        // ... map
        mMap = new MapCube(
                mapSizeX, mapSizeY, mapSizeZ,
                mapUnitLength,
                mapMaxHeight, mapMinHeight,
                mapComplexity,
                1.0f, true
        );

        // ... sea (same x, z size as map)
        mSea = new SeaRectangle(mapSizeX, mapSizeZ);
        mSkyBox = new SkyBox();

        //INITIALIZE WORLD
        mWorld = new World();
        ArrayList<Particle> particles = GeomOperator.genParticles(mSphere.getVertices());
        mWorld.setParticles(particles);
        ArrayList<Spring> springs = GeomOperator.genSprings(particles);
        mWorld.setSprings(springs);

        // INITIALIZE LIGHTS
        mLight = new float[]{2.0f, 3.0f, 14.0f};
        mLight2 = new float[]{-2.0f, -3.0f, -5.0f};

        // INITIALIZE MATRICES
        resetViewMatrix();

        // ... view
        Matrix.setIdentityM(mViewRotationMatrix, 0);
        Matrix.setIdentityM(mViewTranslationMatrix, 0);
        Matrix.translateM(mViewTranslationMatrix, 0, 0, 0, -14.0f);

        // ... cube
        Matrix.setIdentityM(mCubeRotationMatrix, 0);
        Matrix.setIdentityM(mCubeTranslationMatrix, 0);

        // ... sphere (spring-mass)
        Matrix.setIdentityM(mSphereRotationMatrix, 0);
        Matrix.setIdentityM(mSphereTranslationMatrix, 0);
        Matrix.translateM(mSphereTranslationMatrix, 0, bubbleStart[0], bubbleStart[1], bubbleStart[2]);

        // ... map
        Matrix.setIdentityM(mMapRotationMatrix, 0);
        Matrix.setIdentityM(mMapTranslationMatrix, 0);
        Matrix.translateM(mMapTranslationMatrix, 0, -10.0f, -5.0f, -10.0f);

        // ... sea
        Matrix.setIdentityM(mSeaRotationMatrix, 0);
        Matrix.setIdentityM(mSeaTranslationMatrix, 0);
        Matrix.translateM(mSeaTranslationMatrix, 0, -10.0f, -4.0f, -10.0f);

        // ... skybox
        Matrix.setIdentityM(mSkyboxRotationMatrix, 0);
        Matrix.setIdentityM(mSkyboxTranslationMatrix, 0);

//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                while(true){
//                    int vol = (int) soundHandler.getAmplitude();
//                    System.out.println("vol: " + vol);
//                    Message msg = mHandler.obtainMessage();
//                    msg.what = 0;
//                    msg.arg1 = vol;
//                    mHandler.sendMessage(msg);
//                    try {
//                        Thread.sleep(800);
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
//                }
//            }
//        }).start();
    }

    @Override
    public void onDrawFrame(GL10 unused) {

        // Move bubble according to soundHandler
        // FIXME WHEN SOUND HANDLER IS ACTIVATED
//        int vol = (int) soundHandler.getAmplitude();
//        System.out.println("vol: " + vol);
//        float goUp = ((float)vol)/200000f;
//        System.out.println("vol/100000: " + goUp);
//        Matrix.translateM(mCubeTranslationMatrix, 0, 0, goUp, 0);

        // CLEAR COLOR & DEPTH BUFFERS
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);

        // CALCULATE VIEWMATRIX
        Matrix.setIdentityM(mViewMatrix, 0);
        Matrix.multiplyMM(mTempMatrix, 0, mViewRotationMatrix, 0, mViewMatrix, 0);
        System.arraycopy(mTempMatrix, 0, mViewMatrix, 0, 16);
        Matrix.multiplyMM(mTempMatrix, 0, mViewTranslationMatrix, 0, mViewMatrix, 0);
        System.arraycopy(mTempMatrix, 0, mViewMatrix, 0, 16);

        // CALCULATE SQUARE MODELMATRIX
        Matrix.setIdentityM(mSquareModelMatrix, 0);
        Matrix.translateM(mSquareModelMatrix, 0, 0, -1, 0);
        Matrix.rotateM(mSquareModelMatrix, 0, -90, 1f, 0, 0);
        Matrix.scaleM(mSquareModelMatrix, 0, 2f, 2f, 2f);

        // CALCULATE CUBE MODELMATRIX
        Matrix.setIdentityM(mCubeModelMatrix, 0);
        Matrix.multiplyMM(mTempMatrix, 0, mCubeRotationMatrix, 0, mCubeModelMatrix, 0);
        System.arraycopy(mTempMatrix, 0, mCubeModelMatrix, 0, 16);
        Matrix.multiplyMM(mTempMatrix, 0, mCubeTranslationMatrix, 0, mCubeModelMatrix, 0);
        System.arraycopy(mTempMatrix, 0, mCubeModelMatrix, 0, 16);

        Matrix.scaleM(mCubeModelMatrix, 0, scale, scale, scale);

        // CALCULATE SPHERE MODELMATRIX
        updateView();

        Matrix.setIdentityM(mSphereModelMatrix, 0);

        Matrix.multiplyMM(mTempMatrix, 0, mSphereRotationMatrix, 0, mSphereModelMatrix, 0);
        System.arraycopy(mTempMatrix, 0, mSphereModelMatrix, 0, 16);

        Matrix.multiplyMM(mTempMatrix, 0, mSphereTranslationMatrix, 0, mSphereModelMatrix, 0);
        System.arraycopy(mTempMatrix, 0, mSphereModelMatrix, 0, 16);
        Matrix.scaleM(mSphereModelMatrix, 0, bubbleScale, bubbleScale, bubbleScale);

        // CALCULATE MAP MODELMATRIX
        Matrix.setIdentityM(mMapModelMatrix, 0);
        Matrix.multiplyMM(mTempMatrix, 0, mMapRotationMatrix, 0, mMapModelMatrix, 0);
        System.arraycopy(mTempMatrix, 0, mMapModelMatrix, 0, 16);
        Matrix.multiplyMM(mTempMatrix, 0, mMapTranslationMatrix, 0, mMapModelMatrix, 0);
        System.arraycopy(mTempMatrix, 0, mMapModelMatrix, 0, 16);

        // CALCULATE SEA MODELMATRIX
        Matrix.setIdentityM(mSeaModelMatrix, 0);
        Matrix.multiplyMM(mTempMatrix, 0, mSeaRotationMatrix, 0, mSeaModelMatrix, 0);
        System.arraycopy(mTempMatrix, 0, mSeaModelMatrix, 0, 16);
        Matrix.multiplyMM(mTempMatrix, 0, mSeaTranslationMatrix, 0, mSeaModelMatrix, 0);
        System.arraycopy(mTempMatrix, 0, mSeaModelMatrix, 0, 16);

        // CALCULATE SKYBOX MODELMATRIX
        Matrix.setIdentityM(mSkyboxModelMatrix, 0);
        Matrix.multiplyMM(mTempMatrix, 0, mSkyboxRotationMatrix, 0, mSkyboxModelMatrix, 0);
        System.arraycopy(mTempMatrix, 0, mSkyboxModelMatrix, 0, 16);
        Matrix.multiplyMM(mTempMatrix, 0, mSkyboxTranslationMatrix, 0, mSkyboxModelMatrix, 0);
        System.arraycopy(mTempMatrix, 0, mSkyboxModelMatrix, 0, 16);

        Matrix.scaleM(mSkyboxModelMatrix, 0, 30, 30, 30);
        // CALCULATE MODELVIEWMATRIX
        Matrix.multiplyMM(mSquareModelViewMatrix, 0, mViewMatrix, 0, mSquareModelMatrix, 0);
        Matrix.multiplyMM(mCubeModelViewMatrix, 0, mViewMatrix, 0, mCubeModelMatrix, 0);
        Matrix.multiplyMM(mSphereModelViewMatrix, 0, mViewMatrix, 0, mSphereModelMatrix, 0);
        Matrix.multiplyMM(mMapModelViewMatrix, 0, mViewMatrix, 0, mMapModelMatrix, 0);
        Matrix.multiplyMM(mSeaModelViewMatrix, 0, mViewMatrix, 0, mSeaModelMatrix, 0);
        Matrix.multiplyMM(mSkyboxModelViewMatrix, 0, mViewMatrix, 0, mSkyboxModelMatrix, 0);

        // CALCULATE NORMALMATRIX
        normalMatrix(mCubeNormalMatrix, 0, mCubeModelViewMatrix, 0);
        normalMatrix(mSphereNormalMatrix, 0, mSphereModelViewMatrix, 0);
        normalMatrix(mSquareNormalMatrix, 0, mSquareModelViewMatrix, 0);
        normalMatrix(mMapNormalMatrix, 0, mMapModelViewMatrix, 0);
        normalMatrix(mSeaNormalMatrix, 0, mSeaModelViewMatrix, 0);
        normalMatrix(mSkyboxNormalMatrix, 0, mSkyboxModelViewMatrix, 0);

        // UPDATE WORLD AND VERTICES OF SPHERE
        mWorld.update();
        float updatedVertices[] = GeomOperator.genVertices(mWorld.getParticles());
        // System.out.println("updated vertices: "+updatedVertices[11]);
        mSphere.setVertices(updatedVertices);
        //FIXME UPDATE NORMALS OF SPHERE

        // DRAW
        // ... gl_depth_test (depth test)
        GLES20.glEnable(GLES20.GL_DEPTH_TEST);

        // ... gl_cull_face (culling)
        GLES20.glEnable(GLES20.GL_CULL_FACE);
        mSkyBox.draw(mProjMatrix, mSkyboxModelViewMatrix, mSkyboxNormalMatrix, mLight, mLight2);
        //mSquare.draw(mProjMatrix, mSquareModelViewMatrix, mSquareNormalMatrix, mLight, mLight2);
        //mCube.draw(mProjMatrix, mCubeModelViewMatrix, mCubeNormalMatrix, mLight, mLight2);
        mMap.draw(mProjMatrix, mMapModelViewMatrix, mMapNormalMatrix, mLight, mLight2);

        // ... gl_blend (alpha blending)
        GLES20.glEnable(GLES20.GL_BLEND);
        GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);
        mSphere.draw(mProjMatrix, mSphereModelViewMatrix, mSphereModelMatrix, mViewMatrix, mSphereNormalMatrix, mLight, mLight2, mCamera,  mSkyBox.getCubeTex());

        mSea.draw(mProjMatrix, mSeaModelViewMatrix, mSeaNormalMatrix, mLight, mLight2);
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
        final float near = 1f;
        final float far = 80.0f; // it should be bigger than map (and skybox)'s size!

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

    public void updateView(){
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
    }
}
