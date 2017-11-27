package org.chathamrobotics.common.systems;

/*!
 * FTC_APP_2018
 * Copyright (c) 2017 Chatham Robotics
 * MIT License
 * @Last Modified by: storm
 * @Last Modified time: 11/9/2017
 */


/**
 * A driving system
 */
@SuppressWarnings("unused")
public interface Driver extends System{
    /**
     * Rotates the robot
     * @param power the power to rotate by
     */
    void rotate(double power);
}
