package org.swerverobotics.library.internal;

import com.qualcomm.robotcore.hardware.*;
import java.util.concurrent.locks.Lock;

/**
 * This implements an the II2cDevice interface to a legacy I2C device
 */
public final class I2cDeviceOnLegacyModule implements II2cDevice, LegacyModule.PortReadyCallback
    {
    //----------------------------------------------------------------------------------------------
    // State
    //----------------------------------------------------------------------------------------------

    private LegacyModule controller;
    private int          port;
    private int          i2cAddr8Bit;
    private LegacyModule.PortReadyCallback callback;

    //----------------------------------------------------------------------------------------------
    // Construction
    //----------------------------------------------------------------------------------------------

    public I2cDeviceOnLegacyModule(LegacyModule controller, int port)
        {
        this.controller  = controller;
        this.port        = port;
        this.i2cAddr8Bit = 0;
        this.callback    = null;
        }

    //----------------------------------------------------------------------------------------------
    // HardwareDevice
    //----------------------------------------------------------------------------------------------

    @Override public String getDeviceName()
        {
        return String.format("Legacy I2C device at address 0x%02x", this.i2cAddr8Bit);
        }

    @Override public String getConnectionInfo()
        {
        return String.format("%s; port: %d", controller.getConnectionInfo(), this.port); 
        }

    @Override public int getVersion()
        {
        // We actually don't have any idea what revision number the device might be
        return 0;
        }

    @Override public void close()
        {
        // There's nothing in particular we need to do to shut this down.
        }

    //----------------------------------------------------------------------------------------------
    // II2cDevice
    //----------------------------------------------------------------------------------------------

    @Override public int getI2cAddr()
        {
        return this.i2cAddr8Bit;
        }
    @Override public void setI2cAddr(int i2cAddr8Bit)
        {
        if (i2cAddr8Bit >= 0)
            this.i2cAddr8Bit = i2cAddr8Bit;
        }

    @Override public void enableI2cReadMode(int ib, int cb)
        {
        this.controller.enableNxtI2cReadMode(port, i2cAddr8Bit, ib, cb);
        }

    @Override public void enableI2cWriteMode(int ib, int cb)
        {
        this.controller.enableNxtI2cReadMode(port, i2cAddr8Bit, ib, cb);
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
        return this.controller.isNxtI2cPortActionFlagSet(port);
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
        this.controller.setNxtI2cPortActionFlag(port);
        }

    @Override public void writeI2cCacheToModule()
        {
        this.controller.writeI2cCacheToModule(port);
        }

    @Override public void writeI2cPortFlagOnlyToModule()
        {
        this.controller.writeI2cPortFlagOnlyToModule(port);
        }

    @Override public synchronized void registerForI2cPortReadyCallback(I2cController.I2cPortReadyCallback callack)
        {
        this.callback = callback;
        this.controller.registerForPortReadyCallback(this, port);
        }

    @Override public synchronized void deregisterForPortReadyCallback()
        {
        this.controller.deregisterForPortReadyCallback(port);
        this.callback = null;
        }
    
    @Override public synchronized void portIsReady(int port)
        {
        if (this.callback != null)
            {
            this.callback.portIsReady(port);
            }
        }

    }
