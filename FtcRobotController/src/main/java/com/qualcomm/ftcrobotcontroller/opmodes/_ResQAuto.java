package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;
import com.technicbots.MainRobot;
import com.technicbots.MainRobot;

public class _ResQAuto extends LinearOpMode {
   // DcMotor leftMotor;
    //DcMotor rightMotor;
    MainRobot MainRobot;

    @Override
    public void runOpMode() throws InterruptedException {
        //leftMotor = hardwareMap.dcMotor.get("left_drive");
       // rightMotor = hardwareMap.dcMotor.get("right_drive");
       // leftMotor.setDirection(DcMotor.Direction.REVERSE);
        //rightMotor.setDirection(DcMotor.Direction.REVERSE);

        waitForStart();
        MainRobot.moveStraight(150.0, 0.9, true);
        //MainRobot.turn(60);
        //MainRobot.moveStraight(80.0, 0.9, true);
        //MainRobot.lineFollower(50);

    }
}