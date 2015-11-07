package com.qualcomm.opmodes;

import android.util.Log;

import com.qualcomm.customsensors.IMUSensor;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;
import com.qualcomm.robotcore.hardware.GyroSensor;
import com.qualcomm.robotcore.hardware.I2cDevice;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.ServoController;

import com.qualcomm.robotcore.exception.RobotCoreException;

/**
 * Created by Eric on 10/3/2015.
 * Created for 2015-2016 FTC Season
 */
public class FTCCompetitionBase extends OpMode {

    // Motor Controllers
    private DcMotorController LeftDriveController;
    private DcMotorController RightDriveController;
    private DcMotorController Arm1Controller;
    private DcMotorController Arm2Controller;
    private ServoController ServoController;

    // Drive Motors
    private DcMotor LeftDrive1;
    private DcMotor LeftDrive2;
    private DcMotor RightDrive1;
    private DcMotor RightDrive2;
    private DcMotor ArmLift;
    private DcMotor ArmTilt;
    private DcMotor BlockPickup;
    private DcMotor PullUpHook;

    // Servos
    private Servo DumpServo1;
    private Servo DumpServo2;

    // Sensors
    private ColorSensor ColorSensor;
    private IMUSensor imuSensor;

    @Override
    public void init() {
        // Controllers
        LeftDriveController = hardwareMap.dcMotorController.get("LeftDriveController");
        RightDriveController = hardwareMap.dcMotorController.get("RightDriveController");
        ServoController = hardwareMap.servoController.get("ServoController");
        Arm1Controller = hardwareMap.dcMotorController.get("Arm1Controller");
        Arm2Controller = hardwareMap.dcMotorController.get("Arm2Controller");

        // DC Motors
        LeftDrive1 = hardwareMap.dcMotor.get("LeftDrive1");
        LeftDrive2 = hardwareMap.dcMotor.get("LeftDrive2");
        RightDrive1 = hardwareMap.dcMotor.get("RightDrive1");
        RightDrive2 = hardwareMap.dcMotor.get("RightDrive2");

        RightDrive1.setDirection(DcMotor.Direction.REVERSE);
        RightDrive2.setDirection(DcMotor.Direction.REVERSE);

        ArmLift = hardwareMap.dcMotor.get("ArmLift");
        ArmTilt = hardwareMap.dcMotor.get("ArmTilt");
        BlockPickup = hardwareMap.dcMotor.get("BlockPickup");
        PullUpHook = hardwareMap.dcMotor.get("PullUpHook");

        // Servos
        DumpServo1 = hardwareMap.servo.get("DumpServo1");
        DumpServo2 = hardwareMap.servo.get("DumpServo2");

        // Sensors
        ColorSensor = hardwareMap.colorSensor.get("ColorSensor");

        // TODO: Create IMU Sensor Driver
        //imuSensor = hardwareMap.i2cDevice.get("IMU");
        long systemTime;//Relevant values of System.nanoTime
        imuSensor = new IMUSensor(hardwareMap, "IMU",
                (byte)(IMUSensor.GYRO_ADDR_I2C)); //By convention the FTC SDK always does 8-bit I2C byte addressing

        //For debugging purposes. This is a way to check basic i2c functionality.
        boolean correctId = imuSensor.testRead_WHO_AM_I();

        //TODO: When uncommented this code is supposed to initialize the IMU gyro control registers
        //There is some error checking and log file entries made for debugging
//        systemTime = System.nanoTime();
//        try {
//            imuSensor.initIMU();
//        } catch (RobotCoreException e){
//            Log.i("FtcRobotController", "Exception: " + e.getMessage());
//        }
//        Log.i("FtcRobotController", "IMU Init method finished in: "
//                + (-(systemTime - (systemTime = System.nanoTime()))) + " ns.");

        //TODO: When we need it this will start a continual update of the gyro yaw rate
        //imuSensor.startIMU();

        //TODO: This is how we read the current instantaneous value once startIMU is running
        //double yawRate = imuSensor.getIMUGyroYawRate();

    }

    @Override
    public void loop() {

    }

    //Drive Functions
    private void DriveSystem(double left, double right){
        LeftDrive1.setPower(left);
        LeftDrive2.setPower(left);
        RightDrive1.setPower(right);
        RightDrive2.setPower(right);
    }

    public void ArcadeDrive(double ForwardPower, double TurnPower){
        DriveSystem(ForwardPower - TurnPower,  //Left Side
                ForwardPower + TurnPower); //Right Side
    }

    // Encoder Functions
    public int getLeftEncoder(){ return LeftDrive1.getCurrentPosition(); }
    public void resetLeftEncoder(){ LeftDrive1.setChannelMode(DcMotorController.RunMode.RESET_ENCODERS);}
    public int getRightEncoder(){ return RightDrive1.getCurrentPosition(); }
    public void resetRightEncoder(){ RightDrive1.setChannelMode(DcMotorController.RunMode.RESET_ENCODERS);}

    // Sensor Functions
    // TODO: setup get functions for blue and red colors

    // TODO: Setup a Gyroscope for getting Rate and Direction

    // Lift Functions
    public void setArmLift(double power){
        ArmLift.setPower(power);
    }
    public void setArmTilt(double power){
        ArmTilt.setPower(power);
    }
    public void BlockPickup(boolean power){
        if(power){
            BlockPickup.setPower(1.0);
        }
        else{
            BlockPickup.setPower(0);
        }
    }

    public void PullupHook(boolean Out, boolean In){
        if (Out){
            PullUpHook.setPower(1.0);
        }
        else if (In){
            PullUpHook.setPower(-1.0);
        }
        else{
            PullUpHook.setPower(0);
        }
    }

    // Dumper Functions
    // TODO: Prove dump function
    // TODO: Determine actual servo set positions
    public void DumpTrash(boolean left,boolean right){
        if (left){
            DumpServo1.setPosition(1);
        }
        else if (right){
            DumpServo1.setPosition(0);
        }
        else{
            DumpServo1.setPosition(0.5);
        }
    }

}