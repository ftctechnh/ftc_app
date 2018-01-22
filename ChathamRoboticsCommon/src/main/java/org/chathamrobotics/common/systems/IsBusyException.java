package org.chathamrobotics.common.systems;

/*!
 * FTC_APP_2018
 * Copyright (c) 2017 Chatham Robotics
 * MIT License
 * @Last Modified by: storm
 * @Last Modified time: 10/29/2017
 */

/**
 * Thrown when a system is busy and cannot perform the operation that was requested
 */
@SuppressWarnings("unused")
public class IsBusyException extends Exception {
    /**
     * Creates a new instance of {@link IsBusyException}
     */
    public IsBusyException() {
    }

    /**
     * Creates a new instance of StoppedException
     *
     * @param detailMessage the detail message for this exception.
     */
    public IsBusyException(String detailMessage) {
        super(detailMessage);
    }
}
