package org.firstinspires.ftc.teamcode.mainRobotPrograms;

import com.qualcomm.hardware.modernrobotics.ModernRoboticsI2cRangeSensor;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.I2cAddr;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.teamcode.BaseFunctions;

import java.util.ArrayList;

public abstract class RobotBase extends BaseFunctions
{
    /*** CONFIGURE ALL ROBOT ELEMENTS HERE ***/
    //Drive motors (they are lists because it helps when we add on new motors.
    protected ArrayList <DcMotor> leftDriveMotors = new ArrayList <>(), rightDriveMotors = new ArrayList<>();
    //Other motors
    protected DcMotor harvester, flywheels, lift;
    protected Servo rightButtonPusher, frontButtonPusher;
    protected Servo capBallHolder;
    protected final double CBH_CLOSED = 0.02, CBH_OPEN = 1.0;
    protected final double FBP_UP = 0.84, FBP_DOWN = FBP_UP - 0.63;
    protected final double MOTOR_POWER_CORRECTION_FACTOR = 0.09; //Range -1 to 1.  Favors left side if positive and vice-versa.

    protected ModernRoboticsI2cRangeSensor sideRangeSensor;
    protected ColorSensor option1ColorSensor, option2ColorSensor;

    // Called on initialization (once)
    protected void initializeHardware() throws InterruptedException
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

        /*************************** OTHER MOTORS AND SERVOS ***************************/
        harvester = initialize(DcMotor.class, "harvester");

        flywheels = initialize(DcMotor.class, "flywheels");
        flywheels.setDirection(DcMotor.Direction.REVERSE);

        lift = initialize(DcMotor.class, "lift");

        rightButtonPusher = initialize(Servo.class, "rightButtonPusher");
        rightButtonPusher.setPosition(0.5); //The stop position for a continuous rotation servo.

        frontButtonPusher = initialize(Servo.class, "frontButtonPusher");
        frontButtonPusher.setPosition(FBP_UP);

        capBallHolder = initialize(Servo.class, "clamp");
        capBallHolder.setPosition(CBH_CLOSED);

        outputNewLineToDrivers ("Initializing Side Range Sensor...");
        sideRangeSensor = initialize(ModernRoboticsI2cRangeSensor.class, "Back Range Sensor");
        sideRangeSensor.setI2cAddress(I2cAddr.create8bit(0x10));
        if (sideRangeSensor.getDistance (DistanceUnit.CM) < 1)
            appendToLastOutputtedLine ("FAILED!");
        else
            appendToLastOutputtedLine ("OK!");

        //Init color sensors.
        outputNewLineToDrivers ("Initializing Color Sensors...");
        option1ColorSensor = initialize(ColorSensor.class, "Option 1 Color Sensor");
        option1ColorSensor.setI2cAddress(I2cAddr.create8bit(0x4c));
        option1ColorSensor.enableLed(false);
        option2ColorSensor = initialize(ColorSensor.class, "Option 2 Color Sensor");
        option2ColorSensor.setI2cAddress(I2cAddr.create8bit(0x5c));
        option2ColorSensor.enableLed(false);
        appendToLastOutputtedLine ("OK!");

        initializeOpModeSpecificHardware ();
    }
    //Optional overload.
    protected void initializeOpModeSpecificHardware() throws InterruptedException
    {
    }

    protected void setRightPower(double power)
    {
        for (DcMotor motor : rightDriveMotors)
            motor.setPower(Range.clip(power * (1 - MOTOR_POWER_CORRECTION_FACTOR), -1, 1));
    }

    protected void setLeftPower(double power)
    {
        for (DcMotor motor : leftDriveMotors)
            motor.setPower(Range.clip(power * (1 + MOTOR_POWER_CORRECTION_FACTOR), -1, 1));
    }
}