package org.swerverobotics.library;

import com.qualcomm.robotcore.hardware.*;
import org.swerverobotics.library.interfaces.*;
import org.swerverobotics.library.internal.*;

/**
 * ClassFactory provides static methods for instantiating objects within the Swerve Robotics
 * Library.
 * 
 * The point here is to separate clients from having to know the actual class names or 
 * package structures in which various object implementations are stored; these are often internal,
 * and thus should not be exposed to clients, as they are not stable from release to release.
 */
public class ClassFactory
    {
    /**
     * Instantiate an AdaFruit BNO055 sensor who resides at the indicated I2cDevice using
     * default values for configuration parameters.
     */
    public static IBNO055IMU createAdaFruitBNO055IMU(I2cDevice i2cDevice)
        {
        return AdaFruitBNO055IMU.create(i2cDevice);
        }

    /**
     * Instantiate an AdaFruit BNO055 sensor who resides at the indicated I2cDevice using
     * the provided configuration parameters.
     */
    public static IBNO055IMU createAdaFruitBNO055IMU(I2cDevice i2cDevice, IBNO055IMU.Parameters parameters)
        {
        return AdaFruitBNO055IMU.create(i2cDevice, parameters);
        }

    /**
     * Instantiate an I2cDeviceClient wrapper around the provided I2cDevice.
     * 
     * @param i2cDevice                 the I2cDevice to wrap
     * @param initialRegisterWindow     the initial register window to read. May be null.
     * @param i2cAddr                   the I2C address to initialize i2cDevice with. If less than 
     *                                  zero, then i2cAddr is ignored, and the address used by 
     *                                  i2cDevice must be initialized by other means.
     */
    public static II2cDeviceClient createI2cDeviceClient(I2cDevice i2cDevice, II2cDeviceClient.RegWindow initialRegisterWindow, int i2cAddr)
        {
        return new I2cDeviceClient(i2cDevice, initialRegisterWindow, i2cAddr);
        }
    }
