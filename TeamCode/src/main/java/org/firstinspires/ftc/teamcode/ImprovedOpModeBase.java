package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.HardwareDevice;

import org.firstinspires.ftc.teamcode.debugging.ConsoleManager;

public abstract class ImprovedOpModeBase extends LinearOpMode
{
    //This took a LONG TIME TO WRITE
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
        //Preliminary stuff.
        ConsoleManager.setMainTelemetry (telemetry);

        //REQUIRED in MainRobotBase.
        initializeHardware();

        //Initialize stuff.
        driverStationSaysINITIALIZE();

        //Wait for the start button to be pressed.
        waitForStart();

        //This is where the child classes differ.
        driverStationSaysGO();
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