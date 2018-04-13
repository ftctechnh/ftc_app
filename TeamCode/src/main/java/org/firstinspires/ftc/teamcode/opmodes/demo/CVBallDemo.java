package org.firstinspires.ftc.teamcode.opmodes.demo;

import android.graphics.Bitmap;
import android.hardware.camera2.CameraDevice;
import android.os.Environment;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;

import org.firstinspires.ftc.teamcode.libraries.VuforiaBallLib;

import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by Noah on 2/27/2018.
 */

@Autonomous(name = "CV Ball Demo")
@Disabled
public class CVBallDemo extends VuforiaBallLib {
    @Override
    public void init() {
        initVuforia(true);
        super.startTracking();
    }

    public void init_loop() {
        getCVBallColor();
    }

    @Override
    public void start() {
        FileOutputStream out = null;
        try{
            out = new FileOutputStream(Environment.getExternalStorageDirectory().getAbsolutePath() + "/balls.png");
            bm.compress(Bitmap.CompressFormat.PNG, 100, out);

        }
        catch(Exception e) {
            //oops
            e.printStackTrace();
        }
        finally {
            try {
                if (out != null) {
                    out.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        com.vuforia.CameraDevice.getInstance().setFlashTorchMode(true);
    }

    @Override
    public void loop() {
        telemetry.addData("Path", Environment.getExternalStorageDirectory().getAbsolutePath() + "/balls.png");
        telemetry.addData("Tracking", isTracking().toString());
        telemetry.addData("Ball Color", getCVBallColor());
    }

    @Override
    public void stop() {
        super.stopVuforia();
        com.vuforia.CameraDevice.getInstance().setFlashTorchMode(true);
    }
}
