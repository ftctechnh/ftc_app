package org.firstinspires.ftc.teamcode;

import android.graphics.Bitmap;

import com.qualcomm.ftcrobotcontroller.R;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.vuforia.Image;
import com.vuforia.PIXEL_FORMAT;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.opencv.android.Utils;
import org.opencv.core.CvType;
import org.opencv.core.Mat;

import ftc.vision.CryptoBoxResult;
import ftc.vision.ImageProcessor;

@Autonomous(name="Watch Triangle", group ="Concept")
public class TriangleWatcher extends LinearOpMode {

    VuforiaLocalizer vuforia;
    ImageProcessor processor;

    @Override public void runOpMode() throws InterruptedException {

        VuforiaLocalizer.Parameters parameters = new VuforiaLocalizer.Parameters(R.id.cameraMonitorViewId);
        parameters.vuforiaLicenseKey = "Ac4jpF3/////AAAAGYER4VUDLEYGlD++ha+MStuNhKORp/7DQz1D1+tQwcrsMnbQwLqRgpkFtCOIGrZ942gdL179juAJmdXeeH+Dk0pVgxLFq6O0AzY1MS3wS5JHvSLppO9v8W//finYio3hQk+TFKD+qWq9Q1nAZx0bMWFeF6IuIjUPQLioBzC/lYzI/L7oi/AJAbFlf6wue3gDs0dgwrAgpe+JFHTgM3g2+y4hS6O0mcJjobAWSNeRxq9caOGfl/q6f09Eu2EccSmHLAaqje0i70eAIZ4Tbg5C31sPZxBOPTEGTQ9NvFhP4FNAXlvPCdiBt6XYE8P17UzPN72p7lRKyp4xR1oC8B/4dYbivso+rQUed5/H7AnQYOdA";
        parameters.cameraDirection = VuforiaLocalizer.CameraDirection.BACK;
        this.vuforia = ClassFactory.createVuforiaLocalizer(parameters);

        // Start using openCV for ball recognition
        while (opModeIsActive()) {
            Image latestRGB = null;

            VuforiaLocalizer.CloseableFrame frame = vuforia.getFrameQueue().take();
            long numImages = frame.getNumImages();

            for (int i = 0; i < numImages; i++) {
                if (frame.getImage(i).getFormat() == PIXEL_FORMAT.RGB565) {
                    latestRGB = frame.getImage(i);
                    break;
                }
            }

            Bitmap bm = Bitmap.createBitmap(latestRGB.getWidth(), latestRGB.getHeight(), Bitmap.Config.RGB_565);
            bm.copyPixelsFromBuffer(latestRGB.getPixels());

            Mat tmp = new Mat(latestRGB.getWidth(), latestRGB.getHeight(), CvType.CV_8UC4);
            Utils.bitmapToMat(bm, tmp);

            frame.close(); // Stop memory leakage

            long frameTime = System.currentTimeMillis();
            CryptoBoxResult result = (CryptoBoxResult) processor.process(frameTime, tmp, true).getResult();

            telemetry.addData("Left ball color", result.getLeftColor().toString());
            telemetry.addData("Right ball color", result.getRightColor().toString());
            telemetry.update();
        }
    }
}
