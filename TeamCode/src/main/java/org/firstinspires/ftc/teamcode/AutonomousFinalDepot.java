package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

@Autonomous(name = "!FinalDepot")
public class AutonomousFinalDepot extends LinearOpMode
{

    CompRobot compRobot;

    public void runOpMode()
    {
        compRobot = new CompRobot(hardwareMap, this);
        waitForStart();
        compRobot.driveStraight(36, 1);
        sleep(500);
        while (compRobot.getFrontDistSens().getDistance(DistanceUnit.INCH) > 28 && compRobot.getFrontRightDistSens().getDistance(DistanceUnit.INCH) > 28)
        {
            compRobot.driveMotors(.4f, .4f);
        }
        compRobot.stopDriveMotors();
        compRobot.deployMarker();
        compRobot.pivotenc(-150, .5f);

        compRobot.hugWall(4,7, 18, false, 84);
        compRobot.driveStraight(6, .8f);
        telemetry.addData("Stopped", null);
        telemetry.update();

        while (!isStopRequested())
        {
            compRobot.stopDriveMotors();
        }

    }
}



