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
public final class ClassFactory
    {
    //----------------------------------------------------------------------------------------------
    
    /**
     * Instantiate an AdaFruit BNO055 sensor who resides at the indicated I2cDevice using
     * default values for configuration parameters.
     * 
     * @param i2cDevice     the robot controller runtime object representing the sensor
     * @return              the interface to the instantiated sensor object
     */
    public static IBNO055IMU createAdaFruitBNO055IMU(I2cDevice i2cDevice)
        {
        return AdaFruitBNO055IMU.create(i2cDevice);
        }

    /**
     * Instantiate an AdaFruit BNO055 sensor who resides at the indicated I2cDevice using
     * the provided configuration parameters.
     * 
     * @param i2cDevice     the robot controller runtime object representing the sensor
     * @param parameters    the parameters with which the sensor should be initialized
     * @return              the interface to the instantiated sensor object
     */
    public static IBNO055IMU createAdaFruitBNO055IMU(I2cDevice i2cDevice, IBNO055IMU.Parameters parameters)
        {
        return AdaFruitBNO055IMU.create(i2cDevice, parameters);
        }
    
    //----------------------------------------------------------------------------------------------

    /**
     * Instantiate an I2cDeviceClient wrapper on an I2cDevice.
     * 
     * @param i2cDevice                 the I2cDevice to wrap
     * @param initialRegisterWindow     the initial register window to read. May be null.
     * @param i2cAddr                   the I2C address to initialize i2cDevice with.
     * @return                          the newly instantiated wrapper
     */
    public static II2cDeviceClient createI2cDeviceClient(I2cDevice i2cDevice, int i2cAddr, II2cDeviceClient.RegWindow initialRegisterWindow)
        {
        I2cController i2cController = Util.<I2cController>getPrivateObjectField(i2cDevice, 0);
        int port = Util.getPrivateIntField(i2cDevice, 1);
        return createI2cDeviceClient(i2cController, port, i2cAddr, initialRegisterWindow);
        }

    /**
     * Instantiate an I2cDeviceClient on an I2cController using a given port and i2cAddr
     * @param i2cController             the controller to use
     * @param port                      the port to use on that controlller
     * @param i2cAddr                   the i2cAddr to talk to through that port
     * @param initialRegisterWindow     the initial register window to read. May be null.
     * @return                          the returned wrapper
     */
    public static II2cDeviceClient createI2cDeviceClient(I2cController i2cController, int port, int i2cAddr, II2cDeviceClient.RegWindow initialRegisterWindow)
        {
        II2cDevice ii2cDevice = new I2cDeviceOnI2cDeviceController(i2cController, port);
        return createI2cDeviceClient(ii2cDevice, i2cAddr, initialRegisterWindow);    
        }
    
    public static II2cDeviceClient createI2cDeviceClient(LegacyModule legacyModule, int port, int i2cAddr, II2cDeviceClient.RegWindow initialRegisterWindow)
        {
        II2cDevice ii2cDevice = new I2cDeviceOnLegacyModule(legacyModule, port);
        return createI2cDeviceClient(ii2cDevice, i2cAddr, initialRegisterWindow);
        }

    /**
     * Instantiate an I2cDeviceClient wrapper around an I2cDevice using the i2cAddr found therein
     * 
     * @param i2cDevice                 the I2cDevice to wrap
     * @param initialRegisterWindow     the initial register window to read. May be null
     * @return                          the newly instantiated wrapper
     */
    public static II2cDeviceClient createI2cDeviceClient(I2cDevice i2cDevice, II2cDeviceClient.RegWindow initialRegisterWindow)
        {
        int i2cAddr = Util.getPrivateIntField(i2cDevice, 2);
        return createI2cDeviceClient(i2cDevice, i2cAddr, initialRegisterWindow);
        }

    /** internal */
    private static II2cDeviceClient createI2cDeviceClient(II2cDevice ii2cDevice, int i2cAddr, II2cDeviceClient.RegWindow initialRegisterWindow)
        {
        return new I2cDeviceClient(ii2cDevice, i2cAddr, initialRegisterWindow);
        }
    
    //----------------------------------------------------------------------------------------------
    // Construction
    //----------------------------------------------------------------------------------------------
    
    private ClassFactory() { }
    }
