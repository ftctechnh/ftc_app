package org.swerverobotics.library.internal;

import com.qualcomm.robotcore.hardware.I2cController;
import com.qualcomm.robotcore.hardware.I2cDevice;

import java.util.concurrent.locks.Lock;

/**
 * Another in our story
 */
public class ThreadSafeI2cDevice extends I2cDevice
    {
    //----------------------------------------------------------------------------------------------
    // State
    //----------------------------------------------------------------------------------------------

    // Super has this:
    // private I2cController a = null;
    // private int channel = -1;
    // private int i2cAddr = 0;

    public static I2cController getController(I2cDevice target)  { return Util.<I2cController>getPrivateObjectField(target, 0); }
    public static int  getChannel(I2cDevice target)              { return Util.getPrivateIntField(target, 1); }
    public static void setChannel(I2cDevice target, int channel) { Util.setPrivateIntField(target, 1, channel); }
    public static int  getI2cAddr(I2cDevice target)              { return Util.getPrivateIntField(target, 2); }
    public static void setI2cAddr(I2cDevice target, int addr)    { Util.setPrivateIntField(target, 2, addr); }
    
    public  int getChannel()            { return getChannel(this); }
    public void setChannel(int channel) { setChannel(this, channel);}

    public  int getI2cAddr()            { return getI2cAddr(this); }
    public void setI2cAddr(int addr)    { setI2cAddr(this, addr);}

    //----------------------------------------------------------------------------------------------
    // Construction
    //----------------------------------------------------------------------------------------------

    public ThreadSafeI2cDevice(I2cController controller, int channel)
        {
        super(controller, channel);
        }

    //----------------------------------------------------------------------------------------------
    // Operations
    //----------------------------------------------------------------------------------------------

    @Override synchronized public void enableI2cReadMode(int i2cAddress, int memAddress, int length)
        {
        super.enableI2cReadMode(i2cAddress, memAddress, length);
        }

    @Override synchronized public void enableI2cWriteMode(int i2cAddress, int memAddress, int length)
        {
        super.enableI2cWriteMode(i2cAddress, memAddress, length);
        }

    @Override synchronized public Lock getI2cReadCacheLock() 
        {
        return super.getI2cReadCacheLock();
        }

    @Override synchronized public Lock getI2cWriteCacheLock() 
        {
        return super.getI2cWriteCacheLock();
        }

    @Override synchronized public byte[] getI2cReadCache() 
        {
        return super.getI2cReadCache();
        }

    @Override synchronized public byte[] getI2cWriteCache() 
        {
        return super.getI2cWriteCache();
        }

    @Override synchronized public void setI2cPortActionFlag() 
        {
        super.setI2cPortActionFlag();
        }

    @Override synchronized public boolean isI2cPortActionFlagSet() 
        {
        return super.isI2cPortActionFlagSet();
        }

    @Override synchronized public void readI2cCacheFromModule() 
        {
        super.readI2cCacheFromModule();
        }

    @Override synchronized public void writeI2cCacheToModule() 
        {
        super.writeI2cCacheToModule();
        }

    @Override synchronized public void writeI2cPortFlagOnlyToModule() 
        {
        super.writeI2cPortFlagOnlyToModule();
        }

    @Override synchronized public boolean isI2cPortInReadMode() 
        {
        return super.isI2cPortInReadMode();
        }

    @Override synchronized public boolean isI2cPortInWriteMode() 
        {
        return super.isI2cPortInWriteMode();
        }

    @Override synchronized public boolean isI2cPortReady()
        {
        return super.isI2cPortReady();
        }

    @Override synchronized public void registerForI2cPortReadyCallback(I2cController.I2cPortReadyCallback callback) 
        {
        super.registerForI2cPortReadyCallback(callback);
        }

    @Override synchronized public void deregisterForPortReadyCallback() 
        {
        super.deregisterForPortReadyCallback();
        }

    }
