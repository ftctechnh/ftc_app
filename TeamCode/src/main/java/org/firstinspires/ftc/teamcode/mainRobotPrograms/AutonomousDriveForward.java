package org.firstinspires.ftc.teamcode.mainRobotPrograms;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

/**
 * Created by makiah on 11/17/16.
 */

@Autonomous(name = "Autonomous - Drive Forward Edition", group = "Autonomous Group")

public class AutonomousDriveForward extends AutonomousBase
{
    //Called after runOpMode() has finished initializing.
    protected void driverStationSaysGO() throws InterruptedException
    {
        drive(80, 2000);
    }
}
