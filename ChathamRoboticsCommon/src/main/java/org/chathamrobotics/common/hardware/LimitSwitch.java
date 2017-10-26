package org.chathamrobotics.common.hardware;


/**
 * {@link LimitSwitch} models a limit switch
 */
@SuppressWarnings("unused")
public interface LimitSwitch {
    /**
     * Whether or not the limit switch is being pressed. Returns true if pressed and false if not.
     * @return  whether or not the limit switch is being pressed
     */
    boolean isPressed();
}
