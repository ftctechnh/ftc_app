package org.chathamrobotics.common.hardware;


import com.qualcomm.robotcore.hardware.HardwareDevice;

/**
 * {@link LimitSwitch} models a limit switch
 */
@SuppressWarnings("unused")
public interface LimitSwitch extends HardwareDevice {
    /**
     * Whether or not the limit switch is being pressed. Returns true if pressed and false if not.
     * @return  whether or not the limit switch is being pressed
     */
    boolean isPressed();
}
