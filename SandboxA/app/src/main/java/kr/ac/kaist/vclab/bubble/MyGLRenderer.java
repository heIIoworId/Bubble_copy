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
import kr.ac.kaist.vclab.bubble.models.BubbleSphere;
import kr.ac.kaist.vclab.bubble.models.Cube;
import kr.ac.kaist.vclab.bubble.models.Sphere;
import kr.ac.kaist.vclab.bubble.models.Square;
import kr.ac.kaist.vclab.bubble.physics.Blower;
import kr.ac.kaist.vclab.bubble.physics.Particle;
import kr.ac.kaist.vclab.bubble.physics.Spring;
import kr.ac.kaist.vclab.bubble.physics.World;
import kr.ac.kaist.vclab.bubble.utils.GeomOperator;

/**
 * Created by sjjeon on 16. 9. 20.
 */

public class MyGLRenderer implements GLSurfaceView.Renderer {

    private static final String TAG = "MyGLRenderer";

    // DECLARE MODELS
    private Cube mCube;
    private Sphere mSphere;
    private Square mSquare;
    private BubbleSphere mBubble;

    // DECLARE PHYSICAL ENTITIES
    private World mWorld;
    private ArrayList<Particle> mParticles;
    private ArrayList<Spring> mSprings;
    private Blower mBlower;
    private Particle mBubbleCore;

    //DECLARE LIGHTS
    private float[] mLight = new float[3];
    private float[] mLight2 = new float[3];

    // VIEW MATRIX
    public float [] mViewRotationMatrix = new float[16];
    public float [] mViewTranslationMatrix = new float[16];
    private float[] mViewMatrix = new float[16];

    // MATRICES FOR mCube
    public float [] mCubeRotationMatrix = new float[16];
    public static float [] mCubeTranslationMatrix = new float[16];
    private float[] mCubeModelMatrix = new float[16];
    private float[] mCubeModelViewMatrix = new float[16];
    private float[] mCubeNormalMatrix = new float[16];

    // MATRICES FOR mSphere
    public float [] mSphereRotationMatrix = new float[16];
    public float [] mSphereTranslationMatrix = new float[16];
    private float[] mSphereModelMatrix = new float[16];
    private float[] mSphereModelViewMatrix = new float[16];
    private float[] mSphereNormalMatrix = new float[16];

    // MATRICES FOR mBubble
    public float [] mBubbleRotationMatrix = new float[16];
    public float [] mBubbleTranslationMatrix = new float[16];
    private float[] mBubbleModelMatrix = new float[16];
    private float[] mBubbleModelViewMatrix = new float[16];
    private float[] mBubbleNormalMatrix = new float[16];

    // MATRICES FOR mSquare
    private float[] mSquareModelMatrix = new float[16];
    private float[] mSquareModelViewMatrix = new float[16];
    private float[] mSquareNormalMatrix = new float[16];

    // OTHER MATRICES
    private float[] mTempMatrix = new float[16];
    private float[] mProjMatrix = new float[16];

    @Override
    // CALLED WHEN SURFACE IS CREATED AT FIRST.
    public void onSurfaceCreated(GL10 unused, EGLConfig config) {

        // Set the background frame color
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
        GLES20.glEnable(GLES20.GL_CULL_FACE);
        GLES20.glEnable(GLES20.GL_DEPTH_TEST);

        // INITIALIZE MODELS
        mSquare = new Square();
        mSquare.color = new float[] {0.1f, 0.95f, 0.1f};
        mCube = new Cube();
        mCube.color = new float[] {0.2f, 0.7f, 0.9f};
        mSphere = new Sphere();
        mSphere.color = new float[] {0.7f, 0.7f, 0.7f};
        // FIXME PARAM OF BUBBLE
        float radius = 4f;
        int level = 3;
        mBubble = new BubbleSphere(radius, level);
        mBubble.color = new float[] {0.3f, 0.8f, 0.9f};

        //INITIALIZE WORLD
//        mParticles = GeomOperator.genParticles(mSphere.getVertices());
        mParticles = GeomOperator.genParticles(mBubble.getVertices());
        mSprings = GeomOperator.genSprings(mParticles);
        mBubbleCore = new Particle(new float[]{0f, 0f, 0f});
        // FIXME BLOWER OUT
//        mBlower = new Blower();
//        mBlower.setBubbleCore(mBubbleCore);

        mWorld = new World();
        mWorld.setParticles(mParticles);
        mWorld.setSprings(mSprings);
        mWorld.setBubbleCore(mBubbleCore);
        // FIXME BLOWER OUT
//        mWorld.setBlower(mBlower);

        // INIT LIGHTS
        mLight = new float[] {2.0f, 3.0f, 14.0f};
        mLight2 = new float[] {-2.0f, -3.0f, -5.0f};

        // INIT VIEW MATRIX
        resetViewMatrix();
        Matrix.setIdentityM(mViewRotationMatrix, 0);
        Matrix.setIdentityM(mViewTranslationMatrix, 0);
        Matrix.translateM(mViewTranslationMatrix, 0, 0, 0, -4f);

        // INIT CUBE MATRIX
        Matrix.setIdentityM(mCubeRotationMatrix, 0);
        Matrix.setIdentityM(mCubeTranslationMatrix, 0);

        // INIT SPHERE MATRIX
        Matrix.setIdentityM(mSphereRotationMatrix, 0);
        Matrix.setIdentityM(mSphereTranslationMatrix, 0);
        Matrix.translateM(mSphereTranslationMatrix, 0, 0, 0, 0);

        // INIT BUBBLE MATRIX
        Matrix.setIdentityM(mBubbleRotationMatrix, 0);
        Matrix.setIdentityM(mBubbleTranslationMatrix, 0);
        Matrix.translateM(mBubbleTranslationMatrix, 0, 0, 0, 0);

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
    //Called at every frame.
    public void onDrawFrame(GL10 unused) {
        // Draw background color
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);

        // FIXME DON'T KNOW WHY IT IS NEEDED
        float scale = 0.4f;

        // CALCULATE VIEW MATRIX
        Matrix.setIdentityM(mViewMatrix, 0);
        Matrix.multiplyMM(mTempMatrix, 0, mViewRotationMatrix, 0, mViewMatrix, 0);
        System.arraycopy(mTempMatrix, 0, mViewMatrix, 0, 16);
        Matrix.multiplyMM(mTempMatrix, 0, mViewTranslationMatrix, 0, mViewMatrix, 0);
        System.arraycopy(mTempMatrix, 0, mViewMatrix, 0, 16);

        // Calculate Square MATRIX
        Matrix.setIdentityM(mSquareModelMatrix, 0);
        Matrix.translateM(mSquareModelMatrix, 0, 0, -1, 0);
        Matrix.rotateM(mSquareModelMatrix, 0, -90, 1f, 0, 0);
        Matrix.scaleM(mSquareModelMatrix, 0, 2f, 2f, 2f);
        Matrix.multiplyMM(mSquareModelViewMatrix, 0, mViewMatrix, 0, mSquareModelMatrix, 0);
        normalMatrix(mSquareNormalMatrix, 0, mSquareModelViewMatrix, 0);

        // Calculate Cube MATRIX
        Matrix.setIdentityM(mCubeModelMatrix, 0);
        Matrix.multiplyMM(mTempMatrix, 0, mCubeRotationMatrix, 0, mCubeModelMatrix, 0);
        System.arraycopy(mTempMatrix, 0, mCubeModelMatrix, 0, 16);
        Matrix.multiplyMM(mTempMatrix, 0, mCubeTranslationMatrix, 0, mCubeModelMatrix, 0);
        System.arraycopy(mTempMatrix, 0, mCubeModelMatrix, 0, 16);
        Matrix.scaleM(mCubeModelMatrix, 0, scale, scale, scale);
        Matrix.multiplyMM(mCubeModelViewMatrix, 0, mViewMatrix, 0, mCubeModelMatrix, 0);
        normalMatrix(mCubeNormalMatrix, 0, mCubeModelViewMatrix, 0);

        // CALCULATE SPHERE MATRIX
        Matrix.setIdentityM(mSphereModelMatrix, 0);
        Matrix.multiplyMM(mTempMatrix, 0, mSphereRotationMatrix, 0, mSphereModelMatrix, 0);
        System.arraycopy(mTempMatrix, 0, mSphereModelMatrix, 0, 16);
        Matrix.multiplyMM(mTempMatrix, 0, mSphereTranslationMatrix, 0, mSphereModelMatrix, 0);
        System.arraycopy(mTempMatrix, 0, mSphereModelMatrix, 0, 16);
        Matrix.scaleM(mSphereModelMatrix, 0, scale, scale, scale);
        Matrix.multiplyMM(mSphereModelViewMatrix, 0, mViewMatrix, 0, mSphereModelMatrix, 0);
        normalMatrix(mSphereNormalMatrix, 0, mSphereModelViewMatrix, 0);

        // CALCULATE BUBBLE MATRIX
        Matrix.setIdentityM(mBubbleTranslationMatrix, 0);
        float curLocation[] = mBubbleCore.getLocation();
        Matrix.translateM(
                mBubbleTranslationMatrix, 0, curLocation[0], curLocation[1], curLocation[2]);

        Matrix.setIdentityM(mBubbleModelMatrix, 0);
        Matrix.multiplyMM(mTempMatrix, 0, mBubbleRotationMatrix, 0, mBubbleModelMatrix, 0);
        System.arraycopy(mTempMatrix, 0, mBubbleModelMatrix, 0, 16);
        Matrix.multiplyMM(mTempMatrix, 0, mBubbleTranslationMatrix, 0, mBubbleModelMatrix, 0);
        System.arraycopy(mTempMatrix, 0, mBubbleModelMatrix, 0, 16);
        Matrix.scaleM(mBubbleModelMatrix, 0, scale, scale, scale);
        Matrix.multiplyMM(mBubbleModelViewMatrix, 0, mViewMatrix, 0, mBubbleModelMatrix, 0);
        normalMatrix(mBubbleNormalMatrix, 0, mBubbleModelViewMatrix, 0);

        //UPDATE WORLD AND VERTICES OF SPHERE
        mBlower.setBlowingDir(mViewRotationMatrix);
        mWorld.applyForce();
        float updatedVertices[] = GeomOperator.genVertices(mWorld.getParticles());
        mBubble.setVertices(updatedVertices);
        //FIXME UPDATE NORMALS OF SPHERE

        //DRAW MODELS
        mSquare.draw(mProjMatrix, mSquareModelViewMatrix, mSquareNormalMatrix, mLight, mLight2);
//        mCube.draw(mProjMatrix, mCubeModelViewMatrix, mCubeNormalMatrix, mLight, mLight2);
//        mSphere.draw(mProjMatrix, mSphereModelViewMatrix, mSphereNormalMatrix, mLight, mLight2);
        mBubble.draw(mProjMatrix, mBubbleModelViewMatrix, mBubbleNormalMatrix, mLight, mLight2);
    }


    @Override
    // Adjust the viewport based on geometry changes,
    // such as screen rotation
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
        final float far = 10.0f;

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
    public static int loadShader(int type, String shaderCode){

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
     *
     * <pre>
     * mColorHandle = GLES20.glGetUniformLocation(mProgram, "vColor");
     * MyGLRenderer.checkGlError("glGetUniformLocation");</pre>
     *
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

}