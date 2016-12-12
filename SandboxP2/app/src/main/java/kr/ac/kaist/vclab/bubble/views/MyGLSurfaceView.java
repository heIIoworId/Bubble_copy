package kr.ac.kaist.vclab.bubble.views;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.view.MotionEvent;

import kr.ac.kaist.vclab.bubble.MyGLRenderer;
import kr.ac.kaist.vclab.bubble.environment.Env;
import kr.ac.kaist.vclab.bubble.environment.GameEnv;
import kr.ac.kaist.vclab.bubble.utils.MatOperator;

/**
 * Created by sjjeon on 16. 9. 20.
 */

public class MyGLSurfaceView extends GLSurfaceView {

    public static MyGLRenderer mRenderer;

    private float mPreviousX;
    private float mPreviousY;

    private float[] temp1 = new float[16];
    private float[] temp2 = new float[16];

    public int mode;

    public MyGLSurfaceView(Context context) {
        super(context);

        // Create an OpenGL ES 2.0 context.
        setEGLContextClientVersion(2);

        // Set the Renderer for drawing on the GLSurfaceView
        mRenderer = new MyGLRenderer();
        setRenderer(mRenderer);

        // Render the view only when there is a change in the drawing data
        if(Env.getInstance().dirtyModeStatus == 1){
            setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
        }
    }


    @Override
    public boolean onTouchEvent(MotionEvent e) {
        // MotionEvent reports input details from the touch screen
        // and other input controls. In this case, you are only
        // interested in events where the touch position changed.

        int count = e.getPointerCount();
        int action = e.getAction();

        float x = e.getX(0);
        float y = e.getY(0);

        if (count == 2) {
            x = (x + e.getX(1)) / 2;
            y = (y + e.getY(1)) / 2;
        }

        float dx = Math.max(Math.min(x - mPreviousX, 10f), -10f);
        float dy = Math.max(Math.min(y - mPreviousY, 10f), -10f);

        // TOUCH DOWN & UP -> HINT MODE ON / OFF (MAP BLENDING ON / OFF)
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                mRenderer.mapBlendFlag = true;
                break;
            case MotionEvent.ACTION_UP:
                mRenderer.mapBlendFlag = false;
                break;

            case MotionEvent.ACTION_MOVE:
                if (true) {
                    // DOUBLE TOUCH -> ROTATE VIEW
                    if (count == 2) {
                        float[] rot = temp1;

                        Matrix.setIdentityM(rot, 0);
                        Matrix.rotateM(rot, 0, dx, 0, 1, 0);
                        Matrix.rotateM(rot, 0, dy, 1, 0, 0);
                        Matrix.multiplyMM(temp2, 0, rot, 0, mRenderer.mViewRotationMatrix, 0);
                        System.arraycopy(temp2, 0, mRenderer.mViewRotationMatrix, 0, 16);
                    }

                    // TRIPLE TOUCH -> TRANSLATE VIEW
                    // FIXME : THIS WORKS IN THE REAL DEVICE, BUT THERE IS NO WAY TO TEST THIS IN THE EMULATOR!
                    if (count == 3) {
                        Matrix.translateM(mRenderer.mViewTranslationMatrix, 0, dx / 100, -dy / 100, 0);
                    }
                }

                break;
        }

        mPreviousX = x;
        mPreviousY = y;

        requestRender();
        return true;
    }


    public void doRotate(float[] rotateMatrix){
        float [] temp = new float[16];
//        float [] temp2 = new float[16];
//        Matrix.setIdentityM(temp2, 0);
//        Matrix.multiplyMM(temp, 0 ,temp2, 0, rotateMatrix, 0);
        Matrix.multiplyMM(temp, 0 , mRenderer.mViewRotationMatrix, 0 ,rotateMatrix, 0);
        System.arraycopy(temp,0, mRenderer.mViewRotationMatrix, 0, 16);
        requestRender();
    }
}