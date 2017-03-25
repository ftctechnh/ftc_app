package org.firstinspires.ftc.teamcode.debugging.programs;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.autonomous.AutoBase;
import org.firstinspires.ftc.teamcode.debugging.ConsoleManager;

@Autonomous(name = "Driving - PID Debug", group = "Utility Group")

public class DriveTesting extends AutoBase
{
    @Override
    protected void driverStationSaysINITIALIZE() throws InterruptedException
    {
        //Set the motor powers.
        leftDrive.setRPS (3);
        rightDrive.setRPS (3);

        while (!isStarted ())
        {
            sleep (100);

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

        while (true)
        {
            sleep (100);

            leftDrive.updateMotorPowerWithPID ();
            rightDrive.updateMotorPowerWithPID ();

            outputData ();
        }
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
