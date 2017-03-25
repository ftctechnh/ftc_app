package org.firstinspires.ftc.teamcode.debugging.programs;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.autonomous.AutoBase;

@Autonomous(name="Flywheel PID Testing", group = "Utility Group")

public class PIDTesting extends AutoBase
{
    //Called after runOpMode() has finished initializing.
    protected void driverStationSaysGO() throws InterruptedException
    {
        flywheels.setRPS (1);

        while (true)
        {
            flywheels.updateMotorPowerWithPID ();

            idle();
        }
    }
}