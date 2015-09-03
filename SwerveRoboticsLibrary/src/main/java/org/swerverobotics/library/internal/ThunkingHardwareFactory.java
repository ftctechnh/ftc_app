package org.swerverobotics.library.internal;

import com.qualcomm.robotcore.hardware.*;

import org.swerverobotics.library.interfaces.IHardwareWrapper;

import java.util.*;

/**
 * ThunkingHardwareFactory creates the wrappers needed to make SynchronousOpMode function correctly. 
 */
public class ThunkingHardwareFactory
    {
    //----------------------------------------------------------------------------------------------
    // State 
    //----------------------------------------------------------------------------------------------
    
    boolean useExperimental;
    
    //----------------------------------------------------------------------------------------------
    // Construction
    //----------------------------------------------------------------------------------------------

    public ThunkingHardwareFactory(boolean useExperimental)
        {
        this.useExperimental = useExperimental;
        }
    
    //----------------------------------------------------------------------------------------------
    // Operations 
    //----------------------------------------------------------------------------------------------
    
    /**
     * Rare: Given a (non-internal) hardware map, create a new hardware map containing
     * all the same devices but in a form that their methods thunk from the main()
     * thread to the loop() thread.
     */
    public final HardwareMap createThunkedHardwareMap(HardwareMap hwmap)
        {
        final HardwareMap result = new HardwareMap();

        //----------------------------------------------------------------------------
        // Modules
        //----------------------------------------------------------------------------

        // Thunk the legacy modules
        createThunks(hwmap.legacyModule, result.legacyModule,
            new IThunkFactory<LegacyModule>()
                {
                @Override public LegacyModule create(LegacyModule target)
                    {
                    return ThunkedLegacyModule.create(target);
                    }
                }
        );

        // Thunk the core device interface modules
        createThunks(hwmap.deviceInterfaceModule, result.deviceInterfaceModule,
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
        createThunks(hwmap.dcMotorController, result.dcMotorController,
            new IThunkFactory<DcMotorController>()
                {
                @Override public DcMotorController create(DcMotorController target)
                    {
                    if (useExperimental)
                        {
                        if (isLegacyMotorController(target))
                            {
                            LegacyModule legacyModule = legacyModuleOfLegacyMotorController(target);
                            int          port         = portOfLegacyMotorController(target);
                            int          i2cAddr8Bit  = i2cAddrOfLegacyMotorController(target);
                            
                            // Disable the existing legacy motor controller
                            legacyModule.deregisterForPortReadyCallback(port);
                            
                            // Make a new experimental legacy motor controller
                            II2cDevice i2cDevice            = new I2cDeviceOnLegacyModule(legacyModule, port);
                            I2cDeviceClient i2cDeviceClient = new I2cDeviceClient(i2cDevice, i2cAddr8Bit, null);
                            DcMotorController controller    = new LegacyDcMotorControllerOnI2cDevice(i2cDeviceClient, target);
                            
                            // Use that one instead
                            return controller;
                            }
                        }
                    
                    // Not experimental or not a legacy motor controller. Proceed as usual.
                    return ThunkedDCMotorController.create(target);
                    }
                }
        );

        // Thunk the servo controllers
        createThunks(hwmap.servoController, result.servoController,
            new IThunkFactory<ServoController>()
                {
                @Override public ServoController create(ServoController target)
                    {
                    return ThunkedServoController.create(target);
                    }
                }
        );

        //----------------------------------------------------------------------------
        // Devices
        //----------------------------------------------------------------------------
        
        // Thunk the DC motors
        createThunks(hwmap.dcMotor, result.dcMotor,
            new IThunkFactory<DcMotor>()
                {
                @Override public DcMotor create(DcMotor target)
                    {
                    DcMotorController targetController = target.getController();
                    DcMotorController controller = findWrapper(result.dcMotorController, targetController, ThunkedDCMotorController.create(targetController));
                    
                    return new ThreadSafeDcMotor(
                            controller,
                            target.getPortNumber(),
                            target.getDirection()
                    );
                    }
                }
        );

        // Thunk the servos
        createThunks(hwmap.servo, result.servo,
            new IThunkFactory<Servo>()
                {
                @Override public Servo create(Servo target)
                    {
                    ServoController targetController = target.getController();
                    ServoController controller = findWrapper(result.servoController, targetController, ThunkedServoController.create(targetController));

                    return new ThreadSafeServo(
                            controller,
                            target.getPortNumber(),
                            target.getDirection()
                    );
                    }
                }
        );

        // Thunk the analog inputs
        createThunks(hwmap.analogInput, result.analogInput,
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
        createThunks(hwmap.analogOutput, result.analogOutput,
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
        createThunks(hwmap.pwmOutput, result.pwmOutput,
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
        createThunks(hwmap.i2cDevice, result.i2cDevice,
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
        createThunks(hwmap.digitalChannel, result.digitalChannel,
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
        createThunks(hwmap.accelerationSensor, result.accelerationSensor,
            new IThunkFactory<AccelerationSensor>()
                {
                @Override public AccelerationSensor create(AccelerationSensor target)
                    {
                    return ThunkedAccelerationSensor.create(target);
                    }
                }
        );

        // Thunk the compass sensors
        createThunks(hwmap.compassSensor, result.compassSensor,
            new IThunkFactory<CompassSensor>()
                {
                @Override public CompassSensor create(CompassSensor target)
                    {
                    return ThunkedCompassSensor.create(target);
                    }
                }
        );

        // Thunk the gyro sensors
        createThunks(hwmap.gyroSensor, result.gyroSensor,
                new IThunkFactory<GyroSensor>()
                {
                @Override public GyroSensor create(GyroSensor target)
                    {
                    return ThunkedGyroSensor.create(target);
                    }
                }
        );

        // Thunk the IR seekers
        createThunks(hwmap.irSeekerSensor, result.irSeekerSensor,
                new IThunkFactory<IrSeekerSensor>()
                {
                @Override public IrSeekerSensor create(IrSeekerSensor target)
                    {
                    return ThunkedIrSeekerSensor.create(target);
                    }
                }
        );

        // Thunk the light sensors
        createThunks(hwmap.lightSensor, result.lightSensor,
                new IThunkFactory<LightSensor>()
                {
                @Override public LightSensor create(LightSensor target)
                    {
                    return ThunkedLightSensor.create(target);
                    }
                }
        );

        // Thunk the optical distance sensors
        createThunks(hwmap.opticalDistanceSensor, result.opticalDistanceSensor,
                new IThunkFactory<OpticalDistanceSensor>()
                {
                @Override public OpticalDistanceSensor create(OpticalDistanceSensor target)
                    {
                    return ThunkedOpticalDistanceSensor.create(target);
                    }
                }
        );

        // Thunk the touch sensors
        createThunks(hwmap.touchSensor, result.touchSensor,
                new IThunkFactory<TouchSensor>()
                {
                @Override public TouchSensor create(TouchSensor target)
                    {
                    return ThunkedTouchSensor.create(target);
                    }
                }
        );

        // Thunk the ultrasonic sensors
        createThunks(hwmap.ultrasonicSensor, result.ultrasonicSensor,
                new IThunkFactory<UltrasonicSensor>()
                {
                @Override public UltrasonicSensor create(UltrasonicSensor target)
                    {
                    return ThunkedUltrasonicSensor.create(target);
                    }
                }
        );

        // Thunk the voltage sensors
        createThunks(hwmap.voltageSensor, result.voltageSensor,
                new IThunkFactory<VoltageSensor>()
                {
                @Override public VoltageSensor create(VoltageSensor target)
                    {
                    return ThunkedVoltageSensor.create(target);
                    }
                }
        );

        result.appContext = hwmap.appContext;

        return result;
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

    private <T> T findWrapper(HardwareMap.DeviceMapping<T> mapping, T target, T ifAbsent)
        {
        for (Map.Entry<String,T> pair : mapping.entrySet())
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
    
    private boolean isLegacyMotorController(DcMotorController controller)
        {
        return controller instanceof LegacyModule.PortReadyCallback;
        }

    private LegacyModule legacyModuleOfLegacyMotorController(DcMotorController controller)
        {
        return Util.<LegacyModule>getPrivateObjectField(controller, 0);
        }

    private int portOfLegacyMotorController(DcMotorController controller)
        {
        return Util.getPrivateIntField(controller, 5);
        }
    
    private int i2cAddrOfLegacyMotorController(DcMotorController controller)
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
