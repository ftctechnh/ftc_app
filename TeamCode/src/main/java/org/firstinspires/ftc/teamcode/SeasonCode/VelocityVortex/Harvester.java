package org.firstinspires.ftc.teamcode.SeasonCode.VelocityVortex;


import android.util.Log;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;


/**
 * <p>
 *      Here's our harvester. Things pertaining solely to the harvester go here.
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
final class Harvester
{
    private Robot _robot = null;                // Robot we're working with
    private DcMotor _motor;                     // The sad lonely motor in the mechanism


    /**
     * Helps determine which direction to run the harvester
     */
    enum Direction
    {
        FORWARD ,
        BACKWARD
    }


    /**
     * Creates a harvester
     *
     * @param ROBOT Robot we're working with
     */
    Harvester(final Robot ROBOT)
    {
        _robot = ROBOT;
    }


    /**
     * Hardware maps the harvester
     */
    void mapHardware()
    {
        HardwareAux mapHelper = new HardwareAux(_robot);    // Helps map hardware

        _motor = mapHelper.mapMotor("harvest" , DcMotorSimple.Direction.FORWARD);
    }


    /**
     * Runs the harvester at default power
     *
     * @param direction The direction in which to run the harvester
     *
     * @return Returns true if successful, false if failed
     */
    boolean run(Direction direction)
    {
        final double _DEFAULT_POWER = 1.0;      // Default power of the harvester

        return run(direction , _DEFAULT_POWER);
    }


    /**
     * Runs the harvester at a given power value
     *
     * @param direction The direction in which to run the harvester
     * @param POWER The power at which to run the harvester
     *
     * @return Returns true if successful, false otherwise
     */
    boolean run(Direction direction , final double POWER)
    {
        boolean success = true;         // Tells if running of harvester is successful or not

        try
        {
            if(direction == Direction.FORWARD)
                _motor.setPower(POWER);

            else
                _motor.setPower(-POWER);
        }
        catch(Exception e)
        {
            Log.e("Error" , "Cannot power harvester, check your mapping");
            success = false;
        }

        return success;
    }


    /**
     * Powers the harvester for a certain duration of time.
     *
     * @param DIRECTION Direction at which to power the harvester
     * @param POWER Power at which to power the harvester
     * @param TIME Time at which to power the harvester for
     *
     * @return True if attempt to power harvester successful, false otherwise
     */
    boolean timedRun(final Direction DIRECTION , final double POWER , final double TIME)
    {
        boolean success = true;         // Tells whether attempt to power is successful or not

        long initialTime = System.currentTimeMillis();         // Initial starting time

        try
        {
            while(System.currentTimeMillis() < initialTime + TIME)
                success = success && run(DIRECTION , POWER);
        }
        catch(Exception e)
        {
            Log.e("Error" , "Cannot power harvester, check your mapping");
            success = false;
        }

        return success;
    }


    /**
     * Attempts to stop the harvester. Recommended to call when initializing or transitioning
     * between states.
     *
     * @return True if attempt succeeded, false otherwise
     */
    boolean stop()
    {
        boolean success = true;     // Tells is stopping of harvester is successful or not

        try
        {
            _motor.setPower(0);
        }
        catch(Exception e)
        {
            Log.e("Error" , "Cannot stop harvester, check your mapping");
            success = false;
        }

        return success;
    }
}