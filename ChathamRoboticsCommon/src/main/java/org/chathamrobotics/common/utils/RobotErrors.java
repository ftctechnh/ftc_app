package org.chathamrobotics.common.utils;

import android.util.Log;

import com.qualcomm.robotcore.util.RobotLog;

import java.lang.reflect.InvocationTargetException;

/*!
 * FTC_APP_2018
 * Copyright (c) 2017 Chatham Robotics
 * MIT License
 *
 * @Last Modified by: storm
 * @Last Modified time: 10/3/2017
 */
public class RobotErrors {
    public static void reportError(String TAG, String format, Object ...args) {
        reportError(TAG, null, format, args);
    }

    public static void reportError(String TAG, Throwable e, String format, Object ...args) {
        if (e instanceof InvocationTargetException) {
            reportError(
                    TAG, ((InvocationTargetException) e).getTargetException(), format, args
            );
        } else if (e instanceof RuntimeException && e.getCause() != null) {
            reportError(
                    TAG, e.getCause(), format, args
            );
        } else {
            String message = String.format(format, args);
            // Show the message in the log
            Log.e(TAG, String.format("configuration error: %s", message), e);

            // Make the message appear on the driver station (only the first one will actually appear)
            RobotLog.setGlobalErrorMsg(message);
        }
    }
}
