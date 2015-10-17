package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.ServoController;

/**
 * Created by Eric on 10/3/2015.
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

    @Override
    public void init() {
        // Controllers
        LeftDriveController = hardwareMap.dcMotorController.get("LeftDriveController");
        RightDriveController = hardwareMap.dcMotorController.get("RightDriveController");
        ServoController = hardwareMap.servoController.get("ServoController");

        // DC Motors
        LeftDrive1 = hardwareMap.dcMotor.get("LeftDrive1");
        LeftDrive2 = hardwareMap.dcMotor.get("LeftDrive2");
        RightDrive1 = hardwareMap.dcMotor.get("RightDrive1");
        RightDrive2 = hardwareMap.dcMotor.get("RightDrive2");
        ArmLift = hardwareMap.dcMotor.get("ArmLift");
        ArmTilt = hardwareMap.dcMotor.get("ArmTilt");
        BlockPickup = hardwareMap.dcMotor.get("BlockPickup");

        // Servos
        DumpServo1 = hardwareMap.servo.get("DumpServo1");
        DumpServo2 = hardwareMap.servo.get("DumpServo2");
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

    // Lift Functions
    // TODO: Look at code to control the lift, fill in
    public void setArmLift(double power){

    }

    // TODO: Look at code to control the Tilt, fill in
    public void setArmTilt(double power){

    }

    // TODO: Program block pickup to run on one button
    public void BlockPickup(boolean power){

    }

    // TODO: Operate Pullup hook on one button
    public void PullupHook(boolean power){

    }

}