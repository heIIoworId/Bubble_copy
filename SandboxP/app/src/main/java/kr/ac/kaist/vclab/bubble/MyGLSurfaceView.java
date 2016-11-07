package kr.ac.kaist.vclab.bubble;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.view.MotionEvent;

/**
 * Created by avantgarde on 2016-11-02.
 */

public class MyGLSurfaceView extends GLSurfaceView {
    private final MyGLRenderer mRenderer;

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

        // single touch -> rotate
        if ((action == MotionEvent.ACTION_MOVE) && (count == 1)) {
            float dx = x - mPreviousX;
            float dy = y - mPreviousY;

            Matrix.setIdentityM(mTempMatrix, 0);
            Matrix.rotateM(mTempMatrix, 0, 0.3f * dx, 0, 1, 0);
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
                    Matrix.multiplyMM(mTemp2Matrix, 0, mTempMatrix, 0, mRenderer.mRecRotationMatrix, 0);
                    System.arraycopy(mTemp2Matrix, 0, mRenderer.mRecRotationMatrix, 0, 16);
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
                    Matrix.translateM(mRenderer.mMapTranslationMatrix, 0, dx, -dy, 0);
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
}
