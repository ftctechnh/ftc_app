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

        // have servos move robot down to the ground

        compRobot.driveStraight(36, .8f);
        sleep(500);

        while (compRobot.getFrontDistSens().getDistance(DistanceUnit.INCH) > 16 && compRobot.getFrontRightDistSens().getDistance(DistanceUnit.INCH) > 16)
        {
            compRobot.driveMotors(.4f, .4f);
        }

        compRobot.stopDriveMotors();
        //drop marker into depot
        compRobot.pivotenc(120, .5f);

        compRobot.hugWall(4,7, 18, true);

        telemetry.addData("Stopped", null);
        telemetry.update();

        while (!isStopRequested())
        {
            compRobot.stopDriveMotors();
        }
    }
}



