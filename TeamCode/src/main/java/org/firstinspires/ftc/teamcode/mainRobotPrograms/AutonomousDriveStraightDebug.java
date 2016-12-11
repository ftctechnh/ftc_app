package org.firstinspires.ftc.teamcode.mainRobotPrograms;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

/**
 * Created by makiah on 11/17/16.
 */

@Autonomous(name = "Autonomous - Debug Edition", group = "Autonomous Group")

public class AutonomousDriveStraightDebug extends _AutonomousBase
{
    //Called after runOpMode() has finished initializing.
    protected void driverStationSaysGO() throws InterruptedException
    {
        driveForTime(0.8, 10000);
    }
}