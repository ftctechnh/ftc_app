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

    public Telemetry target;

    //----------------------------------------------------------------------------------------------
    // Construction
    //----------------------------------------------------------------------------------------------

    private ThunkedTelemetry(Telemetry target)
        {
        if (target == null) throw new NullPointerException("null " + this.getClass().getSimpleName() + " target");
        this.target = target;
        }

    static public ThunkedTelemetry create(Telemetry target)
        {
        return new ThunkedTelemetry(target);
        }

    //----------------------------------------------------------------------------------------------
    // Operations
    //----------------------------------------------------------------------------------------------

    synchronized public long getTimestamp()
        {
        return (new ResultableThunk<Long>()
            {
            @Override protected void actionOnLoopThread()
                {
                this.result = target.getTimestamp();
                }
            }).doReadOperation();
        }

    synchronized public void setTag(final String tag)
        {
        (new NonwaitingThunk()
            {
            @Override protected void actionOnLoopThread()
                {
                target.setTag(tag);
                }
            }).doWriteOperation();
        }

    synchronized public String getTag()
        {
        return (new ResultableThunk<String>()
            {
            @Override protected void actionOnLoopThread()
                {
                this.result = target.getTag();
                }
            }).doReadOperation();
        }

    synchronized public void addData(final String key, final String msg)
        {
        (new NonwaitingThunk()
            {
            @Override protected void actionOnLoopThread()
                {
                target.addData(key, msg);
                }
            }).doWriteOperation();
        }

    synchronized public void addData(final String msg, final float value)
        {
        (new NonwaitingThunk()
            {
            @Override protected void actionOnLoopThread()
                {
                target.addData(msg, value);
                }
            }).doWriteOperation();
        }

    synchronized public void addData(final String msg, final double value)
        {
        (new NonwaitingThunk()
            {
            @Override protected void actionOnLoopThread()
                {
                target.addData(msg, value);
                }
            }).doWriteOperation();
        }

    synchronized public Map<String, String> getDataStrings()
        {
        return (new ResultableThunk<Map<String, String>>()
            {
            @Override protected void actionOnLoopThread()
                {
                this.result = target.getDataStrings();
                }
            }).doReadOperation();
        }

    synchronized public Map<String, Float> getDataNumbers()
        {
        return (new ResultableThunk<Map<String, Float>>()
            {
            @Override protected void actionOnLoopThread()
                {
                this.result = target.getDataNumbers();
                }
            }).doReadOperation();
        }

    synchronized public boolean hasData()
        {
        return (new ResultableThunk<Boolean>()
            {
            @Override protected void actionOnLoopThread()
                {
                this.result = target.hasData();
                }
            }).doReadOperation();
        }

    synchronized public void clearData()
        {
        (new NonwaitingThunk()
            {
            @Override protected void actionOnLoopThread()
                {
                target.clearData();
                }
            }).doWriteOperation();
        }
    }
