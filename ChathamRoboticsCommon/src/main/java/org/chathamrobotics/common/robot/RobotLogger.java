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
import java.util.Locale;
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

    // a logger for a robot system
    private static class SystemLogger extends RobotLogger {
        private final RobotLogger parent;
        private final String systemTag;

        public SystemLogger(RobotLogger parent, String systemTag) {
            super(parent.tag, parent.telemetry);

            this.parent = parent;
            this.systemTag = systemTag;
        }

        @Override
        public void setTelemetryLevel(Level level) {
            // Do nothing
        }

        @Override
        public void setTelemetryCapacity(int capacity) {
            // Do nothing
        }

        @Override
        public Level getTelemetryLevel() {
            return parent.teleLevel;
        }

        @Override
        protected void logTele(Level level, String line) {
            if(level == Level.FATAL) {
                RobotLog.setGlobalErrorMsg(line);
            } else if (level.priority <= parent.teleLevel.priority) {
                // [system/level]: line (if systemTag)
                // [level]: line (if no systemTag)
                parent.telemetry.addData(
                        "[" + systemTag + "/" + level.name().toUpperCase() + "]",
                        line
                );
            }
        }
    }

    public String tag;
    private Telemetry telemetry;
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
     * Creates a new logger meant for a robot system
     * @param systemTag the tag used to identify this system
     * @return          the system logger
     */
    public RobotLogger systemLogger(String systemTag) {
        return new SystemLogger(this, systemTag);
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
     * Formats the line as "{caption} = {value}" and logs at the fatal level.
     *
     * @param caption   the caption for the value.
     * @param value     the value to log.
     */
    public void fatal(String caption, Object value) {
        fatal(formatCapVal(caption, value));
    }

    /**
     * Formats the line like String.format and logs at the fatal level
     * @param format    the format string
     * @param args      the arguments to replace the format specifiers
     */
    public void fatalf(String format, Object ...args) {
        fatal(String.format(Locale.US, format, args));
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
     * Formats the line as "{caption} = {value}" and logs at the error level.
     *
     * @param caption   the caption for the value.
     * @param value     the value to log.
     */
    public void error(String caption, Object value) {
        error(formatCapVal(caption, value));
    }

    /**
     * Formats the line like String.format and logs at the error level
     * @param format    the format string
     * @param args      the arguments to replace the format specifiers
     */
    public void errorf(String format, Object ...args) {
        error(String.format(Locale.US, format, args));
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
     * Formats the line as "{caption} = {value}" and logs at the warning level.
     *
     * @param caption   the caption for the value.
     * @param value     the value to log.
     */
    public void warn(String caption, Object value) {
        warn(formatCapVal(caption, value));
    }

    /**
     * Formats the line like String.format and logs at the warning level
     * @param format    the format string
     * @param args      the arguments to replace the format specifiers
     */
    public void warnf(String format, Object ...args) {
        warn(String.format(Locale.US, format, args));
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
     * Formats the line as "{caption} = {value}" and logs at the info level.
     *
     * @param caption   the caption for the value.
     * @param value     the value to log.
     */
    public void info(String caption, Object value) {
        info(formatCapVal(caption, value));
    }

    /**
     * Formats the line like String.format and logs at the info level
     * @param format    the format string
     * @param args      the arguments to replace the format specifiers
     */
    public void infof(String format, Object ...args) {
        info(String.format(Locale.US, format, args));
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
     * Formats the line as "{caption} = {value}" and logs at the debug level.
     *
     * @param caption   the values caption.
     * @param value     the value to log.
     */
    public void debug(String caption, Object value) {
        debug(formatCapVal(caption, value));
    }

    /**
     * Formats the line like String.format and logs at the debug level
     * @param format    the format string
     * @param args      the arguments to replace the format specifiers
     */
    public void debugf(String format, Object ...args) {
        debug(String.format(Locale.US, format, args));
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
     * formats the line as "{caption} = {value}" and logs at the verbose level.
     *
     * @param caption   the caption for the value.
     * @param value     the value to log.
     */
    public void verbose(String caption, Object value) {
        verbose(formatCapVal(caption, value));
    }

    /**
     * Formats the line like String.format and logs at the verbose level
     * @param format    the format string
     * @param args      the arguments to replace the format specifiers
     */
    public void verbosef(String format, Object ...args) {
        verbose(String.format(Locale.US, format, args));
    }

    /**
     * Updates the telemetry
     */
    public void update() {
        telemetry.update();
    }

    protected void logTele(Level level, String line) {
        if(level == Level.FATAL) {
            RobotLog.setGlobalErrorMsg(line);
        } else if (level.priority <= this.teleLevel.priority) {
            // [system/level]: line (if systemTag)
            // [level]: line (if no systemTag)
            telemetry.addData(
                    "[" + level.name().toUpperCase() + "]",
                    line
            );
        }
    }

    protected void logAndroid(Level level, String line) {
        if (! isRecentLog(line)) {
            Log.println(level.priority, this.tag, line);

            synchronized (recentLogs) {
                recentLogs.add(line);
            }
        }
    }

    private String formatCapVal(String caption, Object value) {
        return caption + " = " + value.toString();
    }

    private boolean isRecentLog(String line) {
        synchronized (recentLogs) {
            return recentLogs.contains(line);
        }
    }
}