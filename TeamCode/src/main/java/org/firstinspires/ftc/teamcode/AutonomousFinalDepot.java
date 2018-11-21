package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

@Autonomous(name = "!Depot")
public class AutonomousFinalDepot extends LinearOpMode
{

    CompRobot compRobot;

    public void runOpMode()
    {
        compRobot = new CompRobot(hardwareMap, this);
        waitForStart();

        // have servos move robot down to the ground

        compRobot.driveStraight(36);
        sleep(500);

        while (compRobot.getFrontDistSens().getDistance(DistanceUnit.INCH) > 16 && compRobot.getFrontRightDistSens().getDistance(DistanceUnit.INCH) > 16)
        {
            compRobot.driveMotors(.4f, .4f);
        }

        compRobot.stopDriveMotors();
        //drop marker into depot
        compRobot.pivotenc(120);

        compRobot.hugWallForward(4,7, 18);

        telemetry.addData("Stopped", null);
        telemetry.update();

        while (!isStopRequested())
        {
            compRobot.stopDriveMotors();
        }
    }
}



