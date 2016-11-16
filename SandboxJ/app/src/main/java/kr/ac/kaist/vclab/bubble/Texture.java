package kr.ac.kaist.vclab.bubble;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES20;
import android.opengl.GLUtils;

/**
 * Created by Jongmin on 2016-11-13.
 */

public class Texture {
    private int m_textureId;
    public Texture()
    {
        m_textureId = loadTexture(R.drawable.water);
    }

    public void setTexture(){
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, m_textureId);
        GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR);

    }

    public static int loadTexture(int resourceId)
    {
        final int[] textureHandle = new int[1];
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
        GLES20.glGenTextures(1, textureHandle, 0);

        if (textureHandle[0] != 0)
        {
            GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureHandle[0]);

            final BitmapFactory.Options options = new BitmapFactory.Options();
            options.inScaled = false;   // No pre-scaling

            // Load the bitmap into the bound texture.
            final Bitmap bitmap = BitmapFactory.decodeResource(MainActivity.context.getResources(), resourceId, options);
            GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, bitmap, 0);
            // Load the bitmap into the bound texture.
            bitmap.recycle();

            // Set filtering
            GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_NEAREST);
            GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_NEAREST);
        }

        else
        {
            throw new RuntimeException("Error loading texture.");
        }

        return textureHandle[0];
    }
}
