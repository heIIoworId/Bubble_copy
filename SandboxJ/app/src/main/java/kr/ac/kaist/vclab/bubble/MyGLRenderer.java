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
import java.util.Arrays;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import kr.ac.kaist.vclab.bubble.Collision.Intersect;
import kr.ac.kaist.vclab.bubble.Collision.TriangleCollision;

/**
 * Created by sjjeon on 16. 9. 20.
 */

public class MyGLRenderer implements GLSurfaceView.Renderer {

    private static final String TAG = "MyGLRenderer";


    private float mapSizeX = 90.0f;
    private float mapSizeY = 25.0f;
    private float mapSizeZ = 90.0f;
    private float mapUnitLength = 1.5f;
    private float mapMaxHeight = 15.0f;
    private float mapMinHeight = -11.5f;
    private float mapComplexity = 5.5f;
    private MapCube mMap;

    Cube mCube;
    SkyBox mCube2;
    Sphere mSphere;
    private Square mSquare;

    public float [] mViewRotationMatrix = new float[16];
    public float [] mViewTranslationMatrix = new float[16];

    public float[] mMapRotationMatrix = new float[16];
    public float[] mMapTranslationMatrix = new float[16];

    public float [] mCubeRotationMatrix = new float[16];
    public float [] mCubeTranslationMatrix = new float[16];
    public float [] mCube2RotationMatrix = new float[16];
    public float [] mCube2TranslationMatrix = new float[16];

    public float [] mSphereRotationMatrix = new float[16];
    public float [] mSphereTranslationMatrix = new float[16];

    private float[] mTempMatrix = new float[16];

    private float[] mViewMatrix = new float[16];

    private float[] mCubeModelMatrix = new float[16];
    private float[] mCube2ModelMatrix = new float[16];
    private float[] mSphereModelMatrix = new float[16];
    private float[] mSquareModelMatrix = new float[16];

    private float[] mCubeModelViewMatrix = new float[16];
    private float[] mCube2ModelViewMatrix = new float[16];
    private float[] mSphereModelViewMatrix = new float[16];
    private float[] mSquareModelViewMatrix = new float[16];

    private float[] mCubeNormalMatrix = new float[16];
    private float[] mCube2NormalMatrix = new float[16];
    private float[] mSphereNormalMatrix = new float[16];
    private float[] mSquareNormalMatrix = new float[16];
    private float[] mMapModelMatrix = new float[16];

    private float[] mMapModelViewMatrix = new float[16];

    private float[] mMapNormalMatrix = new float[16];
    private float[] mProjMatrix = new float[16];

    private float[] mLight = new float[3];
    private float[] mLight2 = new float[3];
    //float scale = 0.4f;
    float[] mCamera = new float[3];

    float[] move = new float[16];
    @Override
    public void onSurfaceCreated(GL10 unused, EGLConfig config) {

        // Set the background frame color
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
        /*
        GLES20.glEnable(GLES20.GL_CULL_FACE);
        GLES20.glEnable(GLES20.GL_DEPTH_TEST);
       // GLES20.glEnable(GLES20.GL_POINT_SMOOTH);
   //     GLES20.glDisable(GLES20.GL_DEPTH_TEST);
        GLES20.glEnable(GLES20.GL_BLEND);;
        GLES20.glActiveTexture(GLES20.GL_TEXTURE1);

        //        GLES20.glBlendFunc(GLES20.GL_DST_ALPHA, GLES20.GL_ONE);
 //       GLES20.glBlendFunc(GLES20.GL_ONE, GLES20.GL_ZERO);
//       GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE);
       GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);
*/

        GLES20.glEnable(GLES20.GL_CULL_FACE);
        GLES20.glEnable(GLES20.GL_DEPTH_TEST);
        resetViewMatrix();

        mLight = new float[] {2.0f, 3.0f, 14.0f};
        mLight2 = new float[] {-2.0f, -3.0f, -5.0f};

        // Initialize square
        mSquare = new Square();
        mSquare.color = new float[] {0.1f, 0.95f, 0.1f};
      //  mSquare.getCollision1().scalevectors(2.0f);
      //  mSquare.getCollision2().scalevectors(2.0f);

        // Initialize cube
        mCube = new Cube();
      //  mCube.getCollision().scaleAxes(scale);
        mCube.color = new float[] {0.2f, 0.7f, 0.9f};


        mCube2 = new SkyBox();

     //   mCube2.getCollision().scaleAxes(scale);
        mCube2.color = new float[] {0.2f, 0.7f, 0.9f};
        // Initialize cube
        mSphere = new Sphere();
     //   mSphere.getCollision().scaleRadius(scale);
        mSphere.color = new float[] {0.7f, 0.7f, 0.7f};
        mMap = new MapCube(
                mapSizeX, mapSizeY, mapSizeZ,
                mapUnitLength,
                mapMaxHeight, mapMinHeight,
                mapComplexity,
                1.0f, true
        );

        // Initialize matrix
        Matrix.setIdentityM(mViewRotationMatrix, 0);
        Matrix.setIdentityM(mViewTranslationMatrix, 0);
        Matrix.translateM(mViewTranslationMatrix, 0, 0, 0, -10f);

        Matrix.setIdentityM(mCubeRotationMatrix, 0);
        Matrix.rotateM(mCubeRotationMatrix, 0, 70.0f, 1f,1f,2f);
        Matrix.setIdentityM(mCubeTranslationMatrix, 0);
        Matrix.translateM(mCubeTranslationMatrix, 0, -5, 0, 0);
        Matrix.setIdentityM(mCube2RotationMatrix, 0);
        Matrix.setIdentityM(mCube2TranslationMatrix, 0);

        Matrix.setIdentityM(mMapRotationMatrix, 0);
        Matrix.setIdentityM(mMapTranslationMatrix, 0);
        Matrix.translateM(mMapTranslationMatrix, 0, -mapSizeZ / 2.0f, 0, -mapSizeZ / 2.0f);
        Matrix.setIdentityM(mSphereRotationMatrix, 0);
        Matrix.setIdentityM(mSphereTranslationMatrix, 0);
        Matrix.translateM(mSphereTranslationMatrix, 0, 0, 15, 0);

        Matrix.setIdentityM(move, 0);
    }

    private void normalMatrix(float[] dst, int dstOffset, float[] src, int srcOffset) {
        Matrix.invertM(dst, dstOffset, src, srcOffset);
        dst[12] = 0;
        dst[13] = 0;
        dst[14] = 0;

        float[] temp = Arrays.copyOf(dst, 16);

        Matrix.transposeM(dst, dstOffset, temp, 0);
    }

    @Override
    public void onDrawFrame(GL10 unused) {
        // Draw background color
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);
        //Matrix.translateM(mSphereTranslationMatrix,0 ,0 ,-0.01f,0);
        // Calculate view matrix

        // Calculate Square ModelMatrix
        Matrix.setIdentityM(mSquareModelMatrix, 0);
        Matrix.translateM(mSquareModelMatrix, 0, 0, -5, 0);
        Matrix.rotateM(mSquareModelMatrix, 0, -90, 1f, 0, 0);
        Matrix.scaleM(mSquareModelMatrix, 0, 2f, 2f, 2f);

        // Calculate Cube ModelMatrix
        Matrix.setIdentityM(mCubeModelMatrix, 0);
        Matrix.multiplyMM(mTempMatrix, 0, mCubeRotationMatrix, 0, mCubeModelMatrix, 0);
        System.arraycopy(mTempMatrix, 0, mCubeModelMatrix, 0, 16);
        Matrix.multiplyMM(mTempMatrix, 0, mCubeTranslationMatrix, 0, mCubeModelMatrix, 0);
        System.arraycopy(mTempMatrix, 0, mCubeModelMatrix, 0, 16);

       // Matrix.scaleM(mCubeModelMatrix, 0, scale, scale, scale);


        // calculate map model matrix
        Matrix.setIdentityM(mMapModelMatrix, 0);
        Matrix.multiplyMM(mTempMatrix, 0, mMapRotationMatrix, 0, mMapModelMatrix, 0);
        System.arraycopy(mTempMatrix, 0, mMapModelMatrix, 0, 16);
        Matrix.multiplyMM(mTempMatrix, 0, mMapTranslationMatrix, 0, mMapModelMatrix, 0);
        System.arraycopy(mTempMatrix, 0, mMapModelMatrix, 0, 16);

        Matrix.setIdentityM(mCube2ModelMatrix, 0);
        Matrix.multiplyMM(mTempMatrix, 0, mCube2RotationMatrix, 0, mCube2ModelMatrix, 0);
        System.arraycopy(mTempMatrix, 0, mCube2ModelMatrix, 0, 16);
        Matrix.multiplyMM(mTempMatrix, 0, mCube2TranslationMatrix, 0, mCube2ModelMatrix, 0);
        System.arraycopy(mTempMatrix, 0, mCube2ModelMatrix, 0, 16);

        Matrix.scaleM(mCube2ModelMatrix, 0, 20, 20, 20);

        // Calculate Sphere ModelMatrix
        Matrix.setIdentityM(mSphereModelMatrix, 0);
        Matrix.multiplyMM(mTempMatrix, 0, mSphereRotationMatrix, 0, mSphereModelMatrix, 0);
        System.arraycopy(mTempMatrix, 0, mSphereModelMatrix, 0, 16);
        Matrix.multiplyMM(mTempMatrix, 0, mSphereTranslationMatrix, 0, mSphereModelMatrix, 0);
        System.arraycopy(mTempMatrix, 0, mSphereModelMatrix, 0, 16);
        //Matrix.scaleM(mSphereModelMatrix, 0, scale, scale, scale);

        Matrix.setIdentityM(mViewMatrix, 0);
        Matrix.multiplyMM(mTempMatrix, 0, mViewRotationMatrix, 0, mViewMatrix, 0);
        System.arraycopy(mTempMatrix, 0, mViewMatrix, 0, 16);
        Matrix.multiplyMM(mTempMatrix, 0, mViewTranslationMatrix, 0, mViewMatrix, 0);
        System.arraycopy(mTempMatrix, 0, mViewMatrix, 0, 16);

        // Calculate ModelViewMatrix
        Matrix.multiplyMM(mSquareModelViewMatrix, 0, mViewMatrix, 0, mSquareModelMatrix, 0);
        Matrix.multiplyMM(mCubeModelViewMatrix, 0, mViewMatrix, 0, mCubeModelMatrix, 0);
        Matrix.multiplyMM(mCube2ModelViewMatrix, 0, mViewMatrix, 0, mCube2ModelMatrix, 0);
        Matrix.multiplyMM(mSphereModelViewMatrix, 0, mViewMatrix, 0, mSphereModelMatrix, 0);

        // Calculate NormalMatrix
        normalMatrix(mCubeNormalMatrix, 0, mCubeModelViewMatrix, 0);
        normalMatrix(mCube2NormalMatrix, 0, mCube2ModelViewMatrix, 0);
        normalMatrix(mSphereNormalMatrix, 0, mSphereModelViewMatrix, 0);
        normalMatrix(mSquareNormalMatrix, 0, mSquareModelViewMatrix, 0);

        Matrix.multiplyMM(mMapModelViewMatrix, 0, mViewMatrix, 0, mMapModelMatrix, 0);
        normalMatrix(mMapNormalMatrix, 0, mMapModelViewMatrix, 0);



        checkMove();
        mMap.draw(mProjMatrix, mMapModelViewMatrix, mMapNormalMatrix, mMapModelMatrix, mLight, mLight2);

        // Draw
        mCube2.draw(mProjMatrix, mCube2ModelViewMatrix, mCube2NormalMatrix, mLight, mLight2);
        mCube.draw(mProjMatrix, mCubeModelViewMatrix, mCubeNormalMatrix, mLight, mLight2, mCube2.getCubeTex());
        mSquare.draw(mProjMatrix, mSquareModelViewMatrix, mSquareNormalMatrix, mLight, mLight2);

        mSphere.draw(mProjMatrix, mSphereModelViewMatrix, mSphereModelMatrix, mViewMatrix, mSphereNormalMatrix, mLight, mLight2, mCamera,  mCube2.getCubeTex());


        mSphere.getCollision().move(mSphereModelMatrix);
        mCube.getCollision().move(mCubeModelMatrix);
        mSquare.getCollision1().move(mSquareModelMatrix);
        mSquare.getCollision2().move(mSquareModelMatrix);
    }

    @Override
    public void onSurfaceChanged(GL10 unused, int width, int height) {
        // Adjust the viewport based on geometry changes,
        // such as screen rotation
        GLES20.glViewport(0, 0, width, height);

        // Create a new perspective projection matrix. The height will stay the same
        // while the width will vary as per aspect ratio.
        final float ratio = (float) width / height;
        final float left = -ratio;
        final float right = ratio;
        final float bottom = -1.0f;
        final float top = 1.0f;
        final float near = 1f;
        final float far = 50.0f;

        Matrix.frustumM(mProjMatrix, 0, left, right, bottom, top, near, far);
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

    void checkMove(){
        float[] linear = new float[16];
        float[] translation = new float[16];
        linear = MatOperator.matLinear(move);
        translation = MatOperator.matTranslation(move);
        Matrix.setIdentityM(move,0);

        float[] temp = new float[16];
       /*
       Matrix.multiplyMM(mCubeRotationMatrix, 0 , mCubeRotationMatrix, 0 , linear, 0);
        Matrix.multiplyMM(mCubeTranslationMatrix, 0 , mCubeTranslationMatrix, 0 , translation, 0);
        Matrix.multiplyMM(temp, 0, mCubeTranslationMatrix, 0 , mCubeRotationMatrix, 0);
        */

        float[] temp1 = new float[16];
        float[] inversetemp = new float[16];
        Matrix.invertM(inversetemp,0, mViewRotationMatrix,0);
        Matrix.setIdentityM(temp1, 0);

       /*
        Matrix.multiplyMM(temp1,0, translation,0, inversetemp,0);
        System.arraycopy(temp1, 0, translation, 0 , 16);
        Matrix.multiplyMM(temp1, 0, mViewRotationMatrix, 0, translation, 0);
        System.arraycopy(temp1, 0, translation, 0 , 16);


        Matrix.multiplyMM(temp1,0, linear,0, inversetemp,0);
        System.arraycopy(temp1, 0, linear, 0 , 16);
        Matrix.multiplyMM(temp1, 0, mViewRotationMatrix, 0, linear, 0);
        System.arraycopy(temp1, 0, linear, 0 , 16);

*/
        float[] temp2 = new float[16];
        Matrix.multiplyMM(temp2, 0, mSphereTranslationMatrix, 0, translation, 0);
        float[] temp3 = new float[16];
        Matrix.multiplyMM(temp3, 0, mSphereRotationMatrix, 0, linear, 0);

        Matrix.multiplyMM(temp1, 0, temp2, 0 , temp3, 0);

        mSphere.getCollision().move(temp1);
        if(Intersect.intersect(mSphere.getCollision(), mCube.getCollision())){
            System.out.println("Collllllll!!!!!!!!!");
            Matrix.invertM(linear, 0, linear , 0);
            Matrix.invertM(translation, 0, translation , 0);
        }
        else if(Intersect.intersect(mSphere.getCollision(), mSquare.getCollision1())){
            System.out.println("Col22222lllllll!!!!!!!!!2");
        }
        else if(Intersect.intersect(mSphere.getCollision(), mSquare.getCollision2())){
            System.out.println("Col3333lllllll!!!!!!!!!333");
        }
        else{
            float x = mSphereTranslationMatrix[12];
            float z = mSphereTranslationMatrix[14];
            float r = mSphere.getCollision().GetRadius();
            TriangleCollision[] collisions = mMap.getCollisions();
  //          x = Math.min(x - r, -mapSizeX/2) + mapSizeX/2;
//            z = Math.min(z - r, -mapSizeZ/2) + mapSizeZ/2;
            int dimX = (int) (mapSizeX / mapUnitLength);
            int dimZ = (int) (mapSizeZ / mapUnitLength);
            for(int i=(int)Math.max((x-r + mapSizeX/2)/mapUnitLength, 0); i<Math.min((x+r + mapSizeX/2)/mapUnitLength, dimX); i++) {
                for (int j=(int)Math.max((z-r + mapSizeZ/2)/mapUnitLength, 0); j<Math.min((z+r + mapSizeZ/2)/mapUnitLength, dimZ); j++) {
                    TriangleCollision collision = collisions[i * dimZ * 2 + j * 2];
                    collision.move(mMapModelMatrix);
                    if(Intersect.intersect(mSphere.getCollision(), collision)){
                        return;
                    }
                    collision = collisions[i * dimZ * 2 + j * 2];
                    collision.move(mMapModelMatrix);
                    if(Intersect.intersect(mSphere.getCollision(), collision)){
                        return;
                    }
                }
            }

            System.arraycopy(temp2, 0, mSphereTranslationMatrix,0,16);
            System.arraycopy(temp3, 0, mSphereRotationMatrix,0,16);
        }


    }

}