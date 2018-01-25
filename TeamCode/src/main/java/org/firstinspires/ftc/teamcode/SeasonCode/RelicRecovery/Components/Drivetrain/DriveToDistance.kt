@file:Suppress("PackageDirectoryMismatch")
package org.directcurrent.season.relicrecovery.drivetrain


import org.firstinspires.ftc.robotcontroller.internal.Core.RobotCommand
import org.firstinspires.ftc.teamcode.SeasonCode.RelicRecovery.Components.Drivetrain.Drivetrain


/**
 * Command to drive the robot to a certain distance
 */
class DriveToDistance(private var _drivetrain: Drivetrain): RobotCommand()
{
    private val _COUNTS_PER_INCH = 64.3304

    private var _interrupt = false
    private var _busy = false

    private var _distance = 0.0
    private var _speed = 1.0
    private var _timeout = 10_000L

    private var _CLOSE_ENOUGH = 10


    /**
     * Sets the destination to drive to. Call this before running the command.
     *
     * You have to.
     *
     * Yes, I know you don't want to.
     */
    @Deprecated("Use setParams()")
    fun setDestination(distance: Double)
    {
        _distance = distance
    }


    fun setParams(distance: Double , speed: Double , timeout: Long)
    {
        _distance = distance
        _speed = speed
        _timeout = timeout
    }


    /**
     * Drives to the defined distance on the main thread
     */
    override fun runSequentially()
    {
        // Freeze input to the drivetrain so that only the command can control it
        // Of course, the idea is that it can be overwritten
        _drivetrain.freezeInput()

        _drivetrain.encoderStopReset()
        _drivetrain.encoderToPos()

        _drivetrain.leftMotor().targetPosition = (_distance * _COUNTS_PER_INCH).toInt()
        _drivetrain.rightMotor().targetPosition = (_distance * _COUNTS_PER_INCH).toInt()

        var eta = Math.abs((_drivetrain.leftEncoderCount() + _drivetrain.rightEncoderCount()) / 2 -
                (_drivetrain.leftEncoderTarget() + _drivetrain.rightEncoderTarget()) / 2)

        _busy = true
        while(eta > _CLOSE_ENOUGH && !_interrupt)
        {
            if(_distance > 0)
            {
                _drivetrain.leftMotor().power = _speed
                _drivetrain.rightMotor().power = _speed
            }
            else
            {
                _drivetrain.leftMotor().power = -_speed
                _drivetrain.rightMotor().power = -_speed
            }

            _drivetrain.base().telMet().tagWrite("Left Target" , _drivetrain.leftEncoderTarget())
            _drivetrain.base().telMet().tagWrite("Right Target" , _drivetrain.rightEncoderTarget())
            _drivetrain.base().telMet().tagWrite("Left Pos" , _drivetrain.leftEncoderCount())
            _drivetrain.base().telMet().tagWrite("Right Pos" , _drivetrain.rightEncoderCount())
            _drivetrain.base().telMet().tagWrite("Avg Distance" , eta)
            _drivetrain.base().telMet().update()

            // We've missed our stop
            if(Math.abs((_drivetrain.leftEncoderCount() + _drivetrain.rightEncoderCount()) / 2 -
                    (_drivetrain.leftEncoderTarget() + _drivetrain.rightEncoderTarget()) / 2) > eta)
            {
                break
            }

            eta = Math.abs((_drivetrain.leftEncoderCount() + _drivetrain.rightEncoderCount()) / 2 -
                    (_drivetrain.leftEncoderTarget() + _drivetrain.rightEncoderTarget()) / 2)

//            if(Math.abs(_drivetrain.leftMotor().power <= .2) || _drivetrain.rightMotor().power <= .2)
//            {
//                break
//            }
        }

        _drivetrain.leftMotor().power = 0.0
        _drivetrain.rightMotor().power = 0.0

        _busy = false
        _drivetrain.allowInput()
    }


    /**
     * Drives to the defined distance on a separate thread
     */
    override fun runParallel()
    {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }


    /**
     * Returns whether or not the command is currently running
     */
    override fun isBusy(): Boolean
    {
        return _busy
    }


    /**
     * Stops execution of the command. Note that this probably won't work if the command is executed
     * on the main thread- in such a case, a call has to originate from within the class
     */
    override fun stop()
    {
        _interrupt = true
    }
}