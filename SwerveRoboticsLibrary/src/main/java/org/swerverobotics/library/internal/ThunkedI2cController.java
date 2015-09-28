package org.swerverobotics.library.internal;

import com.qualcomm.robotcore.hardware.*;
import com.qualcomm.robotcore.util.*;
import org.swerverobotics.library.interfaces.*;
import java.util.concurrent.locks.Lock;


/**
 * ThunkedI2cController implements I2cController interface by thunking over to a target
 * I2cController on the loop() thread, except where it can get away with performing operations 
 * locally (which turns out to be a lot).
 */
public class ThunkedI2cController implements I2cController, IThunkWrapper<I2cController>
    {
    //----------------------------------------------------------------------------------------------
    // State
    //----------------------------------------------------------------------------------------------

    private I2cController target;          // can only talk to him on the loop thread

    @Override public I2cController getWrappedTarget() { return this.target; }

    //----------------------------------------------------------------------------------------------
    // Construction
    //----------------------------------------------------------------------------------------------

    private ThunkedI2cController(I2cController target)
        {
        if (target == null) throw new NullPointerException("null " + this.getClass().getSimpleName() + " target");
        this.target = target;
        }

    static public ThunkedI2cController create(I2cController target)
        {
        return target instanceof ThunkedI2cController ? (ThunkedI2cController)target : new ThunkedI2cController(target);
        }

    //----------------------------------------------------------------------------------------------
    // HardwareDevice
    //----------------------------------------------------------------------------------------------

    @Override public void close()
        {
        // This we still do on the loop() thread out of paranoia 
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
        return target.getVersion();
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
        return target.getDeviceName();
        }

    //----------------------------------------------------------------------------------------------
    // I2cController
    //----------------------------------------------------------------------------------------------

    @Override public SerialNumber getSerialNumber()
        {
        return target.getSerialNumber();
        }

    @Override public void enableI2cReadMode(final int physicalPort, final int i2cAddress, final int memAddress, final int length)
        {
        target.enableI2cReadMode(physicalPort, i2cAddress, memAddress, length);
        }

    @Override public void enableI2cWriteMode(final int physicalPort, final int i2cAddress, final int memAddress, final int length)
        {
        target.enableI2cWriteMode(physicalPort, i2cAddress, memAddress, length);
        }

    @Override public byte[] getCopyOfReadBuffer(final int physicalPort)
        {
        return (new ThunkForReading<byte[]>()
            {
            @Override protected void actionOnLoopThread()
                {
                this.result = target.getCopyOfReadBuffer(physicalPort);
                }
            }).doReadOperation();
        }

    @Override public byte[] getCopyOfWriteBuffer(final int physicalPort)
        {
        return (new ThunkForReading<byte[]>()
            {
            @Override protected void actionOnLoopThread()
                {
                this.result = target.getCopyOfWriteBuffer(physicalPort);
                }
            }).doReadOperation();
        }

    @Override public void copyBufferIntoWriteBuffer(final int physicalPort, final byte[] data)
        {
        (new ThunkForWriting()
            {
            @Override protected void actionOnLoopThread()
                {
                target.copyBufferIntoWriteBuffer(physicalPort, data);
                }
            }).doWriteOperation();
        }


    @Override public Lock getI2cReadCacheLock(final int physicalPort)
        {
        return target.getI2cReadCacheLock(physicalPort);
        }

    @Override public Lock getI2cWriteCacheLock(final int physicalPort)
        {
        return target.getI2cWriteCacheLock(physicalPort);
        }

    @Override public byte[] getI2cReadCache(final int physicalPort)
        {
        return target.getI2cReadCache(physicalPort);
        }

    @Override public byte[] getI2cWriteCache(final int physicalPort)
        {
        return target.getI2cWriteCache(physicalPort);
        }

    @Override public void setI2cPortActionFlag(final int physicalPort)
        {
        target.setI2cPortActionFlag(physicalPort);
        }

    @Override public boolean isI2cPortActionFlagSet(final int physicalPort)
        {
        return target.isI2cPortActionFlagSet(physicalPort);
        }

    @Override public synchronized void readI2cCacheFromModule(final int physicalPort)
    // Synchronized is necessary to avoid possible double queue insertion of segment
        {
        target.readI2cCacheFromModule(physicalPort);
        }

    @Override public synchronized void readI2cCacheFromController(final int physicalPort)
    // Synchronized is necessary to avoid possible double queue insertion of segment
        {
        target.readI2cCacheFromController(physicalPort);
        }

    @Override public synchronized void writeI2cCacheToModule(final int physicalPort)
    // Synchronized is necessary to avoid possible double queue insertion of segment
        {
        target.writeI2cCacheToModule(physicalPort);
        }

    @Override public synchronized void writeI2cCacheToController(final int physicalPort)
    // Synchronized is necessary to avoid possible double queue insertion of segment
        {
        target.writeI2cCacheToController(physicalPort);
        }

    @Override public synchronized void writeI2cPortFlagOnlyToModule(final int physicalPort)
    // Synchronized is necessary to avoid possible double queue insertion of segment
        {
        target.writeI2cPortFlagOnlyToModule(physicalPort);
        }

    @Override public synchronized void writeI2cPortFlagOnlyToController(final int physicalPort)
    // Synchronized is necessary to avoid possible double queue insertion of segment
        {
        target.writeI2cPortFlagOnlyToController(physicalPort);
        }

    @Override public boolean isI2cPortInReadMode(final int physicalPort)
        {
        return target.isI2cPortInReadMode(physicalPort);
        }

    @Override public boolean isI2cPortInWriteMode(final int physicalPort)
        {
        return target.isI2cPortInWriteMode(physicalPort);
        }

    @Override public boolean isI2cPortReady(final int physicalPort)
        {
        return target.isI2cPortReady(physicalPort);
        }

    /**
     * @hide
     */
    @Override public synchronized void registerForI2cPortReadyCallback(final I2cController.I2cPortReadyCallback callback, final int physicalPort)
        {
        target.registerForI2cPortReadyCallback(callback, physicalPort);
        }

    /**
     * @hide
     */
    @Override public synchronized void deregisterForPortReadyCallback(final int physicalPort)
        {
        target.deregisterForPortReadyCallback(physicalPort);
        }

    }
