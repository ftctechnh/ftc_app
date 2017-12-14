package org.firstinspires.ftc.teamcode.SeasonCode.VelocityVortex;

import android.util.Log;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;


/**
 * <p>
 *      Here's our lift. Things pertaining solely to the lift go here.
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
final class Lift
{
    private Robot _robot = null;                // Robot we're working with
    private DcMotor _motor = null;              // The winch used to raise the lift


    /**
     * Creates a lift
     *
     * @param ROBOT The robot we're working withs
     */
    Lift(final Robot ROBOT)
    {
        _robot = ROBOT;
    }


    /**
     * Maps the lift
     */
    void mapHardware()
    {
        HardwareAux mapHelper = new HardwareAux(_robot);    // Helps map hardware

        _motor = mapHelper.mapMotor("lift" , DcMotorSimple.Direction.FORWARD);
    }


    /**
     * Attempts to power the lift at the power passed in
     *
     * @param POWER The value to power the lift at
     *
     * @return True if attempt to power lift is successful, false otherwise
     */
    boolean run(final double POWER)
    {
        boolean success = true;     // Tells whether attempt to power lift is successful or not

        try
        {
            _motor.setPower(POWER);
        }
        catch(Exception e)
        {
            Log.e("Error" , "Cannot power lift, check your mapping");
            success = false;
        }

        return success;
    }


    /**
     * Attempts to stop the lift
     *
     * @return True if attempt ot power lift is successful, false otherwise
     */
    boolean stop()
    {
        boolean success = true;         // Tells whether attempt to stop lift is successful or not

        try
        {
            _motor.setPower(0);
        }
        catch(Exception e)
        {
            Log.e("Error" , "Cannot stop lift, check your mapping");
            success = false;
        }

        return success;
    }
}