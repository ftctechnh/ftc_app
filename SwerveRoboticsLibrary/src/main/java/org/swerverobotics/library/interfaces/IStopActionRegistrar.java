package org.swerverobotics.library.interfaces;

import com.qualcomm.robotcore.hardware.HardwareDevice;
import org.swerverobotics.library.SynchronousOpMode;

/**
 * IStopActionRegistrar can be used to register for a callback when an OpMode stops.
 */
public interface IStopActionRegistrar
    {
    /**
     * Registers an action to be called when the contextually associated opMode stops
     * @param action the action to be called on stop
     * @see SynchronousOpMode#getStopActionRegistrar()
     */
    void registerActionOnStop(IAction action);
    }
