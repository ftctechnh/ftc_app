package org.firstinspires.ftc.teamcode.seasons.velocityvortex;

import com.qualcomm.hardware.modernrobotics.ModernRoboticsI2cGyro;
import com.qualcomm.hardware.modernrobotics.ModernRoboticsI2cRangeSensor;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.LightSensor;
import com.qualcomm.robotcore.hardware.OpticalDistanceSensor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

/**
 * Created by ftc6347 on 10/16/16.
 */
public abstract class LinearOpModeBase extends LinearOpMode {

    private static final int WHEEL_DIAMETER_INCHES = 4;

    private static final int COUNTS_PER_MOTOR_REV = 1120;

    protected static final int COUNTS_PER_INCH = (int)(COUNTS_PER_MOTOR_REV /
            (WHEEL_DIAMETER_INCHES * Math.PI));

    private DcMotorController attachmentsController;

    private DcMotor frontLeftDrive;
    private DcMotor frontRightDrive;
    private DcMotor backLeftDrive;
    private DcMotor backRightDrive;

    private DcMotor launcherMotor;
    private DcMotor intakeMotor;

    private Servo blue1;
    private Servo red2;
    private Servo door3;

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

        attachmentsController = hardwareMap.dcMotorController.get("mc3");

        frontLeftDrive = hardwareMap.dcMotor.get("fl");
        frontRightDrive = hardwareMap.dcMotor.get("fr");
        backLeftDrive = hardwareMap.dcMotor.get("bl");
        backRightDrive = hardwareMap.dcMotor.get("br");

        launcherMotor = hardwareMap.dcMotor.get("launcher");
        intakeMotor = hardwareMap.dcMotor.get("intake");

        blue1 = hardwareMap.servo.get("b1");    // Up =.3 Down =1.0
        red2 = hardwareMap.servo.get("r2");     //Up =.7 Down =0.0
        door3 = hardwareMap.servo.get("d3");   //Closed = 0.55 Open = 0.25

        frontLightSensor = hardwareMap.lightSensor.get("fls");
        backLightSensor = hardwareMap.lightSensor.get("bls");

        frontLightSensor.enableLed(true);
        backLightSensor.enableLed(true);

        colorSensor = hardwareMap.colorSensor.get("clr");
        colorSensor.enableLed(false);

        frontRange = hardwareMap.get(ModernRoboticsI2cRangeSensor.class, "frs");
        leftRange = hardwareMap.get(ModernRoboticsI2cRangeSensor.class, "lrs");

        launcherOds = hardwareMap.opticalDistanceSensor.get("launcherOds");
        diskOds = hardwareMap.opticalDistanceSensor.get("diskOds");
        ods3 = hardwareMap.opticalDistanceSensor.get("ods3");

        gyroSensor = (ModernRoboticsI2cGyro)hardwareMap.gyroSensor.get("gy");

        // reverse all drive motors
        backLeftDrive.setDirection(DcMotorSimple.Direction.REVERSE);
        backRightDrive.setDirection(DcMotorSimple.Direction.REVERSE);
        frontLeftDrive.setDirection(DcMotorSimple.Direction.REVERSE);
        frontRightDrive.setDirection(DcMotorSimple.Direction.REVERSE);

        // initialize servo positions
        blue1.setPosition(1.0);
        red2.setPosition(0.0);
        door3.setPosition(0.53);

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

    protected void claimBeacon() {
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

        resetDriveEncoders();

        // drive backward
        encoderDrive(0.5, -2, -2);

        // check for red
        if(getColorSensor().red() > 0) {
            getRobotRuntime().reset();
            while(opModeIsActive() && getRobotRuntime().milliseconds() < 5000) {
                idle();
            }

            // second push
            while(opModeIsActive() && getFrontRange().cmUltrasonic() >= 5) {
                driveForward(0.2);
            }

            resetDriveEncoders();

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
        int leftTarget = (int)(leftInches * LinearOpModeBase.COUNTS_PER_INCH);
        int rightTarget = (int)(rightInches * LinearOpModeBase.COUNTS_PER_INCH);

        // set the target position for each motor
        getFrontLeftDrive().setTargetPosition(leftTarget);
        getFrontRightDrive().setTargetPosition(-rightTarget);
        getBackRightDrive().setTargetPosition(-rightTarget);
        getBackLeftDrive().setTargetPosition(leftTarget);

        // set RUN_TO_POSITION for each motor
        getFrontLeftDrive().setMode(DcMotor.RunMode.RUN_TO_POSITION);
        getFrontRightDrive().setMode(DcMotor.RunMode.RUN_TO_POSITION);
        getBackRightDrive().setMode(DcMotor.RunMode.RUN_TO_POSITION);
        getBackLeftDrive().setMode(DcMotor.RunMode.RUN_TO_POSITION);

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

        resetDriveEncoders();

        // set RUN_WITHOUT_ENCODER for each motor
        getFrontLeftDrive().setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        getFrontRightDrive().setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        getBackRightDrive().setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        getBackLeftDrive().setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
    }

    protected void encoderStrafe(double speed, double frontInches, double backInches) {
        int frontTarget = (int)(frontInches * LinearOpModeBase.COUNTS_PER_INCH);
        int backTarget = (int)(backInches * LinearOpModeBase.COUNTS_PER_INCH);

        // set the target position for each motor
        getFrontLeftDrive().setTargetPosition(frontTarget);
        getFrontRightDrive().setTargetPosition(frontTarget);

        getBackRightDrive().setTargetPosition(-backTarget);
        getBackLeftDrive().setTargetPosition(-backTarget);

        // set RUN_TO_POSITION for each motor
        getFrontLeftDrive().setMode(DcMotor.RunMode.RUN_TO_POSITION);
        getFrontRightDrive().setMode(DcMotor.RunMode.RUN_TO_POSITION);
        getBackRightDrive().setMode(DcMotor.RunMode.RUN_TO_POSITION);
        getBackLeftDrive().setMode(DcMotor.RunMode.RUN_TO_POSITION);

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

        resetDriveEncoders();

        // set RUN_WITHOUT_ENCODER for each motor
        getFrontLeftDrive().setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        getFrontRightDrive().setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        getBackRightDrive().setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        getBackLeftDrive().setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
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

    protected void resetDriveEncoders() {
        // reset the encoders
        backLeftDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        backRightDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        frontLeftDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        frontRightDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
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

    protected Servo getBlue1() { return blue1; }

    protected Servo getRed2() { return red2; }

    protected Servo getDoor3() { return door3; }

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
