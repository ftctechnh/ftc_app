package org.swerverobotics.library.interfaces;

/**
 * I2cDeviceClientUser allows one to retrieve an underlying I2cDeviceClient from 
 * an object that represents an I2C hardware sensor or other device
 */
public interface I2cDeviceClientUser
    {
    /**
     * Returns the device client currently used by this object
     * @return the device client currently used by this object
     */
    I2cDeviceClient getI2cDeviceClient();
    }
