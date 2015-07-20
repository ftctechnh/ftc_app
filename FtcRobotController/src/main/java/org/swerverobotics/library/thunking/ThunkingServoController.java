package org.swerverobotics.library.thunking;

import com.qualcomm.robotcore.hardware.*;

/**
 * An implementation of ServoController that talks to a non-thunking target implementation
 * by thunking all calls over to the loop thread and back gain.
 */
public class ThunkingServoController implements ServoController
    {
    //----------------------------------------------------------------------------------------------
    // State
    //----------------------------------------------------------------------------------------------

    ServoController target;   // can only talk to him on the loop thread

    //----------------------------------------------------------------------------------------------
    // Construction
    //----------------------------------------------------------------------------------------------

    private ThunkingServoController(ServoController target)
        {
        this.target = target;
        }

    static public ThunkingServoController Create(ServoController target)
        {
        return target instanceof ThunkingServoController ? (ThunkingServoController)target : new ThunkingServoController(target);
        }

    //----------------------------------------------------------------------------------------------
    // ServoController interface
    //----------------------------------------------------------------------------------------------

    @Override public String getDeviceName()
        {
        class Thunk extends ResultableThunk<String>
            {
            @Override public void actionOnLoopThread()
                {
                this.result = target.getDeviceName();
                }
            }
        Thunk thunk = new Thunk();
        thunk.dispatch();
        return thunk.result;
        }

    @Override public int getVersion()
        {
        class Thunk extends ResultableThunk<Integer>
            {
            @Override public void actionOnLoopThread()
                {
                this.result = target.getVersion();
                }
            }
        Thunk thunk = new Thunk();
        thunk.dispatch();
        return thunk.result;
        }

    @Override public void close()
        {
        class Thunk extends NonwaitingThunk
            {
            @Override public void actionOnLoopThread()
                {
                target.close();
                }
            }
        Thunk thunk = new Thunk();
        thunk.dispatch();
        }

    @Override public void pwmEnable()
        {
        class Thunk extends NonwaitingThunk
            {
            @Override public void actionOnLoopThread()
                {
                target.pwmEnable();
                }
            }
        Thunk thunk = new Thunk();
        thunk.dispatch();
        }

    @Override public void pwmDisable()
        {
        class Thunk extends NonwaitingThunk
            {
            @Override public void actionOnLoopThread()
                {
                target.pwmDisable();
                }
            }
        Thunk thunk = new Thunk();
        thunk.dispatch();
        }

    @Override public ServoController.PwmStatus getPwmStatus()
        {
        class Thunk extends ResultableThunk<PwmStatus>
            {
            @Override public void actionOnLoopThread()
                {
                this.result = target.getPwmStatus();
                }
            }
        Thunk thunk = new Thunk();
        thunk.dispatch();
        return thunk.result;
        }

    @Override public void setServoPosition(final int channel, final double position)
        {
        class Thunk extends NonwaitingThunk
            {
            @Override public void actionOnLoopThread()
                {
                target.setServoPosition(channel, position);
                }
            }
        Thunk thunk = new Thunk();
        thunk.dispatch();
        }

    @Override public double getServoPosition(final int channel)
        {
        class Thunk extends ResultableThunk<Double>
            {
            @Override public void actionOnLoopThread()
                {
                this.result = target.getServoPosition(channel);
                }
            }
        Thunk thunk = new Thunk();
        thunk.dispatch();
        return thunk.result;
        }
    }