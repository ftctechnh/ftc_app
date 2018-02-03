@file:Suppress("PackageDirectoryMismatch")
package org.directcurrent.season.relicrecovery.drivetrain


import com.qualcomm.robotcore.hardware.DcMotor
import org.firstinspires.ftc.robotcontroller.internal.Core.RobotCommand
import org.firstinspires.ftc.robotcontroller.internal.Core.Sensors.REVIMU
import org.firstinspires.ftc.robotcontroller.internal.Core.Utility.Util
import org.firstinspires.ftc.teamcode.SeasonCode.RelicRecovery.Components.Drivetrain.Drivetrain


/**
 * Command that turns the drivetrain to a certain angle
 *
 * Declare a new instance in the Drivetrain init() method
 */
@Suppress("LocalVariableName", "unused")
class TurnTo(private val _drivetrain: Drivetrain , private val _imu: REVIMU): RobotCommand()
{
    private var _interrupted = false
    private var _busy = false

    private var _targetAngle = 0.0
    private var _maxSpeed = 0.0
    private var _timeout = 10_000L

    private var _t: Thread? = null


    /**
     * Sets the parameters of drivetrain turning. Call this before running the command!
     *
     * If you don't the computer will explode (may fail repeatedly)
     */
    fun setParams(targetAngle: Double , maxSpeed: Double)
    {
        setParams(targetAngle , maxSpeed , 10_000L)
    }


    /**
     * Sets the parameters of drivetrain turning. Call this before running the command!
     */
    fun setParams(targetAngle: Double , maxSpeed: Double , timeout: Long)
    {
        _targetAngle = targetAngle
        _maxSpeed = maxSpeed
        _timeout = timeout
    }


    /**
     * Turns the drivetrain on the main thread
     */
    override fun runSequentially()
    {
        if(_drivetrain.encoderMode() != DcMotor.RunMode.RUN_USING_ENCODER)
        {
            _drivetrain.encoderOn()
        }

        _imu.pull()

        val TOLERANCE = 5                                   // "Close enough" amount
        val MIN_SPEED = .30                                 // Minimum speed to rotate at
        val SPEED_MULTIPLIER = 3.0                          // Constant to adjust speed

        val initHeading = _imu.zAngle()
        var speed: Double                                               // Speed of rotation
        var error = Util.angleError(initHeading.toInt(), _targetAngle.toInt()).toDouble()
        val initError = error                                   // Initial error
        val distanceModifier = Math.abs(error) / 180            // Distance modifier, turning
        // should be slower when there's
        // less distance to cover

        val startTime = System.currentTimeMillis()


        _busy = true

        _drivetrain.setState(Drivetrain.State.FORWARD_FAST)

        while (Math.abs(error) > TOLERANCE && System.currentTimeMillis() - startTime < _timeout
                && !_interrupted && _drivetrain.base().opMode().opModeIsActive())
        {
            _imu.pull()

            error = Util.angleError(_imu.zAngle().toInt(), _targetAngle.toInt()).toDouble()

            speed = error / initError * SPEED_MULTIPLIER * distanceModifier

            if (speed < MIN_SPEED)
                speed = MIN_SPEED

            if (speed > _maxSpeed)
            {
                speed = _maxSpeed
            }


            if(speed < MIN_SPEED)
            {
                speed = MIN_SPEED
            }

            _drivetrain.base().telMet().tagWrite("Current Heading" , _imu.zAngle())
            _drivetrain.base().telMet().tagWrite("Current Error" , error)
            _drivetrain.base().telMet().update()


            _drivetrain.run(0.0 , Math.abs(error) / -error * speed , false)
        }

        _drivetrain.leftMotor().power = 0.0
        _drivetrain.rightMotor().power = 0.0

        _busy = false
    }


    /**
     * Turns the drivetrain on a separate thread
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
     * Returns whether the command is busy or not
     */
    override fun isBusy(): Boolean = _busy


    /**
     * Stops turning function
     */
    override fun stop()
    {
        _busy = true
    }

}