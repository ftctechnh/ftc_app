package org.firstinspires.ftc.teamcode.debugging;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.autonomous.AutoBase;
import org.firstinspires.ftc.teamcode.enhancements.ConsoleManager;
import org.firstinspires.ftc.teamcode.enhancements.ProgramFlow;

@Autonomous(name = "Shooting - PID Debug", group = "Utility Group")

public class ShootingDebugging extends AutoBase
{
    //Called after runOpMode() has finished initializing.
    protected void driverStationSaysGO() throws InterruptedException
    {
        //Set the motor powers.
        flywheels.setRPS (19);
        harvester.setRPS (5);

        while (true)
        {
            ConsoleManager.outputConstantDataToDrivers (
                    new String[]
                            {
                                    "F conversion " + flywheels.getRPSConversionFactor (),
                                    "H conversion " + harvester.getRPSConversionFactor (),
                                    "F expected " + flywheels.getExpectedTicksSinceUpdate (),
                                    "F actual " + flywheels.getActualTicksSinceUpdate (),
                                    "H expected " + harvester.getExpectedTicksSinceUpdate (),
                                    "H actual " + harvester.getActualTicksSinceUpdate ()
                            }
            );
            ProgramFlow.pauseForSingleFrame ();
        }
    }
}
