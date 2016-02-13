package org.swerverobotics.library.internal;

import com.qualcomm.hardware.hitechnic.*;
import com.qualcomm.hardware.modernrobotics.*;
import com.qualcomm.robotcore.eventloop.opmode.*;
import com.qualcomm.robotcore.hardware.*;
import org.swerverobotics.library.*;
import org.swerverobotics.library.interfaces.*;

import java.util.*;

/**
 * SynchronousOpModeHardwareFactory processes an SDK-provided hardware map so as to allow
 * SynchronousOpMode to function correctly.
 */
public class SynchronousOpModeHardwareFactory
    {
    //----------------------------------------------------------------------------------------------
    // State 
    //----------------------------------------------------------------------------------------------

    OpMode               opmodeContext;
    HardwareMap          unthunkedHwmap;
    HardwareMap          thunkedHwmap;
    boolean              useExperimental;

    //----------------------------------------------------------------------------------------------
    // Construction
    //----------------------------------------------------------------------------------------------

    public SynchronousOpModeHardwareFactory(OpMode opmodeContext, boolean useExperimental)
        {
        this.opmodeContext      = opmodeContext;
        this.thunkedHwmap       = null;
        this.unthunkedHwmap     = opmodeContext.hardwareMap;
        this.useExperimental    = useExperimental;
        }
    
    //----------------------------------------------------------------------------------------------
    // Operations 
    //----------------------------------------------------------------------------------------------

    /**
     * Given a (non-internal) hardware map, create a new hardware map containing
     * all the same devices but in a the processed form needed by SynchronousOpMode.
     */
    public final HardwareMap createProcessedHardwareMap()
        {
        this.thunkedHwmap = new HardwareMap(this.opmodeContext.hardwareMap.appContext);

        //----------------------------------------------------------------------------
        // Modules
        //----------------------------------------------------------------------------

        // Process the legacy modules
        processHardwareMapping(unthunkedHwmap.legacyModule, thunkedHwmap.legacyModule,
                new IFactory<LegacyModule>()
                {
                @Override
                public LegacyModule create(LegacyModule target)
                    {
                    return ThunkedLegacyModule.create(target);
                    }
                }
        );

        // Process the core device interface modules
        processHardwareMapping(unthunkedHwmap.deviceInterfaceModule, thunkedHwmap.deviceInterfaceModule,
                new IFactory<DeviceInterfaceModule>()
                {
                @Override
                public DeviceInterfaceModule create(DeviceInterfaceModule target)
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
            if (MemberUtil.isModernMotorController(controller))
                {
                DcMotor motor1 = motors.get(controller).get(0);
                DcMotor motor2 = motors.get(controller).size() > 1 ? motors.get(controller).get(1) : null;
                ClassFactory.createEasyMotorController(this.opmodeContext, motor1, motor2);
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
            if (MemberUtil.isModernServoController(controller))
                {
                Collection<Servo> thisControllersServos = servos.get(controller);
                ClassFactory.createEasyServoController(this.opmodeContext, thisControllersServos);
                }
            }

        //----------------------------------------------------------------------------
        // Controllers
        //----------------------------------------------------------------------------

        // Process the motor controllers
        processHardwareMapping(unthunkedHwmap.dcMotorController, thunkedHwmap.dcMotorController,
                new IFactory<DcMotorController>()
                {
                @Override
                public DcMotorController create(DcMotorController target)
                    {
                    // All the implementations in the SDK are good
                    return target;
                    }
                }
        );

        // Process the servo controllers
        processHardwareMapping(unthunkedHwmap.servoController, thunkedHwmap.servoController,
                new IFactory<ServoController>()
                {
                @Override
                public ServoController create(ServoController target)
                    {
                    // All the implementations in the SDK are good
                    return target;
                    }
                }
        );

        //----------------------------------------------------------------------------
        // Actuators
        //----------------------------------------------------------------------------
        
        // Process the DC motors
        processHardwareMapping(unthunkedHwmap.dcMotor, thunkedHwmap.dcMotor,
                new IFactory<DcMotor>()
                {
                @Override
                public DcMotor create(DcMotor target)
                    {
                    return target;
                    }
                }
        );

        // Process the servos
        processHardwareMapping(unthunkedHwmap.servo, thunkedHwmap.servo,
                new IFactory<Servo>()
                {
                @Override
                public Servo create(Servo target)
                    {
                    return target;
                    }
                }
        );

        //----------------------------------------------------------------------------
        // Sensors
        //----------------------------------------------------------------------------

        // Thunk the analog inputs
        processHardwareMapping(unthunkedHwmap.analogInput, thunkedHwmap.analogInput,
                new IFactory<AnalogInput>()
                {
                @Override
                public AnalogInput create(AnalogInput target)
                    {
                    return new ThreadSafeAnalogInput(
                            ThunkedAnalogInputController.create(ThreadSafeAnalogInput.getController(target)),
                            ThreadSafeAnalogInput.getChannel(target)
                    );
                    }
                }
        );

        // Thunk the analog outputs
        processHardwareMapping(unthunkedHwmap.analogOutput, thunkedHwmap.analogOutput,
                new IFactory<AnalogOutput>()
                {
                @Override
                public AnalogOutput create(AnalogOutput target)
                    {
                    return new ThreadSafeAnalogOutput(
                            ThunkedAnalogOutputController.create(ThreadSafeAnalogOutput.getController(target)),
                            ThreadSafeAnalogOutput.getChannel(target)
                    );
                    }
                }
        );

        // Thunk the pwm outputs
        processHardwareMapping(unthunkedHwmap.pwmOutput, thunkedHwmap.pwmOutput,
                new IFactory<PWMOutput>()
                {
                @Override
                public PWMOutput create(PWMOutput target)
                    {
                    return new ThreadSafePWMOutput(
                            ThunkedPWMOutputController.create(ThreadSafePWMOutput.getController(target)),
                            ThreadSafePWMOutput.getChannel(target)
                    );
                    }
                }
        );

        // Thunk the i2c devices
        processHardwareMapping(unthunkedHwmap.i2cDevice, thunkedHwmap.i2cDevice,
                new IFactory<I2cDevice>()
                {
                @Override
                public I2cDevice create(I2cDevice target)
                    {
                    return target;
                    }
                }
        );

        // Thunk the digital channels
        processHardwareMapping(unthunkedHwmap.digitalChannel, thunkedHwmap.digitalChannel,
                new IFactory<DigitalChannel>()
                {
                @Override
                public DigitalChannel create(DigitalChannel target)
                    {
                    return new ThreadSafeDigitalChannel(
                            ThunkedDigitalChannelController.create(ThreadSafeDigitalChannel.getController(target)),
                            ThreadSafeDigitalChannel.getChannel(target)
                    );
                    }
                }
        );

        // Thunk the acceleration sensors
        processHardwareMapping(unthunkedHwmap.accelerationSensor, thunkedHwmap.accelerationSensor,
                new IFactory<AccelerationSensor>()
                {
                @Override
                public AccelerationSensor create(AccelerationSensor target)
                    {
                    return ThunkedAccelerationSensor.create(target);
                    }
                }
        );

        // Thunk the compass sensors
        processHardwareMapping(unthunkedHwmap.compassSensor, thunkedHwmap.compassSensor,
                new IFactory<CompassSensor>()
                {
                @Override
                public CompassSensor create(CompassSensor target)
                    {
                    return ThunkedCompassSensor.create(target);
                    }
                }
        );

        // Thunk the gyro sensors
        processHardwareMapping(unthunkedHwmap.gyroSensor, thunkedHwmap.gyroSensor,
                new IFactory<GyroSensor>()
                {
                @Override
                public GyroSensor create(GyroSensor target)
                    {
                    return ThunkedGyroSensor.create(target);
                    }
                }
        );

        // Thunk the IR seekers
        processHardwareMapping(unthunkedHwmap.irSeekerSensor, thunkedHwmap.irSeekerSensor,
                new IFactory<IrSeekerSensor>()
                {
                @Override
                public IrSeekerSensor create(IrSeekerSensor target)
                    {
                    return ThunkedIrSeekerSensor.create(target);
                    }
                }
        );

        // Thunk the light sensors
        processHardwareMapping(unthunkedHwmap.lightSensor, thunkedHwmap.lightSensor,
                new IFactory<LightSensor>()
                {
                @Override
                public LightSensor create(LightSensor target)
                    {
                    return ThunkedLightSensor.create(target);
                    }
                }
        );

        // Thunk the optical distance sensors
        processHardwareMapping(unthunkedHwmap.opticalDistanceSensor, thunkedHwmap.opticalDistanceSensor,
                new IFactory<OpticalDistanceSensor>()
                {
                @Override
                public OpticalDistanceSensor create(OpticalDistanceSensor target)
                    {
                    return ThunkedOpticalDistanceSensor.create(target);
                    }
                }
        );

        // Thunk the touch sensors
        processHardwareMapping(unthunkedHwmap.touchSensor, thunkedHwmap.touchSensor,
                new IFactory<TouchSensor>()
                {
                @Override
                public TouchSensor create(TouchSensor target)
                    {
                    return ThunkedTouchSensor.create(target);
                    }
                }
        );

        // Thunk the ultrasonic sensors
        processHardwareMapping(unthunkedHwmap.ultrasonicSensor, thunkedHwmap.ultrasonicSensor,
                new IFactory<UltrasonicSensor>()
                {
                @Override
                public UltrasonicSensor create(UltrasonicSensor target)
                    {
                    return ThunkedUltrasonicSensor.create(target);
                    }
                }
        );

        // Copy the voltage sensors
        processHardwareMapping(unthunkedHwmap.voltageSensor, thunkedHwmap.voltageSensor,
                new IFactory<VoltageSensor>()
                {
                @Override
                public VoltageSensor create(VoltageSensor target)
                    {
                    return target;
                    }
                }
        );

        // Thunk or reimplement the color sensors
        // NOTE: Use of Swerve color sensor implementation is currently disabled pending testing
        processHardwareMapping(unthunkedHwmap.colorSensor, thunkedHwmap.colorSensor,
                new IFactory<ColorSensor>()
                {
                @Override
                public ColorSensor create(ColorSensor target)
                    {
                    if (target instanceof LegacyOrModernColorSensor)
                        {
                        return target;
                        }
                    else if (target instanceof HiTechnicNxtColorSensor || target instanceof ModernRoboticsI2cColorSensor)
                        {
                        return ClassFactory.createSwerveColorSensor(opmodeContext, target);
                        }
                    else
                        return ThunkedColorSensor.create(target);
                    }
                }
        );

        // Thunk the LEDs
        processHardwareMapping(unthunkedHwmap.led, thunkedHwmap.led,
                new IFactory<LED>()
                {
                @Override
                public LED create(LED target)
                    {
                    return ThunkedLED.create(target);
                    }
                }
        );

        // Thunk the TouchSensorMultiplexers
        processHardwareMapping(unthunkedHwmap.touchSensorMultiplexer, thunkedHwmap.touchSensorMultiplexer,
                new IFactory<TouchSensorMultiplexer>()
                {
                @Override
                public TouchSensorMultiplexer create(TouchSensorMultiplexer target)
                    {
                    return ThunkedTouchSensorMultiplexer.create(target);
                    }
                }
        );

        return thunkedHwmap;
        }

    public void stop()
        {
        // Nothing to do, these days
        }
        
    private interface IFactory<T>
        {
        T create(T t);
        }

    private <T> void processHardwareMapping(HardwareMap.DeviceMapping<T> from, HardwareMap.DeviceMapping<T> to, IFactory<T> factory)
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
            T thunked = factory.create(pair.getValue());
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
