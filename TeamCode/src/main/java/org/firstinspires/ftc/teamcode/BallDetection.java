package org.firstinspires.ftc.teamcode;

/**
 * Created by DanielLuo on 11/12/17.
 */

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import org.firstinspires.ftc.robotcore.external.navigation.*;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer.CloseableFrame;
import org.firstinspires.ftc.robotcore.external.ClassFactory;
import com.vuforia.*;
import java.nio.ByteBuffer;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class BallDetection extends LinearOpMode {

    private Image getPicture() {

        CloseableFrame frame = null; //takes the frame at the head of the queue
        try {
            frame = locale.getFrameQueue().take();
        } catch (InterruptedException e) {
            e.printStackTrace();
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

        while (opModeIsActive()) {

            time = System.currentTimeMillis();

            if (time > lastPictureTime + pictureDelta) {
                lastPictureTime = time;
                byte[] p = getPicture().getPixels().array();
                Bitmap c = BitmapFactory.decodeByteArray(p, 0, p.length);

                telemetry.addData("Middle Pixel: ", c.getPixel(c.getWidth() / 2, c.getHeight() / 2));
            }
        }
    }
}
