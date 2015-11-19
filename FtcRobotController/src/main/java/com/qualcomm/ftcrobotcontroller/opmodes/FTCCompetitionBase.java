package com.qualcomm.ftcrobotcontroller.opmodes;

import android.util.Log;
import android.widget.Switch;

import com.qualcomm.ftcrobotcontroller.customsensors.IMUSensor;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.ServoController;
import com.qualcomm.robotcore.exception.RobotCoreException;
import java.lang.Math;

/**
 * Created by Eric on 10/3/2015.
 * Created for 2015-2016 FTC Season
 */
public class FTCCompetitionBase extends OpMode {

    // Motor Controllers
    private DcMotorController LeftDriveController, RightDriveController,
            Arm1Controller, Arm2Controller;
    private ServoController ServoController;

    // Drive Motors
    private DcMotor LeftDrive1, LeftDrive2, RightDrive1, RightDrive2, ArmLift,
            ArmTilt, BlockPickup, PullUpHook;

    // Servos
    private Servo DumpServo, LeftTabTripper, RightTabTripper, AutonFlag;


    // Sensors
    //private ColorSensor ColorSensor;
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

        LeftDrive1.setDirection(DcMotor.Direction.REVERSE);
        RightDrive2.setDirection(DcMotor.Direction.REVERSE);

        ArmLift = hardwareMap.dcMotor.get("ArmLift");
        ArmTilt = hardwareMap.dcMotor.get("ArmTilt");
        BlockPickup = hardwareMap.dcMotor.get("BlockPickup");
        PullUpHook = hardwareMap.dcMotor.get("PullUpHook");

        // Servos
        DumpServo = hardwareMap.servo.get("DumpServo");
        LeftTabTripper = hardwareMap.servo.get("LeftTabTripper");
        RightTabTripper = hardwareMap.servo.get("RightTabTripper");
        AutonFlag = hardwareMap.servo.get("AutonFlag");

        /*
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
*/
    }

    @Override
    public void loop(){}

    //Drive Functions
    private double deadzone(double input){
        return (input > 1.0 ? 1.0 : (input < -1.0 ? -1.0 : input));
    }

    private void DriveSystem(double left, double right){
        left = deadzone(left);
        right = deadzone(right);
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
    public int getLeftEncoder(){ return Math.abs(LeftDrive1.getCurrentPosition()); }
    public void resetLeftEncoder(){ LeftDrive1.setMode(DcMotorController.RunMode.RESET_ENCODERS);}
    public int getRightEncoder(){ return Math.abs(RightDrive1.getCurrentPosition()); }
    public void resetRightEncoder(){ RightDrive1.setMode(DcMotorController.RunMode.RESET_ENCODERS);}

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
        BlockPickup.setPower(power ? 1 : 0);
    }

    public void PullupHook(boolean Out, boolean In){
        PullUpHook.setPower(Out ? -1.0 : (In ? 1.0 : 0));
    }

    void pullUpMountain(boolean Activate){
        if (Activate){
            this.PullupHook(false, true);
            this.ArcadeDrive(-0.30,0);
        }
    }

    // Dumper Functions
    public void DumpTrash(boolean left,boolean right){
        DumpServo.setPosition(left ? 1 : (right ? 0 : 0.5));
    }

    public void TabDropper(boolean left, boolean right){
        LeftTabTripper.setPosition(left ? 0.0 : 0.9);
        RightTabTripper.setPosition(right ? 1.0 : 0.1);
    }

    private final static double AFNeutral = 0.5, AFLeft = 0.0, AFRight = 1.0;
    public void AutonFlag(boolean left, boolean right){
        AutonFlag.setPosition(left ? AFLeft : (right ? AFRight : AFNeutral));
    }

    // Autonimous Mode functions
    enum DirectionSelect{left,right,both}


    boolean atEncVal(DirectionSelect Select, int val){
        switch (Select){
            case left:
                return (Math.abs(LeftDrive1.getCurrentPosition()) > val);
            case right:
                return (Math.abs(RightDrive1.getCurrentPosition()) > val);
            case both:
                return (Math.abs(LeftDrive1.getCurrentPosition()) > val && (Math.abs(LeftDrive1.getCurrentPosition()) > val));
            default:
                return false;
        }
    }

    void setRunMode(DcMotorController.RunMode Mode){
        LeftDrive1.setMode(Mode);
        RightDrive1.setMode(Mode);
    }

    boolean hasEncoderReset(DcMotor check){
        return (check.getCurrentPosition() == 0);
    }

    private final double power = 0.75;
    boolean AutonDrive(int length, double ForwardPower, double LateralPower) {
        // Always reset to false condition on check
        boolean l_return = false;

        // Ensure run mode is set to run with encoders
        setRunMode(DcMotorController.RunMode.RUN_USING_ENCODERS);

        // Check if at position currently
        if (atEncVal(DirectionSelect.both, length)){
            l_return = true;
            setRunMode(DcMotorController.RunMode.RESET_ENCODERS);
        }
        return l_return;
    }



}