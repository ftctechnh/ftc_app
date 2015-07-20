package org.swerverobotics.library.thunking;

import java.util.*;

import com.qualcomm.robotcore.robocol.Telemetry;

/**
 * A wrapper for Telemetry that can be called from a synchronous thread.
 */
public class ThunkedTelemetry
// Note: we may not actually need to thunk for telemetry: all its entry points are synchronized,
// so it's certainly threadsafe. But can the resources it calls upon to send data be used from
// a non-loop() thread? We thunk for now, just to be sure.
//
// It's also a nice way to test the thunking infrastructure. :-)
    {
    //----------------------------------------------------------------------------------------------
    // State
    //----------------------------------------------------------------------------------------------

    private Telemetry target;

    //----------------------------------------------------------------------------------------------
    // Construction
    //----------------------------------------------------------------------------------------------

    public ThunkedTelemetry(Telemetry target)
        {
        this.target = target;
        }

    //----------------------------------------------------------------------------------------------
    // Operations
    //----------------------------------------------------------------------------------------------

    public long getTimestamp()
        {
        class Thunk extends ResultableThunk<Long>
            {
            @Override public void actionOnLoopThread()
                {
                this.result = target.getTimestamp();
                }
            }
        Thunk thunk = new Thunk();
        thunk.dispatch();
        return thunk.result;
        }

    public void setTag(final String tag)
        {
        class Thunk extends NonwaitingThunk
            {
            @Override public void actionOnLoopThread()
                {
                target.setTag(tag);
                }
            }
        Thunk thunk = new Thunk();
        thunk.dispatch();
        }

    public String getTag()
        {
        class Thunk extends ResultableThunk<String>
            {
            @Override public void actionOnLoopThread()
                {
                this.result = target.getTag();
                }
            }
        Thunk thunk = new Thunk();
        thunk.dispatch();
        return thunk.result;
        }

    public void addData(final String key, final String msg)
        {
        class Thunk extends NonwaitingThunk
            {
            @Override public void actionOnLoopThread()
                {
                target.addData(key, msg);
                }
            }
        Thunk thunk = new Thunk();
        thunk.dispatch();
        }

    public void addData(final String key, final float msg)
        {
        class Thunk extends NonwaitingThunk
            {
            @Override public void actionOnLoopThread()
                {
                target.addData(key, msg);
                }
            }
        Thunk thunk = new Thunk();
        thunk.dispatch();
        }

    public void addData(final String key, final double msg)
        {
        class Thunk extends NonwaitingThunk
            {
            @Override public void actionOnLoopThread()
                {
                target.addData(key, msg);
                }
            }
        Thunk thunk = new Thunk();
        thunk.dispatch();
        }

    public Map<String, String> getDataStrings()
        {
        class Thunk extends ResultableThunk<Map<String, String>>
            {
            @Override public void actionOnLoopThread()
                {
                this.result = target.getDataStrings();
                }
            }
        Thunk thunk = new Thunk();
        thunk.dispatch();
        return thunk.result;
        }

    public Map<String, Float> getDataNumbers()
        {
        class Thunk extends ResultableThunk<Map<String, Float>>
            {
            @Override public void actionOnLoopThread()
                {
                this.result = target.getDataNumbers();
                }
            }
        Thunk thunk = new Thunk();
        thunk.dispatch();
        return thunk.result;
        }

    public boolean hasData()
        {
        class Thunk extends ResultableThunk<Boolean>
            {
            @Override public void actionOnLoopThread()
                {
                this.result = target.hasData();
                }
            }
        Thunk thunk = new Thunk();
        thunk.dispatch();
        return thunk.result;
        }

    public void clearData()
        {
        class Thunk extends NonwaitingThunk
            {
            @Override public void actionOnLoopThread()
                {
                target.clearData();
                }
            }
        Thunk thunk = new Thunk();
        thunk.dispatch();
        }

    }
