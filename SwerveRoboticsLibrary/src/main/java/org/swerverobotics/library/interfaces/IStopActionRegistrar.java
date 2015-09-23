package org.swerverobotics.library.interfaces;

import com.qualcomm.robotcore.hardware.HardwareDevice;

/**
 * IStopActionRegistrar can be used to register for a callback when a synchronous opmode stops
 */
public interface IStopActionRegistrar
    {
    /**
     * Registers an action to be called when the contextually associated opMode stops
     * @param action the action to be called on stop
     */
    void registerActionOnStop(IAction action);
    }
