package org.firstinspires.ftc.teamcode.SeasonCode.RelicRecovery.Components.GlyphGrabber

import org.firstinspires.ftc.robotcontroller.internal.Core.RobotCommand


class ActivateForTime(private var _glyphGrabber: GlyphGrabber): RobotCommand()
{
    private var _activationTime = 0L
    private var _state = GlyphGrabber.State.STOP

    private var _interrupt = false
    private var _busy = false


    fun setParams(time: Long , state: GlyphGrabber.State)
    {
        _activationTime = time
        _state = state

    }


    override fun runSequentially()
    {
        val startTime = System.currentTimeMillis()

        _glyphGrabber.setState(_state)
        _busy = true

        while(!_interrupt)
        {
            if(System.currentTimeMillis() - startTime >= _activationTime)
            {
                break
            }
        }

        _glyphGrabber.setState(GlyphGrabber.State.STOP)
        _busy = false
    }


    override fun runParallel()
    {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }


    override fun isBusy(): Boolean
        = _busy


    override fun stop()
    {
        _interrupt = true
    }

}