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
    protected final AdvancedMotorController
            leftDrive = new AdvancedMotorController (initialize (DcMotor.class, "backLeft"), initialize (DcMotor.class, "frontLeft")).
                    setRPSConversionFactor (0.40).
                    setMotorDirection (DcMotorSimple.Direction.REVERSE).
                    setAdjustmentSensitivity (.00001).
                    setAdjustmentSensitivityBounds (0.3),

            rightDrive = new AdvancedMotorController (initialize (DcMotor.class, "backRight"), initialize (DcMotor.class, "frontRight")).
                    setRPSConversionFactor (0.36).
                    setAdjustmentSensitivity (.00001).
                    setAdjustmentSensitivityBounds (0.3),

            harvester = new AdvancedMotorController (initialize(DcMotor.class, "harvester")).
                    setRPSConversionFactor (0.40).
                    setMotorDirection (DcMotorSimple.Direction.REVERSE),

            flywheels = new AdvancedMotorController (initialize(DcMotor.class, "Flywheels")).
                    setRPSConversionFactor (0.02). //Very little resistance on the flywheels.
                    setMotorType (AdvancedMotorController.MotorType.NeverRest3P7).
                    setMotorDirection (DcMotor.Direction.REVERSE).
                    setAdjustmentSensitivity (.00001).
                    setAdjustmentSensitivityBounds (0.3);

    protected final DcMotor lift = initialize (DcMotor.class, "lift");

    protected final SmartServo
            rightButtonPusher = new SmartServo (initialize(Servo.class, "rightButtonPusher"), 0.5),
            frontButtonPusher = new SmartServo (initialize (Servo.class, "frontButtonPusher"), .21, 0.84, .84),
            capBallHolder = new SmartServo (initialize (Servo.class, "clamp"), .02, .02, 1.0);

    protected void initializeHardware () throws InterruptedException
    {
        //Flywheels and harvester encoders.
        ConsoleManager.outputNewLineToDrivers ("Enabling PID updates on flywheels and harvester...");
        flywheels.resetEncoder ();
        harvester.resetEncoder ();
        flywheels.enablePIDUpdateTask ();
        harvester.enablePIDUpdateTask ();
        ConsoleManager.appendToLastOutputtedLine ("OK!");

        //Certain things are only applicable in autonomous or teleop.
        initializeOpModeSpecificHardware ();
    }

    //Optional overload.
    protected void initializeOpModeSpecificHardware () throws InterruptedException {}
}