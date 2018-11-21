package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
@Autonomous(name = "CompRobotCalibration")
public class CompRobotCalibration extends LinearOpMode
{
    CompRobot festus;

    public void runOpMode() throws InterruptedException
    {
        festus = new CompRobot(hardwareMap, this);
        waitForStart();
        /*festus.driveStraight(100, 1);
        while (!gamepad1.a && !isStopRequested())
        {

        }
        festus.driveStraight(-250, 1);
        while (!gamepad1.b && !isStopRequested())
        {

        }festus.driveStraight(-400, 1);
        while (!gamepad1.x && !isStopRequested())
        {

        }
        festus.driveStraight(-500, 1 );
        while (!gamepad1.y && !isStopRequested())
        {

        }
        festus.driveStraight(-750, 1 );
        while (!gamepad1.a && !isStopRequested())
        {

        }
        festus.driveStraight(-1000, 1 );
        while (!gamepad1.b && !isStopRequested())
        {

        }
        festus.driveStraight(-1250, 1 );
        while (!gamepad1.x && !isStopRequested())
        {

        }
        festus.drivestraight(-1500, 1);
        while (!gamepad1.y && !isStopRequested())
        {

        }

        festus.pivotenc(50,.5f);
        while (!gamepad1.a && !isStopRequested())
        {

        }
        festus.pivotenc(75,.5f);
        while (!gamepad1.b && !isStopRequested())
        {

        }
        festus.pivotenc(100,.5f);
        while (!gamepad1.x && !isStopRequested())
        {

        }
        festus.pivotenc(125,.5f);
        while (!gamepad1.y && !isStopRequested())
        {

        }*/
        festus.pivotenc(25,.5f);
        while (!gamepad1.a && !isStopRequested())
        {

        }
        festus.pivotenc(150,.5f);
        while (!gamepad1.b && !isStopRequested())
        {

        }
        festus.pivotenc(175,.5f);
        while (!gamepad1.x && !isStopRequested())
        {

        }
        festus.pivotenc(200,.5f);
        while (!gamepad1.y && !isStopRequested())
        {

        }
    }
}
