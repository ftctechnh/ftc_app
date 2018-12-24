package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

@Autonomous(name = "SwitchAutoDepotV1")
public class SwitchAutoDepotV1 extends LinearOpMode
{
    CompRobot compRobot;
    VuforiaFunctions vuforiaFunctions;

    public void runOpMode()
    {
        compRobot = new CompRobot(hardwareMap, this);
        vuforiaFunctions = new VuforiaFunctions(this, hardwareMap);
        boolean switchSample = compRobot.getSwitchSample().getState();
        boolean switchDelay = compRobot.getSwitchDelay().getState();
        boolean switchDepot = compRobot.getSwitchDepot().getState();
        boolean switchCrater = compRobot.getSwitchCrater().getState();
        waitForStart();
        compRobot.climbDown();
        sleep(200);

        if (switchSample = true)
        {
            compRobot.driveStraight(10, .5f); //adjust distance when integrating code
            //sample
        }
        if (switchDelay = true) //do we want to have this for the depot program?
        {
            sleep(2000);
        }
        if (switchDepot = true)
        {
            if (switchSample = false)
            {
                compRobot.driveStraight(10, .5f);
                compRobot.driveStraight(30, 1);
                sleep(500);
                while (compRobot.getFrontDistSens().getDistance(DistanceUnit.INCH) > 28 && compRobot.getFrontRightDistSens().getDistance(DistanceUnit.INCH) > 28)
                {
                    compRobot.driveMotors(.4f, .4f);
                }
                compRobot.stopDriveMotors();
                compRobot.deployMarker();
            }
            else
            {
                compRobot.driveStraight(30, 1);
                sleep(500);
                while (compRobot.getFrontDistSens().getDistance(DistanceUnit.INCH) > 28 && compRobot.getFrontRightDistSens().getDistance(DistanceUnit.INCH) > 28)
                {
                    compRobot.driveMotors(.4f, .4f);
                }
                compRobot.stopDriveMotors();
                compRobot.deployMarker();
            }
        }
        if (switchCrater = true)
        {
            if (switchDepot = false)
            {
                compRobot.driveStraight(10, .5f);
                compRobot.driveStraight(30, 1);
                sleep(500);
                while (compRobot.getFrontDistSens().getDistance(DistanceUnit.INCH) > 28 && compRobot.getFrontRightDistSens().getDistance(DistanceUnit.INCH) > 28)
                {
                    compRobot.driveMotors(.4f, .4f);
                }
                compRobot.driveStraight(-6, 1);
                compRobot.pivotenc(160, .5f);

                compRobot.pivotenc(15, .5f);
                compRobot.driveStraight(16, .6f);

                compRobot.pivotenc(55, .5f);
                telemetry.addData("No target seen, so -55 deg turn", null);
                telemetry.update();

                sleep(600);
                compRobot.hugWallToRight(4, 6, 18, 84);
                compRobot.driveStraight(6, .8f);
                telemetry.addData("Stopped", null);
                telemetry.update();
                compRobot.stopDriveMotors();
            }
            else
            {
                compRobot.driveStraight(-6, 1);
                compRobot.pivotenc(160, .5f);

                compRobot.pivotenc(15, .5f);
                compRobot.driveStraight(16, .6f);

                compRobot.pivotenc(55, .5f);
                telemetry.addData("No target seen, so -55 deg turn", null);
                telemetry.update();

                sleep(600);
                compRobot.hugWallToRight(4, 6, 18, 84);
                compRobot.driveStraight(6, .8f);
                telemetry.addData("Stopped", null);
                telemetry.update();
                compRobot.stopDriveMotors();
            }
        }
    }
}
