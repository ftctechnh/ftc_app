package org.swerverobotics.library.internal;

import junit.framework.Assert;
import com.qualcomm.robotcore.hardware.*;
import org.swerverobotics.library.*;
import org.swerverobotics.library.interfaces.*;

/**
 * An IrSeekerSensor that can be called on a synchronous thread
 */
public class ThunkedIrSeekerSensor extends IrSeekerSensor implements IThunkedReadWriteListener, IThunkWrapper<IrSeekerSensor>
    {
    //----------------------------------------------------------------------------------------------
    // State
    //----------------------------------------------------------------------------------------------

    private IrSeekerSensor target;   // can only talk to him on the loop thread

    @Override public IrSeekerSensor getWrappedTarget() { return this.target; }

    private LegacyModule legacyModule = null;
    private int          port;
    
    private int readThunkKey  = Thunk.getNewActionKey();
    private int writeThunkKey = Thunk.getNewActionKey();

    private boolean getIsOffLine()
        {
        return Util.getPrivateBooleanField(this, 7);
        }

    //----------------------------------------------------------------------------------------------
    // Construction
    //----------------------------------------------------------------------------------------------

    private ThunkedIrSeekerSensor(IrSeekerSensor target)
        {
        if (target == null) throw new NullPointerException("null " + this.getClass().getSimpleName() + " target");
        this.target = target;
        //
        if (this.isTargetLegacy())
            {
            // Make sure our hack is at least plausible
            Assert.assertEquals(true, Util.<Object>getPrivateObjectField(target, 6) instanceof Mode);
        
            // Hack: did out the gunk we need to implement our mode-switching-waiting
            this.legacyModule = (LegacyModule)Util.<LegacyModule>getPrivateObjectField(target, 0);
            this.port         = Util.getPrivateIntField(target, 5);
            }
        }

    static public ThunkedIrSeekerSensor create(IrSeekerSensor target)
        {
        return target instanceof ThunkedIrSeekerSensor ? (ThunkedIrSeekerSensor)target : new ThunkedIrSeekerSensor(target);
        }

    //----------------------------------------------------------------------------------------------
    // Device information: I2C registers for NXT IR seeker
    //----------------------------------------------------------------------------------------------
    /*
    #define HTIRS2_I2C_ADDR    0x10      /*!< IR Seeker I2C device address                           
    #define HTISR2_REG_MODE    0x41      /* register address of the IR reading mode                  
    #define HTIRS2_DIR_DC      0x42      /* DC direction: 0-9                                        
    #define HTIRS2_REG_DC      0x43      /* register address of first DC signal strength 0x43 - 0x47 
    #define HTIRS2_MEAN_DC     0x48      /* sensor DC mean                                           
    #define HTIRS2_DIR_AC      0x49      /* AC direction: 0-9                                        
    #define HTIRS2_REG_AC      0x4A      /* register address of first AC signal strength 0x4A - 0x4E 
    
    #define HTIRS2_READ_BASE_DC   0x42
    #define HTIRS2_READ_BASE_AC   0x49
    */
    
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
    // IrSeekerSensor
    //----------------------------------------------------------------------------------------------

    @Override public void setMode(final IrSeekerSensor.Mode mode)
    // For legacy IR seekers, setting the mode puts signalDetected(), getAngle(), getStrength(), 
    // and getIndividualSensors() out of commission, as it puts the NXT i2c port into write mode.
    // They come back into commission once the port gets auto-reset to read mode 
        {
        (new ThunkForWriting()
            {
            @Override protected void actionOnLoopThread()
                {
                target.setMode(mode);
                }
            }).doWriteOperation(this);
        }

    @Override public IrSeekerSensor.Mode getMode()
        {
        return (new ThunkForReading<Mode>()
            {
            @Override protected void actionOnLoopThread()
                {
                this.result = target.getMode();
                }
            }).doReadOperation(this);
        }

    @Override public boolean signalDetected()
        {
        return (new ThunkForReading<Boolean>()
            {
            @Override protected void actionOnLoopThread()
                {
                this.result = target.signalDetected();
                }
            }).doReadOperation(this);
        }

    @Override public double getAngle()
        {
        return (new ThunkForReading<Double>()
            {
            @Override protected void actionOnLoopThread()
                {
                this.result = target.getAngle();
                }
            }).doReadOperation(this);
        }

    @Override public double getStrength()
        {
        return (new ThunkForReading<Double>()
            {
            @Override protected void actionOnLoopThread()
                {
                this.result = target.getStrength();
                }
            }).doReadOperation(this);
        }

    @Override public IrSeekerSensor.IrSeekerIndividualSensor[] getIndividualSensors()
        {
        return (new ThunkForReading<IrSeekerIndividualSensor[]>()
            {
            @Override protected void actionOnLoopThread()
                {
                this.result = target.getIndividualSensors();
                }
            }).doReadOperation(this);
        }

    }
