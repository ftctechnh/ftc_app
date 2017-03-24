package org.firstinspires.ftc.teamcode.autonomous.utility;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.autonomous.AutoBase;
import org.firstinspires.ftc.teamcode.programflow.ConsoleManager;
import org.firstinspires.ftc.teamcode.programflow.ProgramFlow;
import org.firstinspires.ftc.teamcode.programflow.RunState;

@Autonomous(name="Drive Forward", group = "Utility Group")

public class DriveForward extends AutoBase
{
    @Override
    protected void driverStationSaysINITIALIZE()
    {
        //Set the motor powers.
        leftDrive.setRPS (.3);
        rightDrive.setRPS (-.3);

        while (RunState.getState () == RunState.DriverSelectedState.INIT)
        {
            ProgramFlow.pauseForMS (80);

            leftDrive.updateMotorPowerWithPID ();
            rightDrive.updateMotorPowerWithPID ();
        }
    }

    protected void driverStationSaysGO() throws InterruptedException
    {
        //Set the motor powers.
        leftDrive.setRPS (.6);
        rightDrive.setRPS (.6);

        while (RunState.getState () == RunState.DriverSelectedState.RUNNING)
        {
            ProgramFlow.pauseForMS (80);

            leftDrive.updateMotorPowerWithPID ();
            rightDrive.updateMotorPowerWithPID ();
        }
    }
}