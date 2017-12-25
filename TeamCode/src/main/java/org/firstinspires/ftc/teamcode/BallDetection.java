package org.firstinspires.ftc.teamcode;

/**
 * Created by DanielLuo on 11/12/17.
 */

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import org.firstinspires.ftc.robotcore.external.navigation.*;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer.CloseableFrame;
import org.firstinspires.ftc.robotcore.external.ClassFactory;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.vuforia.*;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import java.nio.ByteBuffer;
import android.graphics.Color;

@TeleOp(name="BallGay", group="Pushbot")
public class BallDetection extends LinearOpMode {

    private Image getPicture() {

        Vuforia.setFrameFormat(PIXEL_FORMAT.RGB565, true); //enables RGB565 format for the image
        locale.setFrameQueueCapacity(1);

        CloseableFrame frame; //takes the frame at the head of the queue
        try {
            frame = locale.getFrameQueue().take();
        } catch (InterruptedException e) {
            telemetry.addData("e",e.getMessage());
            return null;
        }

        Image rgb = null;

        long numImages = frame.getNumImages();

        for (int i = 0; i < numImages; i++) {
            if (frame.getImage(i).getFormat() == PIXEL_FORMAT.RGB565) {
                rgb = frame.getImage(i);
                break;
            }
        }

        return rgb;
    }

    VuforiaLocalizer.Parameters params;
    VuforiaLocalizer locale;

    int pictureDelta = 500; // Time in milliseconds between picture captures

    HardwareDRive robot = new HardwareDRive();

    int extractR(int a) {
        return Color.red(a);
    }

    int extractG(int a) {
        return Color.green(a);
    }

    int extractB(int a) {
        return Color.blue(a);
    }

    private boolean isBlue(float[] pix) {
        return (Math.abs((pix[0] / 256.) - .59091) < 0.3);
    }

    private void processBitmap(Bitmap bm) {
        int width = bm.getWidth();
        int height = bm.getHeight();

        int coarseness = 31;
        int pixelCount = 0;
        float[] pix = {0, 0, 0};

        boolean[][] pixels = new boolean[width / coarseness + 1][height / coarseness + 1];

        for (int i = 0; i < width; i += coarseness) {
            for (int j = 0; j < height; j += coarseness) {
                Color.colorToHSV(bm.getPixel(i, j), pix);
                // pix[0] = H, pix[1] = S, pix[2] = V

                pixels[i / coarseness][j / coarseness] = isBlue(pix);
                pixelCount += 1;
            }
        }

        telemetry.addData("PixCount",pixelCount);

        width /= coarseness;
        height /= coarseness;

        for (int i = 0; i < width; i++) {
            String p = "";
            for (int j = 0; j < height; j++) {
                p = p + (pixels[i][j] ? "#" : "_");
            }
            telemetry.addData("",p);
        }
    }

    @Override
    public void runOpMode() {
        long lastPictureTime = 0;
        long time;

        robot.init(hardwareMap);

        params = new VuforiaLocalizer.Parameters(R.id.cameraMonitorViewId);
        params.vuforiaLicenseKey = "ARjW6VD/////AAAAGbCMKpMCSEgSunPcA5cUQkuEKymuh9/mOQ5b+ngfYCdx3gPONkD3mscU39FUD7mRQRZSRZpjHZfohKwL2PYsVZrBcTlaY1JcJ9J5orZKqTxxy68irqEBuQkkfG72xEEPYuNq+yEJCNzYKhx3wFGqUV1H05Z1fFJa1ZiWfe4Tn9aO2Yf5AIkYCMz4K75LFU3ZM1wCgz9ubLhxZH2BWF9X0rhvnhZS2rnLHkxm+C+xzRbs2ZoGCOpDRb3Dy0iMG2y4Ve9/AApZQ+6sgSwlc9liA5jZ0QyT0dLqyfaoXwNxPqzBjhOj3FltEHxrWPdpOQm6B8BDC9Kv+BShnpi6g3yhf+msI3Qeqsns/nm6DrGF5zum";
        params.cameraDirection = VuforiaLocalizer.CameraDirection.BACK;

        locale = ClassFactory.createVuforiaLocalizer(params);
        Vuforia.setFrameFormat(PIXEL_FORMAT.RGB565, true); //enables RGB565 format for the image
        locale.setFrameQueueCapacity(1); //tells VuforiaLocalizer to only store one frame at a time

        while (!opModeIsActive()) {
            sleep(250);
        }

        int x1 = 0, y1 = 0;

        while (opModeIsActive()) {

            time = System.currentTimeMillis();
            Bitmap b = null;
            ByteBuffer l;

            int width = 0, height = 0;

            if (time > lastPictureTime + pictureDelta) {
                if (gamepad1.dpad_down) {
                    y1 += 1;
                    if (y1 >= height) {
                        y1 -= 1;
                    }
                }

                if (gamepad1.dpad_up) {
                    y1 -= 1;
                    if (y1 < 0) {
                        y1 += 1;
                    }
                }

                if (gamepad1.dpad_right) {
                    x1 += 1;
                    if (x1 >= width) {
                        x1 -= 1;
                    }
                }

                if (gamepad1.dpad_left) {
                    x1 -= 1;
                    if (x1 >= width) {
                        x1 += 1;
                    }
                }

                lastPictureTime = time;
                Image k = getPicture();

                if (k != null) {
                    if (b == null) {
                        b = Bitmap.createBitmap(k.getHeight(), k.getWidth(), Bitmap.Config.RGB_565);
                    }

                    l = k.getPixels();
                    b.copyPixelsFromBuffer(l);

                    telemetry.addData("BufferHeight", k.getHeight());
                    telemetry.addData("BufferWidth", k.getWidth());

                    telemetry.addData("BitmapHeight", b.getHeight());
                    telemetry.addData("BitmapWidth", b.getWidth());

                    width = b.getWidth();
                    height = b.getHeight();

                    int pixel = b.getPixel(x1, y1);

                    processBitmap(b);

                    float[] pix = new float[3];
                    Color.colorToHSV(pixel, pix);

                    telemetry.addData("HSV", pix[0]);
                    telemetry.addData("HSV", pix[1]);
                    telemetry.addData("HSV", pix[2]);

                    // Bottom right

                    telemetry.addData("time", time);
                    telemetry.update();
                }
            }
        }
    }
}
