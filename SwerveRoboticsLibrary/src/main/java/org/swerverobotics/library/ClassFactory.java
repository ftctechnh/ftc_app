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
     * Creates an II2cDevice interface around an I2cDevice.
     *
     * @param i2cDevice the device to wrap
     * @return          the II2cDevice wrapping
     */
    public static II2cDevice createI2cDeviceFrom(I2cDevice i2cDevice)
        {
        I2cController i2cController = Util.<I2cController>getPrivateObjectField(i2cDevice, 0);
        int port                    = Util.getPrivateIntField(i2cDevice, 1);
        return createI2cDeviceFrom(i2cController, port);
        }

    /**
     * Creates an II2Device instance on a specific port on an I2cDeviceController
     *
     * @param i2cController the controller on which to create the device
     * @param port          the port on the controller to use
     * @return              the created II2cDevice instance
     */
    public static II2cDevice createI2cDeviceFrom(I2cController i2cController, int port)
        {
        return new I2cDeviceOnI2cDeviceController(i2cController, port);
        }

    /**
     * Create a new II2cDeviceClient on an II2cDevice instance.
     *
     * @param i2cDevice             the II2cDevice to wrap
     * @param i2cAddr8Bit           the I2C address at which the client is to communicate
     * @param initialReadWindow     the initial read window to use for the device. May be null
     * @return                      the newly instantiated I2c device client
     */
    public static II2cDeviceClient createI2cDeviceClientFrom(II2cDevice i2cDevice, int i2cAddr8Bit, II2cDeviceClient.ReadWindow initialReadWindow)
        {
        return new I2cDeviceClient(i2cDevice, i2cAddr8Bit, initialReadWindow, true, null);
        }

    /**
     * Create a new II2cDeviceClient on an II2cDevice instance.
     *
     * @param i2cDevice             the II2cDevice to wrap
     * @param i2cAddr8Bit           the I2C address at which the client is to communicate
     * @param initialReadWindow     the initial read window to use for the device. May be null
     * @param autoClose             whether the I2cDevice should register itself to auto-close on OpMode stop
     * @param registrar             the optional registrar with which to attempt auto closing, if requested. If null, then
     *                              if we are on a synchronous thread, the contextual registrar is used. If both
     *                              are absent, then no auto registration occurs.
     * @return                      the newly instantiated I2c device client
     */
    public static II2cDeviceClient createI2cDeviceClientFrom(II2cDevice i2cDevice, int i2cAddr8Bit, II2cDeviceClient.ReadWindow initialReadWindow, boolean autoClose, IStopActionRegistrar registrar)
        {
        return new I2cDeviceClient(i2cDevice, i2cAddr8Bit, initialReadWindow, autoClose, registrar);
        }


    //----------------------------------------------------------------------------------------------
    // Construction
    //----------------------------------------------------------------------------------------------
    
    private ClassFactory() { }
    }
