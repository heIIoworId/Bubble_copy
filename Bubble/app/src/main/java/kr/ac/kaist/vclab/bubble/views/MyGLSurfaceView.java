package kr.ac.kaist.vclab.bubble.views;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.view.MotionEvent;

import kr.ac.kaist.vclab.bubble.MyGLRenderer;
import kr.ac.kaist.vclab.bubble.environment.Env;
import kr.ac.kaist.vclab.bubble.environment.GameEnv;

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

    public MyGLRenderer getmRenderer() {
        return mRenderer;
    }

    @Override
    public boolean onTouchEvent(MotionEvent e) {
        // MotionEvent reports input details from the touch screen
        // and other input controls. In this case, you are only
        // interested in events where the touch position changed.

        int count = e.getPointerCount();

        float x = e.getX(0);
        float y = e.getY(0);

        if (count == 2) {
            x = (x + e.getX(1)) / 2;
            y = (y + e.getY(1)) / 2;
        }

        float dx = Math.max(Math.min(x - mPreviousX, 10f), -10f);
        float dy = Math.max(Math.min(y - mPreviousY, 10f), -10f);

        if (e.getAction() == MotionEvent.ACTION_MOVE) {
            switch (mode) {
                case 0:
                    if (count == 1) {
                        // Rotate world
                        float[] rot = temp1;

                        Matrix.setIdentityM(rot, 0);
                        Matrix.rotateM(rot, 0, dx, 0, 1, 0);
                        Matrix.rotateM(rot, 0, dy, 1, 0, 0);
                        Matrix.multiplyMM(temp2, 0, rot, 0, mRenderer.mViewRotationMatrix, 0);
                        System.arraycopy(temp2, 0, mRenderer.mViewRotationMatrix, 0, 16);
                    } else if (count == 2) {
                        // Translate world
                        Matrix.translateM(mRenderer.mViewTranslationMatrix, 0, dx / 100, -dy / 100, 0);
                    }
                    break;
                case 1:
                    if (count == 2) {
                        // Translate cube1
                        float[] temp1 = new float[16];
                        float[] move = new float[16];
                        float[] inversetemp = new float[16];
                        Matrix.invertM(inversetemp,0,mRenderer.mViewRotationMatrix,0);
                        Matrix.setIdentityM(temp1, 0);
                        Matrix.setIdentityM(move, 0);
                        Matrix.translateM(move, 0, -dx/100, -dy/100, 0);
                        Matrix.multiplyMM(temp1,0, move,0, inversetemp,0);
                        System.arraycopy(temp1, 0, move, 0 , 16);
                        Matrix.multiplyMM(temp1, 0, mRenderer.mViewRotationMatrix, 0, move, 0);
                        System.arraycopy(temp1, 0, move, 0 , 16);
//                        Matrix.multiplyMM(temp1, 0, mRenderer.mSphereTranslationMatrix, 0, move, 0);
//                        System.arraycopy(temp1, 0, mRenderer.mSphereTranslationMatrix, 0 , 16);

                    }
                    else if (count == 3){
                        float[] temp1 = new float[16];
                        float[] move = new float[16];
                        float[] inversetemp = new float[16];
                        Matrix.invertM(inversetemp,0,mRenderer.mViewRotationMatrix,0);
                        Matrix.setIdentityM(temp1, 0);
                        Matrix.setIdentityM(move, 0);
                        Matrix.translateM(move, 0, 0, 0, -dy/100);
                        Matrix.multiplyMM(temp1,0, move,0, inversetemp,0);
                        System.arraycopy(temp1, 0, move, 0 , 16);
                        Matrix.multiplyMM(temp1, 0, mRenderer.mViewRotationMatrix, 0, move, 0);
                        System.arraycopy(temp1, 0, move, 0 , 16);
//                        Matrix.multiplyMM(temp1, 0, mRenderer.mSphereTranslationMatrix, 0, move, 0);
//                        System.arraycopy(temp1, 0, mRenderer.mSphereTranslationMatrix, 0 , 16);
                    }
                    break;

//                case 2:
//                    if (count == 1) {
//                        // Rotate cube2
//                        float[] rot = temp1;
//                        Matrix.setIdentityM(rot, 0);
//                        Matrix.rotateM(rot, 0, dx, 0, 1, 0);
//                        Matrix.rotateM(rot, 0, dy, 1, 0, 0);
//                        Matrix.multiplyMM(temp2, 0, rot, 0, mRenderer.mSphereRotationMatrix, 0);
//                        System.arraycopy(temp2, 0, mRenderer.mSphereRotationMatrix, 0, 16);
//                    } else if (count == 2) {
//                        // Translate cube2
//                        Matrix.translateM(mRenderer.mSphereTranslationMatrix, 0, dx/ 100, -dy / 100, 0);
//                    }
//                    break;
            }
        }

        mPreviousX = x;
        mPreviousY = y;

        requestRender();
        return true;
    }

    public void rotateByGyroSensor(float gyroX, float gyroY, float gyroZ) {

        float[] rotate = new float[16];
        Matrix.setIdentityM(rotate, 0);

        float scale = GameEnv.getInstance().gyroScale;
        Matrix.rotateM(rotate, 0, -gyroX * scale, 1, 0, 0);
        Matrix.rotateM(rotate, 0, -gyroY * scale, 0, 1, 0);
        Matrix.rotateM(rotate, 0, -gyroZ * scale, 0, 0, 1);

        //mRenderer.mViewTranslationMatrix = Util.transformUsingAuxiliary(mRenderer.mViewRotationMatrix, mRenderer.mViewTranslationMatrix, rotate);


        float [] temp = new float[16];
        Matrix.multiplyMM(temp, 0 , rotate, 0,  mRenderer.mViewRotationMatrix, 0);
        System.arraycopy(temp,0, mRenderer.mViewRotationMatrix,0, 16);

        requestRender();
    }
}
