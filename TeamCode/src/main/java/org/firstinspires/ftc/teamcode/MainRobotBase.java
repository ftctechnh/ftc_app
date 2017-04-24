/**
 * This base class contains the robot initialization steps for hardware used in both autonomous and teleop.
 */

package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

import org.firstinspires.ftc.teamcode.console.NiFTConsole;
import org.firstinspires.ftc.teamcode.hardware.NiFTInitializer;
import org.firstinspires.ftc.teamcode.hardware.NiFTMotorController;
import org.firstinspires.ftc.teamcode.hardware.NiFTServo;

public abstract class MainRobotBase extends NiFTBase
{
    /*** CONFIGURE ALL ROBOT ELEMENTS HERE ***/
    //Drive motors (they are lists because it helps when we add on new motors.
    protected NiFTMotorController leftDrive, rightDrive;
    //Other motors
    protected NiFTMotorController harvester, flywheels;
    protected DcMotor lift;
    protected NiFTServo rightButtonPusher, frontButtonPusher, capBallHolder;

    protected void initializeHardware () throws InterruptedException
    {
        //Make sure that the robot components are found and initialized correctly.
        /*************************** DRIVING MOTORS ***************************/
        NiFTConsole.outputNewSequentialLine ("Setting up drive motors...");

        //The back motors are the ones that have functional encoders, while the front ones don't currently work.
        leftDrive = new NiFTMotorController ("Left Drive", "backLeft", "frontLeft").
                setRPSConversionFactor (0.40).
                setMotorDirection (DcMotorSimple.Direction.REVERSE).
                setAdjustmentSensitivity (.00001).
                setAdjustmentSensitivityBounds (0.3);

        rightDrive = new NiFTMotorController ("Right Drive", "backRight", "frontRight").
                setRPSConversionFactor (0.36).
                setAdjustmentSensitivity (.00001).
                setAdjustmentSensitivityBounds (0.3);

        NiFTConsole.appendToLastSequentialLine ("OK!");

        /*************************** OTHER MOTORS AND SERVOS ***************************/
        NiFTConsole.outputNewSequentialLine ("Setting up harvester...");
        harvester = new NiFTMotorController ("Harvester", "harvester").
                setRPSConversionFactor (0.40).
                setMotorDirection (DcMotorSimple.Direction.REVERSE);
        NiFTConsole.appendToLastSequentialLine ("OK!");

        NiFTConsole.outputNewSequentialLine ("Setting up flywheels...");
        flywheels = new NiFTMotorController ("Flywheels", "flywheels").
                setRPSConversionFactor (0.02). //Very little resistance on the flywheels.
                setMotorType (NiFTMotorController.MotorType.NeverRest3P7).
                setMotorDirection (DcMotor.Direction.REVERSE).
                setAdjustmentSensitivity (.00001).
                setAdjustmentSensitivityBounds (0.3);
        NiFTConsole.appendToLastSequentialLine ("OK!");

        NiFTConsole.outputNewSequentialLine ("Setting up lift...");
        lift = NiFTInitializer.initialize (DcMotor.class, "lift");
        lift.setDirection (DcMotorSimple.Direction.REVERSE);
        NiFTConsole.appendToLastSequentialLine ("OK!");

        NiFTConsole.outputNewSequentialLine ("Setting up servos...");
        rightButtonPusher = new NiFTServo ("rightButtonPusher", 0.5);
        frontButtonPusher = new NiFTServo ("frontButtonPusher", .21, .84, .84);
        capBallHolder = new NiFTServo ("clamp", .02, .02, 1.0);
        NiFTConsole.appendToLastSequentialLine ("OK!");

        //Certain things are only applicable in autonomous or teleop.
        initializeOpModeSpecificHardware ();
    }

    //Optional overload.
    protected void initializeOpModeSpecificHardware () throws InterruptedException {}
}