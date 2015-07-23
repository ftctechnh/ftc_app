package com.fellowshipoftheloosescrews.opmodes;


import android.util.Log;

import com.fellowshipoftheloosescrews.camera.CameraListener;
import com.fellowshipoftheloosescrews.camera.Image;
import com.fellowshipoftheloosescrews.camera.ThomasCamera;
import com.fellowshipoftheloosescrews.utilities.ThreadedOpModeJob;

/**
 * Created by Thomas on 7/23/2015.
 */
public class CameraTesting extends ThreadedOpModeJob implements CameraListener{

    private ThomasCamera thomasCamera;


    @Override
    public void start() {
        thomasCamera = new ThomasCamera();
        thomasCamera.init();
        thomasCamera.addListener(this);
    }

    @Override
    public void loop() {
        opMode.telemetry.addData("B", b);
        opMode.telemetry.addData("G", g);
        opMode.telemetry.addData("R", r);
    }

    @Override
    public void stop() {
        thomasCamera.release();
    }

    private int r = 0, g = 0, b = 0;

    @Override
    public void run() {
        while(isRunning())
        {
            thomasCamera.capture();
            while(thomasCamera.getState() == ThomasCamera.CameraStates.CAMERA_CAPTURING);
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void imageCallback(Image image) {

        Log.d("Camera", "TRACE");

        r = image.getR(image.getWidth() / 2, image.getHeight() / 2);
        g = image.getG(image.getWidth() / 2, image.getHeight() / 2);
        b = image.getB(image.getWidth() / 2, image.getHeight() / 2);
    }
}
