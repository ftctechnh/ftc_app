package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

@Autonomous(name = "PracticeDepot")
public class PracticeDepot extends LinearOpMode
{
    CompRobot compRobot;

    public void runOpMode()
    {
        compRobot = new CompRobot(hardwareMap, this);
        waitForStart();
        compRobot.climbDown();
        sleep(2000);
        compRobot.driveStraight(10, .5f);

        compRobot.driveStraight(30, 1);
        sleep(500);
        while (compRobot.getFrontDistSens().getDistance(DistanceUnit.INCH) > 28 && compRobot.getFrontRightDistSens().getDistance(DistanceUnit.INCH) > 28)
        {
            compRobot.driveMotors(.4f, .4f);
        }
        compRobot.stopDriveMotors();
        compRobot.deployMarker();
        compRobot.driveStraight(-6,1);
        compRobot.pivotenc(-150, .5f);
        compRobot.driveStraight(80, .8f);
        telemetry.addData("Stopped", null);
        telemetry.update();

        while (!isStopRequested())
        {
            compRobot.stopDriveMotors();
        }

    }
}
