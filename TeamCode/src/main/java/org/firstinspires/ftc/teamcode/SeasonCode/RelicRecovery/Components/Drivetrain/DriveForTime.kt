package org.firstinspires.ftc.teamcode.SeasonCode.RelicRecovery.Components.Drivetrain

import org.firstinspires.ftc.robotcontroller.internal.Core.RobotCommand


class DriveForTime(private var _drivetrain: Drivetrain): RobotCommand()
{
    private var _interrupt = false
    private var _busy = false

    private var _travelTime = 0L
    private var _robotSpeed = 0.0


    /**
     * Sets the time and speed to drive for. Call this before running the command.
     *
     * You have to.
     *
     * Yes, I know you don't want to.
     */
    fun setParams(time: Long , speed: Double)
    {
        _travelTime = time
        _robotSpeed = speed
    }


    /**
     * Drives to the defined distance on the main thread
     */
    override fun runSequentially()
    {
        _drivetrain.encoderStopReset()
        _drivetrain.encoderOn()
        // Freeze input to the drivetrain so that only the command can control it
        // Of course, the idea is that it can be overwritten
        _drivetrain.freezeInput()

        val startTime = System.currentTimeMillis()

        _drivetrain.leftMotor().power = _robotSpeed
        _drivetrain.rightMotor().power = _robotSpeed

        while(System.currentTimeMillis() - startTime < _travelTime && !_interrupt)
        {
            // Nothing
        }

        _drivetrain.leftMotor().power = 0.0
        _drivetrain.rightMotor().power = 0.0

        _drivetrain.allowInput()
        _drivetrain.encoderStopReset()
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