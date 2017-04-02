package org.firstinspires.ftc.teamcode.debugging;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.autonomous.AutoBase;
import org.firstinspires.ftc.teamcode.enhancements.ConsoleManager;
import org.firstinspires.ftc.teamcode.enhancements.ProgramFlow;

@Autonomous(name="Turn Testing", group = "Test Group")

public class TurnTesting extends AutoBase
{
    //Called after runOpMode() has finished initializing by ImprovedOpModeBase.
    protected void driverStationSaysGO() throws InterruptedException
    {
        int turns = 0, turnSign = (int) (Math.random () * 2) == 0 ? 1 : -1;
        //Turn in all cardinal directions.
        while (opModeIsActive ())
        {
            turnToHeading (turnSign * (90 * ((turns + 1) % 4)), TurnMode.BOTH, 4000);
            hardBrake (0);
            ConsoleManager.outputNewLineToDrivers ("Turn " + turns + " completed.");
            ProgramFlow.pauseForMS (2000);
            turns++;
        }
    }
}