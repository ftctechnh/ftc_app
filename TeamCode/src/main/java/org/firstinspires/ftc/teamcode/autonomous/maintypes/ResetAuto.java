/**
 * This reset auto enables the user to be lazy and drive the robot back to the original starting position without getting up :P
 */

package org.firstinspires.ftc.teamcode.autonomous.maintypes;

import com.qualcomm.robotcore.hardware.DcMotorSimple;

import org.firstinspires.ftc.teamcode.NiFTBase;
import org.firstinspires.ftc.teamcode.console.NiFTConsole;
import org.firstinspires.ftc.teamcode.hardware.NiFTMotorController;
import org.firstinspires.ftc.teamcode.threads.NiFTFlow;

public abstract class ResetAuto extends NiFTBase
{
    private NiFTMotorController leftDrive, rightDrive;

    protected void initializeHardware () throws InterruptedException
    {
        //Make sure that the robot components are found and initialized correctly.
        /*------------------- DRIVING MOTORS ----------------------*/
        NiFTConsole.outputNewSequentialLine ("Setting up drive motors...");

        leftDrive = new NiFTMotorController ("Left Drive", "backLeft", "frontLeft").
                setRPSConversionFactor (0.40).
                setMotorDirection (DcMotorSimple.Direction.REVERSE).
                setAdjustmentSensitivity (.00001).
                setAdjustmentSensitivityBounds (0.2);

        rightDrive = new NiFTMotorController ("Right Drive", "backRight", "frontRight").
                setRPSConversionFactor (0.36).
                setAdjustmentSensitivity (.00001).
                setAdjustmentSensitivityBounds (0.2);

        NiFTConsole.appendToLastSequentialLine ("OK!");
    }

    protected abstract int getSign();

    @Override
    protected void driverStationSaysINITIALIZE()
    {
        //Set the motor powers.
        leftDrive.setDirectMotorPower (getSign() * -.5);
        rightDrive.setDirectMotorPower (getSign () * .5);
    }

    @Override
    protected void driverStationSaysGO () throws InterruptedException
    {
        //Set the motor powers.
        leftDrive.setDirectMotorPower (getSign() * .5);
        rightDrive.setDirectMotorPower (getSign () * .5);

        while (true)
            NiFTFlow.pauseForSingleFrame ();
    }
}
