package org.firstinspires.ftc.teamcode.SeasonCode.RelicRecovery.Components.GlyphGrabber

import org.firstinspires.ftc.robotcontroller.internal.Core.RobotCommand


class ActivateForTime(private var _glyphGrabber: GlyphGrabber): RobotCommand()
{
    private var _activationTime = 0L
    private var _state = GlyphGrabber.State.STOP

    private var _interrupt = false
    private var _busy = false

    private var _t: Thread? = null


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

        while(!_interrupt &&
                _glyphGrabber.base().opMode().opModeIsActive())
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
        if(_t == null)
        {
            _t = Thread(Runnable
            {
                runSequentially()
            })

            _t!!.start()
        }
    }


    override fun isBusy(): Boolean
        = _busy


    override fun stop()
    {
        _interrupt = true
    }

}