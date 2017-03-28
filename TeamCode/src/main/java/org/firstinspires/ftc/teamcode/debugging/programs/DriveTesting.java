package org.firstinspires.ftc.teamcode.debugging.programs;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.autonomous.AutoBase;
import org.firstinspires.ftc.teamcode.debugging.ConsoleManager;

@Autonomous(name = "Driving - PID Debug", group = "Utility Group")

public class DriveTesting extends AutoBase
{
    //Called after runOpMode() has finished initializing.
    protected void driverStationSaysGO() throws InterruptedException
    {
        //Set the motor powers.
        leftDrive.setRPS (3);
        rightDrive.setRPS (3);

        while (true)
        {
            sleep (100); //100ms seems to be the ideal rate at which PID is updated.

            leftDrive.updateMotorPowerWithPID ();
            rightDrive.updateMotorPowerWithPID ();

            ConsoleManager.outputConstantDataToDrivers (
                    new String[]
                            {
                                    "L conversion " + leftDrive.getRPSConversionFactor (),
                                    "R conversion " + rightDrive.getRPSConversionFactor (),
                                    "L expected " + leftDrive.getExpectedTicksSinceUpdate (),
                                    "L actual " + leftDrive.getActualTicksSinceUpdate (),
                                    "R expected " + rightDrive.getExpectedTicksSinceUpdate (),
                                    "R actual " + rightDrive.getActualTicksSinceUpdate ()
                            }
            );
        }
    }
}
