package org.firstinspires.ftc.teamcode.seasons.relicrecovery.summer;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.util.Log;

import org.opencv.android.JavaCameraView;
import org.opencv.android.Utils;
import org.opencv.core.Mat;
import org.opencv.core.Size;

import java.util.List;

/**
 * Created by ftc6347 on 7/11/17.
 */

public class RobotControllerCameraView extends JavaCameraView {
    public RobotControllerCameraView(Context context, int cameraId) {
        super(context, cameraId);
    }

    @Override
    protected Size calculateCameraFrameSize(List<?> supportedSizes, ListItemAccessor accessor, int surfaceWidth, int surfaceHeight) {
        return super.calculateCameraFrameSize(supportedSizes, accessor, surfaceWidth, surfaceHeight);
    }

    @Override
    protected void deliverAndDrawFrame(CvCameraViewFrame frame) {
        Mat modified;

        if (mListener != null) {
            modified = mListener.onCameraFrame(frame);
        } else {
            modified = frame.rgba();
        }

        boolean bmpValid = true;
        if (modified != null) {
            try {
                Utils.matToBitmap(modified, mCacheBitmap);
            } catch(Exception e) {
                Log.e(TAG, "Mat type: " + modified);
                Log.e(TAG, "Bitmap type: " + mCacheBitmap.getWidth() + "*" + mCacheBitmap.getHeight());
                Log.e(TAG, "Utils.matToBitmap() throws an exception: " + e.getMessage());
                bmpValid = false;
            }
        }

        if (bmpValid && mCacheBitmap != null) {
            Canvas canvas = getHolder().lockCanvas();
            if (canvas != null) {
                canvas.drawColor(0, android.graphics.PorterDuff.Mode.CLEAR);
                Log.d(TAG, "mStretch value: " + mScale);

                Matrix matrix = new Matrix();
                matrix.postRotate(90); // rotate 90 degrees for portrait orientation

                Bitmap bitmap = Bitmap.createBitmap(mCacheBitmap, 0, 0, mCacheBitmap.getWidth(),
                        mCacheBitmap.getHeight(), matrix, true);

                if (mScale != 0) {
                    canvas.drawBitmap(bitmap, new Rect(0,0,bitmap.getWidth(), bitmap.getHeight()),
                            new Rect((int)((canvas.getWidth() - mScale*bitmap.getWidth()) / 2),
                                    (int)((canvas.getHeight() - mScale*bitmap.getHeight()) / 2),
                                    (int)((canvas.getWidth() - mScale*bitmap.getWidth()) / 2 + mScale*bitmap.getWidth()),
                                    (int)((canvas.getHeight() - mScale*bitmap.getHeight()) / 2 + mScale*bitmap.getHeight())), null);
                } else {
                    canvas.drawBitmap(bitmap, new Rect(0,0,bitmap.getWidth(), bitmap.getHeight()),
                            new Rect((canvas.getWidth() - bitmap.getWidth()) / 2,
                                    (canvas.getHeight() - bitmap.getHeight()) / 2,
                                    (canvas.getWidth() - bitmap.getWidth()) / 2 + bitmap.getWidth(),
                                    (canvas.getHeight() - bitmap.getHeight()) / 2 + bitmap.getHeight()), null);
                }

                if (mFpsMeter != null) {
                    mFpsMeter.measure();
                    mFpsMeter.draw(canvas, 20, 30);
                }
                getHolder().unlockCanvasAndPost(canvas);
            }
        }
    }
}
