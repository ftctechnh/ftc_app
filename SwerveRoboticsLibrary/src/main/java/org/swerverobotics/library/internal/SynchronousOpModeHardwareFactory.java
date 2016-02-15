package org.swerverobotics.library.internal;

import com.qualcomm.robotcore.eventloop.opmode.*;
import com.qualcomm.robotcore.hardware.*;

import org.swerverobotics.library.interfaces.*;

import java.util.*;

/**
 * SynchronousOpModeHardwareFactory processes an SDK-provided hardware map so as to allow
 * SynchronousOpMode to function better.
 */
public class SynchronousOpModeHardwareFactory
    {
    //----------------------------------------------------------------------------------------------
    // State 
    //----------------------------------------------------------------------------------------------

    OpMode               opmodeContext;
    HardwareMap          unprocessedMap;
    HardwareMap          processedMap;
    boolean              useExperimental;

    //----------------------------------------------------------------------------------------------
    // Construction
    //----------------------------------------------------------------------------------------------

    public SynchronousOpModeHardwareFactory(OpMode opmodeContext, boolean useExperimental)
        {
        this.opmodeContext      = opmodeContext;
        this.processedMap       = null;
        this.unprocessedMap     = opmodeContext.hardwareMap;
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
        this.processedMap = new HardwareMap(this.opmodeContext.hardwareMap.appContext);

        //----------------------------------------------------------------------------
        // Modules
        //----------------------------------------------------------------------------

        // Process the legacy modules
        processHardwareMapping(unprocessedMap.legacyModule, processedMap.legacyModule,
                new IFactory<LegacyModule>()
                {
                @Override
                public LegacyModule create(LegacyModule target)
                    {
                    return target;
                    }
                }
        );

        // Process the core device interface modules
        processHardwareMapping(unprocessedMap.deviceInterfaceModule, processedMap.deviceInterfaceModule,
                new IFactory<DeviceInterfaceModule>()
                {
                @Override
                public DeviceInterfaceModule create(DeviceInterfaceModule target)
                    {
                    return target;
                    }
                }
        );

        //----------------------------------------------------------------------------
        // Controllers
        //----------------------------------------------------------------------------

        // Process the motor controllers
        processHardwareMapping(unprocessedMap.dcMotorController, processedMap.dcMotorController,
                new IFactory<DcMotorController>()
                {
                @Override
                public DcMotorController create(DcMotorController target)
                    {
                    return target;
                    }
                }
        );

        // Process the servo controllers
        processHardwareMapping(unprocessedMap.servoController, processedMap.servoController,
                new IFactory<ServoController>()
                {
                @Override
                public ServoController create(ServoController target)
                    {
                    return target;
                    }
                }
        );

        //----------------------------------------------------------------------------
        // Actuators
        //----------------------------------------------------------------------------
        
        // Process the DC motors
        processHardwareMapping(unprocessedMap.dcMotor, processedMap.dcMotor,
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
        processHardwareMapping(unprocessedMap.servo, processedMap.servo,
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

        // Process the analog inputs
        processHardwareMapping(unprocessedMap.analogInput, processedMap.analogInput,
                new IFactory<AnalogInput>()
                {
                @Override
                public AnalogInput create(AnalogInput target)
                    {
                    return target;
                    }
                }
        );

        // Process the analog outputs
        processHardwareMapping(unprocessedMap.analogOutput, processedMap.analogOutput,
                new IFactory<AnalogOutput>()
                {
                @Override
                public AnalogOutput create(AnalogOutput target)
                    {
                    return target;
                    }
                }
        );

        // Process the pwm outputs
        processHardwareMapping(unprocessedMap.pwmOutput, processedMap.pwmOutput,
                new IFactory<PWMOutput>()
                {
                @Override
                public PWMOutput create(PWMOutput target)
                    {
                    return target;
                    }
                }
        );

        // Process the i2c devices
        processHardwareMapping(unprocessedMap.i2cDevice, processedMap.i2cDevice,
                new IFactory<I2cDevice>()
                {
                @Override
                public I2cDevice create(I2cDevice target)
                    {
                    return target;
                    }
                }
        );

        // Process the digital channels
        processHardwareMapping(unprocessedMap.digitalChannel, processedMap.digitalChannel,
                new IFactory<DigitalChannel>()
                {
                @Override
                public DigitalChannel create(DigitalChannel target)
                    {
                    return target;
                    }
                }
        );

        // Process the acceleration sensors
        processHardwareMapping(unprocessedMap.accelerationSensor, processedMap.accelerationSensor,
                new IFactory<AccelerationSensor>()
                {
                @Override
                public AccelerationSensor create(AccelerationSensor target)
                    {
                    return target;
                    }
                }
        );

        // Process the compass sensors
        processHardwareMapping(unprocessedMap.compassSensor, processedMap.compassSensor,
                new IFactory<CompassSensor>()
                {
                @Override
                public CompassSensor create(CompassSensor target)
                    {
                    return target;
                    }
                }
        );

        // Process the gyro sensors
        processHardwareMapping(unprocessedMap.gyroSensor, processedMap.gyroSensor,
                new IFactory<GyroSensor>()
                {
                @Override
                public GyroSensor create(GyroSensor target)
                    {
                    return target;
                    }
                }
        );

        // Process the IR seekers
        processHardwareMapping(unprocessedMap.irSeekerSensor, processedMap.irSeekerSensor,
                new IFactory<IrSeekerSensor>()
                {
                @Override
                public IrSeekerSensor create(IrSeekerSensor target)
                    {
                    return target;
                    }
                }
        );

        // Process the light sensors
        processHardwareMapping(unprocessedMap.lightSensor, processedMap.lightSensor,
                new IFactory<LightSensor>()
                {
                @Override
                public LightSensor create(LightSensor target)
                    {
                    return target;
                    }
                }
        );

        // Process the optical distance sensors
        processHardwareMapping(unprocessedMap.opticalDistanceSensor, processedMap.opticalDistanceSensor,
                new IFactory<OpticalDistanceSensor>()
                {
                @Override
                public OpticalDistanceSensor create(OpticalDistanceSensor target)
                    {
                    return target;
                    }
                }
        );

        // Process the touch sensors
        processHardwareMapping(unprocessedMap.touchSensor, processedMap.touchSensor,
                new IFactory<TouchSensor>()
                {
                @Override
                public TouchSensor create(TouchSensor target)
                    {
                    return target;
                    }
                }
        );

        // Process the ultrasonic sensors
        processHardwareMapping(unprocessedMap.ultrasonicSensor, processedMap.ultrasonicSensor,
                new IFactory<UltrasonicSensor>()
                {
                @Override
                public UltrasonicSensor create(UltrasonicSensor target)
                    {
                    return target;
                    }
                }
        );

        // Process the voltage sensors
        processHardwareMapping(unprocessedMap.voltageSensor, processedMap.voltageSensor,
                new IFactory<VoltageSensor>()
                {
                @Override
                public VoltageSensor create(VoltageSensor target)
                    {
                    return target;
                    }
                }
        );

        // Process the color sensors
        processHardwareMapping(unprocessedMap.colorSensor, processedMap.colorSensor,
                new IFactory<ColorSensor>()
                {
                @Override
                public ColorSensor create(ColorSensor target)
                    {
                    return target;
                    }
                }
        );

        // Process the LEDs
        processHardwareMapping(unprocessedMap.led, processedMap.led,
                new IFactory<LED>()
                {
                @Override
                public LED create(LED target)
                    {
                    return target;
                    }
                }
        );

        // Process the TouchSensorMultiplexers
        processHardwareMapping(unprocessedMap.touchSensorMultiplexer, processedMap.touchSensorMultiplexer,
                new IFactory<TouchSensorMultiplexer>()
                {
                @Override
                public TouchSensorMultiplexer create(TouchSensorMultiplexer target)
                    {
                    return target;
                    }
                }
        );

        return processedMap;
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
