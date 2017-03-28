package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.HardwareDevice;

import org.firstinspires.ftc.teamcode.enhancements.EZThread;
import org.firstinspires.ftc.teamcode.debugging.ConsoleManager;

public abstract class ImprovedOpModeBase extends LinearOpMode
{
    //Initializes any hardware device provided by some class.
    protected <T extends HardwareDevice> T initialize(Class <T> hardwareDevice, String name)
    {
        try
        {
            //Returns the last subclass (if this were a DcMotor it would pass back a Dc Motor.
            return hardwareDevice.cast(hardwareMap.get(name));
        }
        catch (Exception e)
        {
            ConsoleManager.outputNewLineToDrivers("Could not find " + name + " in hardware map.");
            return null;
        }
    }

    // Called on initialization (once)
    @Override
    public void runOpMode() throws InterruptedException
    {
        try
        {
            //Preliminary stuff.
            ConsoleManager.setMainTelemetry (telemetry);
            EZThread.initializeThreadCreator (hardwareMap.appContext);

            //REQUIRED in MainRobotBase.
            initializeHardware ();

            //Initialize stuff.
            driverStationSaysINITIALIZE ();

            //Wait for the start button to be pressed.
            waitForStart ();

            //This is where the child classes differ.
            driverStationSaysGO ();
        }
        catch (InterruptedException e)
        {
            ConsoleManager.outputNewLineToDrivers ("Quitting app early.");
        }
        finally
        {
            EZThread.killAllThreads ();
            driverStationSaysSTOP ();
        }
    }

    //Required overload.
    protected abstract void initializeHardware() throws InterruptedException;
    //Optional overload.
    protected void driverStationSaysINITIALIZE() throws InterruptedException {}
    //Has to be implemented.
    protected abstract void driverStationSaysGO() throws InterruptedException;
    //Optional overload.
    protected void driverStationSaysSTOP() {}
}