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
     * @return              the interface to the instantiated sensor object. This object also
     *                      supports the II2cDeviceClientUser interface; this is often useful
     *                      for debugging.
     * @see #createAdaFruitBNO055IMU(I2cDevice, IBNO055IMU.Parameters) 
     */
    public static IBNO055IMU createAdaFruitBNO055IMU(I2cDevice i2cDevice)
        {
        return createAdaFruitBNO055IMU(i2cDevice, new IBNO055IMU.Parameters());
        }

    /**
     * Instantiate an AdaFruit BNO055 sensor who resides at the indicated I2cDevice using
     * the provided configuration parameters.
     * 
     * @param i2cDevice     the robot controller runtime object representing the sensor
     * @param parameters    the parameters with which the sensor should be initialized
     * @return              the interface to the instantiated sensor object. This object also
     *                      supports the II2cDeviceClientUser interface; this is often useful
     *                      for debugging.
     * @see #createAdaFruitBNO055IMU(I2cDevice) 
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
     * @param i2cAddr8Bit               the I2C address to initialize i2cDevice with. Ignored if less than zero.
     * @return                          the newly instantiated wrapper
     */
    public static II2cDeviceClient createI2cDeviceClient(I2cDevice i2cDevice, int i2cAddr8Bit, II2cDeviceClient.RegWindow initialRegisterWindow)
        {
        I2cController i2cController = Util.<I2cController>getPrivateObjectField(i2cDevice, 0);
        int port = Util.getPrivateIntField(i2cDevice, 1);
        return createI2cDeviceClient(i2cController, port, i2cAddr8Bit, initialRegisterWindow);
        }

    /**
     * Instantiate an I2cDeviceClient on an I2cController using a given port and i2cAddr
     * @param i2cController             the controller to use
     * @param port                      the port to use on that controller
     * @param i2cAddr8Bit               the I2C address to talk to through that port. Ignored if less than zero.
     * @param initialRegisterWindow     the initial register window to read. May be null.
     * @return                          the returned wrapper
     * 
     * @see #createI2cDeviceClient(LegacyModule, int, int, II2cDeviceClient.RegWindow) 
     */
    public static II2cDeviceClient createI2cDeviceClient(I2cController i2cController, int port, int i2cAddr8Bit, II2cDeviceClient.RegWindow initialRegisterWindow)
        {
        II2cDevice ii2cDevice = new I2cDeviceOnI2cDeviceController(i2cController, port);
        return createI2cDeviceClient(ii2cDevice, i2cAddr8Bit, initialRegisterWindow);    
        }

    /**
     * Instantiate an I2cDeviceClient on a LegacyModule using a given port and i2cAddr
     * @param legacyModule              the legacy module to use
     * @param port                      the port to use on that controller
     * @param i2cAddr8Bit               the I2C address to talk to through that port. Ignored if less than zero.
     * @param initialRegisterWindow     the initial register window to read. May be null.
     * @return                          the returned wrapper
     * 
     * @see #createI2cDeviceClient(I2cController, int, int, II2cDeviceClient.RegWindow) 
     */
    public static II2cDeviceClient createI2cDeviceClient(LegacyModule legacyModule, int port, int i2cAddr8Bit, II2cDeviceClient.RegWindow initialRegisterWindow)
        {
        II2cDevice ii2cDevice = new I2cDeviceOnLegacyModule(legacyModule, port);
        return createI2cDeviceClient(ii2cDevice, i2cAddr8Bit, initialRegisterWindow);
        }

    /**
     * Instantiate an I2cDeviceClient wrapper around an I2cDevice using the I2C address currently found therein
     * 
     * @param i2cDevice                 the I2cDevice to wrap
     * @param initialRegisterWindow     the initial register window to read. May be null
     * @return                          the newly instantiated wrapper
     */
    public static II2cDeviceClient createI2cDeviceClient(I2cDevice i2cDevice, II2cDeviceClient.RegWindow initialRegisterWindow)
        {
        int i2cAddr8Bit = Util.getPrivateIntField(i2cDevice, 2);
        return createI2cDeviceClient(i2cDevice, i2cAddr8Bit, initialRegisterWindow);
        }

    /** internal */
    private static II2cDeviceClient createI2cDeviceClient(II2cDevice ii2cDevice, int i2cAddr8Bit, II2cDeviceClient.RegWindow initialRegisterWindow)
        {
        return new I2cDeviceClient(ii2cDevice, i2cAddr8Bit, initialRegisterWindow);
        }
    
    //----------------------------------------------------------------------------------------------
    // Construction
    //----------------------------------------------------------------------------------------------
    
    private ClassFactory() { }
    }
