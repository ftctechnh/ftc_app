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


    /**
     * Sets the destination to drive to. Call this before running the command.
     *
     * You have to.
     *
     * Yes, I know you don't want to.
     */
    fun setDestination(distance: Double)
    {
        _drivetrain.leftMotor().targetPosition = (distance / _COUNTS_PER_INCH).toInt()
    }


    /**
     * Drives to the defined distance on the main thread
     */
    override fun runSequentially()
    {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }


    /**
     * Drives to the defined distance on a seperate thread
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