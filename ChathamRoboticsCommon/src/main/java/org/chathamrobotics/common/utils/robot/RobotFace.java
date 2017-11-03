package org.chathamrobotics.common.utils.robot;

import java.util.Locale;

/**
 * FTC_APP_2018
 * Copyright (c) 2017 Chatham Robotics
 * MIT License
 *
 * @Last Modified by: storm
 * @Last Modified time: 10/5/2017
 */
@SuppressWarnings("unused")
public enum RobotFace {
    FRONT(0),
    BACK(Math.PI),
    LEFT(Math.PI/2),
    Right(3* Math.PI / 2);

    public double holonomicOffset;

    RobotFace(double holonomicOffset) {
        this.holonomicOffset = holonomicOffset;
    }

    @Override
    public String toString() {
        return String.format(Locale.US, "%s(offset=%.2f)", name(), holonomicOffset);
    }
}
