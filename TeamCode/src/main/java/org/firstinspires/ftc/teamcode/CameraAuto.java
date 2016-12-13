package org.firstinspires.ftc.teamcode;

import android.graphics.Bitmap;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

/**
 * TeleOp Mode
 * <p/>
 * Enables control of the robot via the gamepad
 */

@TeleOp(name = "Camera: Auto", group = "Linear OpMode")
//@Disabled
public class CameraAuto extends CameraProcessor {
    int ds2 = 2;  // additional downsampling of the image
    // set to 1 to disable further downsampling

    @Override
    public void runOpMode() {
        String colorString = "NONE";

        if(isCameraAvailable()) {
            setCameraDownsampling(8);
            // parameter determines how downsampled you want your images
            // 8, 4, 2, or 1.
            // higher number is more downsampled, so less resolution but faster
            // 1 is original resolution, which is detailed but slow
            // must be called before super.init sets up the camera

            telemetry.addLine("Wait for camera to finish initializing!");
            telemetry.update();
            startCamera();  // can take a while.
            // best started before waitForStart
            telemetry.addLine("Camera ready!");
            telemetry.update();

            try {
                waitForStart();
            } catch(Exception e) {

            }

            while (opModeIsActive()) {
                if (imageReady()) { // only do this if an image has been returned from the camera
                    int redValue = 0;
                    int blueValue = 0;
                    int greenValue = 0;

                    // get image, rotated so (0,0) is in the bottom left of the preview window
                    Bitmap rgbImage;
                    rgbImage = convertYuvImageToRgb(yuvImage, width, height, ds2);

                    for (int x = 0; x < rgbImage.getWidth(); x++) {
                        for (int y = 0; y < rgbImage.getHeight(); y++) {
                            int pixel = rgbImage.getPixel(x, y);
                            redValue += red(pixel);
                            blueValue += blue(pixel);
                            greenValue += green(pixel);
                        }
                    }
                    int color = highestColor(redValue, greenValue, blueValue);

                    switch (color) {
                        case 0:
                            colorString = "RED";
                            break;
                        case 1:
                            colorString = "GREEN";
                            break;
                        case 2:
                            colorString = "BLUE";
                    }

                } else {
                    colorString = "NONE";
                }

                telemetry.addData("Color:", "Color detected is: " + colorString);
                telemetry.update();

                try {
                    sleep(10);
                } catch(Exception e) {

                }
            }
            stopCamera();

        }
    }
}