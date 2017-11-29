package org.chathamrobotics.common.systems;

/*!
 * FTC_APP_2018
 * Copyright (c) 2017 Chatham Robotics
 * MIT License
 * @Last Modified by: storm
 * @Last Modified time: 11/26/2017
 */

/**
 * Systems of hardware on the robot
 */
public interface System {
    /**
     * Initializes the system
     */
    void init();

    /**
     * Returns true if the system has been initialized
     * @return true if the system has been initialized
     */
    boolean isInitialized();

    /**
     * Starts the system
     */
    void start();

    /**
     * Returns true if the system has been started
     * @return true if the system has been started
     */
    boolean isRunning();

    /**
     * Returns true if the system is currently busy
     * @return  true if the system is currently busy
     */
    boolean isBusy();

    /**
     * Stops the system
     */
    void stop();
}
