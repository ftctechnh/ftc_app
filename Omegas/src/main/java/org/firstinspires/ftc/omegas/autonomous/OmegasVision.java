package org.firstinspires.ftc.omegas.autonomous;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.omegas.HardwareOmegas;
import org.firstinspires.ftc.omegas.OmegasAlliance;
import org.firstinspires.ftc.omegas.OmegasBeacon;
import org.lasarobotics.vision.android.Cameras;
import org.lasarobotics.vision.detection.ColorBlobDetector;
import org.lasarobotics.vision.ftc.velocityvortex.Beacon;
import org.lasarobotics.vision.opmode.ManualVisionOpMode;
import org.lasarobotics.vision.util.ScreenOrientation;
import org.lasarobotics.vision.util.color.ColorHSV;
import org.opencv.core.Mat;
import org.opencv.core.Size;

import java.util.ArrayList;
import java.util.Arrays;

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

abstract class OmegasVision extends ManualVisionOpMode {

    /* Declare OpMode members. */
    private boolean shouldApproachBeaconator = false;
    private boolean approachedCapBall = false;
    private boolean startedDriving = false;
    private HardwareOmegas Ω = null;
    private Thread driveThread = null;
    private Thread beaconThread = null;
    private Thread capBallThread = null;
    private final ElapsedTime runtime = new ElapsedTime();

    private static final ColorHSV lowerBoundRed = new ColorHSV((int) (305 / 360.0 * 255.0), (int) (0.200 * 255.0), (int) (0.300 * 255.0));
    private static final ColorHSV upperBoundRed = new ColorHSV((int) ((360.0 + 5.0) / 360.0 * 255.0), 255, 255);
    private static final ColorHSV lowerBoundBlue = new ColorHSV((int) (170.0 / 360.0 * 255.0), (int) (0.200 * 255.0), (int) (0.750 * 255.0));
    private static final ColorHSV upperBoundBlue = new ColorHSV((int) (227.0 / 360.0 * 255.0), 255, 255);
    private Beacon.BeaconAnalysis colorAnalysis = new Beacon.BeaconAnalysis();
    private ColorBlobDetector detectorRed;
    private ColorBlobDetector detectorBlue;
    private final ArrayList<OmegasBeacon> beaconColorArrayList = new ArrayList<>();

    @Override
    public void init() {
        super.init();

        /* Initialize all detectors here */
        detectorRed = new ColorBlobDetector(lowerBoundRed, upperBoundRed);
        detectorBlue = new ColorBlobDetector(lowerBoundBlue, upperBoundBlue);

        Ω = new HardwareOmegas() {
            @Override
            public void init() {
                initLightSensor(hardwareMap);
                initDriveMotors(hardwareMap);
                initBeaconators(hardwareMap);
                initAppContext(hardwareMap);
                initTelemetry(telemetry);
                initAudio();

                getLightSensor().enableLed(true);
                sayMessage();
            }
        };

        driveThread = new Thread() {
            public void run() {
                while (true) {
                    if (!shouldApproachBeaconator) {
                        if (Ω.getLightSensor().getLightDetected() >= 0.4) {
                            Ω.rotate(Math.PI * 1 / 2, getColor() == OmegasAlliance.BLUE);
                            Ω.driveForward(600.0);

                            shouldApproachBeaconator = true;
                        } else {
                            for (DcMotor motor : Ω.getMotors()) {
                                motor.setPower(0.15);
                            }
                        }
                    } else {
                        return;
                    }
                }
            }
        };

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

        String[] currentBeaconColors = colorAnalysis.getColorString().split(", ");
        beaconColorArrayList.add(new OmegasBeacon(currentBeaconColors[0], currentBeaconColors[1]));

        double leftBlue = 0.0, rightBlue = 0.0;
        int leftCount = 0, rightCount = 0;
        final double light = Ω.getLightSensor().getLightDetected();

        for (int i = beaconColorArrayList.size() - 1; i > 0 && leftCount <= 100; i--) {
            if (beaconColorArrayList.get(i).left != OmegasBeacon.Color.UNDEFINED) {
                if (beaconColorArrayList.get(i).left == OmegasBeacon.Color.BLUE) leftBlue++;
                leftCount++;
            }
        }

        for (int i = beaconColorArrayList.size() - 1; i > 0 && rightCount <= 100; i--) {
            if (beaconColorArrayList.get(i).right != OmegasBeacon.Color.UNDEFINED) {
                if (beaconColorArrayList.get(i).right == OmegasBeacon.Color.BLUE) rightBlue++;
                rightCount++;
            }
        }

        leftBlue /= leftCount;
        rightBlue /= rightCount;

        telemetry.addData("Vision FPS", fps.getFPSString());
        telemetry.addData("Vision Color", Arrays.toString(currentBeaconColors));
        telemetry.addData("Analysis Confidence", "Left: " + leftBlue + " Right: " + rightBlue);
        telemetry.addData("Vision Size", "Width: " + width + " Height: " + height);
        telemetry.addData("Data", "Light amount: " + light);
        telemetry.addData("Data", "Light sensor activated: " + (light > 0.4));
        telemetry.update();

        if (!startedDriving) {
            driveThread.start();
            startedDriving = true;
        }

        if (approachedCapBall) approachCapBall();
        if (shouldApproachBeaconator) approachBeaconator(leftBlue, rightBlue);

        try {
            Thread.sleep(2);
        } catch (Exception e) {
            System.err.print("Thread.sleep failure");
        }
    }

    private void approachBeaconator(double leftBlue, double rightBlue) {
        final boolean blueBeacon = leftBlue > rightBlue;

        /**
         * Beacon: Which beacon is blue - `leftBlue > rightBlue`
         *      true: Left beacon
         *      false: Right beacon
         * Alliance: Which alliance we are - `getColor() == OmegasAlliance.RED`
         *      true: Blue alliance
         *      false: Red alliance
         * Beaconator: Which beaconator to extend - `Beacon == Alliance`
         *      true: Left beaconator
         *      false: Right beaconator
         *
         * XNOR/Equality Logic Table:
         *
         * |                       | Left (Beacon==true)       | Right (Beacon==false)     |
         * |-----------------------|---------------------------|---------------------------|
         * | Blue (Alliance==true) | Left (Beaconator==true)   | Right (Beaconator==false) |
         * | Red (Alliance==false) | Right (Beaconator==false) | Left (Beaconator==true)   |
         */
        beaconThread = new Thread() {
            @Override
            public void run() {
                /**
                 * Three times for good luck.
                 *
                 * Yes, we also believe in wizardry.
                 */
                for (int i = 0; i < 3; i++) {
                    if (blueBeacon == (getColor() == OmegasAlliance.RED)) {
                        Ω.rightBeaconatorSequence(Ω.getRightBeaconator());
                    } else {
                        Ω.leftBeaconatorSequence(Ω.getLeftBeaconator());
                    }
                }
            }
        };

        shouldApproachBeaconator = false;
        beaconThread.start();
    }

    private void approachCapBall() {
        capBallThread = new Thread() {
            public void run() {
                runtime.reset();

                while (runtime.milliseconds() <= 2600) {
                    for (DcMotor motor : Ω.getMotors()) {
                        motor.setPower((runtime.milliseconds() < 2500) ? -0.25 : 0.0);
                    }
                }
            }
        };

        approachedCapBall = true;
        capBallThread.start();
    }

    abstract OmegasAlliance getColor();

    @Override
    public void stop() {
        super.stop();

        driveThread.interrupt();
        if (beaconThread != null) beaconThread.interrupt();
        if (capBallThread != null) capBallThread.interrupt();
    }

    @Override
    public Mat frame(Mat rgba, Mat gray) {
        try {
            //Prepare beacon instance
            Beacon beacon = new Beacon(Beacon.AnalysisMethod.COMPLEX);
            //You may need to change the Screen Orientation to your preference
            ScreenOrientation orientation = ScreenOrientation.PORTRAIT;
            //Analyze the frame and return the analysis
            colorAnalysis = beacon.analyzeFrame(detectorBlue, detectorRed, rgba, gray,
                    orientation);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return rgba;
    }
}
