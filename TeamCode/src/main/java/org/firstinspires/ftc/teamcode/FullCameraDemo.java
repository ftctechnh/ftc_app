package org.firstinspires.ftc.teamcode;

import com.qualcomm.ftcrobotcontroller.R;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackable;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackableDefaultListener;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackables;

import java.util.ArrayList;
import java.util.List;

/*import org.opencv.android.Utils;
import org.opencv.core.CvType;
import org.opencv.core.Mat;*/

/*import ftc.vision.BallProcessor;
import ftc.vision.CryptoBoxResult;
import ftc.vision.ImageProcessor;*/

@Autonomous(name="Nullbot: Full camera test", group ="Concept")
public class FullCameraDemo extends LinearOpMode {

    VuforiaLocalizer vuforia;
    //ImageProcessor processor;

    @Override public void runOpMode() throws InterruptedException {

        VuforiaLocalizer.Parameters parameters = new VuforiaLocalizer.Parameters(R.id.cameraMonitorViewId);
        parameters.vuforiaLicenseKey = "Ac4jpF3/////AAAAGYER4VUDLEYGlD++ha+MStuNhKORp/7DQz1D1+tQwcrsMnbQwLqRgpkFtCOIGrZ942gdL179juAJmdXeeH+Dk0pVgxLFq6O0AzY1MS3wS5JHvSLppO9v8W//finYio3hQk+TFKD+qWq9Q1nAZx0bMWFeF6IuIjUPQLioBzC/lYzI/L7oi/AJAbFlf6wue3gDs0dgwrAgpe+JFHTgM3g2+y4hS6O0mcJjobAWSNeRxq9caOGfl/q6f09Eu2EccSmHLAaqje0i70eAIZ4Tbg5C31sPZxBOPTEGTQ9NvFhP4FNAXlvPCdiBt6XYE8P17UzPN72p7lRKyp4xR1oC8B/4dYbivso+rQUed5/H7AnQYOdA";
        parameters.cameraDirection = VuforiaLocalizer.CameraDirection.BACK;
        this.vuforia = ClassFactory.createVuforiaLocalizer(parameters);

        VuforiaTrackables pictographAssets = this.vuforia.loadTrackablesFromAsset("FirstRelicRecoveryPictographs");

        VuforiaTrackable blueCryptoBox  = pictographAssets.get(0);
        blueCryptoBox.setName("BlueCryptoBox");

        VuforiaTrackable rightPictograph = pictographAssets.get(1);
        rightPictograph.setName("RightTarget");

        VuforiaTrackable centerPictograph  = pictographAssets.get(2);
        centerPictograph.setName("CenterTarget");

        VuforiaTrackable leftPictograph  = pictographAssets.get(3);
        leftPictograph.setName("LeftTarget");

        List<VuforiaTrackable> trackables = new ArrayList<VuforiaTrackable>();
        trackables.addAll(pictographAssets);


        //processor = new BallProcessor();

        telemetry.addData(">", "Press Play to start tracking");
        telemetry.update();
        waitForStart();

        // Begin looking for pictographs
        pictographAssets.activate();

        String pictographLocation = null;

        while (opModeIsActive()) {

            for (VuforiaTrackable trackable : trackables) {
                boolean seen = ((VuforiaTrackableDefaultListener)trackable.getListener()).isVisible();
                telemetry.addData(trackable.getName(), seen ? "Visible" : "Not Visible");
                if (seen) {
                    pictographLocation = trackable.getName();
                    break;
                }
            }
            telemetry.update();
        }

        // Stop looking for pictographs
        pictographAssets.deactivate();
        telemetry.log().add("Switching to ball identification mode");


        // Start using openCV for ball recognition
        /*while (opModeIsActive()) {
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
        }*/
    }
}
