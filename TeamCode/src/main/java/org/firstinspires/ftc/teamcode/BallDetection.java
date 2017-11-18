package org.firstinspires.ftc.teamcode;

/**
 * Created by DanielLuo on 11/12/17.
 */

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;
import android.hardware.Camera;
import android.graphics.BitmapFactory;
import android.graphics.Bitmap;
import android.view.SurfaceView;
import android.view.SurfaceHolder;
import android.content.Context;
import android.app.Activity;

public class BallDetection extends LinearOpMode {

    Bitmap cameraData;
    Camera camera;
    boolean bitmapReady = false;
    boolean inPreview = false;

    int pictureDelta = 500; // Time in milliseconds between picture captures

    private class CameraJPGCallback implements Camera.PictureCallback {
        // callback from Camera.takePicture(null, null, null, CameraJPGCallback)
        public void onPictureTaken(byte[] data, Camera v) {
            cameraData = BitmapFactory.decodeByteArray(data, 0, data.length);
            bitmapReady = true;
        }
    }

    private Camera getCamera() {
        Camera c = null;
        try {
            c = Camera.open(); // attempt to get a Camera instance
        }
        catch (Exception e){
            ;
        }
        return c;
    }

    public class CameraActivity extends Activity {

    }

    public void initializeCamera() {
        // Initialize the camera to be used
        camera = getCamera();
        return;
    }

    public void releaseCamera() {
        // Release the camera so that other applications can use it
        camera.release();
        return;
    }

    public void stopPreview() {
        // Stop the camera preview
        camera.stopPreview();
        inPreview = false;
        return;
    }

    public void startPreview() {
        // Start the camera preview
        camera.startPreview();
        inPreview = true;
        return;
    }

    public void takePicture() {
        // Take a picture async
        if (inPreview) {
            camera.takePicture(null, null, null, new CameraJPGCallback());
            inPreview = false;
        }
        return;
    }

    public void processBitmap() {
        // Process the bitmap
        telemetry.addData("Middle Pixel: ", cameraData.getPixel(cameraData.getWidth() / 2,
                cameraData.getHeight() / 2));
        telemetry.update();
    }

    HardwareDRive robot = new HardwareDRive();

    @Override
    public void runOpMode() {
        long lastPictureTime = 0;
        long time;

        robot.init(hardwareMap);

        initializeCamera();
        startPreview();

        while (opModeIsActive()) {
            if (bitmapReady) {
                bitmapReady = false;

                processBitmap();
                startPreview();
            }

            time = System.currentTimeMillis();

            if (time > lastPictureTime + pictureDelta) {
                takePicture();
                lastPictureTime = time;
            }
        }

        releaseCamera();
    }
}
