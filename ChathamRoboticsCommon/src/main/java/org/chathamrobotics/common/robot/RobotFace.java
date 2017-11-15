package org.chathamrobotics.common.robot;

/*!
 * FTC_APP_2018
 * Copyright (c) 2017 Chatham Robotics
 * MIT License
 *
 * @Last Modified by: storm
 * @Last Modified time: 10/5/2017
 */

import java.util.Locale;

/**
 * Represents a face of the robot
 */
@SuppressWarnings("unused")
public enum RobotFace {
    FRONT(0),
    BACK(Math.PI),
    LEFT(Math.PI/2),
    RIGHT(3* Math.PI / 2);

    /**
     * The offset for a holonomic drive system
     */
    public double holonomicOffset;

    RobotFace(double holonomicOffset) {
        this.holonomicOffset = holonomicOffset;
    }

    @Override
    public String toString() {
        return String.format(Locale.US, "%s(holonomicOffset=%.2f)", name(), holonomicOffset);
    }
}
