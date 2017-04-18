package org.firstinspires.ftc.teamcode.debugging;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.autonomous.AutoBase;
import org.firstinspires.ftc.teamcode.console.NiFTConsole;
import org.firstinspires.ftc.teamcode.threads.NiFTFlow;

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
            NiFTConsole.outputNewSequentialLine ("Turn " + turns + " completed.");
            NiFTFlow.pauseForMS (2000);
            turns++;
        }
    }
}