package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

/**
 * Created by Peter on 11/19/2015.
 */
public class BeaconAuto extends LinearOpMode
{
    DriverInterface robot;

    @Override public void runOpMode() throws InterruptedException
    {
        DriverInterface robot = new CompBot(hardwareMap);
        waitForStart();
        robot.moveStraightEncoders(48, 1.0f);
        robot.spinOnCenter(90, 0.75f);
        robot.moveStraightEncoders(48, 1.0f);
        robot.spinOnCenter(-90, 0.75f);
        robot.moveStraightEncoders(24, 1.0f);
        robot.spinOnCenter(90, 0.75f);
        robot.moveStraightEncoders(24, 1.0f);
        robot.spinOnCenter(90, 0.75f);
        robot.moveStraightEncoders(24 , 1.0f);
        robot.stop();
    }
}
