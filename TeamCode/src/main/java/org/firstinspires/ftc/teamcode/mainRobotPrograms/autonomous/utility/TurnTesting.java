package org.firstinspires.ftc.teamcode.mainRobotPrograms.autonomous.utility;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.mainRobotPrograms.autonomous.AutonomousBase;

@Autonomous(name="Turn Testing", group = "Test Group")

public class TurnTesting extends AutonomousBase
{
    //Called after runOpMode() has finished initializing by BaseFunctions.
    protected void driverStationSaysGO() throws InterruptedException
    {
        int turns = 0, turnSign = (int) (Math.random () * 2) == 0 ? 1 : -1;
        //Turn in all cardinal directions.
        while (opModeIsActive ())
        {
            turnToHeading (turnSign * (90 * ((turns + 1) % 4)), TurnMode.BOTH, 4000);
            stopDriving ();
            outputNewLineToDrivers ("Turn " + turns + " completed.");
            sleep(2000);
            turns++;
        }
    }
}