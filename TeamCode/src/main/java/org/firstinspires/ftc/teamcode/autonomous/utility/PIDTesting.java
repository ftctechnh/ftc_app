package org.firstinspires.ftc.teamcode.autonomous.utility;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.autonomous.AutoBase;
import org.firstinspires.ftc.teamcode.programflow.RunState;

@Autonomous(name="Flywheel PID Testing", group = "Utility Group")

public class PIDTesting extends AutoBase
{
    //Called after runOpMode() has finished initializing.
    protected void driverStationSaysGO() throws InterruptedException
    {
        flywheels.setRPS (1);

        while (!RunState.stopRequested ())
        {
            flywheels.updateMotorPowerWithPID ();

            idle();
        }
    }
}