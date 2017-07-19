package com.qualcomm.ftcrobotcontroller.opmodes;

import org.lasarobotics.vision.android.Cameras;
import org.lasarobotics.vision.detection.ColorBlobDetector;
import org.lasarobotics.vision.ftc.resq.Beacon;
import org.lasarobotics.vision.opmode.ManualVisionOpMode;
import org.lasarobotics.vision.util.ScreenOrientation;
import org.lasarobotics.vision.util.color.ColorHSV;
import org.opencv.core.Mat;
import org.opencv.core.Size;

/**
 * Manual Vision Sample
 * <p/>
 * Use when you need absolute control of each frame and want to customize
 * how Vision works for you. In a ManualVisionOpMode, you have far more control
 * and can even use the entirety of OpenCV for your own custom processing.
 * <p/>
 * Please note that you cannot use any Vision Extensions in a ManualVisionOpMode, but you
 * can still call the extensions' init(), loop(), and frame() methods if you want to use them,
 */
public class ManualVisionSample extends ManualVisionOpMode {

    private static final ColorHSV lowerBoundRed = new ColorHSV((int) (305 / 360.0 * 255.0), (int) (0.200 * 255.0), (int) (0.300 * 255.0));
    private static final ColorHSV upperBoundRed = new ColorHSV((int) ((360.0 + 5.0) / 360.0 * 255.0), 255, 255);
    private static final ColorHSV lowerBoundBlue = new ColorHSV((int) (170.0 / 360.0 * 255.0), (int) (0.200 * 255.0), (int) (0.750 * 255.0));
    private static final ColorHSV upperBoundBlue = new ColorHSV((int) (227.0 / 360.0 * 255.0), 255, 255);
    private Beacon.BeaconAnalysis colorAnalysis = new Beacon.BeaconAnalysis();
    private ColorBlobDetector detectorRed;
    private ColorBlobDetector detectorBlue;

    @Override
    public void init() {
        super.init();

        /* Initialize all detectors here */
        detectorRed = new ColorBlobDetector(lowerBoundRed, upperBoundRed);
        detectorBlue = new ColorBlobDetector(lowerBoundBlue, upperBoundBlue);

        /**
         * Set the camera used for detection
         * PRIMARY = Front-facing, larger camera
         * SECONDARY = Screen-facing, "selfie" camera :D
         **/
        this.setCamera(Cameras.PRIMARY);

        /**
         * Set the frame size
         * Larger = sometimes more accurate, but also much slower
         * After this method runs, it will set the "width" and "height" of the frame
         **/
        this.setFrameSize(new Size(900, 900));
    }

    @Override
    public void loop() {
        super.loop();

        telemetry.addData("Vision FPS", fps.getFPSString());
        telemetry.addData("Vision Color", colorAnalysis.getColorString());
        telemetry.addData("Analysis Confidence", colorAnalysis.getConfidenceString());
        telemetry.addData("Vision Size", "Width: " + width + " Height: " + height);
    }

    @Override
    public void stop() {
        super.stop();
    }

    @Override
    public Mat frame(Mat rgba, Mat gray) {
        try {
            //Prepare beacon instance
            Beacon beacon = new Beacon(Beacon.AnalysisMethod.FAST);
            //You may need to change the Screen Orientation to your preference
            ScreenOrientation orientation = ScreenOrientation.LANDSCAPE_REVERSE;
            //Analyze the frame and return the analysis
            colorAnalysis = beacon.analyzeFrame(detectorBlue, detectorRed, rgba, gray,
                    orientation);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return rgba;
    }
}
