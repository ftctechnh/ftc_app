package org.swerverobotics.library.interfaces;

import com.qualcomm.robotcore.hardware.I2cDeviceSynch;

/**
 * {@link I2cDeviceSynchUser} allows one to retrieve an underlying {@link I2cDeviceSynch} from
 * an a client of the latter. Such objects usually represent an semantic layer for an I2c sensor
 * or actuator.
 */
public interface I2cDeviceSynchUser
    {
    /**
     * Returns the device client currently used by this object
     * @return the device client currently used by this object
     */
    I2cDeviceSynch getI2cDeviceSynch();
    }
