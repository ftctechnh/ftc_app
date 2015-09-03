package org.swerverobotics.library.interfaces;

/**
 * II2cDeviceClientUser allows one to retrieve an underlying I2cDeviceClient from 
 * an object that respresents an I2C hardware sensor.
 */
public interface II2cDeviceClientUser
    {
    II2cDeviceClient getI2cDeviceClient();
    }
