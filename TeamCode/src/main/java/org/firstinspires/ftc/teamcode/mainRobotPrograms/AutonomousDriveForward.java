package org.firstinspires.ftc.teamcode.mainRobotPrograms;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

@Autonomous(name = "Autonomous - Drive Forward Edition", group = "Autonomous Group")

public class AutonomousDriveForward extends _AutonomousBase
{
    //Called after runOpMode() has finished initializing.
    protected void driverStationSaysGO()
    {
        driveForTime(0.8f, 3f);
    }
}