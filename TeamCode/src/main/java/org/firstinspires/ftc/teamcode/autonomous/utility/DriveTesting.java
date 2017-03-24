package org.firstinspires.ftc.teamcode.autonomous.utility;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.autonomous.AutoBase;
import org.firstinspires.ftc.teamcode.programflow.ConsoleManager;
import org.firstinspires.ftc.teamcode.programflow.ProgramFlow;
import org.firstinspires.ftc.teamcode.programflow.RunState;

@Autonomous(name = "Driving - PID Debug", group = "Utility Group")

public class DriveTesting extends AutoBase
{
    @Override
    protected void driverStationSaysINITIALIZE()
    {
        //Set the motor powers.
        leftDrive.setRPS (3);
        rightDrive.setRPS (3);

        while (RunState.getState () == RunState.DriverSelectedState.INIT)
        {
            ProgramFlow.pauseForMS (100);

            leftDrive.updateMotorPowerWithPID ();
            rightDrive.updateMotorPowerWithPID ();

            outputData ();
        }

        leftDrive.reset ();
        rightDrive.reset ();
    }

    //Called after runOpMode() has finished initializing.
    protected void driverStationSaysGO() throws InterruptedException
    {
        //Set the motor powers.
        leftDrive.setRPS (1);
        rightDrive.setRPS (1);

        while (RunState.getState () == RunState.DriverSelectedState.RUNNING)
        {
            ProgramFlow.pauseForMS (100);

            leftDrive.updateMotorPowerWithPID ();
            rightDrive.updateMotorPowerWithPID ();

            outputData ();
        }

        leftDrive.reset ();
        rightDrive.reset ();
    }

    private void outputData()
    {
        ConsoleManager.outputConstantDataToDrivers (
                new String[]
                        {
                                "L conversion " + leftDrive.rpsConversionFactor,
                                "R conversion " + rightDrive.rpsConversionFactor,
                                "L expected " + leftDrive.expectedTicksSinceUpdate,
                                "L actual " + leftDrive.actualTicksSinceUpdate,
                                "R expected " + rightDrive.expectedTicksSinceUpdate,
                                "R actual " + rightDrive.actualTicksSinceUpdate
                        }
        );
    }
}
