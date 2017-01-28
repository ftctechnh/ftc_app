package org.firstinspires.ftc.teamcode.seasons.velocityvortex;

import com.qualcomm.hardware.modernrobotics.ModernRoboticsI2cGyro;
import com.qualcomm.hardware.modernrobotics.ModernRoboticsI2cRangeSensor;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.LightSensor;
import com.qualcomm.robotcore.hardware.OpticalDistanceSensor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;

/**
 * Created by ftc6347 on 10/16/16.
 */
public abstract class LinearOpModeBase extends LinearOpMode {

    private static final int WHEEL_DIAMETER_INCHES = 4;

    private static final int COUNTS_PER_MOTOR_REV = 1120;

    private static final double GYRO_ERROR_THRESHOLD = 5;

    private static final double P_GYRO_TURN_COEFF = 0.01;

    protected static final int COUNTS_PER_INCH = (int)(COUNTS_PER_MOTOR_REV /
            (WHEEL_DIAMETER_INCHES * Math.PI));

    private DcMotor frontLeftDrive;
    private DcMotor frontRightDrive;
    private DcMotor backLeftDrive;
    private DcMotor backRightDrive;

    private DcMotor launcherMotor;
    private DcMotor intakeMotor;

    private DcMotor spoolMotor1;
    private DcMotor spoolMotor2;

    private Servo beaconsServo1;
    private Servo beaconsServo2;
    private Servo door3;
    private Servo latch4;

    private LightSensor frontLightSensor;
    private LightSensor backLightSensor;

    private ColorSensor colorSensor;

    private ModernRoboticsI2cRangeSensor frontRange;
    private ModernRoboticsI2cRangeSensor leftRange;

    private OpticalDistanceSensor launcherOds;
    private OpticalDistanceSensor diskOds;
    private OpticalDistanceSensor ods3;

    private ModernRoboticsI2cGyro gyroSensor;

    private ElapsedTime robotRuntime;

    protected void initializeHardware() {
        // initialize robotRuntime instance variable
        robotRuntime = new ElapsedTime();

        frontLeftDrive = hardwareMap.dcMotor.get("fl");
        frontRightDrive = hardwareMap.dcMotor.get("fr");
        backLeftDrive = hardwareMap.dcMotor.get("bl");
        backRightDrive = hardwareMap.dcMotor.get("br");

        launcherMotor = hardwareMap.dcMotor.get("launcher");
        intakeMotor = hardwareMap.dcMotor.get("intake");

        spoolMotor1 = hardwareMap.dcMotor.get("s1");
        spoolMotor2 = hardwareMap.dcMotor.get("s2");

        beaconsServo1 = hardwareMap.servo.get("b1");  // Up = 0.3, Down = 1.0
        beaconsServo2 = hardwareMap.servo.get("r2");   // Up = 0.7, Down = 0.0
        door3 = hardwareMap.servo.get("d3");  // Closed = 0.55, Open = 0.25
        latch4 = hardwareMap.servo.get("l4"); // Up = 0.5

        colorSensor = hardwareMap.colorSensor.get("clr");
        colorSensor.enableLed(false);

        frontRange = hardwareMap.get(ModernRoboticsI2cRangeSensor.class, "frs");
        leftRange = hardwareMap.get(ModernRoboticsI2cRangeSensor.class, "lrs");

        launcherOds = hardwareMap.opticalDistanceSensor.get("launcherOds");
        diskOds = hardwareMap.opticalDistanceSensor.get("diskOds");
        ods3 = hardwareMap.opticalDistanceSensor.get("ods3");

        gyroSensor = (ModernRoboticsI2cGyro)hardwareMap.gyroSensor.get("gy");

        // reverse all drive motors
        backLeftDrive.setDirection(DcMotor.Direction.REVERSE);
        backRightDrive.setDirection(DcMotor.Direction.REVERSE);
        frontLeftDrive.setDirection(DcMotor.Direction.REVERSE);
        frontRightDrive.setDirection(DcMotor.Direction.REVERSE);

        // reverse only one spool motor
        spoolMotor1.setDirection(DcMotor.Direction.REVERSE);
        spoolMotor2.setDirection(DcMotor.Direction.FORWARD);

        robotRuntime.reset();
        // initialize servo positions to up

        while(robotRuntime.seconds() < 2)
        {
            beaconsServo1.setPosition(0.7);
            beaconsServo2.setPosition(0.3);
        }
        // then put them back down
        beaconsServo1.setPosition(1.0);
        beaconsServo2.setPosition(0);


        door3.setPosition(0.53);
        latch4.setPosition(0.5);

        // stop all motors
        launcherMotor.setPower(0);
        intakeMotor.setPower(0);
        stopRobot();

        telemetry.addData(">", "Calibrating Gyro");
        telemetry.update();

        gyroSensor.calibrate();

        // make sure the gyro is calibrated before continuing
        while (!isStopRequested() && gyroSensor.isCalibrating()) {
            idle();
        }

        telemetry.addData(">", "Gyro caibrated");
        telemetry.update();
    }

    protected void claimBeaconRed() {
        // first push
        while(opModeIsActive() && getFrontRange().cmUltrasonic() >= 7) {
            // run without encoders again
            getBackLeftDrive().setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            getBackRightDrive().setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            getFrontLeftDrive().setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            getFrontRightDrive().setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            driveForward(0.2);
        }
        stopRobot();

        // pause for the beacon to change color
        getRobotRuntime().reset();
        while(opModeIsActive() && getRobotRuntime().milliseconds() < 500) {
            idle();
        }

        setDriveMotorsMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        // drive backward
        encoderDrive(0.5, -2, -2);

        // check for blue
        if(getColorSensor().blue() > 0) {
            getRobotRuntime().reset();
            while(opModeIsActive() && getRobotRuntime().milliseconds() < 5000) {
                idle();
            }

            // second push
            while(opModeIsActive() && getFrontRange().cmUltrasonic() >= 5) {
                driveForward(0.2);
            }

            setDriveMotorsMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

            // second drive backward
            encoderDrive(0.5, -2, -2);
        }

        stopRobot();
    }

    protected void claimBeaconBlue() {
        // first push
        while(opModeIsActive() && getFrontRange().cmUltrasonic() >= 7) {
            // run without encoders again
            getBackLeftDrive().setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            getBackRightDrive().setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            getFrontLeftDrive().setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            getFrontRightDrive().setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            driveForward(0.2);
        }
        stopRobot();

        // pause for the beacon to change color
        getRobotRuntime().reset();
        while(opModeIsActive() && getRobotRuntime().milliseconds() < 500) {
            idle();
        }

        setDriveMotorsMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        // drive backward
        encoderDrive(0.5, -2, -2);

        // check for blue
        if(getColorSensor().red() > 0) {
            getRobotRuntime().reset();
            while(opModeIsActive() && getRobotRuntime().milliseconds() < 5000) {
                idle();
            }

            // second push
            while(opModeIsActive() && getFrontRange().cmUltrasonic() >= 5) {
                driveForward(0.2);
            }

            setDriveMotorsMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

            // second drive backward
            encoderDrive(0.5, -2, -2);
        }

        stopRobot();
    }

    protected void launchParticle() {
        robotRuntime.reset();

        // run launcher motor for an entire rotation
        while(opModeIsActive() && robotRuntime.milliseconds() < 900) {
            launcherMotor.setPower(1.0);
        }

        // stop the launcher motor on the black line
        while(opModeIsActive() && diskOds.getRawLightDetected() > 1) {
            launcherMotor.setPower(0.3);
        }

        launcherMotor.setPower(0);
    }

    protected void encoderDrive(double speed, double leftInches, double rightInches) {
        int leftTarget = (int)(leftInches * COUNTS_PER_INCH);
        int rightTarget = (int)(rightInches * COUNTS_PER_INCH);

        // set the target position for each motor
        getFrontLeftDrive().setTargetPosition(leftTarget);
        getFrontRightDrive().setTargetPosition(-rightTarget);
        getBackRightDrive().setTargetPosition(-rightTarget);
        getBackLeftDrive().setTargetPosition(leftTarget);

        // set RUN_TO_POSITION for each motor
        setDriveMotorsMode(DcMotor.RunMode.RUN_TO_POSITION);

        // set the power for the left drive motors
        getFrontLeftDrive().setPower(speed);
        getBackLeftDrive().setPower(speed);

        // set the power for the right drive motors
        getFrontRightDrive().setPower(speed);
        getBackRightDrive().setPower(speed);

        while(opModeIsActive() && areDriveMotorsBusy()) {
            idle();
        }

        stopRobot();

        setDriveMotorsMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        // set RUN_WITHOUT_ENCODER for each motor
        setDriveMotorsMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
    }

    protected void encoderStrafe(double speed, double frontInches, double backInches) {
        int frontTarget = (int)(frontInches * COUNTS_PER_INCH);
        int backTarget = (int)(backInches * COUNTS_PER_INCH);

        // set the target position for each motor
        getFrontLeftDrive().setTargetPosition(frontTarget);
        getFrontRightDrive().setTargetPosition(frontTarget);

        getBackRightDrive().setTargetPosition(-backTarget);
        getBackLeftDrive().setTargetPosition(-backTarget);

        // set RUN_TO_POSITION for each motor
        setDriveMotorsMode(DcMotor.RunMode.RUN_TO_POSITION);

        // set the power for the left drive motors
        getFrontLeftDrive().setPower(speed);
        getBackLeftDrive().setPower(speed);

        // set the power for the right drive motors
        getFrontRightDrive().setPower(speed);
        getBackRightDrive().setPower(speed);

        while(opModeIsActive() && areDriveMotorsBusy()) {
            idle();
        }

        stopRobot();

        setDriveMotorsMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        // set RUN_WITHOUT_ENCODER for each motor
        setDriveMotorsMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
    }

    private double getGyroError(double targetAngle) {
        double error = targetAngle - gyroSensor.getIntegratedZValue();

        // keep the error on a range of -179 to 180
        while (opModeIsActive() && error > 180)  error -= 360;
        while (opModeIsActive() && error <= -180) error += 360;

        return Range.clip(error * P_GYRO_TURN_COEFF, -1, 1);
    }

    protected void driveRight(double power) {
        frontLeftDrive.setPower(power);
        backLeftDrive.setPower(-power);
        frontRightDrive.setPower(power);
        backRightDrive.setPower(-power);
    }

    protected void driveLeft(double power) {
        frontLeftDrive.setPower(-power);
        backLeftDrive.setPower(power);
        frontRightDrive.setPower(-power);
        backRightDrive.setPower(power);
    }

    protected void driveForward(double power) {
        frontLeftDrive.setPower(power);
        backLeftDrive.setPower(power);
        frontRightDrive.setPower(-power);
        backRightDrive.setPower(-power);
    }

    protected void driveBackward(double power) {
        frontLeftDrive.setPower(-power);
        backLeftDrive.setPower(-power);
        frontRightDrive.setPower(power);
        backRightDrive.setPower(power);
    }

    protected void stopRobot() {
        frontLeftDrive.setPower(0);
        frontRightDrive.setPower(0);
        backLeftDrive.setPower(0);
        backRightDrive.setPower(0);
    }

    protected void gyroPivot(double speed, double angle) {
        double steer;
        double error = getGyroError(angle);

        while(opModeIsActive() && Math.abs(error) > GYRO_ERROR_THRESHOLD) {

            telemetry.addData("error", error);
            telemetry.update();

            error = getGyroError(angle);

            steer = Range.clip(error * P_GYRO_TURN_COEFF , -1, 1);

            double proportionalSpeed = speed * steer;

            getFrontLeftDrive().setPower(proportionalSpeed);
            getFrontRightDrive().setPower(proportionalSpeed);

            getBackLeftDrive().setPower(proportionalSpeed);
            getBackRightDrive().setPower(proportionalSpeed);
        }

        // when we're on target, stop the robot
        stopRobot();
    }

    protected void pivotLeft(double power) {
        frontLeftDrive.setPower(power);
        frontRightDrive.setPower(power);
        backLeftDrive.setPower(power);
        backRightDrive.setPower(power);
    }

    protected void pivotRight(double power) {
        frontLeftDrive.setPower(-power);
        frontRightDrive.setPower(-power);
        backLeftDrive.setPower(-power);
        backRightDrive.setPower(-power);
    }

    protected void setDriveMotorsMode(DcMotor.RunMode runMode) {
        backLeftDrive.setMode(runMode);
        backRightDrive.setMode(runMode);
        frontLeftDrive.setMode(runMode);
        frontRightDrive.setMode(runMode);
    }

    protected boolean areDriveMotorsBusy() {
        return frontLeftDrive.isBusy() && frontRightDrive.isBusy()
                && backLeftDrive.isBusy() && backRightDrive.isBusy();
    }

    protected DcMotor getIntakeMotor() {
        return intakeMotor;
    }

    protected DcMotor getFrontLeftDrive() {
        return frontLeftDrive;
    }

    protected DcMotor getFrontRightDrive() {
        return frontRightDrive;
    }

    protected DcMotor getBackLeftDrive() {
        return backLeftDrive;
    }

    protected DcMotor getBackRightDrive() {
        return backRightDrive;
    }

    protected DcMotor getLauncherMotor() {
        return launcherMotor;
    }

    protected DcMotor getSpoolMotor1() {
        return spoolMotor1;
    }

    protected DcMotor getSpoolMotor2() {
        return spoolMotor2;
    }

    protected Servo getBeaconsServo1() { return beaconsServo1; }

    protected Servo getBeaconsServo2() { return beaconsServo2; }

    protected Servo getDoor3() { return door3; }

    protected Servo getLatch4() {
        return latch4;
    }

    protected LightSensor getFrontLightSensor() {
        return frontLightSensor;
    }

    protected LightSensor getBackLightSensor() {
        return backLightSensor;
    }

    protected ColorSensor getColorSensor() {
        return colorSensor;
    }

    protected ModernRoboticsI2cRangeSensor getFrontRange() {
        return frontRange;
    }

    protected ModernRoboticsI2cRangeSensor getLeftRange() {
        return leftRange;
    }

    protected OpticalDistanceSensor getLauncherOds() { return launcherOds; }

    protected OpticalDistanceSensor getDiskOds() { return diskOds; }

    protected OpticalDistanceSensor getOds3() {
        return ods3;
    }

    protected ModernRoboticsI2cGyro getGyroSensor() {
        return gyroSensor;
    }

    protected ElapsedTime getRobotRuntime() {
        return robotRuntime;
    }

}
