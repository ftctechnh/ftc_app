package org.chathamrobotics.common.robot;

/*!
 * FTC_APP_2018
 * Copyright (c) 2017 Chatham Robotics
 * MIT License
 * @Last Modified by: storm
 * @Last Modified time: 9/17/2017
 */

import android.util.Log;

import com.qualcomm.robotcore.util.RobotLog;

import org.firstinspires.ftc.robotcore.external.Telemetry;

import java.util.Collections;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Handles logging for the robot
 */
@SuppressWarnings({"WeakerAccess", "unused", "SameParameterValue"})
public class RobotLogger {
    /**
     * Interval at which to clear the recent logs list
     */
    private static final int CLEAN_UP_INTERVAL = 500;

    /**
     * A list containing all of the recent logs. This is used to prevent filling the flog file with repetitive logs
     */
//    private static final List<String> recentLogs = Collections.synchronizedList(new ArrayList<>());
    private static  final Set<String> recentLogs = Collections.newSetFromMap(new ConcurrentHashMap<String, Boolean>());

    static {
        // clear recent logs using CLEAN_UP_INTERVAL as the rate
        Executors.newScheduledThreadPool(1).scheduleAtFixedRate(() -> {
            synchronized (recentLogs) {
                recentLogs.clear();
            }
        }, CLEAN_UP_INTERVAL, CLEAN_UP_INTERVAL, TimeUnit.MILLISECONDS);
    }

    /**
     * The tag the logger is using.
     */
    public String tag;

    /**
     * The telemetry object to write data to.
     */
    private Telemetry telemetry;

    /**
     * The current log level of the telemetry.
     */
    private Level teleLevel = Level.DEBUG;

    /**
     * Creates a new instance of RobotLogger
     *
     * @param tag       the tag to use for all the logs
     * @param telemetry the opmodes telemetry
     */
    public RobotLogger(String tag, Telemetry telemetry) {
        this.tag = tag;
        this.telemetry = telemetry;
    }

    /**
     * Represents all of the log levels
     */
    public enum Level {
        FATAL(1),
        ERROR(2),
        WARN(3),
        INFO(4),
        DEBUG(5),
        VERBOSE(6);

        public int priority;

        Level(int priority) {
            this.priority = priority;
        }
    }

    /**
     * Sets the telemetry's log level.
     *
     * @param level the level to set.
     */
    public void setTelemetryLevel(Level level) {this.teleLevel = level;}

    /**
     * Gets the telemetry's level.
     *
     * @return  the telemetry's level.
     */
    public Level getTelemetryLevel() {return this.teleLevel;}

    /**
     * Sets the max number of lines the telemetry can display
     * @param capacity  the number of lines
     */
    public void setTelemetryCapacity(int capacity) {
        this.telemetry.log().setCapacity(capacity);
    }

    /**
     * Logs at the fatal level.
     *
     * @param line the line to log.
     */
    public void fatal(String line) {
        logAndroid(Level.FATAL, line);
        logTele(Level.FATAL, line);
    }

    /**
     * Logs a exception at the fatal level.
     *
     * @param line  the line to log.
     * @param tr    the exception to log.
     */
    public void fatal(String line, Throwable tr) {
        Log.wtf(this.tag, line, tr);
        logTele(Level.FATAL, tr.getLocalizedMessage());
    }

    /**
     * Formats the line as "{caption}: {value}" and logs at the fatal level.
     *
     * @param caption   the caption for the value.
     * @param value     the value to log.
     */
    public void fatal(String caption, Object value) {
        fatal(format(caption, value));
    }

    /**
     * Logs at the error level.
     *
     * @param line  the line to log.
     */
    public void error(String line) {
        logAndroid(Level.ERROR, line);
        logTele(Level.ERROR, line);
    }

    /**
     * Logs a exception at the error level.
     *
     * @param line  the line to log.
     * @param tr    the exception to log.
     */
    public void error(String line, Throwable tr) {
        Log.e(this.tag, line, tr);
        logTele(Level.ERROR, tr.getLocalizedMessage());
    }

    /**
     * Formats the line as "{caption}: {value}" and logs at the error level.
     *
     * @param caption   the caption for the value.
     * @param value     the value to log.
     */
    public void error(String caption, Object value) {
        error(format(caption, value));
    }

    /**
     * Logs at the warning level.
     *
     * @param line  the line to log.
     */
    public void warn(String line) {
        logAndroid(Level.WARN, line);
        logTele(Level.WARN, line);
    }

    /**
     * Logs a exception at the warning level.
     *
     * @param line  the line to log.
     * @param tr    the exception to log.
     */
    public void warn(String line, Throwable tr) {
        Log.w(this.tag, line, tr);
        logTele(Level.WARN, tr.getLocalizedMessage());
    }

    /**
     * Formats the line as "{caption}: {value}" and logs at the warning level.
     *
     * @param caption   the caption for the value.
     * @param value     the value to log.
     */
    public void warn(String caption, Object value) {
        warn(format(caption, value));
    }

    /**
     * Logs at the info level.
     *
     * @param line  the line to log.
     */
    public void info(String line) {
        logAndroid(Level.INFO, line);
        logTele(Level.INFO, line);
    }

    /**
     * Formats the line as "{caption}: {value}" and logs at the info level.
     *
     * @param caption   the caption for the value.
     * @param value     the value to log.
     */
    public void info(String caption, Object value) {
        info(format(caption, value));
    }


    /**
     * Logs at the debug level.
     *
     * @param line  the line to log.
     */
    public void debug(String line) {
        logAndroid(Level.DEBUG, line);
        logTele(Level.DEBUG, line);
    }

    /**
     * Formats the line as "{caption}: {value}" and logs at the debug level.
     *
     * @param caption   the values caption.
     * @param value     the value to log.
     */
    public void debug(String caption, Object value) {
        debug(format(caption, value));
    }

    /**
     * logs at the verbose level.
     *
     * @param line      the line to log.
     */
    public void verbose(String line) {
        logAndroid(Level.VERBOSE, line);
        logTele(Level.VERBOSE, line);
    }

    /**
     * formats the line as "{caption}: {value}" and logs at the verbose level.
     *
     * @param caption   the caption for the value.
     * @param value     the value to log.
     */
    public void verbose(String caption, Object value) {
        verbose(format(caption, value));
    }

    /**
     * logs to the telemetry
     *
     * @param level the level to log at
     * @param line  the line to log
     */
    private void logTele(Level level, String line) {
        if(level == Level.FATAL) {
            RobotLog.setGlobalErrorMsg(line);
        } else if (level.priority <= this.teleLevel.priority) {
            telemetry.addData("[" + level.name().toUpperCase() + "]", line);
        }
    }

    /**
     * Logs to androids logging facilities.
     *
     * @param level     the level to log at
     * @param line      the line to log
     */
    private void logAndroid(Level level, String line) {
        if (! isRecentLog(line)) {
            Log.println(level.priority, this.tag, line);

            synchronized (recentLogs) {
                recentLogs.add(line);
            }
        }
    }

    /**
     * formats a line using a value and it's caption
     *
     * @param caption   the caption for the value
     * @param value     the value
     * @return          the formatted line
     */
    private String format(String caption, Object value) {
        return caption + ": " + value.toString();
    }

    /**
     * Check if the line was recently logged
     * @param line  the line to check for
     * @return  whether the line was recently logged
     */
    private boolean isRecentLog(String line) {
        synchronized (recentLogs) {
            return recentLogs.contains(line);
        }
    }
}