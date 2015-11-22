package org.swerverobotics.library;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.*;
import org.swerverobotics.library.interfaces.*;
import org.swerverobotics.library.internal.*;
import java.util.*;

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
    // Actuators
    //----------------------------------------------------------------------------------------------

    /**
     * If the provided motors are using a legacy motor controller, createEasyMotorController swaps
     * that controller out and installs an alternate 'EasyLegacyMotorController' DCMotorController
     * implementation for the duration of the OpMode. If the motors are using a modern motor controller,
     * an analogous swap to an 'EasyModernMotorController' is made. If a matrix motor controller is in
     * use, this function has no effect. The APIs to easy legacy and modern motor controllers
     * are <em>identical</em>, which helps simplify programming.
     *
     * <p>EasyLegacyMotorController is implemented on top of an {@link II2cDeviceClient} instance
     * which completely handles all the complexities of read vs write mode switching and the
     * like, allowing the logic inside the controller itself to be extraordinarily simple.
     * In particular, the manual mode switching and loop() counting necessary with the stock
     * controller implementation is not needed. Just call the motor getPosition() or setPower()
     * methods or what have you, and the necessary bookkeeping details will be taken care of.</p>
     *
     * <p>The improvements in EasyModernMotorController are less dramatic, but still significant.
     * Of particular note is that write operations, including motor mode and power changes, happen
     * immediately rather than being deferred to the end of a loop() cycle. This significantly
     * simplifies the steps that are necessary to create code that uses a modern motor controller
     * reliably.</p>
     *
     * <p>Easy motor controllers are not tied to SynchronousOpMode or any other particular
     * OpMode. They can be used, for example, from LinearOpModes. In SynchronousOpMode,
     * easy motor controllers are used automatically; in other OpModes, you'll have to manually
     * call {@link #createEasyMotorController} yourself.</p>
     *
     * This method takes as parameters one or both motors that reside on a given  motor
     * controller. If two motors are provided, they must share the same controller, and conversely
     * if two motors reside on a controller then both must be provided. The method creates a new
     * easy motor controller of the appropriate type and installs it as the controller for the provided
     * motors; the existing controller is disabled. When the current OpMode is complete, the processed
     * is reversed.
     *
     * @param context   the OpMode within which this creation is occurring
     * @param motor1    one of the up-to-two motor controllers which share a legacy motor controller. May be null.
     * @param motor2    the possibly other motor controller on the same controller. May be null.
     *
     * @see org.swerverobotics.library.examples.SynchMotorLoopPerf
     */
    public static void createEasyMotorController(OpMode context, DcMotor motor1, DcMotor motor2)
        {
        DcMotorController target = motor1==null ? null : motor1.getController();

        if (motor2 != null && target != null && motor2.getController()!=target)
            throw new IllegalArgumentException("motors do not share the same controller");

        if (MemberUtil.isLegacyMotorController(target))
            EasyLegacyMotorController.create(context, target, motor1, motor2);

        else if (MemberUtil.isModernMotorController(target))
            EasyModernMotorController.create(context, target, motor1, motor2);
        }

    @Deprecated
    public static void createEasyLegacyMotorController(OpMode context, DcMotor motor1, DcMotor motor2)
        {
        createEasyMotorController(context, motor1, motor2);
        }

    /**
     * Creates an alternate 'easy' implementation of the controller for the indicated collection
     * of servos, which must all share the same controller, and must be <em>all</em> the servos
     * which are found on that controller.
     *
     * A notable feature of the easy servo controller implementation is that position change
     * requests and other servo writes are issued immediately instead of being deferred to the
     * end of the next loop() cycle, which simplifies programming. This is similar to the
     * enhancements found in the easy motor controller. Some small bugs are also fixed.
     *
     * @param context   the OpMode within which the creation is occurring
     * @param servos    the list of servos whose controller implementation we are to change.
     *                  May not be null or empty.
     *
     * @see #createEasyMotorController(OpMode, DcMotor, DcMotor)
     */
    public static void createEasyServoController(OpMode context, Collection<Servo> servos)
        {
        if (servos != null && !servos.isEmpty())
            {
            ServoController controller = null;
            for (Servo servo : servos)
                {
                if (controller==null)
                    controller = servo.getController();
                else if (controller != servo.getController())
                    throw new IllegalArgumentException("not all servos share the same controller");
                }

            if (MemberUtil.isModernServoController(controller))
                EasyModernServoController.create(context, controller, servos);

            if (MemberUtil.isLegacyServoController(controller))
                EasyLegacyServoController.create(context, controller, servos);
            }
        else
            throw new IllegalArgumentException("no servos provided");
        }

    //----------------------------------------------------------------------------------------------
    // Sensors
    //----------------------------------------------------------------------------------------------

    /** An enumeration of the various sensor manufacturers we are involved with */
    public enum SENSOR_FLAVOR { HITECHNIC, MODERNROBOTICS, ADAFRUIT };

    /**
     * Instantiates a driver object for a  AdaFruit BNO055 sensor which resides at the indicated I2cDevice using
     * default values for configuration parameters.
     *
     * @param context       the OpMode within which this creation is taking place
     * @param i2cDevice     the robot controller runtime object representing the sensor
     * @return              the interface to the instantiated sensor object.
     * @see #createAdaFruitBNO055IMU(OpMode, I2cDevice, IBNO055IMU.Parameters)
     */
    public static IBNO055IMU createAdaFruitBNO055IMU(OpMode context, I2cDevice i2cDevice)
        {
        return createAdaFruitBNO055IMU(context, i2cDevice, new IBNO055IMU.Parameters());
        }

    /**
     * Instantiates a driver object for an AdaFruit BNO055 sensor which resides at the indicated I2cDevice using
     * the provided configuration parameters. This creation method only functions in a SynchronousOpMode.
     *
     * @param i2cDevice     the robot controller runtime object representing the sensor
     * @param parameters    the parameters with which the sensor should be initialized
     * @return              the interface to the instantiated sensor object.
     * @see #createAdaFruitBNO055IMU(OpMode, I2cDevice, IBNO055IMU.Parameters)
     */
    public static IBNO055IMU createAdaFruitBNO055IMU(I2cDevice i2cDevice, IBNO055IMU.Parameters parameters)
        {
        SwerveThreadContext.assertSynchronousThread();
        return createAdaFruitBNO055IMU(SwerveThreadContext.getOpMode(), i2cDevice, parameters);
        }

    /**
     * Instantiates a driver object for an AdaFruit BNO055 sensor which resides at the indicated I2cDevice using
     * the provided configuration parameters.
     *
     * <p>Features of this sensor include a gyro that does rate integration in hardware to
     * provide robust and accurate angular position indications, and a separation of the output of the
     * accelerometer into gravity and linear-motion-induced components.</p>
     *
     * <p>The driver builds on the linear-acceleration information to provide linear velocity
     * and position measurements using integration in software. That said, the built-in accelerometer
     * integration algorithm is quite naive. For a real robot, you'll want to do some investigation
     * and reading and make a better one, whose use you can indicate using
     * {@link org.swerverobotics.library.interfaces.IBNO055IMU.Parameters#accelerationIntegrationAlgorithm parameters.accelerationIntegrationAlgorithm}.</p>
     *
     * <p>Also, while the out-of-box sensor BNO055 works remarkably well, Bosch
     * <a href="https://github.com/SwerveRobotics/ftc_app/raw/master/SwerveRoboticsLibrary/doc/reference/BST_BNO055_DS000_13.pdf">describes</a>
     * a one-time calibration process that will make it even better (see Section 3.11).
     * Perform this calibration process in the lab. Once you've got the sensor fully
     * calibrated (or at least the gyro and the accelerometer), extract the configuration
     * state with {@link IBNO055IMU#readCalibrationData()}. We suggest that you then incorporate
     * the results as constants in your code, and provide them during OpMode startup in
     * {@link org.swerverobotics.library.interfaces.IBNO055IMU.Parameters#calibrationData parameters.calibrationData}
     * where they will automatically be applied.</p>
     *
     * @param context       the OpMode within which this creation is taking place
     * @param i2cDevice     the robot controller runtime object representing the sensor
     * @param parameters    the parameters with which the sensor should be initialized
     * @return              the interface to the instantiated sensor object. This object also
     *                      supports the II2cDeviceClientUser interface, which can be useful
     *                      for debugging.
     * @see #createAdaFruitBNO055IMU(OpMode, I2cDevice)
     */
    public static IBNO055IMU createAdaFruitBNO055IMU(OpMode context, I2cDevice i2cDevice, IBNO055IMU.Parameters parameters)
        {
        return AdaFruitBNO055IMU.create(context, i2cDevice, parameters);
        }


    /**
     * Creates an alternate implementation of the target color sensor. The target sensor is
     * disabled in the process. The alternate implementation robustly implements the ColorSensor
     * functionality.
     *
     * @param context       the OpMode within which this sensor is to be used
     * @param target        the sensor whose implementation we are to replace
     * @return an alternate color sensor implementation
     *
     * @see #createSwerveColorSensor(OpMode, I2cController, int, int, SENSOR_FLAVOR)
     * @see org.swerverobotics.library.examples.LinearColorDemo
     * @see org.swerverobotics.library.examples.SyncColorDemo
     */
    public static ColorSensor createSwerveColorSensor(OpMode context, ColorSensor target)
        {
        return LegacyOrModernColorSensor.create(context, target);
        }

    /**
     * Creates a implementation of a color sensor from the provided information.
     *
     * @param context       the OpMode within which this sensor is to be used
     * @param controller    the controller on which the sensor resides
     * @param port          the port on the controller at which it so resides
     * @param i2cAddr8Bit   the I2C address of the sensor
     * @param flavor        the flavor of color sensor that resides there
     * @return an alternate color sensor implementation
     *
     * @see #createSwerveColorSensor(OpMode, ColorSensor)
     * @see org.swerverobotics.library.examples.LinearColorDemo
     * @see org.swerverobotics.library.examples.SyncColorDemo
     */
    public static ColorSensor createSwerveColorSensor(OpMode context, I2cController controller, int port, int i2cAddr8Bit, ClassFactory.SENSOR_FLAVOR flavor)
        {
        return LegacyOrModernColorSensor.create(context, controller, port, i2cAddr8Bit, flavor, null);
        }


    //----------------------------------------------------------------------------------------------
    // Low level I2cDevice creation
    //----------------------------------------------------------------------------------------------

    /**
     * Creates an II2cDevice interface around an I2cDevice.
     *
     * @param i2cDevice the device to wrap
     * @return          the II2cDevice wrapping
     */
    public static II2cDevice createI2cDevice(I2cDevice i2cDevice)
        {
        I2cController i2cController = MemberUtil.i2cControllerOfI2cDevice(i2cDevice);
        int port                    = MemberUtil.portOfI2cDevice(i2cDevice);
        return createI2cDevice(i2cController, port);
        }

    /**
     * Creates an II2Device instance on a specific port on an I2cDeviceController
     *
     * @param i2cController the controller on which to create the device
     * @param port          the port on the controller to use
     * @return              the created II2cDevice instance
     */
    public static II2cDevice createI2cDevice(I2cController i2cController, int port)
        {
        return new I2cDeviceOnI2cDeviceController(i2cController, port);
        }

    /**
     * Create a new II2cDeviceClient on an I2cDevice instance. The client is initially disarmed,
     * and must be armed before use.
     *
     * @param context               the OpMode within which the creation is taking place
     * @param i2cDevice             the II2cDevice to wrap
     * @param i2cAddr8Bit           the I2C address at which the client is to communicate
     * @param closeOnOpModeStop     if true, then when the OpMode stops, the client will automatically close
     * @return                      the newly instantiated I2c device client
     * @see II2cDeviceClient#arm()
     */
    public static II2cDeviceClient createI2cDeviceClient(OpMode context, I2cDevice i2cDevice, int i2cAddr8Bit, boolean closeOnOpModeStop)
        {
        II2cDevice ii2cDevice = createI2cDevice(i2cDevice);
        return createI2cDeviceClient(context, ii2cDevice, i2cAddr8Bit, closeOnOpModeStop);
        }


    /**
     * Create a new II2cDeviceClient on an II2cDevice instance. The client is initially
     * disarmed, and must be armed before use.
     *
     * @param context               the OpMode within which the creation is taking place
     * @param i2cDevice             the II2cDevice to wrap
     * @param i2cAddr8Bit           the I2C address at which the client is to communicate
     * @param closeOnOpModeStop     if true, then when the OpMode stops, the client will automatically close
     * @return                      the newly instantiated I2c device client
     * @see II2cDeviceClient#arm()
     */
    public static II2cDeviceClient createI2cDeviceClient(OpMode context, II2cDevice i2cDevice, int i2cAddr8Bit, boolean closeOnOpModeStop)
        {
        return new I2cDeviceClient(context, i2cDevice, i2cAddr8Bit, closeOnOpModeStop);
        }

    //----------------------------------------------------------------------------------------------
    // Miscellaneous
    //----------------------------------------------------------------------------------------------

    /**
     * Creates an object that can report the number of times loop() has been called in both
     * Synchronous and Linear OpModes. In the former, this is not necessary, as getLoopCount()
     * may simply be called directly, but this utility object is handy in the latter.
     * @param opMode    the OpMode in which this loop counter is to report
     * @return          the new utility object. Be sure to close() this object before exiting your OpMode
     */
    public static IOpModeLoopCounter createLoopCounter(OpMode opMode)
        {
        return new OpModeLoopCounter(opMode);
        }

    //----------------------------------------------------------------------------------------------
    // Construction
    //----------------------------------------------------------------------------------------------
    
    private ClassFactory() { }
    }
