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
        for (HardwareMap.DeviceMapping<?> mapping : Util.deviceMappings(this.context.hardwareMap))
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
    }
