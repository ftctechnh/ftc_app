package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.OpticalDistanceSensor;
//Replace with the robot we are using
import com.qualcomm.robotcore.hardware.Servo;
import com.technicbots.PushBot;

public class ResQAuto extends LinearOpMode {
    final static double delayStart = 0;
    final static boolean redTeam = true;

    // DcMotor rightMotor;
    //DcMotor leftMotor;

    PushBot pushBot;


    @Override
    public void runOpMode() throws InterruptedException {

        //  rightMotor = hardwareMap.dcMotor.get("rightwheel");
        //   leftMotor = hardwareMap.dcMotor.get("leftwheel");

        pushBot = new PushBot(hardwareMap);
        // pushBot = new PushBot(leftMotor, rightMotor, null, null, null, null, null);



        // wait for the start button to be pressed
        waitForStart();

        pushBot.moveStraight(20,0.5,false);

    }
}
