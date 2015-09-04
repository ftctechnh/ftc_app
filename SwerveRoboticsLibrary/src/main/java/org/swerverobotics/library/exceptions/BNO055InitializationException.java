package org.swerverobotics.library.exceptions;

import org.swerverobotics.library.interfaces.IBNO055IMU;

/**
 * A BNO055InitializationException is thrown if a BNO055 fails to initialize successfully
 */
public class BNO055InitializationException extends RuntimeException
    {
    public final IBNO055IMU imu;
    public final String     message;
    public BNO055InitializationException(IBNO055IMU imu, String message)
        {
        this.imu = imu;
        this.message = message;
        }
    }
