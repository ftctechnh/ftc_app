package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

@Autonomous(name = "!FinalCrater")
public class AutonomousCraterFinal extends LinearOpMode
{
    CompRobot compRobot;
    VuforiaFunctions vuforiaFunctions;

    public void runOpMode()
    {
        compRobot = new CompRobot(hardwareMap, this);
        vuforiaFunctions = new VuforiaFunctions(this, hardwareMap);
        float yawAngle = 90;
        float frontSensorDepth = 2;
        float rightSensorDepth = 2;
        float leftSensorDepth = 2;
        float backSensorDepth = 2;
        float yawAngleTurn;
        float distanceTraveled = 0;
        waitForStart();
        compRobot.climbDown();
        sleep(200);
        compRobot.pivotenc(15, .6f);
        compRobot.pivotenc(-15, .6f);
        compRobot.pivotenc(15, .6f);
        compRobot.pivotenc(-15, .6f);
        compRobot.driveStraight(48, .8f);
        compRobot.stopDriveMotors();
    }
}
