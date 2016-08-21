package com.qualcomm.ftcrobotcontroller.opmodes.ResQ_2016;


import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

/**
 * Created by grempa on 12/29/15,
 */
public class BeaconZoneRed extends LinearOpMode{

    DriverInterface drive;

    @Override
    public void runOpMode() throws InterruptedException
    {
        drive = new CompBot(hardwareMap);

        telemetry.addData("(1)Before waitForStart", 2);
        waitForStart();

        telemetry.addData("(2)Before moving forward 36 inches", 2);
        drive.moveStraightEncoders(36,1);

        telemetry.addData("(3)Before spinning 45 degrees", 2);
        drive.spinOnCenter(-45, (float) 0.25); //leftTurn(45, .25);//(-45,.25);

        telemetry.addData("(4)Before moving forward 96 inches or 4 blocks", 2);
        drive.moveStraightEncoders(96, 1);

        telemetry.addData("(5)Before spinning 45 degrees counter clockwise", 2);
        drive.spinOnCenter(-45, (float) 0.25);//leftTurn(90, 0.25); //should only be 90 but calibration

        telemetry.addData("(5)Before going forward 6 inches",2);
        drive.moveStraightEncoders(6, 1); //or use gyro to stop at a certain angle
        //stop the bot

    }

}
