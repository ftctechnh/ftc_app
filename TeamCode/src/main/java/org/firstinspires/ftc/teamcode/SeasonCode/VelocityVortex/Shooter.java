package org.firstinspires.ftc.teamcode.SeasonCode.VelocityVortex;


import android.util.Log;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;


/**
 * <p>
 *      Here's our shooter. Things pertaining solely to the shooter go here.
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
final class Shooter
{
    private Robot _robot = null;            // Robot we're working with
    private DcMotor _motor = null;          // The flywheel

    private double speed = 0;               // Current speed of the flywheel. This measurement is
                                            // (# of encoder counts passed) / (unit of time)

    private long startEncoder = 0;                              // Starting encoder count

    private double motorPower = .75;                    // Holds the power to be set to the motor
    private double targetSpeed;                         // Target speed to run the flywheel at
    private final double CLOSE_ENOUGH_SPEED = 5;        // A "close enough" buffer for the targets

    private UtilPulsar adjustPulsar = new UtilPulsar();     // Used to adjust the frequency at which
                                                            // the motor speed adjusts

    private UtilPulsar speedPulsar = new UtilPulsar();      // Used to adjust the frequency at which
                                                            // the motor speed is calculated

    private State _state = State.OFF;                       // Current state of the shooter

    /**
     * Helps determine which direction to power the shooter
     */
    enum Direction
    {
        FORWARD ,
        BACKWARD
    }


    /**
     * Helps determine which power mode to shoot the ball
     */
    enum Power
    {
        HIGH ,
        LOW
    }


    /**
     * Helps determine at what state the shooter is, used for knowing when to shoot the ball
     */
    enum State
    {
        INITIALIZING("INITIALIZING") ,
        READY("READY") ,
        RECOVERING("RECOVERING") ,
        OFF("OFF");

        private String name;

        State(String s)
        {
            name = s;
        }


        /**
         * @return Returns the corresponding string value of the enumeration
         */
        String asString()
        {
            return this.name;
        }
    }


    /**
     * Creates a shooter
     *
     * @param ROBOT The robot we're workin with
     */
    Shooter(final Robot ROBOT)
    {
        _robot = ROBOT;
    }


    /**
     * Maps the shooter
     */
    void mapHardware()
    {
        HardwareAux mapHelper = new HardwareAux(_robot);    // Helps map hardware

        _motor = mapHelper.mapMotor("tennis" , DcMotorSimple.Direction.FORWARD);

        try
        {
            startEncoder = _motor.getCurrentPosition();
        }
        catch(Exception e)
        {
            Log.e("Error" , "Cannot get shooter motor position, check your mapping");
        }
    }


    /**
     * Powers the shooter based on power mode and direction passed in
     *
     * @param POWER Determines at which power to run the shooter
     * @param DIRECTION Determines at which direction to run the shooter
     *
     * @return True if the attempt to power the shooter is successful, false otherwise
     */
    boolean run(final Shooter.Power POWER , final Shooter.Direction DIRECTION)
    {
        final double HIGH_TARGET_SPEED = 185;    // Target speed for high power shot
        final double LOW_TARGET_SPEED = 175;     // Target speed for low power shot

        boolean success = true;                 // Tells if running shooter is successful or not

        try
        {
            if(POWER == Power.HIGH)
                targetSpeed = HIGH_TARGET_SPEED;

            else
                targetSpeed = LOW_TARGET_SPEED;

            if(DIRECTION == Direction.BACKWARD)
                targetSpeed *= -1;

            adjustPower(targetSpeed);

            _motor.setPower(motorPower);

            manageState();
        }
        catch(Exception e)
        {
            Log.e("Error" , "Cannot run shooter, check your mapping");
            success = false;
        }

        return success;
    }


    /**
     * Powers the shooter for a certain duration of time.
     *
     * @param DIRECTION Direction at which to power the shooter
     * @param POWER Power at which to power the shooter
     * @param TIME Time at which to power the shooter for
     *
     * @return True if attempt to power shooter successful, false otherwise
     */
    boolean timedRun(final Power POWER  , final Direction DIRECTION , final double TIME)
    {
        boolean success = true;         // Tells whether attempt to power is successful or not

        long initialTime = System.currentTimeMillis();         // Initial starting time

        try
        {
            while(System.currentTimeMillis() < initialTime + TIME)
                success = success && run(POWER , DIRECTION);
        }
        catch(Exception e)
        {
            Log.e("Error" , "Cannot power harvester, check your mapping");
            success = false;
        }

        return success;
    }


    /**
     * Gets the current speed of the motor measured as (# of encoder counts passed) / (unit of time)
     *
     * @return Returns the speed of the motor
     */
    double speed()
    {
        final int CHECK_TIME = 250;         // Interval of time to check at

        if(speedPulsar.pulse(CHECK_TIME))
        {
            try
            {
                speed = (double)(_motor.getCurrentPosition() - startEncoder);
                startEncoder = _motor.getCurrentPosition();
            }
            catch(Exception e)
            {
                Log.e("Error" , "Cannot get shooter motor position, check your mapping");
            }
        }

        return speed;
    }


    /**
     * Adjusts the shooter's power to match the motor speed with a specified target speed.
     *
     * @param TARGET The target speed at which to run the motor at
     */
    private void adjustPower(final double TARGET)
    {
        final int ADJUSTMENT_FREQUENCY = 1_000;     // How often to adjust the speed

        speed = speed();

        if((speed > (TARGET + CLOSE_ENOUGH_SPEED) || speed < (TARGET - CLOSE_ENOUGH_SPEED)) &&
                adjustPulsar.pulse(ADJUSTMENT_FREQUENCY))
        {
            motorPower *= TARGET / speed;

            if(motorPower > 1)
                motorPower = 1;

            else if(motorPower < -1)
                motorPower = -1;
        }
    }


    /**
     * Auxiliary method used for debugging.
     *
     * @return Attempts to return the motor power. In the event of failure, 2 is returned.
     */
    double motorPower()
    {
        final double ERROR_CODE = 2;               // Number to return if getting power fails

        try
        {
            return _motor.getPower();
        }
        catch(Exception e)
        {
            Log.e("Error" , "Cannot get motor power, check your mapping");
            return ERROR_CODE;
        }
    }


    /**
     * @return Returns the current state of the shooter.
     */
    State state()
    {
        return _state;
    }

    /**
     * Auxiliary method for managing shooter states
     */
    private void manageState()
    {
        /*
            If the motor isn't powered, then the shooter is OFF
            If the shooter was OFF and the motor power is not 0, then the shooter is INITIALIZING
            If the shooter is within target speed, then the shooter is READY
            If the shooter was READY and isn't within target speed, the shooter is RECOVERING
         */
        if(_state == State.OFF && _motor.getPower() != 0)
            _state = State.INITIALIZING;

        if(speed < targetSpeed + CLOSE_ENOUGH_SPEED && speed > targetSpeed - CLOSE_ENOUGH_SPEED)
            _state = State.READY;

        else if(_state == State.READY)
            _state = State.RECOVERING;

        if(_motor.getPower() == 0)
            _state = State.OFF;
    }


    /**
     * Attempts to stop the shooter
     *
     * @return True if attempt to stop the shooter succeeds, false otherwise.
     */
    boolean stop()
    {
        boolean success = true;         // Tells if attempt to stop the shooter is successful or not

        try
        {
            _motor.setPower(0);
            _state = State.OFF;
        }
        catch(Exception e)
        {
            Log.e("Error" , "Cannot stop shooter, check your mapping");
            success = false;
        }

        return success;
    }
}