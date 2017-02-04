package org.firstinspires.ftc.teamcode;

import android.graphics.Bitmap;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import org.firstinspires.ftc.robotcontroller.internal.CameraProcessor;

/**
 * TeleOp Mode
 * <p/>
 * Enables control of the robot via the gamepad
 */

@TeleOp(name = "Camera: Auto", group = "Linear OpMode")
//@Disabled
public class CameraAuto extends CameraProcessor {
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

            int left_intensity = 0;

            for(int x = 0; x < image.getWidth() / 2; x++) {
                for(int y = 0; y < image.getHeight(); y++) {
                    int pixel = image.getPixel(x, y);
                    int pixel_blue = blue(pixel);

                    left_intensity += pixel_blue;
                }
            }

            int right_intensity = 0;

            for(int x = image.getWidth() / 2; x < image.getWidth(); x++) {
                for(int y = 0; y < image.getHeight(); y++) {
                    int pixel = image.getPixel(x, y);
                    int pixel_blue = blue(pixel);

                    right_intensity += pixel_blue;
                }
            }

            String left;
            String right;

            if(left_intensity < right_intensity) {
                left = "BLUE";
                right = "RED";
            } else {
                left = "RED";
                right = "BLUE";
            }

            telemetry.addData("Status:", "Running");
            telemetry.addData("Time:", System.currentTimeMillis() - startTime);
            telemetry.addData("Image size:", image.getWidth() + "x" + image.getHeight() + " (" + size.width + "x" + size.height + ")");
            telemetry.addData("Image left:", left_intensity);
            telemetry.addData("Image right:", right_intensity);
            telemetry.addData("Image result:", "left: " + left + " | right: " + right);
            telemetry.update();
        }

        stopCamera();
    }
}