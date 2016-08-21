package com.qualcomm.ftcrobotcontroller.opmodes.ResQ_2016;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;

/**
 * Created by Kaitlin on 12/1/15.
 * Edited by Kaitlin and Peter on 1/3/15
 */
public class RedMountainAuto1v2 extends OpMode
{
    DriverInterface drive;
    boolean run;

    @Override
    public void init()
    {
        run = true;
        drive = new CompBot(hardwareMap);
    }

    @Override
    public void loop()
    {
        if (run)
        {
            telemetry.addData("moving robot", 2);
            
            drive.moveStraightEncoders(24,1);

            /*
            drive.spinOnCenter(-45, (float) 0.25); //leftTurn(45, .25);//(-45,.25);

             drive.moveStraightEncoders(46, 1);

            drive.spinOnCenter(-100, (float) 0.25);//leftTurn(90, 0.25); //should only be 90 but calibration

            drive.moveStraightEncoders(47, 1);

             //pitch front tracks
            drive.pitchFrontTracks(2000, 1);

            //pitch back tracks
            //telemetry.addData("(7)Before pitching back tracks first time",2);
            drive.pitchBackTracks(1000,1);

            //move forward 4 inches
            // telemetry.addData("(8)move forward 4 inches",2);
            drive.moveStraightEncoders(4,1);

            //pitch front tracks
            drive.pitchFrontTracks(4000,1);

            //pitch back tracks
            drive.pitchBackTracks(2000,1);

            //move forward 4 inches
            drive.moveStraightEncoders(4,1);

            run = false;
            */
        }

        else
        {
            telemetry.addData("run is false", 0);
        }
    }
}
