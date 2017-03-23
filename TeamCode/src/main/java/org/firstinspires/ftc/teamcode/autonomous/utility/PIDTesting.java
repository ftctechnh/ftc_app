package org.firstinspires.ftc.teamcode.autonomous.utility;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.autonomous.AutoBase;
import org.firstinspires.ftc.teamcode.programflow.RunState;

@Autonomous(name="Drive Backward", group = "Utility Group")

public class PIDTesting extends AutoBase
{
    //Called after runOpMode() has finished initializing.
    protected void driverStationSaysGO() throws InterruptedException
    {
        flywheels.moveAtRPS (1);

        while (RunState.getState () == RunState.DriverSelectedState.RUNNING)
        {
            flywheels.updateMotorPowerWithPID ();
        }
    }
}