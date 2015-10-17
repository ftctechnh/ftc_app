package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;

/**
 * Created by Eric on 10/3/2015.
 */
public class FTCCompetitionBase extends OpMode {

    // Motor Controllers
    private DcMotorController LeftDriveController;
    private DcMotorController RightDriveController;
    private DcMotorController Arm1Controller;
    private DcMotorController Arm2Controller;

    // Drive Motors
    private DcMotor LeftDrive1;
    private DcMotor LeftDrive2;
    private DcMotor RightDrive1;
    private DcMotor RightDrive2;
    private DcMotor ArmLift;
    private DcMotor ArmTilt;
    private DcMotor BlockPickup;
    private DcMotor PullupHook;

    @Override
    public void init() {
        LeftDriveController = hardwareMap.dcMotorController.get("LeftDriveController");
        RightDriveController = hardwareMap.dcMotorController.get("RightDriveController");
        LeftDrive1 = hardwareMap.dcMotor.get("LeftDrive1");
        LeftDrive2 = hardwareMap.dcMotor.get("LeftDrive2");
        RightDrive1 = hardwareMap.dcMotor.get("RightDrive1");
        RightDrive2 = hardwareMap.dcMotor.get("RightDrive2");
    }

    @Override
    public void loop() {

    }

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

    public int getLeftEncoder(){ return LeftDrive1.getCurrentPosition(); }
    public void resetLeftEncoder(){ LeftDrive1.setChannelMode(DcMotorController.RunMode.RESET_ENCODERS);}
    public int getRightEncoder(){ return RightDrive1.getCurrentPosition(); }
    public void resetRightEncoder(){ RightDrive1.setChannelMode(DcMotorController.RunMode.RESET_ENCODERS);}

}