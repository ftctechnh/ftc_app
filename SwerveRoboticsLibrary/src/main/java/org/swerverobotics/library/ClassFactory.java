package org.swerverobotics.library;

import com.qualcomm.robotcore.hardware.I2cDevice;

import org.swerverobotics.library.interfaces.IAdaFruitBNO055IMU;
import org.swerverobotics.library.internal.AdaFruitBNO055IMU;

/**
 * 
 */
public class ClassFactory
    {
    public static IAdaFruitBNO055IMU createAdaFruitBNO055IMU(I2cDevice i2cDevice)
        {
        return AdaFruitBNO055IMU.create(i2cDevice);
        }
    }
