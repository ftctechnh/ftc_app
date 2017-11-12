package org.firstinspires.ftc.teamcode.SeasonCode.VelocityVortex;


import android.util.Log;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;


/**
 * <p>
 *      Here's our spool. Things pertaining solely to the spool go here.
 * </p>
 *
 *
 * <p>
 *      While coding in this package, keep these units in mind: <br>
 *      1. Assume all angles are measured in degrees <br>
 *      2. Assume all distances are measured in centimeters <br>
 *      3. Assume all measurements of time are done in milliseconds <br>
 * </p>
 *
 *
 * <p>
 *      Hardware checklist: <br>
 *      1. 1 Motor
 * </p>
 *
 *
 * That's all, folks!
 */
@SuppressWarnings("all")
final class Spool
{
    private Robot _robot = null;            // Robot we're working with
    private DcMotor _motor = null;          // Motor used to spool the "tape measure"


    /**
     * Constructor- Creates a spool object
     *
     * @param ROBOT Robot we're working with
     */
    Spool(final Robot ROBOT)
    {
        _robot = ROBOT;
    }


    /**
     * Maps the spool
     */
    void mapHardware()
    {
        HardwareAux mapHelper = new HardwareAux(_robot);        // Helps with mapping

        _motor = mapHelper.mapMotor("spool" , DcMotorSimple.Direction.FORWARD);
    }


    /**
     * Attempts to power the spool given a power level to run at
     *
     * @param POWER The power level to run the spool at
     *
     * @return True if attempt to power spool is successful, false otherwise
     */
    boolean run(final double POWER)
    {
        boolean success = true;     // Tells whether attempt to power spool is successful or not

        try
        {
            _motor.setPower(POWER);
        }
        catch(Exception e)
        {
            Log.e("Error" , "Spool cannot be powered, check your mapping");
            success = false;
        }

        return success;
    }


    /**
     * Attempts to stop the spool
     *
     * @return True if attempt to stop spool is successful, false otherwise
     */
    boolean stop()
    {
        boolean success = true;

        try
        {
            _motor.setPower(0);
        }
        catch(Exception e)
        {
            Log.e("Error" , "Spool cannot be stopped, check your mapping");
            success = false;
        }

        return success;
    }
}