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

            List<ColorCoordinate> reds = new ArrayList<ColorCoordinate>();
            List<ColorCoordinate> blues = new ArrayList<ColorCoordinate>();
            int reds_size = 10;
            int blues_size = 10;

            // Fill a list array with "reds_size" number of ColorCoordinate objects
            for(int i = 0; i < reds_size; i++) {
                reds.add(new ColorCoordinate());
            }

            // Fill another list array with "blues_size" number of ColorCoordinate objects
            for(int i = 0; i < blues_size; i++) {
                blues.add(new ColorCoordinate());
            }

            // Scan every pixel in the image
            for(int x = 0; x < image.getWidth(); x++) {
                for(int y = 0; y < image.getHeight(); y++) {
                    int pixel = image.getPixel(x, y);
                    int pixel_red = red(pixel);
                    int pixel_blue = blue(pixel);

                    // Scan "history" of red pixels to update with highest values
                    for(int i = 0; i < reds_size; i++) {
                        // Check each stored pixel to see if the current pixel color is higher than it
                        if(pixel_red > reds.get(i).color) {
                            // If the current pixel color is higher than the stored pixel color, then
                            // move all of the other pixels down a "rank"
                            for(int j = reds.size() - 1; j > i; j--) {
                                reds.get(j).set(reds.get(j - 1));
                            }

                            // Replace the stored pixel color with the current (new) pixel color
                            reds.get(i).set(x, y, pixel_red);

                            // A slot for the new highest pixel was found, so no need to check the others
                            break;
                        }
                    }

                    // Same logic as above but replicated for the blue pixels
                    for(int i = 0; i < blues_size; i++) {
                        if(pixel_blue > blues.get(i).color) {
                            for(int j = blues.size() - 1; j > i; j--) {
                                blues.get(j).set(blues.get(j - 1));
                            }

                            blues.get(i).set(x, y, pixel_blue);
                            break;
                        }
                    }
                }
            }

            int red_pos = 0;
            int blue_pos = 0;

            // Average the red pixels' x position
            for(int i = 0; i < reds_size; i++) {
                red_pos += reds.get(i).x;
            }

            red_pos /= reds_size;

            // Average the blue pixels' x position
            for(int i = 0; i < blues_size; i++) {
                blue_pos += blues.get(i).x;
            }

            blue_pos /= blues_size;

            String left = "ERROR";
            String right = "ERROR";

            // If the red pixel is more left than the blue pixel, then the left beacon color is RED,
            // and the right beacon color is BLUE.
            // Otherwise...
            // If the blue pixel is more left than the red pixel, then the left beacon color is BLUE,
            // and the right beacon color is RED
            if(red_pos < blue_pos) {
                left = "RED";
                right = "BLUE";
            } else if(blue_pos < red_pos) {
                left = "BLUE";
                right = "RED";
            }

            long endTime = System.currentTimeMillis();

            telemetry.addData("Status:", "Running");
            telemetry.addData("Time (camera):", cameraTime - startTime);
            telemetry.addData("Time (algorithm):", endTime - cameraTime);
            telemetry.addData("Time (total):", endTime - startTime);
            telemetry.addData("Image red pos:", red_pos);
            telemetry.addData("Image blue pos:", blue_pos);
            telemetry.addData("Image result:", "left: " + left + " | right: " + right);
            telemetry.update();
        }

        stopCamera();
    }
}