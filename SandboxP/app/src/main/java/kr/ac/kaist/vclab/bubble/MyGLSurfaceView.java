package kr.ac.kaist.vclab.bubble;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.view.MotionEvent;

/**
 * Created by avantgarde on 2016-11-02.
 */

public class MyGLSurfaceView extends GLSurfaceView {
    public final MyGLRenderer mRenderer;

    private float mPreviousX;
    private float mPreviousY;

    // temporary matrix for calculation
    private float[] mTempMatrix = new float[16];
    private float[] mTemp2Matrix = new float[16];

    public String mode;

    public MyGLSurfaceView(Context context) {
        super(context);

        // OpenGL ES 2.0 context
        setEGLContextClientVersion(2);

        setEGLConfigChooser(8, 8, 8, 8, 16, 0);

        // set the renderer for drawing on the MyGLSurfaceView
        mRenderer = new MyGLRenderer();
        setRenderer(mRenderer);

        // render the view only when there is a change in the drawing data
        setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX(0);
        float y = event.getY(0);
        int count = event.getPointerCount();
        int action = event.getAction();

        // hint mode
        if (mode == "hint") {
            if (action == MotionEvent.ACTION_DOWN) {
                mRenderer.mapBlendFlag = true;
            }
            if (action == MotionEvent.ACTION_UP) {
                mRenderer.mapBlendFlag = false;
            }
        }

        // single touch -> rotate
        if ((action == MotionEvent.ACTION_MOVE) && (count == 1)) {
            float dx = x - mPreviousX;
            float dy = y - mPreviousY;

            Matrix.setIdentityM(mTempMatrix, 0);
            Matrix.rotateM(mTempMatrix, 0, -0.3f * dx, 0, 1, 0);
            Matrix.rotateM(mTempMatrix, 0, 0.3f * dy, 1, 0, 0);

            switch (mode) {
                case "world":
                    Matrix.multiplyMM(mTemp2Matrix, 0, mTempMatrix, 0, mRenderer.mViewRotationMatrix, 0);
                    System.arraycopy(mTemp2Matrix, 0, mRenderer.mViewRotationMatrix, 0, 16);
                    break;
                case "cube":
                    Matrix.multiplyMM(mTemp2Matrix, 0, mTempMatrix, 0, mRenderer.mCubeRotationMatrix, 0);
                    System.arraycopy(mTemp2Matrix, 0, mRenderer.mCubeRotationMatrix, 0, 16);
                    break;
                case "map":
                    Matrix.multiplyMM(mTemp2Matrix, 0, mTempMatrix, 0, mRenderer.mMapRotationMatrix, 0);
                    System.arraycopy(mTemp2Matrix, 0, mRenderer.mMapRotationMatrix, 0, 16);

                    Matrix.multiplyMM(mTemp2Matrix, 0, mTempMatrix, 0, mRenderer.mSeaRotationMatrix, 0);
                    System.arraycopy(mTemp2Matrix, 0, mRenderer.mSeaRotationMatrix, 0, 16);

                    for (int i = 0; i < mRenderer.itemCount; i++) {
                        Matrix.multiplyMM(mTemp2Matrix, 0, mTempMatrix, 0, mRenderer.mItemRotationMatrix[i], 0);
                        System.arraycopy(mTemp2Matrix, 0, mRenderer.mItemRotationMatrix[i], 0, 16);
                        Matrix.multiplyMM(mTemp2Matrix, 0, mTempMatrix, 0, mRenderer.mItemRotationMatrix[i], 0);
                        System.arraycopy(mTemp2Matrix, 0, mRenderer.mItemRotationMatrix[i], 0, 16);
                    }

                    break;
                case "bubble":
                    Matrix.multiplyMM(mTemp2Matrix, 0, mTempMatrix, 0, mRenderer.mSphereRotationMatrix, 0);
                    System.arraycopy(mTemp2Matrix, 0, mRenderer.mSphereRotationMatrix, 0, 16);
                    break;
            }

            // render
            requestRender();
        }

        // double touch -> move
        if ((action == MotionEvent.ACTION_MOVE) && (count == 2)) {
            float dx = x - mPreviousX;
            float dy = y - mPreviousY;

            dx *= 0.02f;
            dy *= 0.02f;

            switch (mode) {
                case "world":
                    Matrix.translateM(mRenderer.mViewTranslationMatrix, 0, dx, -dy, 0);
                    break;
                case "cube":
                    Matrix.translateM(mRenderer.mCubeTranslationMatrix, 0, dx, -dy, 0);
                    break;
                case "map":
                    Matrix.translateM(mRenderer.mMapTranslationMatrix, 0, -dx, -dy, 0);
                    Matrix.translateM(mRenderer.mSeaTranslationMatrix, 0, -dx, -dy, 0);

                    for (int i = 0; i < mRenderer.itemCount; i++) {
                        Matrix.translateM(mRenderer.mItemTranslationMatrix[i], 0, -dx, -dy, 0);
                    }

                    break;
                case "bubble":
                    float[] temp1 = new float[16];
                    float[] move = new float[16];
                    float[] inversetemp = new float[16];
                    Matrix.invertM(inversetemp, 0, mRenderer.mViewRotationMatrix, 0);
                    Matrix.setIdentityM(temp1, 0);
                    Matrix.setIdentityM(move, 0);
                    Matrix.translateM(move, 0, -dx, -dy, 0);
                    Matrix.multiplyMM(temp1, 0, move, 0, inversetemp, 0);
                    System.arraycopy(temp1, 0, move, 0, 16);
                    Matrix.multiplyMM(temp1, 0, mRenderer.mViewRotationMatrix, 0, move, 0);
                    System.arraycopy(temp1, 0, move, 0, 16);
                    Matrix.multiplyMM(temp1, 0, mRenderer.mSphereTranslationMatrix, 0, move, 0);
                    System.arraycopy(temp1, 0, mRenderer.mSphereTranslationMatrix, 0, 16);

//                    Matrix.translateM(mRenderer.mSphereTranslationMatrix, 0, dx, -dy, 0);
                    break;
            }

            // render
            requestRender();
        }

        if ((action == MotionEvent.ACTION_MOVE) && (count == 3)) {
            float dx = x - mPreviousX;
            float dy = y - mPreviousY;

            dx *= 0.02f;
            dy *= 0.02f;

            switch (mode) {
                case "world":
                    //Matrix.translateM(mRenderer.mViewTranslationMatrix, 0, dx, -dy, 0);
                    break;
                case "cube":
                    Matrix.translateM(mRenderer.mCubeTranslationMatrix, 0, dx, -dy, 0);
                    break;
                case "map":
                    Matrix.translateM(mRenderer.mMapTranslationMatrix, 0, dx, -dy, 0);
                    break;
                case "bubble":
                    float[] temp1 = new float[16];
                    float[] move = new float[16];
                    float[] inversetemp = new float[16];
                    Matrix.invertM(inversetemp, 0, mRenderer.mViewRotationMatrix, 0);
                    Matrix.setIdentityM(temp1, 0);
                    Matrix.setIdentityM(move, 0);
                    Matrix.translateM(move, 0, 0, 0, -dy);
                    Matrix.multiplyMM(temp1, 0, move, 0, inversetemp, 0);
                    System.arraycopy(temp1, 0, move, 0, 16);
                    Matrix.multiplyMM(temp1, 0, mRenderer.mViewRotationMatrix, 0, move, 0);
                    System.arraycopy(temp1, 0, move, 0, 16);
                    Matrix.multiplyMM(temp1, 0, mRenderer.mSphereTranslationMatrix, 0, move, 0);
                    System.arraycopy(temp1, 0, mRenderer.mSphereTranslationMatrix, 0, 16);

//                    Matrix.translateM(mRenderer.mSphereTranslationMatrix, 0, dx, -dy, 0);
                    break;
            }

            // render
            requestRender();
        }

        mPreviousX = x;
        mPreviousY = y;

        // dummy
        return true;
    }

    public void rotateByGyroSensor(float gyroX, float gyroY, float gyroZ) {

        float[] rotate = new float[16];
        Matrix.setIdentityM(rotate, 0);

        Matrix.rotateM(rotate, 0, -gyroX, 1, 0, 0);
        Matrix.rotateM(rotate, 0, -gyroY, 0, 1, 0);
        Matrix.rotateM(rotate, 0, -gyroZ, 0, 0, 1);

        float[] temp = new float[16];
        Matrix.multiplyMM(temp, 0, rotate, 0, mRenderer.mViewRotationMatrix, 0);
        System.arraycopy(temp, 0, mRenderer.mViewRotationMatrix, 0, 16);
        requestRender();
    }
}
