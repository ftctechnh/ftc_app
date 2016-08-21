package com.qualcomm.ftcrobotcontroller.opmodes.ResQ_2016;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;


/**
 * Created by grempa on 12/31/15.
 */
public class BeaconZoneRedOp extends OpMode
{
    CompBot drive;
    public boolean run;

    @Override
    public void init()
    {
        drive = new CompBot(hardwareMap);
        run = true;
    }

    @Override
    public void loop()
    {
        if ( run )
        {
        /*drive.moveStraightEncoders(36, 1);

        drive.spinOnCenter(45, (float) 0.5); //leftTurn(45, .25);//(-45,.25);

        drive.moveStraightEncoders(96, 1);

        drive.spinOnCenter(45, (float) 0.5);//leftTurn(90, 0.25); //should only be 90 but calibration

        drive.moveStraightEncoders(6, 1); //or use gyro to stop at a certain angle*/
            drive.getLeftMotor().setPower(1);
            drive.getRightMotor().setPower(1);
            run = false;
        }

    }

    @Override
    public void stop()
    {

    }


}
