package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

public class CompRobotCalibration extends LinearOpMode
{
    CompRobot compRobot;
    @Override
    public void runOpMode() throws InterruptedException
    {
        compRobot = new CompRobot(hardwareMap, this);
        waitForStart();
        compRobot.driveStraight(250, 1);
        while (!gamepad1.a && !isStopRequested())
        {

        }
        compRobot.driveStraight(500, 1 );
        while (!gamepad1.a && !isStopRequested())
        {

        }
        compRobot.driveStraight(750, 1 );
        while (!gamepad1.a && !isStopRequested())
        {

        }
        compRobot.driveStraight(1000, 1 );
        while (!gamepad1.a && !isStopRequested())
        {

        }
        compRobot.driveStraight(1250, 1 );
        while (!gamepad1.a && !isStopRequested())
        {

        }
        compRobot.driveStraight(1500, 1 );
        while (!gamepad1.a && !isStopRequested())
        {

        }

    }
}
