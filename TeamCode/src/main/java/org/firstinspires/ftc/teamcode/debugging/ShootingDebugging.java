package org.firstinspires.ftc.teamcode.debugging;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.autonomous.AutoBase;
import org.firstinspires.ftc.teamcode.enhancements.ConsoleManager;

@Autonomous(name = "Shooting - PID Debug", group = "Utility Group")

public class ShootingDebugging extends AutoBase
{
    //Called after runOpMode() has finished initializing.
    protected void driverStationSaysGO() throws InterruptedException
    {
        //Set the motor powers.
        flywheels.setRPS (.5);
        harvester.setRPS (-4);

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
            idle();
        }
    }
}
