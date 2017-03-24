package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.modernrobotics.ModernRoboticsI2cRangeSensor;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.I2cAddr;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.teamcode.programflow.ConsoleManager;
import org.firstinspires.ftc.teamcode.speedregulation.PIDMotorController;

public abstract class MainRobotBase extends ImprovedOpModeBase
{
    /*** CONFIGURE ALL ROBOT ELEMENTS HERE ***/
    //Drive motors (they are lists because it helps when we add on new motors.
    protected PIDMotorController leftDrive, rightDrive;
    //Other motors
    protected PIDMotorController harvester, flywheels, lift;
    protected Servo rightButtonPusher, frontButtonPusher;
    protected Servo capBallHolder;
    protected final double CBH_CLOSED = 0.02, CBH_OPEN = 1.0;
    protected final double FBP_UP = 0.84, FBP_DOWN = FBP_UP - 0.63;
    protected final double MOTOR_POWER_CORRECTION_FACTOR = 0.09; //Range -1 to 1.  Favors left side if positive and vice-versa.

    // Called on initialization (once)
    protected void initializeHardware() throws InterruptedException
    {
        //Make sure that the robot components are found and initialized correctly.
        //This all happens during init()
        /*************************** DRIVING MOTORS ***************************/
        DcMotor frontRight = initialize(DcMotor.class, "frontRight"),
                backRight = initialize(DcMotor.class, "backRight"),
                frontLeft = initialize(DcMotor.class, "frontLeft"),
                backLeft = initialize(DcMotor.class, "backLeft");
        frontLeft.setDirection (DcMotorSimple.Direction.REVERSE);
        backLeft.setDirection (DcMotorSimple.Direction.REVERSE);

        leftDrive = new PIDMotorController(backLeft, frontLeft);
        rightDrive = new PIDMotorController (backRight, frontRight);

        /*************************** OTHER MOTORS AND SERVOS ***************************/
        harvester = new PIDMotorController (initialize(DcMotor.class, "harvester"));

        flywheels = new PIDMotorController (initialize(DcMotor.class, "flywheels"));
        flywheels.encoderMotor.setDirection (DcMotor.Direction.REVERSE);

        lift = new PIDMotorController (initialize(DcMotor.class, "lift"));
        lift.encoderMotor.setDirection (DcMotorSimple.Direction.REVERSE);

        rightButtonPusher = initialize(Servo.class, "rightButtonPusher");
        rightButtonPusher.setPosition(0.5); //The stop position for a continuous rotation servo.

        frontButtonPusher = initialize(Servo.class, "frontButtonPusher");
        frontButtonPusher.setPosition(FBP_UP);

        capBallHolder = initialize(Servo.class, "clamp");
        capBallHolder.setPosition(CBH_CLOSED);

        //Certain things are only applicable in autonomous or teleop.
        initializeOpModeSpecificHardware ();
    }

    //Optional overload.
    protected void initializeOpModeSpecificHardware() throws InterruptedException
    {

    }
}