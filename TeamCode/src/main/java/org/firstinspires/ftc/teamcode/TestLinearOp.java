package org.firstinspires.ftc.teamcode;
//Created to demonstrate both opmodes, used as an example mostly.

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

/**
 * Created by Jeremy on 8/27/2017.
 */
@Autonomous(name = "24in", group = "concept")
public class TestLinearOp extends LinearOpMode
{
    private TankBase robot;
    @Override
    public void runOpMode() throws InterruptedException
    {
        robot = new TankBase(hardwareMap);
        waitForStart();
        robot.driveStraight_In(24);
        sleep(5000);
        robot.driveStraight_In(-24);
    }
}


































