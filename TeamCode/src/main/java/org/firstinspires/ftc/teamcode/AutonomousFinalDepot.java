package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

@Autonomous(name = "!FinalDepot")
public class AutonomousFinalDepot extends LinearOpMode
{
    CompRobot compRobot;
    VuforiaFunctions vuforiaFunctions;

    public void runOpMode()
    {
        compRobot = new CompRobot(hardwareMap, this);
        vuforiaFunctions = new VuforiaFunctions(this, hardwareMap);
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
        compRobot.pivotenc(160, .5f);

        compRobot.pivotenc(15, .5f);
        compRobot.driveStraight(16,.6f);

        compRobot.pivotenc(55, .5f);
        telemetry.addData("No target seen, so -55 deg turn", null);
        telemetry.update();

        sleep(600);
        compRobot.hugWallToRight(4,6, 18, 84);
        compRobot.driveStraight(6, .8f);
        telemetry.addData("Stopped", null);
        telemetry.update();

        while (!isStopRequested())
        {
            compRobot.stopDriveMotors();
        }
    }
}



