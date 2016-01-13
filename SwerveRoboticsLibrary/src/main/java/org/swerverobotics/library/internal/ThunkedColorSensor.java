package org.swerverobotics.library.internal;

import com.qualcomm.robotcore.hardware.ColorSensor;

/**
 * Another in our series...
 */
public class ThunkedColorSensor extends ColorSensor implements IThunkWrapper<ColorSensor>
    {
    //----------------------------------------------------------------------------------------------
    // State
    //----------------------------------------------------------------------------------------------

    private ColorSensor target;   // can only talk to him on the loop thread

    @Override public ColorSensor getWrappedTarget() { return this.target; }

    //----------------------------------------------------------------------------------------------
    // Construction
    //----------------------------------------------------------------------------------------------

    protected ThunkedColorSensor(ColorSensor target)
        {
        if (target == null) throw new NullPointerException("null " + this.getClass().getSimpleName() + " target");
        this.target = target;
        }

    static public ThunkedColorSensor create(ColorSensor target)
        {
        return target instanceof ThunkedColorSensor ? (ThunkedColorSensor)target : new ThunkedColorSensor(target);
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
    // ColorSensor
    //----------------------------------------------------------------------------------------------

    @Override public int red()
        {
        return (new ThunkForReading<Integer>()
            {
            @Override protected void actionOnLoopThread()
                {
                this.result = target.red();
                }
            }).doUntrackedReadOperation();
        }

    @Override public int green()
        {
        return (new ThunkForReading<Integer>()
            {
            @Override protected void actionOnLoopThread()
                {
                this.result = target.green();
                }
            }).doUntrackedReadOperation();
        }

    @Override public int blue()
        {
        return (new ThunkForReading<Integer>()
            {
            @Override protected void actionOnLoopThread()
                {
                this.result = target.blue();
                }
            }).doUntrackedReadOperation();
        }

    @Override public int alpha()
        {
        return (new ThunkForReading<Integer>()
            {
            @Override protected void actionOnLoopThread()
                {
                this.result = target.alpha();
                }
            }).doUntrackedReadOperation();
        }

    @Override public int argb()
        {
        return (new ThunkForReading<Integer>()
            {
            @Override protected void actionOnLoopThread()
                {
                this.result = target.argb();
                }
            }).doUntrackedReadOperation();
        }

    @Override public void enableLed(final boolean enabled)
        {
        (new ThunkForWriting()
            {
            @Override protected void actionOnLoopThread()
                {
                target.enableLed(enabled);
                }
            }).doUntrackedWriteOperation();
        }

    @Override public void setI2cAddress(final int i2cAddr8Bit)
        {
        (new ThunkForWriting()
            {
            @Override protected void actionOnLoopThread()
                {
                target.setI2cAddress(i2cAddr8Bit);
                }
            }).doUntrackedWriteOperation();
        }

    @Override public int getI2cAddress()
        {
        return (new ThunkForReading<Integer>()
            {
            @Override protected void actionOnLoopThread()
                {
                this.result = target.getI2cAddress();
                }
            }).doUntrackedReadOperation();
        }

    }
