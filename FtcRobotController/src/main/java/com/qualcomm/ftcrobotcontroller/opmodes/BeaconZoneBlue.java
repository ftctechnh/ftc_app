package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
/**
 * Created by grempa on 12/29/15.
 */
public class BeaconZoneBlue extends LinearOpMode {

    DriverInterface drive;

    @Override
    public void runOpMode() throws InterruptedException
    {
        drive = new CompBot(hardwareMap);

        waitForStart();

        drive.moveStraightEncoders(36,1);

        drive.spinOnCenter(45, (float) 0.25); //leftTurn(45, .25);//(-45,.25);

        drive.moveStraightEncoders(96, 1);

        drive.spinOnCenter(45, (float) 0.25);//leftTurn(90, 0.25); //should only be 90 but calibration

        drive.moveStraightEncoders(6, 1); //or use gyro to stop at a certain angle
        //stop the bot

    }
}
