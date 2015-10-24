package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;
import com.technicbots.PushBot;

public class _ResQAuto extends LinearOpMode {
   // DcMotor leftMotor;
    //DcMotor rightMotor;
    PushBot PushBot;

    @Override
    public void runOpMode() throws InterruptedException {
        //leftMotor = hardwareMap.dcMotor.get("left_drive");
       // rightMotor = hardwareMap.dcMotor.get("right_drive");
       // leftMotor.setDirection(DcMotor.Direction.REVERSE);
        //rightMotor.setDirection(DcMotor.Direction.REVERSE);

        waitForStart();
        PushBot.moveStraight(150.0, 0.9, false);
        PushBot.turn(60);
        PushBot.moveStraight(80.0, 0.9, false);
        PushBot.lineFollower(50);

    }
}