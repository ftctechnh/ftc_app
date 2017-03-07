package org.firstinspires.ftc.teamcode.mainRobotPrograms;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

@Autonomous(name="Turn Testing", group = "Test Group")

public class TurnTesting extends _AutonomousBase
{
    //Called after runOpMode() has finished initializing by BaseFunctions.
    protected void driverStationSaysGO() throws InterruptedException
    {
        int turns = 0;
        while (opModeIsActive ())
        {
            turnToHeading (90 * ((turns + 1) % 4), TurnMode.BOTH, 4000);
            turns++;
        }
    }
}