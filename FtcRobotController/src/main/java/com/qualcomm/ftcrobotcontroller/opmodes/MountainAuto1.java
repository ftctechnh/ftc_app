package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

/**
 * Created by Kaitlin on 12/1/15.
 */
public class MountainAuto1 extends LinearOpMode
{
    DriverInterface drive;


    @Override
    public void runOpMode() throws InterruptedException
    {
        drive = new CompBot(hardwareMap);
        drive.moveStraightEncoders(24,1);
        drive.spinOnCenter(-45, (float) 0.25); //leftTurn(45, .25);//(-45,.25);
        drive.moveStraightEncoders(24, 1);
        drive.spinOnCenter(90, (float) 0.25);//leftTurn(90, 0.25);
        drive.moveStraightEncoders(47, 1); //or use gyro to stop at a certain angle
        //stop the bot
    }
}
