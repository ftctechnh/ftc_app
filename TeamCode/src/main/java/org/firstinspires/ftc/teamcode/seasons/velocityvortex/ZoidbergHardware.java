package org.firstinspires.ftc.teamcode.seasons.velocityvortex;

import com.qualcomm.hardware.modernrobotics.ModernRoboticsI2cGyro;
import com.qualcomm.hardware.modernrobotics.ModernRoboticsI2cRangeSensor;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.GyroSensor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.I2cAddr;
import com.qualcomm.robotcore.hardware.I2cDevice;
import com.qualcomm.robotcore.hardware.I2cDeviceSynch;
import com.qualcomm.robotcore.hardware.LightSensor;
import com.qualcomm.robotcore.hardware.OpticalDistanceSensor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

/**
 * Created by ftc6347 on 10/16/16.
 */
public class ZoidbergHardware {

    public static final int WHEEL_DIAMETER_INCHES = 4;

    public static final int COUNTS_PER_MOTOR_REV = 1120;

    public static final int COUNTS_PER_INCH = (int)(COUNTS_PER_MOTOR_REV /
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

    private ElapsedTime runtime;

    public ZoidbergHardware(HardwareMap hardwareMap) throws InterruptedException {
        // initialize runtime instance variable
        runtime = new ElapsedTime();

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

//        gyroSensor = (ModernRoboticsI2cGyro)hardwareMap.gyroSensor.get("gy");
//        gyroSensor.calibrate();

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
    }

    public void driveRight(double power) {
        frontLeftDrive.setPower(power);
        backLeftDrive.setPower(-power);
        frontRightDrive.setPower(power);
        backRightDrive.setPower(-power);
    }

    public void driveLeft(double power) {
        frontLeftDrive.setPower(-power);
        backLeftDrive.setPower(power);
        frontRightDrive.setPower(-power);
        backRightDrive.setPower(power);
    }

    public void driveForward(double power) {
        frontLeftDrive.setPower(power);
        backLeftDrive.setPower(power);
        frontRightDrive.setPower(-power);
        backRightDrive.setPower(-power);
    }

    public void backwardPull(double power) {
        backRightDrive.setPower(power);
        backLeftDrive.setPower(-power);
    }

    public void driveBackward(double power) {
        frontLeftDrive.setPower(-power);
        backLeftDrive.setPower(-power);
        frontRightDrive.setPower(power);
        backRightDrive.setPower(power);
    }

    public void stopRobot() {
        frontLeftDrive.setPower(0);
        frontRightDrive.setPower(0);
        backLeftDrive.setPower(0);
        backRightDrive.setPower(0);
    }

    public void pivotLeft(double power) {
        frontLeftDrive.setPower(power);
        frontRightDrive.setPower(power);
        backLeftDrive.setPower(power);
        backRightDrive.setPower(power);
    }

    public void pivotRight(double power) {
        frontLeftDrive.setPower(-power);
        frontRightDrive.setPower(-power);
        backLeftDrive.setPower(-power);
        backRightDrive.setPower(-power);
    }

    public void resetDriveEncoders() {
        // reset the encoders
        backLeftDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        backRightDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        frontLeftDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        frontRightDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
    }

    public void launchParticle() {
        runtime.reset();

        // run launcher motor for an entire rotation
        while(runtime.milliseconds() < 900) {
            launcherMotor.setPower(1.0);
        }

        // stop the launcher motor on the black line
        while(diskOds.getRawLightDetected() > 1) {
            launcherMotor.setPower(0.3);
        }

        launcherMotor.setPower(0);
    }

    public boolean areDriveMotorsBusy() {
        return frontLeftDrive.isBusy() && frontRightDrive.isBusy()
                && backLeftDrive.isBusy() && backRightDrive.isBusy();
    }

    public DcMotor getIntakeMotor() {
        return intakeMotor;
    }

    public DcMotor getFrontLeftDrive() {
        return frontLeftDrive;
    }

    public DcMotor getFrontRightDrive() {
        return frontRightDrive;
    }

    public DcMotor getBackLeftDrive() {
        return backLeftDrive;
    }

    public DcMotor getBackRightDrive() {
        return backRightDrive;
    }

    public DcMotor getLauncherMotor() {
        return launcherMotor;
    }

    public Servo getBlue1() { return blue1; }

    public Servo getRed2() { return red2; }

    public Servo getDoor3() { return door3; }

    public LightSensor getFrontLightSensor() {
        return frontLightSensor;
    }

    public LightSensor getBackLightSensor() {
        return backLightSensor;
    }

    public ColorSensor getColorSensor() {
        return colorSensor;
    }

    public ModernRoboticsI2cRangeSensor getFrontRange() {
        return frontRange;
    }

    public ModernRoboticsI2cRangeSensor getLeftRange() {
        return leftRange;
    }

    public OpticalDistanceSensor getLauncherOds() { return launcherOds; }

    public OpticalDistanceSensor getDiskOds() { return diskOds; }

    public OpticalDistanceSensor getOds3() {
        return ods3;
    }

    public ModernRoboticsI2cGyro getGyroSensor() {
        return gyroSensor;
    }

    public ElapsedTime getRuntime() {
        return runtime;
    }

}
