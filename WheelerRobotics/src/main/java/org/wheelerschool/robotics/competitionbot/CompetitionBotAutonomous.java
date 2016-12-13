package org.wheelerschool.robotics.competitionbot;

import android.util.Log;

import com.qualcomm.hardware.adafruit.BNO055IMU;
import com.qualcomm.hardware.adafruit.JustLoggingAccelerationIntegrator;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.UltrasonicSensor;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.robotcore.external.matrices.OpenGLMatrix;
import org.firstinspires.ftc.robotcore.external.matrices.VectorF;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.wheelerschool.robotics.library.navigation.TranslationMotorNavigation;
import org.wheelerschool.robotics.library.util.DcMotorUtil;
import org.wheelerschool.robotics.library.vision.VuforiaLocation;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by luciengaitskell on 11/24/16.
 */

public abstract class CompetitionBotAutonomous extends LinearOpMode {
    // Variables to set when extending class:
    /**
     * What to set:
     *  1. closeMotors
     *  2. fartherMotors
     *  3. sideUltrasonicSensor
     *  4. INITAL_LOCATION
     */
    //      Wall Follow Motors:
    public List<DcMotor> closeMotors;
    public double closeMotorGain;
    public List<DcMotor> fartherMotors;
    public double fartherMotorGain;
    //      Sensors:
    public UltrasonicSensor sideUltrasonicSensor;
    //      Other:
    public VectorF INITAL_LOCATION;


    // Hardware Setup:
    //      Motor:
    public List<DcMotor> leftMotors = new ArrayList<>();
    public List<DcMotor> rightMotors = new ArrayList<>();
    //      IMU:
    BNO055IMU imu;

    // Setup:
    //      Phone Location
    private OpenGLMatrix phoneLocation = OpenGLMatrix
            .translation((15 * VuforiaLocation.MM_PER_INCH) / 2,
                    -1.75f * VuforiaLocation.MM_PER_INCH,
                    4 * VuforiaLocation.MM_PER_INCH)
            .multiplied(Orientation.getRotationMatrix(
                    AxesReference.EXTRINSIC, AxesOrder.XYZ,
                    AngleUnit.DEGREES, 0, -90, 0));
    //      Vuforia Target Setup:
    private VuforiaLocation vuforia = new VuforiaLocation(phoneLocation);

    // Config:
    public static String LOG_TAG = "Comp Bot Auto";
    private static long MAX_TIME_TIMEOUT = 200; // MAX time until TIMEOUT when running OpMode (millis)
    private static double NO_BEACON_ROTATE_SPEED = 0.25;



    /*------------------------------------AUTONOMOUS SECTIONS-------------------------------------*/

    private void driveToPosition() throws InterruptedException {
        /*---------------------------------DRIVE TO INITIAL POINT---------------------------------*/
        // Translation Navigation Setup:
        TranslationMotorNavigation translationNavigation = new TranslationMotorNavigation();

        long time = System.currentTimeMillis();
        while (opModeIsActive()) {
            // Set 'Phase' in the telemetry:
            telemetry.addData("Phase", "Drive to Initial Position");

            // Read and get robot location data using vuforia:
            vuforia.readData();
            VectorF lastLocation = vuforia.lastLocationXYZ;
            Orientation lastRotation = vuforia.lastRotationXYZ;

            // If robot location is known:
            if (lastLocation != null && lastRotation != null) {
                time = System.currentTimeMillis();

                // Get specific values from robot location data:
                float x = lastLocation.get(0);
                float y = lastLocation.get(1);
                float robotRot = lastRotation.thirdAngle;

                // Log data for debug:
                Log.d(LOG_TAG, String.format("X: %.3f, Y: %.3f, Rot: " + lastRotation.toString(), x, y));

                // Calculate translation required to get to target:
                VectorF translation = INITAL_LOCATION.subtracted(lastLocation);

                // Calculate motor power for navigation to target:
                TranslationMotorNavigation.NavigationData calculationData =
                        translationNavigation.calculateNavigationData(translation.get(0), translation.get(1), robotRot);

                // Break if on target:
                if (calculationData.onTarget) {
                    break;
                }

                // Add data to telemetry for debug:
                telemetry.addData("On Target", calculationData.onTarget);
                telemetry.addData("Left Power", calculationData.leftMotorPower);
                telemetry.addData("Right Power", calculationData.rightMotorPower);
                telemetry.addData("Needed Distance", calculationData.translationDistance);
                telemetry.addData("Needed Angle", calculationData.translationAngle);
                telemetry.addData("Robot Angle", robotRot);
                telemetry.addData("Needed Angle Change", calculationData.rotationAmount);
                telemetry.addData("Forward Power", calculationData.forwardPower);
                telemetry.addData("Rotation Power", calculationData.rotationPower);
                telemetry.addData("Robot X", x);
                telemetry.addData("Robot Y", y);

                // Drive Motors:
                DcMotorUtil.setMotorsPower(this.leftMotors, calculationData.leftMotorPower);
                DcMotorUtil.setMotorsPower(this.rightMotors, calculationData.rightMotorPower);
            }

            // If target are not seen after some amount of time, rotate to find one:
            if (System.currentTimeMillis() - time > MAX_TIME_TIMEOUT) {
                // Log data and add to telemetry for debug:
                telemetry.addData("ERROR", "Target has not been seen in " + MAX_TIME_TIMEOUT + "ms");
                Log.d("Vuforia Data", "Lost beacon (" + MAX_TIME_TIMEOUT + "ms). Rotating...");

                // Set motors to rotate:
                DcMotorUtil.setMotorsPower(this.leftMotors, NO_BEACON_ROTATE_SPEED);
                DcMotorUtil.setMotorsPower(this.rightMotors, NO_BEACON_ROTATE_SPEED);

                // Sleep to allow for rotation:
                Thread.sleep(200);

                // Stop motors:
                DcMotorUtil.setMotorsPower(this.leftMotors, 0);
                DcMotorUtil.setMotorsPower(this.rightMotors, 0);
                Log.d("Vuforia Data", "Ended Rotation");

                // Wait to allow camera to adjust and acquire a lock on a target
                Thread.sleep(500);
            }

            // Update telemetry:
            telemetry.update();

            // Sleep to control loop:
            Thread.sleep(50);
        }
    }



    // OpMode:
    public void runOpMode() throws InterruptedException {
        // Hardware Setup:
        //      Motors:
        this.leftMotors.add(hardwareMap.dcMotor.get("frontLeft"));
        this.leftMotors.add(hardwareMap.dcMotor.get("backLeft"));
        DcMotorUtil.setMotorsRunMode(this.leftMotors, DcMotor.RunMode.RUN_USING_ENCODER);
        this.rightMotors.add(hardwareMap.dcMotor.get("frontRight"));
        this.rightMotors.add(hardwareMap.dcMotor.get("backRight"));
        DcMotorUtil.setMotorsRunMode(this.rightMotors, DcMotor.RunMode.RUN_USING_ENCODER);
        //      IMU:
        BNO055IMU.Parameters parameters = new BNO055IMU.Parameters();
        parameters.angleUnit           = BNO055IMU.AngleUnit.DEGREES;
        parameters.accelUnit           = BNO055IMU.AccelUnit.METERS_PERSEC_PERSEC;
        parameters.calibrationDataFile = "AdafruitIMUCalibration.json"; // see the calibration sample opmode
        parameters.loggingEnabled      = true;
        parameters.loggingTag          = "IMU";
        parameters.accelerationIntegrationAlgorithm = new JustLoggingAccelerationIntegrator();
        //          Retrieve and initialize the IMU:
        imu = hardwareMap.get(BNO055IMU.class, "imu");
        imu.initialize(parameters);

        // Wait for start button to be pushed:
        waitForStart();

        // Autonomous Sections:

        driveToPosition();
    }
}
