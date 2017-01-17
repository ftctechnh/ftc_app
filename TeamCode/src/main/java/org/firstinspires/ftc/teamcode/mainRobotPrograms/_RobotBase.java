package org.firstinspires.ftc.teamcode.mainRobotPrograms;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareDevice;
import com.qualcomm.robotcore.hardware.OpticalDistanceSensor;
import com.qualcomm.robotcore.hardware.Servo;

import java.util.ArrayList;

//Edited in order to have all of the important constants as final, so that no unintentional modifications are made.
//This class should be used so that any changes made to the robot configuration propagates through all parts of the code that has been written.

public abstract class _RobotBase extends LinearOpMode
{
    /*** CONFIGURE ALL ROBOT ELEMENTS HERE ***/
    //Drive motors (they are lists because it helps when we add on new motors.
    protected ArrayList <DcMotor> leftDriveMotors = new ArrayList <>(), rightDriveMotors = new ArrayList<>();
    //Other motors
    protected DcMotor harvester, flywheels;

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
            outputNewLineToDriverStation("Could not find " + name + " in hardware map.");
            return null;
        }
    }

    // Called on initialization (once)
    @Override
    public void runOpMode() throws InterruptedException
    {
        //Make sure that the robot components are found and initialized correctly.
        //This all happens during init()
        /*************************** DRIVING MOTORS ***************************/
        rightDriveMotors.add(initialize(DcMotor.class, "frontRight"));
        rightDriveMotors.add(initialize(DcMotor.class, "backRight"));

        leftDriveMotors.add(initialize(DcMotor.class, "frontLeft"));
        leftDriveMotors.add(initialize(DcMotor.class, "backLeft"));
        for(DcMotor motor : leftDriveMotors)
                motor.setDirection(DcMotor.Direction.REVERSE);

        /*************************** OTHER MOTORS ***************************/
        harvester = initialize(DcMotor.class, "harvester");
        flywheels = initialize(DcMotor.class, "flywheels");
        flywheels.setDirection(DcMotor.Direction.REVERSE);

        //NOTE: Actually attempting to use null motors will cause the program to terminate.
        //This advanced system is designed for when only specific hardware is required.
        //This code should tell you which motors and sensors are not configured before the program starts running.
        //Kudos Makiah

        //Actual program thread
        //Custom Initialization steps.
        try
        {
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

    //Optional overload.
    protected void driverStationSaysINITIALIZE() throws InterruptedException {}
    //Has to be implemented.
    protected abstract void driverStationSaysGO() throws InterruptedException;
    //Optional overload.
    protected void driverStationSaysSTOP() {}

    /*** USE TO OUTPUT DATA IN A SLIGHTLY BETTER WAY THAT LINEAR OP MODES HAVE TO ***/
    ArrayList<String> linesAccessible = new ArrayList<>();
    private int maxLines = 7;
    protected void outputNewLineToDriverStation(String newLine)
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
    protected void outputConstantLinesToDriverStation (String[] data)
    {
        telemetry.update();
        for (String s : data)
            telemetry.addLine(s);
        telemetry.update();
    }
}