package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

@Autonomous(name = "CompRobotCalibration")
public class CompRobotCalibration extends LinearOpMode
{
    CompRobot compRobot;

    public void runOpMode() throws InterruptedException
    {
        compRobot = new CompRobot(hardwareMap, this);
        waitForStart();
        compRobot.deployMarker();
        while (opModeIsActive())
        {

        }
    }
}
