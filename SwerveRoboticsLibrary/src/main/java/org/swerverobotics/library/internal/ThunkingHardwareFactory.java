package org.swerverobotics.library.internal;

import com.qualcomm.robotcore.hardware.*;
import org.swerverobotics.library.interfaces.*;

import java.util.*;

/**
 * ThunkingHardwareFactory creates the wrappers needed to make SynchronousOpMode function correctly. 
 */
public class ThunkingHardwareFactory
    {
    //----------------------------------------------------------------------------------------------
    // State 
    //----------------------------------------------------------------------------------------------

    HardwareMap          unthunkedHwmap;
    HardwareMap          thunkedHwmap;
    IStopActionRegistrar stopRegistrar;
    boolean              useExperimental;
    final String         swerveVoltageSensorName = "<|Swerve VoltageSensor|>";

    //----------------------------------------------------------------------------------------------
    // Construction
    //----------------------------------------------------------------------------------------------

    public ThunkingHardwareFactory(HardwareMap unthunkedHwmap, IStopActionRegistrar stopRegistrar, boolean useExperimental)
        {
        this.thunkedHwmap       = null;
        this.unthunkedHwmap     = unthunkedHwmap;
        this.stopRegistrar      = stopRegistrar;
        this.useExperimental    = useExperimental;
        }
    
    //----------------------------------------------------------------------------------------------
    // Operations 
    //----------------------------------------------------------------------------------------------
    
    /**
     * Given a (non-internal) hardware map, create a new hardware map containing
     * all the same devices but in a form that their methods thunk from the main()
     * thread to the loop() thread.
     */
    public final HardwareMap createThunkedHardwareMap()
        {
        this.thunkedHwmap = new HardwareMap();

        //----------------------------------------------------------------------------
        // Modules
        //----------------------------------------------------------------------------

        // Thunk the legacy modules
        createThunks(unthunkedHwmap.legacyModule, thunkedHwmap.legacyModule,
            new IThunkFactory<LegacyModule>()
                {
                @Override public LegacyModule create(LegacyModule target)
                    {
                    return ThunkedLegacyModule.create(target);
                    }
                }
        );

        // Thunk the core device interface modules
        createThunks(unthunkedHwmap.deviceInterfaceModule, thunkedHwmap.deviceInterfaceModule,
            new IThunkFactory<DeviceInterfaceModule>()
                {
                @Override public DeviceInterfaceModule create(DeviceInterfaceModule target)
                    {
                    return ThunkedDeviceInterfaceModule.create(target);
                    }
                }
        );

        //----------------------------------------------------------------------------
        // Controllers
        //----------------------------------------------------------------------------

        // Thunk the motor controllers. Some of these we might treat specially. 
        createThunks(unthunkedHwmap.dcMotorController, thunkedHwmap.dcMotorController,
            new IThunkFactory<DcMotorController>()
                {
                @Override public DcMotorController create(DcMotorController target)
                    {
                    if (useExperimental)
                        {
                        DcMotorController newController = createNxtMotorControllerOnI2cDevice(target, stopRegistrar);
                        if (newController != null)
                            return newController;
                        }
                    
                    // Not experimental or not a legacy motor controller. Proceed as usual.
                    return ThunkedDCMotorController.create(target);
                    }
                }
        );

        // Thunk the servo controllers
        createThunks(unthunkedHwmap.servoController, thunkedHwmap.servoController,
            new IThunkFactory<ServoController>()
                {
                @Override public ServoController create(ServoController target)
                    {
                    return ThunkedServoController.create(target);
                    }
                }
        );

        //----------------------------------------------------------------------------
        // Actuators
        //----------------------------------------------------------------------------
        
        // Thunk the DC motors
        createThunks(unthunkedHwmap.dcMotor, thunkedHwmap.dcMotor,
            new IThunkFactory<DcMotor>()
                {
                @Override public DcMotor create(DcMotor target)
                    {
                    DcMotorController targetController = target.getController();
                    DcMotorController controller = findWrapper(thunkedHwmap.dcMotorController, targetController, ThunkedDCMotorController.create(targetController));
                    
                    return new ThreadSafeDcMotor(
                            controller,
                            target.getPortNumber(),
                            target.getDirection()
                    );
                    }
                }
        );

        // Thunk the servos
        createThunks(unthunkedHwmap.servo, thunkedHwmap.servo,
            new IThunkFactory<Servo>()
                {
                @Override public Servo create(Servo target)
                    {
                    ServoController targetController = target.getController();
                    ServoController controller = findWrapper(thunkedHwmap.servoController, targetController, ThunkedServoController.create(targetController));

                    return new ThreadSafeServo(
                            controller,
                            target.getPortNumber(),
                            target.getDirection()
                    );
                    }
                }
        );

        //----------------------------------------------------------------------------
        // Sensors
        //----------------------------------------------------------------------------

        // Thunk the analog inputs
        createThunks(unthunkedHwmap.analogInput, thunkedHwmap.analogInput,
            new IThunkFactory<AnalogInput>()
                {
                @Override public AnalogInput create(AnalogInput target)
                    {
                    return new ThreadSafeAnalogInput(
                            ThunkedAnalogInputController.create(ThreadSafeAnalogInput.getController(target)),
                            ThreadSafeAnalogInput.getChannel(target)
                    );
                    }
                }
        );

        // Thunk the analog outputs
        createThunks(unthunkedHwmap.analogOutput, thunkedHwmap.analogOutput,
                new IThunkFactory<AnalogOutput>()
                {
                @Override public AnalogOutput create(AnalogOutput target)
                    {
                    return new ThreadSafeAnalogOutput(
                            ThunkedAnalogOutputController.create(ThreadSafeAnalogOutput.getController(target)),
                            ThreadSafeAnalogOutput.getChannel(target)
                    );
                    }
                }
        );

        // Thunk the pwm outputs
        createThunks(unthunkedHwmap.pwmOutput, thunkedHwmap.pwmOutput,
            new IThunkFactory<PWMOutput>()
                {
                @Override public PWMOutput create(PWMOutput target)
                    {
                    return new ThreadSafePWMOutput(
                            ThunkedPWMOutputController.create(ThreadSafePWMOutput.getController(target)),
                            ThreadSafePWMOutput.getChannel(target)
                    );
                    }
                }
        );

        // Thunk the i2c devices
        createThunks(unthunkedHwmap.i2cDevice, thunkedHwmap.i2cDevice,
            new IThunkFactory<I2cDevice>()
                {
                @Override public I2cDevice create(I2cDevice target)
                    {
                    return new ThreadSafeI2cDevice(
                            ThunkedI2cController.create(ThreadSafeI2cDevice.getController(target)),
                            ThreadSafeI2cDevice.getChannel(target)
                    );
                    }
                }
        );

        // Thunk the digital channels
        createThunks(unthunkedHwmap.digitalChannel, thunkedHwmap.digitalChannel,
            new IThunkFactory<DigitalChannel>()
                {
                @Override public DigitalChannel create(DigitalChannel target)
                    {
                    return new ThreadSafeDigitalChannel(
                            ThunkedDigitalChannelController.create(ThreadSafeDigitalChannel.getController(target)),
                            ThreadSafeDigitalChannel.getChannel(target)
                    );
                    }
                }
        );

        // Thunk the acceleration sensors
        createThunks(unthunkedHwmap.accelerationSensor, thunkedHwmap.accelerationSensor,
            new IThunkFactory<AccelerationSensor>()
                {
                @Override public AccelerationSensor create(AccelerationSensor target)
                    {
                    return ThunkedAccelerationSensor.create(target);
                    }
                }
        );

        // Thunk the compass sensors
        createThunks(unthunkedHwmap.compassSensor, thunkedHwmap.compassSensor,
            new IThunkFactory<CompassSensor>()
                {
                @Override public CompassSensor create(CompassSensor target)
                    {
                    return ThunkedCompassSensor.create(target);
                    }
                }
        );

        // Thunk the gyro sensors
        createThunks(unthunkedHwmap.gyroSensor, thunkedHwmap.gyroSensor,
                new IThunkFactory<GyroSensor>()
                {
                @Override public GyroSensor create(GyroSensor target)
                    {
                    return ThunkedGyroSensor.create(target);
                    }
                }
        );

        // Thunk the IR seekers
        createThunks(unthunkedHwmap.irSeekerSensor, thunkedHwmap.irSeekerSensor,
                new IThunkFactory<IrSeekerSensor>()
                {
                @Override public IrSeekerSensor create(IrSeekerSensor target)
                    {
                    return ThunkedIrSeekerSensor.create(target);
                    }
                }
        );

        // Thunk the light sensors
        createThunks(unthunkedHwmap.lightSensor, thunkedHwmap.lightSensor,
                new IThunkFactory<LightSensor>()
                {
                @Override public LightSensor create(LightSensor target)
                    {
                    return ThunkedLightSensor.create(target);
                    }
                }
        );

        // Thunk the optical distance sensors
        createThunks(unthunkedHwmap.opticalDistanceSensor, thunkedHwmap.opticalDistanceSensor,
                new IThunkFactory<OpticalDistanceSensor>()
                {
                @Override public OpticalDistanceSensor create(OpticalDistanceSensor target)
                    {
                    return ThunkedOpticalDistanceSensor.create(target);
                    }
                }
        );

        // Thunk the touch sensors
        createThunks(unthunkedHwmap.touchSensor, thunkedHwmap.touchSensor,
                new IThunkFactory<TouchSensor>()
                {
                @Override public TouchSensor create(TouchSensor target)
                    {
                    return ThunkedTouchSensor.create(target);
                    }
                }
        );

        // Thunk the ultrasonic sensors
        createThunks(unthunkedHwmap.ultrasonicSensor, thunkedHwmap.ultrasonicSensor,
                new IThunkFactory<UltrasonicSensor>()
                {
                @Override public UltrasonicSensor create(UltrasonicSensor target)
                    {
                    return ThunkedUltrasonicSensor.create(target);
                    }
                }
        );

        // Paranoia
        removeSwerveVoltageSensor();

        // Thunk the voltage sensors
                createThunks(unthunkedHwmap.voltageSensor, thunkedHwmap.voltageSensor,
                new IThunkFactory<VoltageSensor>()
                {
                @Override public VoltageSensor create(VoltageSensor target)
                    {
                    return ThunkedVoltageSensor.create(target);
                    }
                }
        );

        // Thunk the color sensors
        createThunks(unthunkedHwmap.colorSensor, thunkedHwmap.colorSensor,
                new IThunkFactory<ColorSensor>()
                {
                @Override public ColorSensor create(ColorSensor target)
                    {
                    return ThunkedColorSensor.create(target);
                    }
                }
        );

        // Thunk the LEDs
        createThunks(unthunkedHwmap.led, thunkedHwmap.led,
                new IThunkFactory<LED>()
                {
                @Override public LED create(LED target)
                    {
                    return ThunkedLED.create(target);
                    }
                }
        );

        // Thunk the TouchSensorMultiplexers
        createThunks(unthunkedHwmap.touchSensorMultiplexer , thunkedHwmap.touchSensorMultiplexer ,
                new IThunkFactory<TouchSensorMultiplexer>()
                {
                @Override public TouchSensorMultiplexer create(TouchSensorMultiplexer target)
                    {
                    return ThunkedTouchSensorMultiplexer.create(target);
                    }
                }
        );

        //----------------------------------------------------------------------------
        // Context
        //----------------------------------------------------------------------------

        // Carry over the app context
        thunkedHwmap.appContext = unthunkedHwmap.appContext;

        // If they haven't actually given us one (early versions of the runtime didn't actually set one),
        // then fill one in through the use of magic.
        if (thunkedHwmap.appContext == null)
            thunkedHwmap.appContext = AnnotatedOpModeRegistrar.getApplicationContext();

        //----------------------------------------------------------------------------
        // Voltage sensor
        //----------------------------------------------------------------------------

        // Are there any voltage sensors there in the map the robot controller runtime made?
        if (unthunkedHwmap.voltageSensor.size() == 0)
            {
            // No, there isn't. See if we can find one in our motor controller. It'll be one we
            // made if it's there, and those don't actually thunk. If we find one, stick it
            // in the *original* map as well as the new one, the former so that the UI will
            // now show voltage, the latter for consistency.
            for (DcMotorController controller : thunkedHwmap.dcMotorController)
                {
                if (controller instanceof VoltageSensor)
                    {
                    unthunkedHwmap.voltageSensor.put(swerveVoltageSensorName, (VoltageSensor)controller);
                      thunkedHwmap.voltageSensor.put(swerveVoltageSensorName, (VoltageSensor)controller);
                    break;
                    }
                }
            }

        return thunkedHwmap;
        }

    public void stop()
        {
        // Paranoia: don't let this little trick of ours outlive the life of our OpMode
        removeSwerveVoltageSensor();
        }

    private void removeSwerveVoltageSensor()
        {
        // Remove any VoltageSensor we made that somehow might be lingering around
        Util.<Map>getPrivateObjectField(unthunkedHwmap.voltageSensor,0).remove(this.swerveVoltageSensorName);
        Util.<Map>getPrivateObjectField(  thunkedHwmap.voltageSensor,0).remove(this.swerveVoltageSensorName);
        }

    private interface IThunkFactory<T>
        {
        T create(T t);
        }

    private <T> void createThunks(HardwareMap.DeviceMapping<T> from, HardwareMap.DeviceMapping<T> to, IThunkFactory<T> thunkFactory)
        {
        for (Map.Entry<String,T> pair : from.entrySet())
            {
            T thunked = thunkFactory.create(pair.getValue());
            to.put(pair.getKey(), thunked);
            }
        }

    // Find and return the wrapping of the indicated target with the wrapping map; if absent,
    // return the ifAbsent value.
    private <T> T findWrapper(HardwareMap.DeviceMapping<T> wrappingMap, T target, T ifAbsent)
        {
        for (Map.Entry<String,T> pair : wrappingMap.entrySet())
            {
            T thunked = pair.getValue();
            if (thunked instanceof IHardwareWrapper)
                {
                Object o = ((IHardwareWrapper<T>)thunked).getWrappedTarget();
                if (o == target)
                    return thunked;
                }
            }
        return ifAbsent;
        }


    //----------------------------------------------------------------------------------------------
    // Skullduggery 
    //----------------------------------------------------------------------------------------------

    public static DcMotorController createNxtMotorControllerOnI2cDevice(DcMotorController target, IStopActionRegistrar stopRegistrar)
        {
        if (isLegacyMotorController(target))
            {
            LegacyModule legacyModule = legacyModuleOfLegacyMotorController(target);
            int          port         = portOfLegacyMotorController(target);
            int          i2cAddr8Bit  = i2cAddrOfLegacyMotorController(target);

            // Disable the existing legacy motor controller
            legacyModule.deregisterForPortReadyCallback(port);

            // Make a new experimental legacy motor controller
            II2cDevice i2cDevice            = new I2cDeviceOnI2cDeviceController(legacyModule, port);
            I2cDeviceClient i2cDeviceClient = new I2cDeviceClient(i2cDevice, i2cAddr8Bit, /*autostop*/true, stopRegistrar);
            DcMotorController controller    = new NxtDcMotorControllerOnI2cDevice(i2cDeviceClient, target);

            // Use that one instead
            return controller;
            }
        else
            {
            return null;
            }
        }

    private static boolean isLegacyMotorController(DcMotorController controller)
        {
        return controller instanceof com.qualcomm.hardware.ModernRoboticsNxtDcMotorController;
        }

    private static LegacyModule legacyModuleOfLegacyMotorController(DcMotorController controller)
        {
        return Util.<LegacyModule>getPrivateObjectField(controller, 0);
        }

    private static int portOfLegacyMotorController(DcMotorController controller)
        {
        return Util.getPrivateIntField(controller, 5);
        }
    
    private static int i2cAddrOfLegacyMotorController(DcMotorController controller)
        {
        // From the spec from HiTechnic:
        //
        // "The first motor controller in the daisy chain will use an I2C address of 02/03. Subsequent 
        // controllers will obtain addresses of 04/05, 06/07 and 08/09. Only four controllers may be 
        // daisy chained."
        //
        // The legacy module appears not to support daisy chaining; it only supports the first
        // address. Note that these are clearly 8-bit addresses, not 7-bit. 
        //
        return 0x02;
        }

    }
