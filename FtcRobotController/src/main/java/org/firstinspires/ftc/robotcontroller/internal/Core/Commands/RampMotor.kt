@file:Suppress("PackageDirectoryMismatch")
package org.directcurrent.core.commands


import com.qualcomm.robotcore.hardware.DcMotor
import org.firstinspires.ftc.robotcontroller.internal.Core.RobotCommand
import org.firstinspires.ftc.robotcontroller.internal.Core.Utility.UtilClock


/**
 * Class for ramping a motor
 *
 * For example, if a motor needs to accelerate to a certain to a certain power over a certain
 * period of time, use this class
 */
@Suppress("unused")
class RampMotor(private var _motor: DcMotor): RobotCommand()
{
    private val tickrate = 10

    private var _startPower = 0.0
    private var _endPower = 1.0
    private var _timeSpan = 3_000L

    private var _interrupt = false
    private var _busy = false

    private var _t: Thread? = null


    /**
     * Sets motor ramping parameters
     *
     * Call this before attempting any motor ramping, otherwise ramping will be performed
     * with previous parameters passed in. In the case that no previous parameters have been
     * passed in, the motor will ramp from 0 to 1 over 3 seconds
     *
     * Be sure to pass in time in milliseconds
     */
    fun setRampParams(START: Double , END: Double , TIME: Long)
    {
        _startPower = START
        _endPower = END
        _timeSpan = TIME
    }


    /**
     * Ramps the motor on the main thread
     */
    override fun runSequentially()
    {
        val clock = UtilClock()
        val startTime = System.currentTimeMillis()

        var motorPower = _startPower

        _busy = true
        while (System.currentTimeMillis() - startTime < _timeSpan && !_interrupt)
        {
            if (clock.tick(tickrate))
            {
                motorPower += (_endPower - _startPower) * tickrate / _timeSpan
                _motor.power = motorPower
            }
        }

        /*
            Only do this if ramping has not been interrupted.

            The final call to adjust motor power in the loop may have resulted in
            a final power that very slightly does not match the specified result-
            this takes care of that.
        */
        if (!_interrupt)
        {
            _motor.power = _endPower
        }
        _busy = false
    }


    /**
     * Ramps the motor on a separate thread
     */
    override fun runParallel()
    {
        if(_t == null)
        {
            _t = Thread(Runnable
            {
                runSequentially()
            })

            _t!!.start()
        }
    }


    /**
     * Returns the power of the motor
     */
    fun motorPower(): Double
    {
        return _motor.power
    }


    /**
     * Returns the encoder count of the motor
     */
    fun motorPos(): Int
    {
        return _motor.currentPosition
    }


    /**
     * Returns whether the command is currently running or not
     */
    override fun isBusy(): Boolean
    {
        return _busy
    }


    /**
     * Stops the ramp process
     */
    override fun stop()
    {
        _interrupt = true
    }

}