package org.firstinspires.ftc.teamcode.autonomous.utility;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.autonomous.AutoBase;
import org.firstinspires.ftc.teamcode.programflow.RunState;

@Autonomous(name="Drive Forward", group = "Utility Group")

public class DriveForward extends AutoBase
{
    //Custom initialization
    @Override
    protected void driverStationSaysINITIALIZE()
    {
        //Set the motor powers.
        leftDrive.setRPS (-.6);
        rightDrive.setRPS (.6);

        while (RunState.getState () == RunState.DriverSelectedState.INIT)
        {
            leftDrive.updateMotorPowerWithPID ();
            rightDrive.updateMotorPowerWithPID ();
        }
    }

    //Called after runOpMode() has finished initializing.
    protected void driverStationSaysGO() throws InterruptedException
    {
        //Set the motor powers.
        leftDrive.setRPS (.8);
        rightDrive.setRPS (.8);

        while (RunState.getState () == RunState.DriverSelectedState.RUNNING)
        {
            leftDrive.updateMotorPowerWithPID ();
            rightDrive.updateMotorPowerWithPID ();
        }
    }
}