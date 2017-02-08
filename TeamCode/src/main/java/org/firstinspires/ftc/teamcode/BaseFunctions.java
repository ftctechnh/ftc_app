package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.HardwareDevice;

import java.util.ArrayList;

public abstract class BaseFunctions extends LinearOpMode
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
            outputNewLineToDrivers("Could not find " + name + " in hardware map.");
            return null;
        }
    }

    // Called on initialization (once)
    @Override
    public void runOpMode() throws InterruptedException
    {
        //Actual program thread
        //Custom Initialization steps.
        try
        {
            //REQUIRED in RobotBase.
            initializeHardware();

            //Initialize stuff.
            driverStationSaysINITIALIZE();

            //Wait for the start button to be pressed.
            waitForStart();

            driverStationSaysGO(); //This is where the child classes differ.
        }
        catch (InterruptedException e)
        {
            driverStationSaysSTOP();
            Thread.currentThread().interrupt();
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

    /*** USE TO OUTPUT DATA IN A SLIGHTLY BETTER WAY THAT LINEAR OP MODES HAVE TO ***/
    ArrayList<String> linesAccessible = new ArrayList<>();
    private int maxLines = 7;
    protected void outputNewLineToDrivers(String newLine)
    {
        //Add new line at beginning of the lines.
        linesAccessible.add(0, newLine);
        //If there is more than 5 lines there, remove one.
        if (linesAccessible.size() > maxLines)
            linesAccessible.remove(maxLines);

        //Output every line in order.
        telemetry.update(); //Empty the output
        for (String s : linesAccessible)
            telemetry.addLine(s); //add all lines
        telemetry.update(); //update the output with the added lines.
    }

    //Allows for more robust output of actual data instead of line by line without wrapping.  Used for driving and turning.
    protected void outputConstantDataToDrivers(String[] data)
    {
        telemetry.update();
        for (String s : data)
            telemetry.addLine(s);
        telemetry.update();
    }
}