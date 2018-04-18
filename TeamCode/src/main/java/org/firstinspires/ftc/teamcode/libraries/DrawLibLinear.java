package org.firstinspires.ftc.teamcode.libraries;

import android.app.Activity;
import android.graphics.Bitmap;
import android.widget.ImageView;

import org.opencv.android.Utils;
import org.opencv.core.Mat;

/**
 * Created by Noah on 4/18/2018.
 */

public abstract class DrawLibLinear extends OpenCVLoadLinear {
    //output bitmap
    protected Bitmap bm;
    private boolean bmChanged = false;
    //output bitmap lock
    private static final Object bmLock = new Object();

    private ImageView mView;

    protected void initDraw() {
        initOpenCV();
        mView = (ImageView)((Activity)hardwareMap.appContext).findViewById(com.qualcomm.ftcrobotcontroller.R.id.OpenCVOverlay);
        mView.post(new Runnable() {
            @Override
            public void run() {
                mView.setAlpha(1.0f);
                mView.setImageBitmap(bm);
            }
        });
    }

    protected void drawFrame(Mat frame) {
        //convert to bitmap, synchronized
        synchronized (bmLock) {
            if(bm == null || frame.rows() != bm.getHeight() && frame.cols() != bm.getWidth()){
                if(bm != null) bm.recycle();
                bm = Bitmap.createBitmap(frame.cols(), frame.rows(), Bitmap.Config.ARGB_8888);
                bmChanged = true;
            }
            Utils.matToBitmap(frame, bm);
        }

        //display!
        mView.getHandler().post(new Runnable() {
            @Override
            public void run() {
                synchronized (bmLock) {
                    if(bmChanged) {
                        mView.setImageBitmap(bm);
                        bmChanged = false;
                    }
                    mView.invalidate();
                }
            }
        });
    }

    protected void stopDraw() {
        mView.setAlpha(0.0f);
        mView.post(new Runnable() {
            @Override
            public void run() {
                synchronized (bmLock) {
                    mView.setImageDrawable(null);
                    mView.invalidate();
                    if(bm != null) bm.recycle();
                }
            }
        });
    }
}
