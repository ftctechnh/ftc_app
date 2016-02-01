package org.swerverobotics.library.internal;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.exception.RobotCoreException;
import com.qualcomm.robotcore.hardware.DeviceInterfaceModule;
import com.qualcomm.robotcore.hardware.Engagable;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.I2cController;
import com.qualcomm.robotcore.hardware.LegacyModule;

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
    private boolean                             isEngaged;

    //----------------------------------------------------------------------------------------------
    // Construction
    //----------------------------------------------------------------------------------------------

    public I2cDeviceReplacementHelper(OpMode context, TARGET client, /* may be null */ TARGET target, I2cController controller, int targetPort)
        {
        this.context        = context;
        this.isEngaged      = false;
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
            this.targetCallback = controller.getI2cPortReadyCallback(targetPort);
            }
        else if (controller instanceof DeviceInterfaceModule)
            {
            this.targetCallback = controller.getI2cPortReadyCallback(targetPort);
            }
        else
            throw new IllegalArgumentException(String.format("unknown controller flavor: %s", controller.getClass().getSimpleName()));
        }

    //----------------------------------------------------------------------------------------------
    // Operations
    //----------------------------------------------------------------------------------------------

    boolean isEngaged()
        {
        return this.isEngaged;
        }

    void engage()
        {
        if (!this.isEngaged)
            {
            // Have the existing controller stop managing its hardware
            ((Engagable)this.target).disengage();

            // Put ourselves in the hardware map
            if (this.targetName != null) this.targetDeviceMapping.put(this.targetName, this.client);

            this.isEngaged = true;
            }
        }

    void disengage()
        {
        if (this.isEngaged)
            {
            this.isEngaged = false;

            // Put the original guy back in the hardware map
            if (this.targetName != null) this.targetDeviceMapping.put(this.targetName, this.target);

            // Start up the original controller again
            ((Engagable)this.target).engage();
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
