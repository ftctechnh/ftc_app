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

        // have servos move robot down to the ground


        sleep(500);

        while (compRobot.getFrontDistSens().getDistance(DistanceUnit.INCH) > 24 && compRobot.getFrontRightDistSens().getDistance(DistanceUnit.INCH) > 24)
        {
            compRobot.driveMotors(.4f, .4f);
        }

        compRobot.stopDriveMotors();
        //drop marker into depot
        compRobot.pivotenc(150, .5f);// pivot should be at 135 degrees if we ever choose to callibrate

        compRobot.hugWall(4,7, 18, true);

        compRobot.driveStraight(10,1);

        telemetry.addData("Stopped", null);
        telemetry.update();

        while (!isStopRequested())
        {
            compRobot.stopDriveMotors();
        }

    }
}



