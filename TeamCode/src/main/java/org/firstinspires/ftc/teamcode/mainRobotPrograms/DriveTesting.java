package org.firstinspires.ftc.teamcode.mainRobotPrograms;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.util.Range;

@Autonomous(name="Drive Testing", group = "Auto Group")

public class DriveTesting extends _AutonomousBase
{
    //Called after runOpMode() has finished initializing by BaseFunctions.
    protected void driverStationSaysGO() throws InterruptedException
    {
        int turns = 0;
        while (opModeIsActive ())
        {
            driveForDistance (Math.random () * 0.7 + 0.3, 2000);
            turnToHeading (90 * ((turns + 1) % 4), TurnMode.BOTH, 3000);
            turns++;
        }
    }
}