package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.debugging.ConsoleManager;
import org.firstinspires.ftc.teamcode.smarthardware.AdvancedMotorController;
import org.firstinspires.ftc.teamcode.smarthardware.SmartServo;

public abstract class MainRobotBase extends ImprovedOpModeBase
{
    /*** CONFIGURE ALL ROBOT ELEMENTS HERE ***/
    //Drive motors (they are lists because it helps when we add on new motors.
    protected AdvancedMotorController leftDrive, rightDrive;
    //Other motors
    protected AdvancedMotorController harvester, flywheels;
    protected DcMotor lift;
    protected SmartServo rightButtonPusher, frontButtonPusher, capBallHolder;

    protected void initializeHardware () throws InterruptedException
    {
        //Make sure that the robot components are found and initialized correctly.
        /*************************** DRIVING MOTORS ***************************/
        ConsoleManager.outputNewSequentialLine ("Setting up drive motors...");
        //The back motors are the ones that have functional encoders, while the front ones don't currently work.
        leftDrive = new AdvancedMotorController (initialize (DcMotor.class, "backLeft"), initialize (DcMotor.class, "frontLeft")).
                setRPSConversionFactor (0.40).
                setMotorDirection (DcMotorSimple.Direction.REVERSE).
                setAdjustmentSensitivity (.00001).
                setAdjustmentSensitivityBounds (0.3);

        rightDrive = new AdvancedMotorController (initialize (DcMotor.class, "backRight"), initialize (DcMotor.class, "frontRight")).
                setRPSConversionFactor (0.36).
                setAdjustmentSensitivity (.00001).
                setAdjustmentSensitivityBounds (0.3);;
        ConsoleManager.appendToLastSequentialLine ("OK!");

        /*************************** OTHER MOTORS AND SERVOS ***************************/
        ConsoleManager.outputNewSequentialLine ("Setting up harvester...");
        harvester = new AdvancedMotorController (initialize (DcMotor.class, "harvester")).
                setRPSConversionFactor (0.40).
                setMotorDirection (DcMotorSimple.Direction.REVERSE);
        harvester.resetEncoder ();
        ConsoleManager.appendToLastSequentialLine ("OK!");

        ConsoleManager.outputNewSequentialLine ("Setting up flywheels...");
        flywheels = new AdvancedMotorController (initialize (DcMotor.class, "flywheels")).
                setRPSConversionFactor (0.02). //Very little resistance on the flywheels.
                setMotorType (AdvancedMotorController.MotorType.NeverRest3P7).
                setMotorDirection (DcMotor.Direction.REVERSE).
                setAdjustmentSensitivity (.00001).
                setAdjustmentSensitivityBounds (0.3);
        flywheels.resetEncoder ();
        ConsoleManager.appendToLastSequentialLine ("OK!");

        ConsoleManager.outputNewSequentialLine ("Setting up lift...");
        lift = initialize (DcMotor.class, "lift");
        lift.setDirection (DcMotorSimple.Direction.REVERSE);
        ConsoleManager.appendToLastSequentialLine ("OK!");

        ConsoleManager.outputNewSequentialLine ("Setting up servos...");
        rightButtonPusher = new SmartServo (initialize (Servo.class, "rightButtonPusher"), 0.5);
        frontButtonPusher = new SmartServo (initialize (Servo.class, "frontButtonPusher"), .21, .84, .84);
        capBallHolder = new SmartServo (initialize (Servo.class, "clamp"), .02, .02, 1.0);
        ConsoleManager.appendToLastSequentialLine ("OK!");

        //Certain things are only applicable in autonomous or teleop.
        initializeOpModeSpecificHardware ();
    }

    //Optional overload.
    protected void initializeOpModeSpecificHardware () throws InterruptedException {}
}