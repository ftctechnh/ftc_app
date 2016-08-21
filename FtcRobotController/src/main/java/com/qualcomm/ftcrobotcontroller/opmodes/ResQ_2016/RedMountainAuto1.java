package com.qualcomm.ftcrobotcontroller.opmodes.ResQ_2016;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

/**
 * Created by Kaitlin on 12/1/15.
 */
public class RedMountainAuto1 extends LinearOpMode
{
    DriverInterface drive;

    @Override
    public void runOpMode() throws InterruptedException
    {
        drive = new CompBot(hardwareMap);

        telemetry.addData("(1)Before waitForStart", 2);
        waitForStart();

        telemetry.addData("(2)Before moving forward 24 inches", 2);
        drive.moveStraightEncoders(24,1);

        telemetry.addData("(3)Before spinning 45 degrees", 2);
        drive.spinOnCenter(-45, (float) 0.25); //leftTurn(45, .25);//(-45,.25);

        telemetry.addData("(4)Before moving forward 46 inches", 2);
        drive.moveStraightEncoders(46, 1);

        telemetry.addData("(5)Before spinning 90 degrees", 2);
        drive.spinOnCenter(-100, (float) 0.25);//leftTurn(90, 0.25); //should only be 90 but calibration

        telemetry.addData("(5)Before going forward 47 inches",2);
        drive.moveStraightEncoders(47, 1); //or use gyro to stop at a certain angle
        //stop the bot
    }
}
