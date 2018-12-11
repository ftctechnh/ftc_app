package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
@Autonomous(name = "PracticePictureFirst")
public class PracticePictureFirst extends LinearOpMode
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
        float  yawAngleTurn;
        float distanceTraveled = 0;
        waitForStart();
        compRobot.climbDown();
        sleep(2000);
        compRobot.driveStraight(10,.8f);
        compRobot.pivotenc(95, .8f); //100 worked about 2/3 of the time

        while (true)
        {
            double frontDist = compRobot.getFrontDistance_IN();
            if (frontDist <= 3 + frontSensorDepth)
                break;
            else
                compRobot.driveStraight(8, .5f);
            distanceTraveled = distanceTraveled + 8;
            if (distanceTraveled == 40)
            {
                break;
            }
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
                yawAngleTurn = 100 - yawAngle;
                compRobot.pivotenc(yawAngleTurn, .8f);
            }
            else
            {
                telemetry.addData("Such target is not in my sight!", null);
                compRobot.pivotenc(60, .8f);
            }

            telemetry.update();
        }
        compRobot.driveStraight(48, .8f);
        compRobot.deployMarker();
        telemetry.update();
        compRobot.driveStraight(-72, .5f);
        compRobot.stopDriveMotors();
    }
}
