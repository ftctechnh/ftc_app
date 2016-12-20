package org.chathamrobotics.ftcutils;

import org.lasarobotics.vision.android.Cameras;
import org.lasarobotics.vision.detection.objects.Rectangle;
import org.lasarobotics.vision.ftc.resq.Beacon;
import org.lasarobotics.vision.opmode.LinearVisionOpMode;
import org.lasarobotics.vision.opmode.extensions.CameraControlExtension;
import org.lasarobotics.vision.util.ScreenOrientation;
import org.opencv.core.Point;
import org.opencv.core.Size;

/**
 * autonomous template for use with FTCVision
 */

public abstract class AutonomousVisionOpMode extends LinearVisionOpMode {
    // Config
    final static Size defaultFrameSize = new Size(900, 900);
    final static ScreenOrientation defaultOrientation = ScreenOrientation.LANDSCAPE;

    // State
    public OmniWheelDriver driver;

    /*
     * Whether the current team is red
     */
    public boolean isRedTeam;



    /*
     * Initializes robot
     */
    public void initRobot() {
        driver = OmniWheelDriver.build(this);

        // Set to front facing camera
        this.setCamera(Cameras.PRIMARY);

        // Set frame size
        this.setFrameSize(defaultFrameSize);

        // Enable extensions
        enableExtension(Extensions.BEACON);
        enableExtension(Extensions.ROTATION);
        enableExtension(Extensions.CAMERA_CONTROL);

        // Set beacon analysis method
        beacon.setAnalysisMethod(Beacon.AnalysisMethod.FAST);

        // Set color tolerance
        beacon.setColorToleranceRed(0);
        beacon.setColorToleranceBlue(0);

//        beacon.setAnalysisBounds(new Rectangle(new Point(width / 2, height / 2), width - 200, 200));

        // Set rotation settings
        rotation.setIsUsingSecondaryCamera(false);
        rotation.disableAutoRotate();
        rotation.setActivityOrientationFixed(defaultOrientation);

        // Camera controls
        cameraControl.setColorTemperature(CameraControlExtension.ColorTemperature.AUTO);
        cameraControl.setAutoExposureCompensation();
    }

    /*
     * Called on start
     */
    abstract public void runRobot() throws StoppedException;

    /*
     * called on stop
     */
    public void stopRobot() {
        OpModeTools.stop(this);
    }

    /*
     * Runs OpMode. Duh!
     */
    @Override
    public void runOpMode() throws InterruptedException {
        waitForVisionStart();

        initRobot();

        // Wait for start call
        waitForStart();

        debug();

        try {
            runRobot();
        }
        catch (StoppedException error) {
            //Just continue to robot stop
        }
        finally {
            debug();
            stopRobot();
        }
    }

    /*
     * periodically checks for stop and updates telemetry
     */
    public void statusCheck() throws StoppedException {
        debug();
        checkForStop();
    }

    /*
     * Updates telemetry readings
     */
    public void debug() {
        telemetry.addData("Beacon Color", beacon.getAnalysis().getColorString());
        telemetry.addData("Beacon Center", beacon.getAnalysis().getLocationString());
        telemetry.addData("Beacon Confidence", beacon.getAnalysis().getConfidenceString());
        telemetry.addData("Beacon Buttons", beacon.getAnalysis().getButtonString());
        telemetry.addData("Screen Rotation", rotation.getScreenOrientationActual());
        telemetry.addData("Frame Rate", fps.getFPSString() + " FPS");
        telemetry.addData("Frame Size", "Width: " + width + " Height: " + height);

        OpModeTools.debug(this);
    }

    /*
     * Checks if opmode is still active and if it's not throws a StoppedException
     */
    public void checkForStop() throws StoppedException{
        if (! opModeIsActive()) throw new StoppedException();
    }
}
