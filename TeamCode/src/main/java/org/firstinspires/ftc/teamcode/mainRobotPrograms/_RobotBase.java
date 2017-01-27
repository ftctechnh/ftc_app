package org.firstinspires.ftc.teamcode.mainRobotPrograms;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareDevice;
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
    protected DcMotor harvester, flywheels, lift;
    protected Servo leftLifterServo, rightLifterServo;
    protected Servo leftButtonPusher, rightButtonPusher;
    protected Servo clamp;
    protected final double RIGHT_SERVO_LOCKED = 0.55, LEFT_SERVO_LOCKED = 0.35, RIGHT_SERVO_UNLOCKED = 0.3, LEFT_SERVO_UNLOCKED = 0.07, CLAMP_CLOSED = 0, CLAMP_OPEN = 1;

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
        //Make sure that the robot components are found and initialized correctly.
        //This all happens during init()
        /*************************** DRIVING MOTORS ***************************/
        rightDriveMotors.add(initialize(DcMotor.class, "frontRight"));
        rightDriveMotors.add(initialize(DcMotor.class, "backRight"));
        for(DcMotor motor : rightDriveMotors)
            motor.setDirection(DcMotor.Direction.REVERSE);

        leftDriveMotors.add(initialize(DcMotor.class, "frontLeft"));
        leftDriveMotors.add(initialize(DcMotor.class, "backLeft"));

        /*************************** OTHER MOTORS ***************************/
        harvester = initialize(DcMotor.class, "harvester");
        flywheels = initialize(DcMotor.class, "flywheels");
        flywheels.setDirection(DcMotor.Direction.REVERSE);

        leftLifterServo = initialize(Servo.class, "leftServo");
        leftLifterServo.setPosition(LEFT_SERVO_LOCKED);
        rightLifterServo = initialize(Servo.class, "rightServo");
        rightLifterServo.setPosition(RIGHT_SERVO_LOCKED);
        lift = initialize(DcMotor.class, "lift");
        leftButtonPusher = initialize(Servo.class, "leftButtonPusher");
        leftButtonPusher.setPosition(0.5);
        rightButtonPusher = initialize(Servo.class, "rightButtonPusher");
        rightButtonPusher.setPosition(0.5);

        clamp = initialize(Servo.class, "clamperooni");
        clamp.setPosition(CLAMP_CLOSED);

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

    protected void setRightPower(double power)
    {
        for (DcMotor motor : rightDriveMotors)
            motor.setPower(power);
    }

    protected void setLeftPower(double power)
    {
        for (DcMotor motor : leftDriveMotors)
            motor.setPower(power);
    }
}