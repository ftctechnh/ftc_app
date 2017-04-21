package org.firstinspires.ftc.teamcode.debugging;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.autonomous.AutoBase;
import org.firstinspires.ftc.teamcode.console.NiFTConsole;
import org.firstinspires.ftc.teamcode.threads.NiFTFlow;

@Autonomous(name = "Shooting - PID Debug", group = "Utility Group")

public class ShootingDebugging extends AutoBase
{
    //Called after runOpMode() has finished initializing.
    protected void driverStationSaysGO() throws InterruptedException
    {
        //Set the motor powers.
        flywheels.setRPS (19);
        flywheels.startPIDTask ();
        harvester.setRPS (5);
        harvester.startPIDTask ();

        NiFTConsole.ProcessConsole processConsole = new NiFTConsole.ProcessConsole ("Shooting Debugger");

        while (true)
        {

            processConsole.updateWith (
                    "F conversion " + flywheels.getRPSConversionFactor (),
                    "H conversion " + harvester.getRPSConversionFactor (),
                    "F expected " + flywheels.getExpectedTicksSinceUpdate (),
                    "F actual " + flywheels.getActualTicksSinceUpdate (),
                    "H expected " + harvester.getExpectedTicksSinceUpdate (),
                    "H actual " + harvester.getActualTicksSinceUpdate ()
            );

            NiFTFlow.pauseForSingleFrame ();
        }
    }
}
