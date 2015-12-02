package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

/**
 * Created by Kaitlin on 12/1/15.
 */
public class MountainAuto1 extends LinearOpMode
{
    DriverInterface drive = new TestBot(hardwareMap);


    @Override
    public void runOpMode() throws InterruptedException
    {
        drive.moveStraightEncoders(24,1);
        drive.spinOnCenter(45,.25,true);
        drive.moveStraightEncoders(24,1);
        drive.spinOnCenter(90,.25,true);
        drive.moveStraightEncoders(28,1);
    }
}
