package org.firstinspires.ftc.teamcode.seasons.velocityvortex;

import com.qualcomm.hardware.modernrobotics.ModernRoboticsI2cRangeSensor;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.I2cAddr;
import com.qualcomm.robotcore.hardware.I2cDevice;
import com.qualcomm.robotcore.hardware.I2cDeviceSynch;
import com.qualcomm.robotcore.hardware.LightSensor;
import com.qualcomm.robotcore.hardware.OpticalDistanceSensor;
import com.qualcomm.robotcore.hardware.Servo;

/**
 * Created by ftc6347 on 10/16/16.
 */
public class ZoidbergHardware {

    private DcMotorController driveController1;
    private DcMotorController driveController2;

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

    private I2cDevice frontRangeSensor;
    private I2cDevice leftRangeSensor;

    private OpticalDistanceSensor launcherOds;
    private OpticalDistanceSensor diskOds;

    public static final I2cAddr FRONT_RANGE_SENSOR_I2C_ADDR = I2cAddr.create8bit(0x28);
    public static final I2cAddr LEFT_RANGE_SENSOR_I2C_ADDR = I2cAddr.create8bit(0x2a);

    public static final int RANGE_SENSOR_REG_START = 0x04;

    public ZoidbergHardware(HardwareMap hardwareMap) {

        driveController1 = hardwareMap.dcMotorController.get("mc0");
        driveController2 = hardwareMap.dcMotorController.get("mc1");
        attachmentsController = hardwareMap.dcMotorController.get("mc3");

        frontLeftDrive = hardwareMap.dcMotor.get("fl");
        frontRightDrive = hardwareMap.dcMotor.get("fr");
        backLeftDrive = hardwareMap.dcMotor.get("bl");
        backRightDrive = hardwareMap.dcMotor.get("br");

        launcherMotor = hardwareMap.dcMotor.get("launcher");
        intakeMotor = hardwareMap.dcMotor.get("intake");

        blue1 = hardwareMap.servo.get("b1");    // Up =.3 Down =1.0
        red2 = hardwareMap.servo.get("r2");     //Up =.7 Down =0.0
        door3 = hardwareMap.servo.get("d3");   //Closed = 0.5 Open = 0.25

        // initialize positions
        blue1.setPosition(1.0);
        red2.setPosition(0.0);
        door3.setPosition(0.5);

        frontLightSensor = hardwareMap.lightSensor.get("fls");
        backLightSensor = hardwareMap.lightSensor.get("bls");

        frontLightSensor.enableLed(true);
        backLightSensor.enableLed(true);

        colorSensor = hardwareMap.colorSensor.get("clr");
        colorSensor.enableLed(false);

        frontRangeSensor = hardwareMap.i2cDevice.get("frs");
        leftRangeSensor = hardwareMap.i2cDevice.get("lrs");

        launcherOds = hardwareMap.opticalDistanceSensor.get("launcherOds");
        diskOds = hardwareMap.opticalDistanceSensor.get("diskOds");
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
        frontLeftDrive.setPower(-power);
        frontRightDrive.setPower(-power);
        backLeftDrive.setPower(-power);
        backRightDrive.setPower(-power);
    }

    public void pivotRight(double power) {
        frontLeftDrive.setPower(power);
        frontRightDrive.setPower(power);
        backLeftDrive.setPower(power);
        backRightDrive.setPower(power);
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

    public I2cDevice getFrontRangeSensor() {
        return frontRangeSensor;
    }

    public I2cDevice getLeftRangeSensor() {
        return leftRangeSensor;
    }

    public OpticalDistanceSensor getLauncherOds() { return launcherOds; }

    public OpticalDistanceSensor getDiskOds() { return diskOds; }

}
