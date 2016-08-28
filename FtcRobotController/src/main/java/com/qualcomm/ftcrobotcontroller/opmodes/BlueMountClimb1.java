package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

/**
 * Created by Kaitlin on 12/19/15.
 */
public class BlueMountClimb1 extends LinearOpMode
{
    DriverInterface drive;

    @Override
    public void runOpMode() throws InterruptedException
    {
        /*
        drive = new CompBot(hardwareMap);

        //wait for start
        telemetry.addData("(1)Before waitForStart", 2);
        waitForStart();

        //move 24 inches forward
        telemetry.addData("(2)Before moving forward 24 inches", 2);
        drive.moveStraightEncoders(24, 1);

        //spin 45 degrees
        telemetry.addData("(3)Before spinning 45 degrees", 2);
        drive.spinOnCenter(45, (float) 0.25); //leftTurn(45, .25);//(-45,.25);

        //move forward 46 inches
        telemetry.addData("(4)Before moving forward 46 inches", 2);
        drive.moveStraightEncoders(46, 1);

        //spin 90 degrees
        telemetry.addData("(5)Before spinning 90 degrees", 2);
        drive.spinOnCenter(100, (float) 0.25);//leftTurn(90, 0.25); //should only be 90 but calibration

        //go forward 47 inches
        telemetry.addData("(5)Before going forward 47 inches",2);
        drive.moveStraightEncoders(47, 1); //or use gyro to stop at a certain angle

        //pitch front tracks
        telemetry.addData("(6)Before pitching front tracks first time",2);
        drive.pitchFrontTracks(2000, 1);

        //pitch back tracks
        telemetry.addData("(7)Before pitching back tracks first time",2);
        drive.pitchBackTracks(1000,1);

        //move forward 4 inches
        telemetry.addData("(8)move forward 4 inches",2);
        drive.moveStraightEncoders(4,1);

        //pitch front tracks
        telemetry.addData("(9)Before pitching front tracks second time",2);
        drive.pitchFrontTracks(4000,1);

        //pitch back tracks
        telemetry.addData("(10)Before pitching back tracks second time",2);
        drive.pitchBackTracks(2000,1);

        //move forward 4 inches
        telemetry.addData("(11)move forward 4 inches",2);
        drive.moveStraightEncoders(4,1);


        //stop the bot
        */
    }
}
