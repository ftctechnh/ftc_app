package org.firstinspires.ftc.teamcode;
//Created to demonstrate both opmodes, used as an example mostly.

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

/**
 * Created by Jeremy on 8/27/2017.
 */
@Autonomous(name = "name", group = "concept")
public class TestLinearOp extends LinearOpMode
{
    @Override
    public void runOpMode() throws InterruptedException
    {
        TankBase robot = new TankBase();
        robot.init(hardwareMap);
        waitForStart();
        robot.spin_Left(120);
        robot.spin_Right(120);
        robot.pivot(120);
        robot.driveStraight_In(24);
        robot.driveStraight_In(-24);
    }
}


































