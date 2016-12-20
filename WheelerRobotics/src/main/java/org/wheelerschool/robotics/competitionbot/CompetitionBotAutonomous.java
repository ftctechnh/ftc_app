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
     *  5. PRE_WALL_FOLLOW_ANGLE
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
    public double TOWARDS_BEACON_ANGLE;
    public double PRE_WALL_FOLLOW_ANGLE; // The angle to turn to before following the wall (radians)


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
    private static double MINIMUM_ROTATION_DIFF = AngleUnit.RADIANS.fromUnit(AngleUnit.DEGREES, 5);
    private static double ROBOT_ROTATION_GAIN = 1.5;


    private void noTargetSearchRotate() throws InterruptedException {
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

    /*------------------------------------AUTONOMOUS SECTIONS-------------------------------------*/

    private Double driveToPosition(VectorF targetLocation) throws InterruptedException {
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
                VectorF translation = targetLocation.subtracted(lastLocation);

                // Calculate motor power for navigation to target:
                TranslationMotorNavigation.NavigationData calculationData =
                        translationNavigation.calculateNavigationData(translation.get(0), translation.get(1), robotRot);

                // Break if on target (and return robot rotation):
                if (calculationData.onTarget) {
                    return (double) robotRot;
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

        // Return null if interrupted:
        return null;
    }


    private boolean robotRotation(double rotationAngle, double rotationGain) {
        /**
         * Drive the motors to rotate the robot based on a target rotation angle and a rotation gain.
         *
         * Returns: (boolean) If robot is on the target or not.
         */

        // Calculate rotation power to be applied to motors:
        double rotationPower = (rotationAngle / Math.PI);
        rotationPower = (rotationPower * rotationGain);
        telemetry.addData("rotationPower", rotationPower);

        // Break if rotation angle is smaller than minimum rotation difference
        //  (on target rotation):
        if (Math.abs(rotationAngle) < MINIMUM_ROTATION_DIFF) {
            Log.d(LOG_TAG, "Rotation Angle: " + rotationAngle + " < Minimum Rot: " + MINIMUM_ROTATION_DIFF);
            return true;
        } else {
            Log.d(LOG_TAG, "Rotation Angle: " + rotationAngle + " > Minimum Rot: " + MINIMUM_ROTATION_DIFF);
        }

        // Calculate left motors power and set motors the reverse rotation of the motors
        //  cancels out the negatives:
        double leftPower = Range.clip(rotationPower, -1, 1);
        telemetry.addData("Left Motor Power", leftPower);
        DcMotorUtil.setMotorsPower(this.leftMotors, leftPower);

        // Calculate right motors power and set motors:
        double rightPower = Range.clip(rotationPower, -1, 1);
        telemetry.addData("Right Motor Power", rightPower);
        DcMotorUtil.setMotorsPower(this.rightMotors, rightPower);

        return false;
    }


    private Double rotateRobotVision(double directAngle, double rotationGain) throws InterruptedException {
        // Initiate variable for no target timeout:
        long timeSinceLastData = System.currentTimeMillis();

        while (opModeIsActive()) {
            // Read vuforia data:
            vuforia.readData();
            Orientation lastRotation = vuforia.lastRotationXYZ;

            if (lastRotation != null) {
                // Update last target read time:
                timeSinceLastData = System.currentTimeMillis();

                // Get the robot rotation from Orientation object
                double robotRotation = lastRotation.thirdAngle;

                // Calculate the needed angle of rotation to get to target:
                double rotationAngle = TranslationMotorNavigation.angleDifference(robotRotation,
                        directAngle);
                telemetry.addData("rotationAmount", rotationAngle);

                // Rotate robot using rotation angle and gain:
                boolean finishedRotation = robotRotation(rotationAngle, rotationGain);
                if (finishedRotation) {  // Break from loop if the rotation has been ended:
                    return robotRotation;
                }
            } if (System.currentTimeMillis() - timeSinceLastData > MAX_TIME_TIMEOUT) {
                // Run a target search if no targets are in sight:
                noTargetSearchRotate();
            }

            // Update telemetry:
            telemetry.update();
        }

        return null;
    }


    private Orientation getIMUOrientation() {
        /**
         * Get the IMU orientation with the designated settings.
         */
        return this.imu.getAngularOrientation().toAngleUnit(AngleUnit.RADIANS)
                .toAxesReference(AxesReference.INTRINSIC).toAxesOrder(AxesOrder.ZYX);
    }

    private void rotateRobotIMU(double angle, double rotationGain) {
        /**
         * Rotate the robot by a certain degree angle using the IMU.
         */
        // Record initial angle:
        Orientation initialAngle = getIMUOrientation();
        // Get robot heading:
        double initialRotation = initialAngle.firstAngle;
        // Add rotation to initial angle and make sure that it is a -180 - 180 value:
        double targetAngle = TranslationMotorNavigation.angleDifference(angle + initialRotation, 0);

        // Loop while OpMode is active
        while (opModeIsActive()) {
            telemetry.addData("Phase", "Rotate to Angle");
            // Get current robot angle:
            double robotAngle = getIMUOrientation().firstAngle;

            // Calculate the needed angle of rotation to get to target:
            double rotationAngle = TranslationMotorNavigation.angleDifference(robotAngle, targetAngle);
            telemetry.addData("rotationAmount", rotationAngle);
            // Calculate rotation power to be applied to motors:
            double rotationPower = (rotationAngle / Math.PI);
            rotationPower = (rotationPower * rotationGain);
            telemetry.addData("rotationPower", rotationPower);

            // Break if rotation angle is smaller than minimum rotation difference
            //  (on target rotation):
            if (Math.abs(rotationAngle) < MINIMUM_ROTATION_DIFF) {
                Log.d(LOG_TAG, "Rotation Angle: " + rotationAngle + " < Minimum Rot: " + MINIMUM_ROTATION_DIFF);
                break;
            }

            // Calculate left motors power and set motors the reverse rotation of the motors
            //  cancels out the negatives:
            double leftPower = Range.clip(rotationPower, -1, 1);
            telemetry.addData("Left Motor Power", leftPower);
            DcMotorUtil.setMotorsPower(this.leftMotors, leftPower);

            // Calculate right motors power and set motors:
            double rightPower = Range.clip(rotationPower, -1, 1);
            telemetry.addData("Right Motor Power", rightPower);
            DcMotorUtil.setMotorsPower(this.rightMotors, rightPower);

            // Update telemetry:
            telemetry.update();
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

        //      Drive to the wall:
        Double robotRot = driveToPosition(INITAL_LOCATION);
        // Log final robot angle:
        Log.d(LOG_TAG, "Robot Angle: " + robotRot);

        //      Rotate to wall (using Vuforia):
        Log.d(LOG_TAG, "Rotate to: " + TOWARDS_BEACON_ANGLE);
        robotRot = rotateRobotVision(TOWARDS_BEACON_ANGLE, ROBOT_ROTATION_GAIN);
        Log.d(LOG_TAG, "SHOULD BE FACING BEACON");
        // Log final robot angle:
        Log.d(LOG_TAG, "Robot Angle: " + robotRot);

        // Sleep to break between rotate towards wall and rotate away
        Thread.sleep(1000);

        //      Rotate to follow wall:
        // Log the needed angle:
        Log.d(LOG_TAG, "Needed Angle: " + PRE_WALL_FOLLOW_ANGLE);

        // Only continue if wasn't interrupted:
        if (robotRot != null) {
            // Calculate needed relative rotation:
            double rotationAngle = TranslationMotorNavigation.angleDifference(PRE_WALL_FOLLOW_ANGLE, robotRot);
            // Log the relative rotation:
            Log.d(LOG_TAG, "Rotation Angle: " + rotationAngle);
            // Start IMU based robot rotation:
            rotateRobotIMU(rotationAngle, ROBOT_ROTATION_GAIN);
        } else {  // This means that the drive to position was interrupted:
            Log.e(LOG_TAG, "Final Robot angle was 'null' (interrupted). ENDING!");
        }
    }
}
