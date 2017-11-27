package org.chathamrobotics.common.hardware;

/*!
 * FTC_APP_2018
 * Copyright (c) 2017 Chatham Robotics
 * MIT License
 *
 * @Last Modified by: storm
 * @Last Modified time: 9/17/2017
 */

import com.qualcomm.robotcore.hardware.DigitalChannel;

/**
 * {@link LimitSwitch} models a limit switch
 */
@SuppressWarnings("unused")
public interface LimitSwitch extends DigitalChannel {
    /**
     * Whether or not the limit switch is being pressed. Returns true if pressed and false if not.
     * @return  whether or not the limit switch is being pressed
     */
    boolean isPressed();
}
