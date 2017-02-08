package org.firstinspires.ftc.teamcode;

import android.graphics.Bitmap;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import org.firstinspires.ftc.robotcontroller.internal.CameraProcessor;

import java.util.ArrayList;
import java.util.List;

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

            averageColor();

            /*
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
            */
        }

        stopCamera();
    }

    public void whichSide() {
        while (!imageReady()) {
            telemetry.addData("Camera:", "Waiting for image...");
            telemetry.update();
        }

        Bitmap image = convertYuvImageToRgb(yuvImage, size.width, size.height, 1);

        int redPixels = 0;
        int redPixelsCount = 0;
        int bluePixels = 0;
        int bluePixelsCount = 0;

        for (int x = 0; x < image.getWidth(); x++) {
            for (int y = 0; y < image.getHeight(); y++) {
                int pixel = image.getPixel(x, y);
                int pixel_red = red(pixel);
                int pixel_blue = blue(pixel);
                int pixel_green = green(pixel);

                double percent_blue = 0;
                double percent_red = 0;

                try {
                    percent_blue = pixel_blue / (pixel_red + pixel_blue + pixel_green);
                    percent_red = pixel_red / (pixel_red + pixel_blue + pixel_green);
                } catch(ArithmeticException e) {

                }

                if (percent_blue > 0.5) {
                    bluePixels += x;
                    bluePixelsCount++;
                }

                if (percent_red > 0.5) {
                    redPixels += x;
                    redPixelsCount++;
                }
            }
        }

        try {
            bluePixels /= bluePixelsCount;
            redPixels /= redPixelsCount;
        } catch(ArithmeticException e) {

        }

        String left;
        String right;

        if (bluePixels > redPixels) { //Blue is on the left, red is on the right
            left = "BLUE";
            right = "RED";
        } else if (redPixels > bluePixels) { //Red is on the left, blue is on the right
            left = "RED";
            right = "BLUE";
        } else {
            left = "ERROR";
            right = "ERROR";
        }

        telemetry.addData("Left Color: ", left);
        telemetry.addData("Right Color: ", right);
        telemetry.addData("Blue pos: ", bluePixels);
        telemetry.addData("Blue num: ", bluePixelsCount);
        telemetry.addData("Red pos: ", redPixels);
        telemetry.addData("Red num: ", redPixelsCount);
        telemetry.update();
    }

    public void averageColor() {
        while (!imageReady()) {
            telemetry.addData("Camera:", "Waiting for image...");
            telemetry.update();
        }

        Bitmap image = convertYuvImageToRgb(yuvImage, size.width, size.height, 1);

        int redColor = 0;
        int redCount = 0;
        int blueColor = 0;
        int blueCount = 0;

        for (int x = 0; x < image.getWidth(); x++) {
            for (int y = 0; y < image.getHeight(); y++) {
                int pixel = image.getPixel(x, y);
                int pixel_red = red(pixel);
                int pixel_blue = blue(pixel);
                int pixel_green = green(pixel);

                double percent_red = 0;
                double percent_blue = 0;

                try {
                    percent_red = pixel_red / (pixel_red + pixel_blue + pixel_green);
                    percent_blue = pixel_blue / (pixel_red + pixel_blue + pixel_green);
                } catch(ArithmeticException e) {

                }

                redColor += percent_red;
                redCount++;

                blueColor += percent_blue;
                blueColor++;
            }
        }

        try {
            redColor /= redCount;
            blueColor /= blueCount;
        } catch(ArithmeticException e) {

        }

        telemetry.addData("Red: ", redCount);
        telemetry.addData("Blue: ", blueCount);
        telemetry.update();
    }
}