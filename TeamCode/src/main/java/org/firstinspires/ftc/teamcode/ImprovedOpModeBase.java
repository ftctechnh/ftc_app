package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.HardwareDevice;

import org.firstinspires.ftc.teamcode.threads.ProgramFlow;
import org.firstinspires.ftc.teamcode.debugging.ConsoleManager;

public abstract class ImprovedOpModeBase extends LinearOpMode
{
    //Initializes any hardware device provided by some class.
    protected <T extends HardwareDevice> T initialize (Class<T> hardwareDevice, String name)
    {
        try
        {
            //Returns the last subclass (if this were a DcMotor it would pass back a Dc Motor.
            return hardwareDevice.cast (hardwareMap.get (name));
        }
        catch (Exception e) //There might be other exceptions that this throws, not entirely sure about which so I am general here.
        {
            throw new NullPointerException ("Couldn't find " + name + " in the configuration file!");
        }
    }

    @Override
    public void runOpMode () throws InterruptedException
    {
        try
        {
            //Preliminary stuff.
            ProgramFlow.initializeWithOpMode (this);
            ConsoleManager.initializeWith (telemetry);

            //REQUIRED in MainRobotBase.
            initializeHardware ();

            //Initialize stuff.
            driverStationSaysINITIALIZE ();

            //Wait for the start button to be pressed.
            waitForStart ();

            //This is where the child classes differ.
            driverStationSaysGO ();
        }
        catch (InterruptedException e) {} //If this is caught, then the user requested program stop.
        catch (Exception e) //If this is caught, it wasn't an InterruptedException and wasn't requested, so the user is notified.
        {
            ConsoleManager.outputNewSequentialLine ("UH OH!  An error was just thrown!");
            ConsoleManager.outputNewSequentialLine (e.getMessage ());
            ConsoleManager.outputNewSequentialLine ("Will end upon tapping stop...");

            //Wait indefinitely.
            try
            {
                while (true)
                    ProgramFlow.pauseForSingleFrame ();
            }
            catch (InterruptedException e2) {} //The user has read the message and stops the program.
        }
        finally //Occurs after all possible endings.
        {
            driverStationSaysSTOP ();
        }
    }

    //Required overload.
    protected abstract void initializeHardware () throws InterruptedException;

    //Optional overload.
    protected void driverStationSaysINITIALIZE () throws InterruptedException {}

    //Has to be implemented.
    protected abstract void driverStationSaysGO () throws InterruptedException;

    //Optional overload.
    protected void driverStationSaysSTOP () {}
}