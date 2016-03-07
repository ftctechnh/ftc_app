package org.swerverobotics.library.internal;

import com.qualcomm.robotcore.hardware.*;
import java.util.concurrent.locks.Lock;

/**
 * This is almost identical to I2cDevice, but it implements an the II2cDevice interface,
 * thus supporting polymorphism with NXT I2C devices
 */
public final class I2cDeviceOnI2cDeviceController implements II2cDevice
// Implementation note: 
//
// "The Core Device Interface module follows the standard I2C protocol. Many device documentation will 
// specify the address as a 7 bit address. But the CDIM takes the address in the standard 8 bit format. 
// To convert the address multiply it by 2 (or bit shift it to the left by one)."
//
//      Jonathan Berling
//      http://ftcforum.usfirst.org/showthread.php?4421-Recommendation-for-a-gyro-sensor-that-will-work-on-new-control-system/page3
//
// See also:
//      http://www.totalphase.com/support/articles/200349176/
    {
    //----------------------------------------------------------------------------------------------
    // State
    //----------------------------------------------------------------------------------------------

    private I2cController controller;
    private int           port;
    private int           i2cAddr8Bit;

    //----------------------------------------------------------------------------------------------
    // Construction
    //----------------------------------------------------------------------------------------------

    public I2cDeviceOnI2cDeviceController(I2cController controller, int port)
        {
        // Get rid of any thunker; we we want to go the metal
        if (controller instanceof IThunkWrapper)
            {
            controller = ((IThunkWrapper<I2cController>)controller).getWrappedTarget();
            }
        
        this.controller  = controller;
        this.port        = port;
        this.i2cAddr8Bit = 0;
        }
    
    //----------------------------------------------------------------------------------------------
    // HardwareDevice
    //----------------------------------------------------------------------------------------------

    @Override public String getDeviceName()
        {
        return String.format("Swerve I2cDeviceOnI2cController");
        }
    
    @Override public String getConnectionInfo()
        {
        String sController = "anI2cController";
        if (this.controller instanceof HardwareDevice)
            sController = ((HardwareDevice)this.controller).getConnectionInfo();
        return String.format("%s; port: %d", sController, this.port);
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
    // I2cControllerPortDevice
    //----------------------------------------------------------------------------------------------

    @Override
    public I2cController getI2cController()
        {
        return this.controller;
        }

    @Override
    public int getPort()
        {
        return this.port;
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
        this.i2cAddr8Bit = i2cAddr8Bit;
        }
    
    @Override public void enableI2cReadMode(int ib, int cb)
        {
        this.controller.enableI2cReadMode(port, i2cAddr8Bit, ib, cb);
        }

    @Override public void enableI2cWriteMode(int ib, int cb)
        {
        this.controller.enableI2cWriteMode(port, i2cAddr8Bit, ib, cb);
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

    @Override public void readI2cCacheFromController()
        {
        this.controller.readI2cCacheFromController(port);
        }

    @Override public void setI2cPortActionFlag()
        {
        this.controller.setI2cPortActionFlag(port);
        }

    @Override public void writeI2cCacheToController()
        {
        this.controller.writeI2cCacheToController(port);
        }

    @Override public void writeI2cPortFlagOnlyToController()
        {
        this.controller.writeI2cPortFlagOnlyToController(port);
        }

    @Override public void deregisterForPortReadyCallback()
        {
        this.controller.deregisterForPortReadyCallback(port);
        }

    @Override public I2cController.I2cPortReadyCallback getI2cPortReadyCallback()
        {
        return this.controller.getI2cPortReadyCallback(port);
        }

    @Override public void registerForI2cPortReadyCallback(I2cController.I2cPortReadyCallback callback)
        {
        this.controller.registerForI2cPortReadyCallback(callback, port);
        }

    @Override public void registerForPortReadyBeginEndCallback(I2cController.I2cPortReadyBeginEndNotifications callback)
        {
        this.controller.registerForPortReadyBeginEndCallback(callback, port);
        }

    @Override public I2cController.I2cPortReadyBeginEndNotifications getPortReadyBeginEndCallback()
        {
        return this.controller.getPortReadyBeginEndCallback(port);
        }

    @Override public void deregisterForPortReadyBeginEndCallback()
        {
        this.controller.deregisterForPortReadyBeginEndCallback(port);
        }
    }
