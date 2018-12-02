package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

@Autonomous(name = "FrontSensTest")
public class FrontSensTest extends LinearOpMode
{
    CompRobot compRobot;
    VuforiaFunctions vuforiaFunctions;

    public void runOpMode()
    {
        compRobot = new CompRobot(hardwareMap,this);
        boolean dummy=true;
        waitForStart();
        while(compRobot.getFrontDistSens().getDistance(DistanceUnit.INCH) > 1)
        {
            telemetry.addData( "FrontDist= ",compRobot.getFrontDistSens().getDistance(DistanceUnit.INCH));
        }
    }
}