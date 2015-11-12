package org.swerverobotics.library.internal;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotorController;
import com.qualcomm.robotcore.hardware.DeviceInterfaceModule;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.I2cController;
import com.qualcomm.robotcore.hardware.LegacyModule;

import org.swerverobotics.library.exceptions.RuntimeInterruptedException;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * A helper object that assists in managing the creation of replacement
 * hardware implementations for I2C-based objects.
 */
public class I2cDeviceReplacementHelper<TARGET>
    {
    //----------------------------------------------------------------------------------------------
    // State
    //----------------------------------------------------------------------------------------------

    private OpMode                              context;
    private TARGET                              client;
    private TARGET                              target;
    private String                              targetName;
    private HardwareMap.DeviceMapping           targetDeviceMapping;
    private I2cController                       controller;
    private int                                 targetPort;
    private I2cController.I2cPortReadyCallback  targetCallback;
    private boolean                             isArmed;

    //----------------------------------------------------------------------------------------------
    // Construction
    //----------------------------------------------------------------------------------------------

    public I2cDeviceReplacementHelper(OpMode context, TARGET client, /* may be null */ TARGET target, I2cController controller, int targetPort)
        {
        this.context        = context;
        this.isArmed        = false;
        this.client         = client;
        this.target         = target;       // may be null
        this.controller     = controller;
        this.targetPort     = targetPort;

        this.targetName     = null;
        this.targetDeviceMapping = null;
        if (this.target != null)
            findTargetNameAndMapping();

        if (controller instanceof LegacyModule)
            {
            this.targetCallback = MemberUtil.callbacksOfLegacyModule((LegacyModule)controller)[targetPort];
            }
        else if (controller instanceof DeviceInterfaceModule)
            {
            this.targetCallback = MemberUtil.callbacksOfDeviceInterfaceModule((DeviceInterfaceModule)controller)[targetPort];
            }
        else
            throw new IllegalArgumentException(String.format("unknown controller flavor: %s", controller.getClass().getSimpleName()));
        }

    //----------------------------------------------------------------------------------------------
    // Operations
    //----------------------------------------------------------------------------------------------

    boolean isArmed()
        {
        return this.isArmed;
        }

    void arm()
        {
        if (!this.isArmed)
            {
            // Have the existing controller stop using the callback
            this.controller.deregisterForPortReadyCallback(this.targetPort);

            // Put ourselves in the hardware map
            if (this.targetName != null) this.targetDeviceMapping.put(this.targetName, this.client);
            this.isArmed = true;
            }
        }

    void disarm()
        {
        if (this.isArmed)
            {
            this.isArmed = false;

            // Put the original guy back in the hardware map
            if (this.targetName != null) this.targetDeviceMapping.put(this.targetName, this.target);

            // Start up the original controller again
            this.controller.registerForI2cPortReadyCallback(this.targetCallback, this.targetPort);
            }
        }

    //----------------------------------------------------------------------------------------------
    // Utility
    //----------------------------------------------------------------------------------------------

    private void findTargetNameAndMapping()
        {
        for (HardwareMap.DeviceMapping<?> mapping : deviceMappings(this.context.hardwareMap))
            {
            for (Map.Entry<String,?> pair : mapping.entrySet())
                {
                if (pair.getValue() == this.target)
                    {
                    this.targetName = pair.getKey();
                    this.targetDeviceMapping = mapping;
                    return;
                    }
                }
            }
        }

    static List<HardwareMap.DeviceMapping<?>> deviceMappings(HardwareMap map)
    // Returns all the device mappings within the map
        {
        List<HardwareMap.DeviceMapping<?>> result = new LinkedList<HardwareMap.DeviceMapping<?>>();
        result.add(map.dcMotorController);
        result.add(map.servoController);
        result.add(map.legacyModule);
        result.add(map.deviceInterfaceModule);
        result.add(map.colorSensor);
        result.add(map.dcMotor);
        result.add(map.gyroSensor);
        result.add(map.servo);
        result.add(map.analogInput);
        result.add(map.digitalChannel);
        result.add(map.opticalDistanceSensor);
        result.add(map.touchSensor);
        result.add(map.pwmOutput);
        result.add(map.i2cDevice);
        result.add(map.analogOutput);
        result.add(map.led);
        result.add(map.accelerationSensor);
        result.add(map.compassSensor);
        result.add(map.irSeekerSensor);
        result.add(map.lightSensor);
        result.add(map.ultrasonicSensor);
        result.add(map.voltageSensor);
        result.add(map.touchSensorMultiplexer);
        return result;
        }
    }
