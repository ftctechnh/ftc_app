package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

/**
 * Created by Peter on 11/19/2015.
 */
public class BeaconAuto extends LinearOpMode
{

    DriverInterface robot = new TestBot(hardwareMap);

    public void runOpMode()
    {
        robot.moveStraightEncoders(48, 1.0f);
        robot.spinOnCenter(90, 0.75f, true);
        robot.moveStraightEncoders(48, 1.0f);
        robot.spinOnCenter(90, 0.75f, false);
        robot.moveStraightEncoders(24, 1.0f);
        robot.spinOnCenter(90, 0.75f, true);
        robot.moveStraightEncoders(24, 1.0f);
        try
        {
            sleep(10);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        robot.spinOnCenter(90, 0.75f, true);
        robot.moveStraightEncoders(24 , 1.0f);
        robot.stop();

    }



}
