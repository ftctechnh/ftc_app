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
    // NxtMotorControllerOnI2cDevice
    //----------------------------------------------------------------------------------------------

    /**
     * NxtMotorControllerOnI2cDevice is an alternative implementation of a Legacy DC Motor controller.
     * It is implemented on top of an {@link II2cDeviceClient} instance which completely handles
     * all the complexities of read vs write mode switching and the like, allowing the logic of the
     * controller itself to be extraordinarily simple.
     *
     * <p>The key fact about NxtMotorControllerOnI2cDevice is that manual mode switching is
     * entirely unnecessary. Just call the getPosition() or setPower() methods or what have you,
     * and the mode switching will be taken care of.</p>
     *
     * <p>NxtMotorControllerOnI2cDevice not tied to SynchronousOpMode. It can also be used from
     * LinearOpMode, or, indeed, any thread that can tolerate operations that can take tens of
     * milliseconds to run. In SynchronousOpMode, NxtMotorControllerOnI2cDevice is currently
     * enabled by setting the {@link SynchronousOpMode#useExperimentalThunking} flag, though that
     * will probably change. In other OpModes, you'll have to manually call this {@link #createNxtDcMotorControllerOnI2cDevice
     * createNxtDcMotorControllerOnI2cDevice()}.</p>
     *
     * <p>You should call {@link DcMotorController#close()} when you want the controller to
     * close down, likely from your stop() logic or the end of your runOpMode() method as the
     * case may be.</p>
     *
     * <p>{@link #createNxtDcMotorControllerOnI2cDevice createNxtDcMotorControllerOnI2cDevice()} takes
     * a ModernRoboticsNxtDcMotorController motor controller as might found in an OpMode's hardware map
     * and converts that into an NxtDcMotorControllerOnI2cDevice. As a side effect of doing so, the
     * ModernRoboticsNxtDcMotorController is disabled, as only one object can be managing the
     * controller at a given time. That importantly also means that any DcMotor objects from
     * the hardware map that were on that controller will need to be recreated. This can be accomplished by calling</p>
     *
     * <code>new DcMotor(controller, portNumber, direction)</code>
     *
     * <p>where controller is the new NxtMotorControllerOnI2cDevice instance and portNumber and
     * direction were retreived from the old, now non-functional DcMotor using getPortNumber()
     * and getDirection() respectively.</p>
     *
     * @param target the ModernRoboticsNxtDcMotorController we are to convert
     * @return an NxtMotorControllerOnI2cDevice, or null if target was not a legacy motor controller
     *
     */
    public static DcMotorController createNxtDcMotorControllerOnI2cDevice(DcMotorController target)
        {
        return ThunkingHardwareFactory.createNxtMotorControllerOnI2cDevice(target, null);
        }

    //----------------------------------------------------------------------------------------------
    // AdaFruit IMU
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
    // Low level I2cDevice manipulation
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
