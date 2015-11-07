package org.swerverobotics.library.internal;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotorController;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.I2cController;

import org.swerverobotics.library.exceptions.RuntimeInterruptedException;

import java.util.Map;

/**
 * Created by Bob on 2015-11-04.
 */
public class HardwareDeviceReplacementHelper<TARGET>
    {
    //----------------------------------------------------------------------------------------------
    // State
    //----------------------------------------------------------------------------------------------

    private OpMode                              context;
    private TARGET                              client;
    private TARGET                              target;
    private String                              targetName;
    private I2cController                       controller;
    private int                                 targetPort;
    private I2cController.I2cPortReadyCallback  targetCallback;
    private boolean                             isArmed;

    //----------------------------------------------------------------------------------------------
    // Construction
    //----------------------------------------------------------------------------------------------

    public HardwareDeviceReplacementHelper(OpMode context, TARGET client,
            TARGET target, I2cController controller, int targetPort, I2cController.I2cPortReadyCallback targetCallback)
        {
        this.context        = context;
        this.isArmed        = false;
        this.client         = client;
        this.target         = target;
        this.targetName     = findTargetName();
        this.controller     = controller;
        this.targetPort     = targetPort;
        this.targetCallback = targetCallback;
        }

    boolean isArmed()
        {
        return this.isArmed;
        }

    void arm()
        {
        if (!this.isArmed)
            {
            this.controller.deregisterForPortReadyCallback(this.targetPort);
            if (this.targetName != null) this.getDeviceMapping().put(this.targetName, this.client);
            this.isArmed = true;
            }
        }

    void disarm()
        {
        if (this.isArmed)
            {
            this.isArmed = false;
            if (this.targetName != null) this.getDeviceMapping().put(this.targetName, this.target);
            this.controller.registerForI2cPortReadyCallback(this.targetCallback, this.targetPort);
            }
        }

    private String findTargetName()
        {
        if (this.context != null)
            {
            for (Map.Entry<String,?> pair : this.getDeviceMapping().entrySet())
                {
                if (pair.getValue() == this.target)
                    return pair.getKey();
                }
            }
        return null;
        }

    HardwareMap.DeviceMapping<TARGET> getDeviceMapping()
        {
        if (target instanceof ColorSensor)          return (HardwareMap.DeviceMapping<TARGET>)this.context.hardwareMap.colorSensor;
        if (target instanceof DcMotorController)    return (HardwareMap.DeviceMapping<TARGET>)this.context.hardwareMap.dcMotorController;
        // TODO: more to come
        return null;
        }
    }
