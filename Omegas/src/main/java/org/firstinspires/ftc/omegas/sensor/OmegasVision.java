package org.firstinspires.ftc.omegas.sensor;

import android.os.Looper;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.omegas.HardwareOmegas;
import org.lasarobotics.vision.android.Cameras;
import org.lasarobotics.vision.ftc.velocityvortex.Beacon;
import org.lasarobotics.vision.opmode.LinearVisionOpMode;
import org.lasarobotics.vision.opmode.extensions.CameraControlExtension;
import org.lasarobotics.vision.util.ScreenOrientation;
import org.opencv.core.Mat;
import org.opencv.core.Size;

/**
 * Created by ethertyper on 11/14/16.
 * <p>
 * Copyright (c) 2015 LASA Robotics and Contributors
 * MIT licensed
 */

@Autonomous(name = "Omegas: FTC-Vision Test", group = "Tests")
public class OmegasVision extends LinearVisionOpMode {

    /* Declare OpMode members. */
    private ElapsedTime runtime = new ElapsedTime();
    HardwareOmegas Ω = new HardwareOmegas() {
        @Override
        public void init() {

        }
    };

    // IPS Units
    static final double FORWARD_SPEED = 0.6;
    static final double TURN_SPEED = 0.5;

    // Frame counter
    private int frameCount = 0;

    @Override
    public void runOpMode() throws InterruptedException {
        telemetry.addData("Status", "Initialized");
        telemetry.update();

        /**
         * Initialize the hardware variables. Note that the strings used here as parameters
         * to 'get' must correspond to the names assigned during the robot configuration
         * step (using the FTC Robot Controller app on the phone).
         */
        Ω.init();

        // Wait for the game to start and vision to initialize (driver presses PLAY)
        waitForStart();
        waitForVisionStart();
        runtime.reset();

        try {
            Looper.prepare();

            /**
             * Set the camera used for detection
             * PRIMARY = Front-facing, larger camera
             * SECONDARY = Screen-facing, "selfie" camera :D
             **/
            setCamera(Cameras.PRIMARY);

            /**
             * Set the frame size
             * Larger = sometimes more accurate, but also much slower
             * After this method runs, it will set the "width" and "height" of the frame
             **/
            setFrameSize(new Size(900, 900));

            /**
             * Enable extensions. Use what you need.
             * If you turn on the BEACON extension, it's best to turn on ROTATION too.
             */
            enableExtension(Extensions.BEACON);         // Beacon detection
            enableExtension(Extensions.ROTATION);       // Automatic screen rotation correction
            enableExtension(Extensions.CAMERA_CONTROL); // Manual camera control

            /**
             * Set the beacon analysis method
             * Try them all and see what works!
             */
            beacon.setAnalysisMethod(Beacon.AnalysisMethod.FAST);

            /**
             * Set color tolerances
             * 0 is default, -1 is minimum and 1 is maximum tolerance
             */
            beacon.setColorToleranceRed(0);
            beacon.setColorToleranceBlue(0);

            /**
             * Set the rotation parameters of the screen
             * If colors are being flipped or output appears consistently incorrect, try changing these.
             *
             * First, tell the extension whether you are using a secondary camera
             * (or in some devices, a front-facing camera that reverses some colors).
             *
             * It's a good idea to disable global auto rotate in Android settings. You can do this
             * by calling disableAutoRotate() or enableAutoRotate().
             *
             * It's also a good idea to force the phone into a specific orientation (or auto rotate) by
             * calling either setActivityOrientationAutoRotate() or setActivityOrientationFixed(). If
             * you don't, the camera reader may have problems reading the current orientation.
             */
            rotation.setIsUsingSecondaryCamera(false);
            rotation.disableAutoRotate();
            rotation.setActivityOrientationFixed(ScreenOrientation.PORTRAIT);

            /**
             * Set camera control extension preferences
             *
             * Enabling manual settings will improve analysis rate and may lead to better results under
             * tested conditions. If the environment changes, expect to change these values.
             */
            cameraControl.setColorTemperature(CameraControlExtension.ColorTemperature.AUTO);
            cameraControl.setAutoExposureCompensation();

            // Wait for the match to begin
            waitForStart();

            // Main loop
            // Camera frames and OpenCV analysis will be delivered to this method as quickly as possible
            // This loop will exit once the opmode is closed
            while (opModeIsActive()) {
                // Log a few things
                telemetry.addData("Beacon Color", beacon.getAnalysis().getColorString());
                telemetry.addData("Beacon Center", beacon.getAnalysis().getLocationString());
                telemetry.addData("Beacon Confidence", beacon.getAnalysis().getConfidenceString());
                telemetry.addData("Beacon Buttons", beacon.getAnalysis().getButtonString());
                telemetry.addData("Screen Rotation", rotation.getScreenOrientationActual());
                telemetry.addData("Frame Rate", fps.getFPSString() + " FPS");
                telemetry.addData("Frame Size", "Width: " + width + " Height: " + height);
                telemetry.addData("Frame Counter", frameCount);

                // You can access the most recent frame data and modify it here using getFrameRgba() or getFrameGray()
                // Vision will run asynchronously (parallel) to any user code so your programs won't hang
                // You can use hasNewFrame() to test whether vision processed a new frame
                // Once you copy the frame, discard it immediately with discardFrame()
                if (hasNewFrame()) {
                    // Get the frame
                    Mat rgba = getFrameRgba();
                    Mat gray = getFrameGray();

                    // Discard the current frame to allow for the next one to render
                    discardFrame();

                    // Do all of your custom frame processing here
                    // For this demo, let's just add to a frame counter
                    frameCount++;
                }

                // Wait for a hardware cycle to allow other processes to run
                waitOneFullHardwareCycle();
            }
        } catch (InterruptedException e) {
            telemetry.addData("ERROR", e.toString());
        }
    }
}