package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;
import com.qualcomm.robotcore.hardware.HardwareMap;

/**
 * Created by Peter on 11/25/2015.
 */
public class TestMove extends LinearOpMode
{
    @Override public void runOpMode()throws InterruptedException
   {
       DriverInterface robotgo = new TestBot(hardwareMap);
       waitForStart(); // this is so the robot moves when you hit the play button not the init button
       robotgo.spinOnCenter(90, 1.0f);
       robotgo.spinOnCenter(-90, -1.0f);
       robotgo.spinOnCenter(-90, 1.0f);
       robotgo.spinOnCenter(90, -1.0f);
       stop();
   }
}
