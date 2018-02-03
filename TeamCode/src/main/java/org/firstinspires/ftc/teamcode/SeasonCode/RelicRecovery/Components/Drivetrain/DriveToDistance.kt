@file:Suppress("PackageDirectoryMismatch")
package org.directcurrent.season.relicrecovery.drivetrain


import com.qualcomm.robotcore.hardware.DcMotor
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
        // This is so that the joystick at rest doesn't overwrite it and stop it instantly
        // Of course, the idea is that it can be overwritten
        _drivetrain.freezeInput()


        if(_drivetrain.encoderMode() != DcMotor.RunMode.RUN_TO_POSITION)
        {
            _drivetrain.encoderToPos()
        }


        // Adding because we're not resetting the encoders- for some reason resetting tends to break things
        _drivetrain.leftMotor().targetPosition = (_distance * _COUNTS_PER_INCH).toInt() + _drivetrain.leftMotor().currentPosition
        _drivetrain.rightMotor().targetPosition = (_distance * _COUNTS_PER_INCH).toInt() + _drivetrain.rightMotor().currentPosition


        _busy = true


        while(!_interrupt && _drivetrain.leftMotor().isBusy && _drivetrain.rightMotor().isBusy)
        {
            // Nothing HA I NEED SLEEP
        }

        _drivetrain.leftMotor().power = 0.0
        _drivetrain.rightMotor().power = 0.0


        _drivetrain.encoderOn()


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