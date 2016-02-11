package org.swerverobotics.library.interfaces;

/**
 * Code that previously used II2cDeviceClient should use {@link I2cDeviceSynch} instead
 */
@Deprecated
public interface II2cDeviceClient extends I2cDeviceSynch
    {
    }
