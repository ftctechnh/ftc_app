package org.firstinspires.ftc.teamcode;

import android.graphics.Bitmap;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.robotcontroller.internal.CameraProcessor;
import org.opencv.android.Utils;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;

/**
 * TeleOp Mode
 * <p/>
 * Enables control of the robot via the gamepad
 */

@TeleOp(name = "Camera: Auto (2)", group = "Linear OpMode")
//@Disabled
public class CameraAuto2 extends CameraProcessor {
    public class ColorCoordinate {
        int x = 0;
        int y = 0;
        int color = 0;

        public void set(int newx, int newy, int newcolor) {
            x = newx;
            y = newy;
            color = newcolor;
        }

        public void set(ColorCoordinate coord) {
            x = coord.x;
            y = coord.y;
            color = coord.color;
        }
    }

    @Override
    public void runOpMode() {
        telemetry.addData("Status:", "Initializing");
        telemetry.update();

        setCameraDownsampling(9);
        startCamera();

        telemetry.addData("Status:", "Initialized (waiting for start)");
        telemetry.update();

        try {
            waitForStart();
        } catch(Exception e) {

        }

        while (opModeIsActive()) {
            if(!imageReady()) { // only do this if an image has been returned from the camera
                telemetry.addData("Status:", "Waiting for image...");
                telemetry.update();
                continue;
            }

            long startTime = System.currentTimeMillis();

            Bitmap image = convertYuvImageToRgb(yuvImage, size.width, size.height, 1);

            long cameraTime = System.currentTimeMillis();

            Mat image_mat = new Mat(image.getWidth(), image.getHeight(), CvType.CV_8UC1);
            Utils.bitmapToMat(image, image_mat);
            Imgproc.cvtColor(image_mat, image_mat, Imgproc.COLOR_RGB2GRAY);
            Utils.matToBitmap(image_mat, image);

            long endTime = System.currentTimeMillis();

            telemetry.addData("Status:", "Running");
            telemetry.addData("Time (camera):", cameraTime - startTime);
            telemetry.addData("Time (algorithm):", endTime - cameraTime);
            telemetry.addData("Time (total):", endTime - startTime);
            //telemetry.addData("Image red pos:", red_pos);
            //telemetry.addData("Image blue pos:", blue_pos);
            //telemetry.addData("Image result:", "left: " + left + " | right: " + right);
            telemetry.update();
        }

        stopCamera();
    }
}