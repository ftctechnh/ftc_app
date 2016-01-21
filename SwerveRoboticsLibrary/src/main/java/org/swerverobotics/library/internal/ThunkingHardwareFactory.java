package org.swerverobotics.library.internal;

import com.qualcomm.hardware.hitechnic.*;
import com.qualcomm.hardware.modernrobotics.*;
import com.qualcomm.robotcore.eventloop.opmode.*;
import com.qualcomm.robotcore.hardware.*;
import org.swerverobotics.library.*;
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

    OpMode               context;
    HardwareMap          unthunkedHwmap;
    HardwareMap          thunkedHwmap;
    boolean              useExperimental;

    //----------------------------------------------------------------------------------------------
    // Construction
    //----------------------------------------------------------------------------------------------

    public ThunkingHardwareFactory(OpMode context, boolean useExperimental)
        {
        this.context            = context;
        this.thunkedHwmap       = null;
        this.unthunkedHwmap     = context.hardwareMap;
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
        // Swapping in new motor controller implementations in place of existing ones
        //----------------------------------------------------------------------------

        // Group the motors and their controller together
        Map<DcMotorController, List<DcMotor>> motors = new HashMap<DcMotorController, List<DcMotor>>();
        for (DcMotor motor : this.unthunkedHwmap.dcMotor)
            {
            if (motors.containsKey(motor.getController()))
                motors.get(motor.getController()).add(motor);
            else
                {
                List<DcMotor> list = new LinkedList<DcMotor>();
                list.add(motor);
                motors.put(motor.getController(), list);
                }
            }

        // Switcheroo the motor controllers
        for (DcMotorController controller : motors.keySet())
            {
            if (MemberUtil.isLegacyMotorController(controller) || MemberUtil.isModernMotorController(controller))
                {
                DcMotor motor1 = motors.get(controller).get(0);
                DcMotor motor2 = motors.get(controller).size() > 1 ? motors.get(controller).get(1) : null;
                ClassFactory.createEasyMotorController(this.context, motor1, motor2);
                }
            }

        //----------------------------------------------------------------------------
        // Now the same for the servo controllers
        //----------------------------------------------------------------------------

        Map<ServoController, List<Servo>> servos = new HashMap<ServoController, List<Servo>>();
        for (Servo servo : this.unthunkedHwmap.servo)
            {
            if (servos.containsKey(servo.getController()))
                servos.get(servo.getController()).add(servo);
            else
                {
                List<Servo> list = new LinkedList<Servo>();
                list.add(servo);
                servos.put(servo.getController(), list);
                }
            }

        for (ServoController controller : servos.keySet())
            {
            if (MemberUtil.isModernServoController(controller) || MemberUtil.isLegacyServoController(controller))
                {
                Collection<Servo> thisControllersServos = servos.get(controller);
                ClassFactory.createEasyServoController(this.context, thisControllersServos);
                }
            }

        //----------------------------------------------------------------------------
        // Controllers
        //----------------------------------------------------------------------------

        // Thunk the motor controllers
        createThunks(unthunkedHwmap.dcMotorController, thunkedHwmap.dcMotorController,
            new IThunkFactory<DcMotorController>()
                {
                @Override public DcMotorController create(DcMotorController target)
                    {
                    if (target instanceof EasyLegacyMotorController)
                        return target;

                    if (target instanceof EasyModernMotorController)
                        return target;

                    // Put a wrapping of the unthunked target in the thunked map
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
                    if (target instanceof EasyLegacyServoController)
                        return target;

                    if (target instanceof EasyModernServoController)
                        return target;

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

                    return new EasyServo(
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

        // Thunk the voltage sensors
        createThunks(unthunkedHwmap.voltageSensor, thunkedHwmap.voltageSensor,
                new IThunkFactory<VoltageSensor>()
                {
                @Override public VoltageSensor create(VoltageSensor target)
                    {
                    return (target instanceof EasyLegacyMotorController) ? target : ThunkedVoltageSensor.create(target);
                    }
                }
        );

        // Thunk or reimplement the color sensors
        // NOTE: Use of Swerve color sensor implementation is currently disabled pending testing
        createThunks(unthunkedHwmap.colorSensor, thunkedHwmap.colorSensor,
                new IThunkFactory<ColorSensor>()
                {
                @Override public ColorSensor create(ColorSensor target)
                    {
                    if (target instanceof LegacyOrModernColorSensor)
                        {
                        return target;
                        }
                    else if (target instanceof HiTechnicNxtColorSensor || target instanceof ModernRoboticsI2cColorSensor)
                        {
                        return ClassFactory.createSwerveColorSensor(context, target);
                        }
                    else
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

        return thunkedHwmap;
        }

    public void stop()
        {
        // Nothing to do, these days
        }
        
    private interface IThunkFactory<T>
        {
        T create(T t);
        }

    private <T> void createThunks(HardwareMap.DeviceMapping<T> from, HardwareMap.DeviceMapping<T> to, IThunkFactory<T> thunkFactory)
        {
        // Get a copy of things first so as to avoid concurrent modification exceptions
        // if the call to create() below happens to modify the map.
        //
        Set<Map.Entry<String,T>> set = new HashSet<Map.Entry<String,T>>();
        for (Map.Entry<String,T> pair : from.entrySet())
            set.add(pair);
        //
        for (Map.Entry<String,T> pair : set)
            {
            T thunked = thunkFactory.create(pair.getValue());
            if (thunked != null)
                {
                to.put(pair.getKey(), thunked);
                }
            }
        }

    // Find and return the wrapping of the indicated target with the wrapping map; if absent,
    // return the ifAbsent value.
    private <T> T findWrapper(HardwareMap.DeviceMapping<T> wrappingMap, T target, T ifAbsent)
        {
        for (Map.Entry<String,T> pair : wrappingMap.entrySet())
            {
            T wrapper = pair.getValue();

            if (wrapper == target)
                return wrapper;     // likely an EasyLegacyMotorController

            if (wrapper instanceof IHardwareWrapper)
                {
                Object o = ((IHardwareWrapper<T>) wrapper).getWrappedTarget();
                if (o == target)
                    return wrapper;
                }
            }
        return ifAbsent;
        }

    }
