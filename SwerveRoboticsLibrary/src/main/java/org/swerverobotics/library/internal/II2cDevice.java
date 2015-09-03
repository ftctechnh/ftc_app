package org.swerverobotics.library.internal;

import com.qualcomm.robotcore.hardware.*;
import java.util.concurrent.locks.Lock;

/**
 * 
 */
public interface II2cDevice extends HardwareDevice
    {
    int     getI2cAddr();
    void    setI2cAddr(int i2cAddr8Bit);
    
    void    deregisterForPortReadyCallback();

    void    enableI2cReadMode(int ib, int cb);

    void    enableI2cWriteMode(int ib, int cb);

    byte[]  getI2cReadCache();

    Lock    getI2cReadCacheLock();

    byte[]  getI2cWriteCache();

    Lock    getI2cWriteCacheLock();

    boolean isI2cPortActionFlagSet();

    boolean isI2cPortInReadMode();

    boolean isI2cPortInWriteMode();

    boolean isI2cPortReady();

    void    readI2cCacheFromModule();

    void    registerForI2cPortReadyCallback(I2cController.I2cPortReadyCallback callback);

    void    setI2cPortActionFlag();

    void    writeI2cCacheToModule();

    void    writeI2cPortFlagOnlyToModule();
    }