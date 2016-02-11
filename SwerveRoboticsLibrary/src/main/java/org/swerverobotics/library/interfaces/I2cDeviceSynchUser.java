package org.swerverobotics.library.interfaces;

/**
 * I2cDeviceSynchUser allows one to retrieve an underlying I2cDeviceSynch from
 * an object that represents an I2C hardware sensor or other device
 */
public interface I2cDeviceSynchUser
    {
    /**
     * Returns the device client currently used by this object
     * @return the device client currently used by this object
     */
    I2cDeviceSynch getI2cDeviceSynch();
    }
