package org.chathamrobotics.common.opmode.exceptions;

/*!
 * FTC_APP_2018
 * Copyright (c) 2017 Chatham Robotics
 * MIT License
 *
 * @Last Modified by: storm
 * @Last Modified time: 9/17/2017
 */

/**
 * Thrown when a opmode is no longer active
 */
@SuppressWarnings({"WeakerAccess", "unused"})
public class StoppedException extends RuntimeException {
    /**
     * Creates a new instance of StoppedException
     */
    public StoppedException() {
    }

    /**
     * Creates a new instance of StoppedException
     *
     * @param detailMessage the detail message for this exception.
     */
    public StoppedException(String detailMessage) {
        super(detailMessage);
    }
}
