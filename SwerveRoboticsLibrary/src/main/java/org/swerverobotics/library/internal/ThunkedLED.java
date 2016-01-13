package org.swerverobotics.library.internal;


import com.qualcomm.robotcore.hardware.*;

/**
 * Yet another in our series
 */
public class ThunkedLED extends LED implements IThunkWrapper<LED>
    {
    //----------------------------------------------------------------------------------------------
    // State
    //----------------------------------------------------------------------------------------------

    private LED target;   // can only talk to him on the loop thread

    @Override public LED getWrappedTarget() { return this.target; }

    //----------------------------------------------------------------------------------------------
    // Construction
    //----------------------------------------------------------------------------------------------

    protected ThunkedLED(LED target)
        {
        super(controllerOf(target), portOf(target));

        if (target == null) throw new NullPointerException("null " + this.getClass().getSimpleName() + " target");
        this.target = target;
        }

    static public ThunkedLED create(LED target)
        {
        return target instanceof ThunkedLED ? (ThunkedLED)target : new ThunkedLED(target);
        }

    private static DigitalChannelController controllerOf(LED target)
        {
        return Util.<DigitalChannelController>getPrivateObjectField(target, 0);
        }
    private static int portOf(LED target)
        {
        return Util.getPrivateIntField(target, 1);
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
    // LED
    //----------------------------------------------------------------------------------------------

    @Override  public void enable(final boolean enabled)
        {
        (new ThunkForWriting()
            {
            @Override protected void actionOnLoopThread()
                {
                target.enable(enabled);
                }
            }).doUntrackedWriteOperation();
        }
    }
