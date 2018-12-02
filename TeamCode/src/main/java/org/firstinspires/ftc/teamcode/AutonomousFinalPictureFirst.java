package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

@Autonomous(name = "!FinalPictureFirst")
public class AutonomousFinalPictureFirst extends LinearOpMode
{
    CompRobot compRobot;
    VuforiaFunctions vuforiaFunctions;

    public void runOpMode()
    {
        compRobot = new CompRobot(hardwareMap,this);
        vuforiaFunctions = new VuforiaFunctions(this, hardwareMap);
        float yawAngle = 90;
        float frontSensorDepth = 2;
        float rightSensorDepth = 2;
        float leftSensorDepth = 2;
        float backSensorDepth = 2;
        float  yawAngleTurn;
        waitForStart();
        compRobot.driveStraight(10,.6f);
        compRobot.pivotenc(90, .6f);

        while (true)
        {
            double frontDist = compRobot.getFrontDistance_IN();
 //           telemetry.addData("Front dist= ", frontDist);
 //           telemetry.update();
 //           sleep(2000);
            if (frontDist <= 24 + frontSensorDepth)
                break;
            compRobot.driveMotors(0.2, 0.2f);
        }
        compRobot.stopDriveMotors();
        {
            if (vuforiaFunctions.hasSeenTarget())
            {
                telemetry.addData(vuforiaFunctions.getCurrentNameOfTargetSeen(), null);
                telemetry.addData("X (in): ", vuforiaFunctions.getXPosIn());
                telemetry.addData("Y (in): ", vuforiaFunctions.getYPosIn());
                telemetry.addData("X (ft): ", vuforiaFunctions.getXPosIn() / 12f);
                telemetry.addData("Y (ft): ", vuforiaFunctions.getYPosIn() / 12f);
                telemetry.addData("YAW ", vuforiaFunctions.getYawDeg());
                sleep(1000);
                yawAngle = vuforiaFunctions.getYawDeg();
                yawAngleTurn = 90 - yawAngle;
                compRobot.pivotenc(yawAngleTurn, .6f);
            }
            else
            {
                telemetry.addData("Such target is not in my sight!", null);
                compRobot.pivotenc(45, .5f);
            }

            telemetry.update();
        }

        compRobot.hugWall(6 + rightSensorDepth, 9 + rightSensorDepth, 24, true);
        //The hug wall code in the method is a bit different than the one that was in the original auto file
        //make sure that it still runs as intended.

        telemetry.addData("Stopped", null);
        sleep(2000); //drop team marker into depot
        telemetry.update();
        compRobot.hugWall(6 + rightSensorDepth, 9 + rightSensorDepth, 36, false);
        compRobot.stopDriveMotors();
    }
}