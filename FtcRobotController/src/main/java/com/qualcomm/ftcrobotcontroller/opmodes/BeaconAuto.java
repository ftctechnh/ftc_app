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
        robot = new CompBot(hardwareMap);
        waitForStart();
        robot.moveStraightEncoders(48, 1);
        robot.spinOnCenter(-270, 1);
        robot.moveStraightEncoders(48, 1);
        robot.spinOnCenter(-90, 1);
        robot.moveStraightEncoders(24, 1);
        robot.spinOnCenter(90, 1);
        robot.moveStraightEncoders(24, 1);
        robot.spinOnCenter(90, 1);
        robot.moveStraightEncoders(24 , 1);
        robot.stop();
    }
}