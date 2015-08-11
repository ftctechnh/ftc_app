package org.swerverobotics.library.internal;

import com.qualcomm.robotcore.hardware.*;
import org.swerverobotics.library.interfaces.*;

/**
 * A GyroSensor that can be called on a synchronous thread.
 */
public class ThunkedGyroSensor extends GyroSensor implements IThunkingWrapper<GyroSensor>
    {
    //----------------------------------------------------------------------------------------------
    // State
    //----------------------------------------------------------------------------------------------

    private GyroSensor target;   // can only talk to him on the loop thread

    @Override public GyroSensor getThunkTarget() { return this.target; }
    
    //----------------------------------------------------------------------------------------------
    // Construction
    //----------------------------------------------------------------------------------------------

    private ThunkedGyroSensor(GyroSensor target)
        {
        if (target == null) throw new NullPointerException("null " + this.getClass().getSimpleName() + " target");
        this.target = target;
        }

    static public ThunkedGyroSensor create(GyroSensor target)
        {
        return target instanceof ThunkedGyroSensor ? (ThunkedGyroSensor)target : new ThunkedGyroSensor(target);
        }

    //----------------------------------------------------------------------------------------------
    // HardwareDevice
    //----------------------------------------------------------------------------------------------

    @Override public void close()
        {
        (new ThunkForWriting()
            {
            @Override protected void actionOnLoopThread()
                {
                target.close();
                }
            }).doUntrackedWriteOperation();
        }

    @Override public int getVersion()
        {
        return (new ThunkForReading<Integer>()
            {
            @Override protected void actionOnLoopThread()
                {
                this.result = target.getVersion();
                }
            }).doUntrackedReadOperation();
        }

    @Override public String getConnectionInfo()
        {
        return (new ThunkForReading<String>()
            {
            @Override protected void actionOnLoopThread()
                {
                this.result = target.getConnectionInfo();
                }
            }).doUntrackedReadOperation();
        }

    @Override public String getDeviceName()
        {
        return (new ThunkForReading<String>()
            {
            @Override protected void actionOnLoopThread()
                {
                this.result = target.getDeviceName();
                }
            }).doUntrackedReadOperation();
        }
    
    //----------------------------------------------------------------------------------------------
    // GyroSensor
    //----------------------------------------------------------------------------------------------

    @Override public double getRotation()
        {
        return (new ThunkForReading<Double>()
            {
            @Override protected void actionOnLoopThread()
                {
                this.result = target.getRotation();
                }
            }).doReadOperation();
        }

    @Override public String status()
        {
        return (new ThunkForReading<String>()
            {
            @Override protected void actionOnLoopThread()
                {
                this.result = target.status();
                }
            }).doReadOperation();
        }
    }
