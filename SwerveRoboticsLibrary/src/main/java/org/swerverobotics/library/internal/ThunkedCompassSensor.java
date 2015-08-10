package org.swerverobotics.library.internal;

import com.qualcomm.robotcore.hardware.*;
import org.swerverobotics.library.*;
import junit.framework.Assert;

/**
 * A CompassSensor that can be called on the main() thread.
 */
public class ThunkedCompassSensor extends CompassSensor implements IThunkedReadWriteListener
    {
    //----------------------------------------------------------------------------------------------
    // State
    //----------------------------------------------------------------------------------------------

    public CompassSensor target;   // can only talk to him on the loop thread

    public LegacyModule  legacyModule = null;
    public int           port;

    private int          readThunkKey  = Thunk.getNewActionKey();
    private int          writeThunkKey = Thunk.getNewActionKey();
    
    private boolean getIsOffLine()
        {
        return Util.getPrivateBooleanField(this, 7);
        }

    //----------------------------------------------------------------------------------------------
    // Construction
    //----------------------------------------------------------------------------------------------

    private ThunkedCompassSensor(CompassSensor target)
        {
        if (target == null) throw new NullPointerException("null " + this.getClass().getSimpleName() + " target");
        this.target = target;

        if (this.isTargetLegacy())
            {
            // Make sure our hack is at least plausible
            Assert.assertEquals(true, Util.<Object>getPrivateObjectField(target, 6) instanceof CompassMode);

            // Hack: did out the gunk we need to implement our mode-switching-waiting
            this.legacyModule = (LegacyModule)Util.<LegacyModule>getPrivateObjectField(target, 0);
            this.port         = Util.getPrivateIntField(target, 5);
            }
        }

    static public ThunkedCompassSensor create(CompassSensor target)
        {
        return target instanceof ThunkedCompassSensor ? (ThunkedCompassSensor)target : new ThunkedCompassSensor(target);
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
    // IThunkedReadWriteListener
    //----------------------------------------------------------------------------------------------

    private boolean isTargetLegacy()
        // Are we hooked to a legacy sensor, and so need to do the read-or-write-not-both dance? 
        {
        return this.target instanceof LegacyModule.PortReadyCallback;
        }
    private boolean isOffline()
        {
        // Can't ask the legacyModule, as the internal 'isOffline' boolean
        // is set AFTER returning to read mode.
        //
        // return !this.legacyModule.isI2cPortInReadMode(this.port);
        return this.getIsOffLine();
        }

    @Override public void enterReadOperation() throws InterruptedException
        {
        if (this.isTargetLegacy())
            {
            // We're about to read. Avoid any writes in this cycle on this object
            SynchronousOpMode.synchronousThreadWaitForLoopCycleEmptyOfActionKey(this.writeThunkKey);

            // Wait until any previous writes have in fact cleared through to the HW
            while (this.isOffline())
                {
                SynchronousOpMode.synchronousThreadIdle();
                }
            }
        }
    @Override public void enterWriteOperation() throws InterruptedException
        {
        if (this.isTargetLegacy())
            {
            SynchronousOpMode.synchronousThreadWaitForLoopCycleEmptyOfActionKey(this.readThunkKey);
            }
        }
    @Override public int getListenerReadThunkKey()
        {
        return this.readThunkKey;
        }
    @Override public int getListenerWriteThunkKey()
        {
        return this.writeThunkKey;
        }


    //----------------------------------------------------------------------------------------------
    // CompassSensor
    //----------------------------------------------------------------------------------------------

    @Override public double getDirection()
        {
        return (new ThunkForReading<Double>()
            {
            @Override protected void actionOnLoopThread()
                {
                this.result = target.getDirection();
                }
            }).doReadOperation(this);
        }

    @Override public String status()
        {
        return (new ThunkForReading<String>()
            {
            @Override protected void actionOnLoopThread()
                {
                this.result = target.status();
                }
            }).doReadOperation(this);
        }

    @Override public void setMode(final CompassSensor.CompassMode mode)
        {
        (new ThunkForWriting()
            {
            @Override protected void actionOnLoopThread()
                {
                target.setMode(mode);
                }
            }).doWriteOperation(this);
        }

    @Override public boolean calibrationFailed()
        {
        return (new ThunkForReading<Boolean>()
            {
            @Override protected void actionOnLoopThread()
                {
                this.result = target.calibrationFailed();
                }
            }).doReadOperation(this);
        }
    }
