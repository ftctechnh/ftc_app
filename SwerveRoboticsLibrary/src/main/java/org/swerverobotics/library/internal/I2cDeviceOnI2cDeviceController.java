package org.swerverobotics.library.internal;

import com.qualcomm.robotcore.hardware.*;
import java.util.concurrent.locks.Lock;

/**
 * This is almost identical to I2cDevice, but it implements an the II2cDevice interface,
 * thus supporting polymorphism with NXT I2C devices
 */
public final class I2cDeviceOnI2cDeviceController implements II2cDevice
    {
    //----------------------------------------------------------------------------------------------
    // State
    //----------------------------------------------------------------------------------------------

    private I2cController controller;
    private int           port;
    private int           i2cAddr;

    //----------------------------------------------------------------------------------------------
    // Construction
    //----------------------------------------------------------------------------------------------

    public I2cDeviceOnI2cDeviceController(I2cController controller, int port)
        {
        this.controller = controller;
        this.port       = port;
        this.i2cAddr    = 0;
        }
    
    //----------------------------------------------------------------------------------------------
    // II2cDevice
    //----------------------------------------------------------------------------------------------
    
    @Override public int getI2cAddr()
        {
        return this.i2cAddr;
        }
    @Override public void setI2cAddr(int i2cAddr)
        {
        this.i2cAddr = i2cAddr;
        }
    
    @Override public void enableI2cReadMode(int ib, int cb)
        {
        this.controller.enableI2cReadMode(port, i2cAddr, ib, cb);
        }

    @Override public void enableI2cWriteMode(int ib, int cb)
        {
        this.controller.enableI2cWriteMode(port, i2cAddr, ib, cb);
        }

    @Override public byte[] getI2cReadCache()
        {
        return this.controller.getI2cReadCache(port);
        }

    @Override public Lock getI2cReadCacheLock()
        {
        return this.controller.getI2cReadCacheLock(port);
        }

    @Override public byte[] getI2cWriteCache()
        {
        return this.controller.getI2cWriteCache(port);
        }

    @Override public Lock getI2cWriteCacheLock()
        {
        return this.controller.getI2cWriteCacheLock(port);
        }

    @Override public boolean isI2cPortActionFlagSet()
        {
        return this.controller.isI2cPortActionFlagSet(port);
        }

    @Override public boolean isI2cPortInReadMode()
        {
        return this.controller.isI2cPortInReadMode(port);
        }

    @Override public boolean isI2cPortInWriteMode()
        {
        return this.controller.isI2cPortInWriteMode(port);
        }

    @Override public boolean isI2cPortReady()
        {
        return this.controller.isI2cPortReady(port);
        }

    @Override public void readI2cCacheFromModule()
        {
        this.controller.readI2cCacheFromModule(port);
        }

    @Override public void setI2cPortActionFlag()
        {
        this.controller.setI2cPortActionFlag(port);
        }

    @Override public void writeI2cCacheToModule()
        {
        this.controller.writeI2cCacheToModule(port);
        }

    @Override public void writeI2cPortFlagOnlyToModule()
        {
        this.controller.writeI2cPortFlagOnlyToModule(port);
        }

    @Override public void deregisterForPortReadyCallback()
        {
        this.controller.deregisterForPortReadyCallback(port);
        }

    @Override public void registerForI2cPortReadyCallback(I2cController.I2cPortReadyCallback callback)
        {
        this.controller.registerForI2cPortReadyCallback(callback, port);
        }

    }
