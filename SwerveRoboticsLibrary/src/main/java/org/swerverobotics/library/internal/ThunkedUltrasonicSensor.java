package org.swerverobotics.library.internal;

import com.qualcomm.robotcore.hardware.*;
import org.swerverobotics.library.interfaces.*;

/**
 * An UltrasonicSensor that can be called on the main() thread.
 */
public class ThunkedUltrasonicSensor implements UltrasonicSensor, IThunkWrapper<UltrasonicSensor>
    {
    //----------------------------------------------------------------------------------------------
    // State
    //----------------------------------------------------------------------------------------------

    private UltrasonicSensor target;   // can only talk to him on the loop thread

    @Override public UltrasonicSensor getWrappedTarget() { return this.target; }

    //----------------------------------------------------------------------------------------------
    // Construction
    //----------------------------------------------------------------------------------------------

    private ThunkedUltrasonicSensor(UltrasonicSensor target)
        {
        if (target == null) throw new NullPointerException("null " + this.getClass().getSimpleName() + " target");
        this.target = target;
        }

    static public ThunkedUltrasonicSensor create(UltrasonicSensor target)
        {
        return target instanceof ThunkedUltrasonicSensor ? (ThunkedUltrasonicSensor)target : new ThunkedUltrasonicSensor(target);
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
    // UltrasonicSensor
    //----------------------------------------------------------------------------------------------

    @Override public double getUltrasonicLevel()
        {
        return (new ThunkForReading<Double>()
            {
            @Override protected void actionOnLoopThread()
                {
                this.result = target.getUltrasonicLevel();
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
