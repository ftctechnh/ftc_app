package org.chathamrobotics.common.utils;


import android.util.Log;

import org.firstinspires.ftc.robotcore.external.Telemetry;

/*!
 * FTC_APP_2018
 * Copyright (c) 2017 Chatham Robotics
 * MIT License
 * @Last Modified by: storm
 * @Last Modified time: 9/17/2017
 */
@SuppressWarnings({"WeakerAccess", "unused"})
public class RobotLogger {
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
     * Logs at the fatal level. This should be information that shows an error that the robot
     * will not be able to recover from.
     *
     * @param line      the line to log.
     * @param looping   whether this is contained in a loop. If it is, the line will not be logged to the log cat facilities.
     */
    public void fatal(String line, boolean looping) {
        logAndroid(Level.FATAL, line, looping);
        logTele(Level.FATAL, line);
    }

    /**
     * Logs at the fatal level. Assumes not in loop.
     * @see RobotLogger#fatal(String, boolean)
     *
     * @param line the line to log.
     */
    public void fatal(String line) {
        fatal(line, false);
    }

    /**
     * Logs a exception at the fatal level. Assumes not in loop.
     * @see RobotLogger#fatal(String, boolean)
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
     * @see RobotLogger#fatal(String, boolean)
     *
     * @param caption   the caption for the value.
     * @param value     the value to log.
     * @param looping   whether this is contained in a loop. If it is, the line will not be logged to the log cat facilities.
     */
    public void fatal(String caption, Object value, boolean looping) {
        fatal(format(caption, value), looping);
    }

    /**
     * Formats the line as "{caption}: {value}" and logs at the fatal level. Assumes not in loop.
     * @see RobotLogger#fatal(String, boolean)
     * @see RobotLogger#fatal(String, Object, boolean)
     *
     * @param caption   the caption for the value.
     * @param value     the value to log.
     */
    public void fatal(String caption, Object value) {
        fatal(format(caption, value), false);
    }

    /**
     * Logs at the error level. This should be information that expresses error might let the robot
     * continue to run.
     *
     * @param line      the line to log.
     * @param looping   whether this is contained in a loop. If it is, the line will not be logged to the log cat facilities.
     */
    public void error(String line, boolean looping) {
        logAndroid(Level.ERROR, line, looping);
        logTele(Level.ERROR, line);
    }

    /**
     * Logs at the error level. Assumes not in loop.
     * @see RobotLogger#error(String, boolean)
     *
     * @param line  the line to log.
     */
    public void error(String line) {
        error(line, false);
    }

    /**
     * Logs a exception at the error level. Assumes not in loop.
     * @see RobotLogger#error(String, boolean)
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
     * @see RobotLogger#error(String, boolean)
     *
     * @param caption   the caption for the value.
     * @param value     the value to log.
     * @param looping   whether this is contained in a loop. If it is, the line will not be logged to the log cat facilities.
     */
    public void error(String caption, Object value, boolean looping) {
        error(format(caption, value), looping);
    }

    /**
     * Formats the line as "{caption}: {value}" and logs at the error level. Assumes not in loop.
     * @see RobotLogger#error(String, boolean)
     * @see RobotLogger#error(String, Object, boolean)
     *
     * @param caption   the caption for the value.
     * @param value     the value to log.
     */
    public void error(String caption, Object value) {
        error(format(caption, value), false);
    }

    /**
     * Logs at the warning level. This should be information that warns of potentially problematic
     * situations.
     *
     * @param line      the line to log.
     * @param looping   whether this is contained in a loop. If it is, the line will not be logged to the log cat facilities.
     */
    public void warn(String line, boolean looping) {
        logAndroid(Level.WARN, line, looping);
        logTele(Level.WARN, line);
    }

    /**
     * Logs at the warning level. Assumes not in loop.
     * @see RobotLogger#warn(String, boolean)
     *
     * @param line  the line to log.
     */
    public void warn(String line) {
        warn(line, false);
    }

    /**
     * Logs a exception at the warning level. Assumes not in loop.
     * @see RobotLogger#warn(String, boolean)
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
     * @see RobotLogger#warn(String, boolean)
     *
     * @param caption   the caption for the value.
     * @param value     the value to log.
     * @param looping   whether this is contained in a loop. If it is, the line will not be logged to the log cat facilities.
     */
    public void warn(String caption, Object value, boolean looping) {
        warn(format(caption, value), looping);
    }

    /**
     * Formats the line as "{caption}: {value}" and logs at the warning level. Assumes not in loop.
     * @see RobotLogger#warn(String, boolean)
     * @see RobotLogger#warn(String, Object, boolean)
     *
     * @param caption   the caption for the value.
     * @param value     the value to log.
     */
    public void warn(String caption, Object value) {
        warn(format(caption, value), false);
    }

    /**
     * Logs at the info level. This should be information show the progress of the robot.
     *
     * @param line      the line to log.
     * @param looping   whether this is contained in a loop. If it is, the line will not be logged to the log cat facilities.
     */
    public void info(String line, boolean looping) {
        logAndroid(Level.INFO, line, looping);
        logTele(Level.INFO, line);
    }

    /**
     * Logs at the info level. Assumes not in loop.
     * @see RobotLogger#info(String, boolean)
     *
     * @param line  the line to log.
     */
    public void info(String line) {
        info(line, false);
    }

    /**
     * Formats the line as "{caption}: {value}" and logs at the info level.
     * @see RobotLogger#debug(String, boolean)
     *
     * @param caption   the caption for the value.
     * @param value     the value to log.
     * @param looping   whether this is contained in a loop. If it is, the line will not be logged to the log cat facilities.
     */
    public void info(String caption, Object value, boolean looping) {
        info(format(caption, value), looping);
    }

    /**
     * Formats the line as "{caption}: {value}" and logs at the info level. Assumes not in loop.
     * @see RobotLogger#debug(String, boolean)
     * @see RobotLogger#debug(String, Object, boolean)
     *
     * @param caption   the caption for the value.
     * @param value     the value to log.
     */
    public void info(String caption, Object value) {
        info(format(caption, value), false);
    }

    /**
     * Logs at the debug level. This should be information that would be used to determine where a
     * bug occurred.
     *
     * @param line      the line to log.
     * @param looping   whether this is contained in a loop. If it is, the line will not be logged to the log cat facilities.
     */
    public void debug(String line, boolean looping) {
        logAndroid(Level.DEBUG, line, looping);
        logTele(Level.DEBUG, line);
    }

    /**
     * Logs at the debug level. Assumes that not in loop.
     * @see RobotLogger#debug(String, boolean)
     *
     * @param line  the line to log.
     */
    public void debug(String line) {
        debug(line, false);
    }

    /**
     * Formats the line as "{caption}: {value}" and logs at the debug level.
     * @see RobotLogger#debug(String, boolean)
     *
     * @param caption   the caption for the value.
     * @param value     the value to log.
     * @param looping   whether this is contained in a loop. If it is, the line will not be logged to the log cat facilities.
     */
    public void debug(String caption, Object value, boolean looping) {
        debug(format(caption, value), looping);
    }

    /**
     * Formats the line as "{caption}: {value}" and logs at the debug level. Assumes not in loop.
     * @see RobotLogger#debug(String, boolean)
     * @see RobotLogger#debug(String, Object, boolean)
     *
     * @param caption   the values caption.
     * @param value     the value to log.
     */
    public void debug(String caption, Object value) {
        debug(format(caption, value), false);
    }

    /**
     * logs at the verbose level. This should be information that is a little overkill.
     * @see <a href="http://www.computerhope.com/jargon/v/verbose.htm">Verbose</a>
     *
     * @param line      the line to log.
     * @param looping   whether this is contained in a loop. If it is, the line will not be logged to the log cat facilities.
     */
    public void verbose(String line, boolean looping) {
        logAndroid(Level.VERBOSE, line, looping);
        logTele(Level.VERBOSE, line);
    }

    /**
     * logs at the verbose level. Assumes not in loop.
     * @see RobotLogger#verbose(String, boolean)
     *
     * @param line      the line to log.
     */
    public void verbose(String line) {
        verbose(line, false);
    }

    /**
     * Formats the line as "{caption}: {value}" and logs at the verbose level.
     * @see RobotLogger#verbose(String, boolean)
     *
     * @param caption   the caption for the value.
     * @param value     the value to log.
     * @param looping   whether this is contained in a loop. If it is, the line will not be logged to the log cat facilities.
     */
    public void verbose(String caption, Object value, boolean looping) {
        verbose(format(caption, value), looping);
    }

    /**
     * formats the line as "{caption}: {value}" and logs at the verbose level. Assumes not in loop.
     * @see RobotLogger#verbose(String, boolean)
     * @see RobotLogger#verbose(String, Object, boolean)
     *
     * @param caption   the caption for the value.
     * @param value     the value to log.
     */
    public void verbose(String caption, Object value) {
        verbose(format(caption, value), false);
    }

    /**
     * logs to the telemetry
     *
     * @param level the level to log at
     * @param line  the line to log
     */
    private void logTele(Level level, String line) {
        if (level.priority >= this.teleLevel.priority) {
            telemetry.addData("[" + this.tag + "/" + level.name().toUpperCase() + "]", line);
        }
    }

    /**
     * Logs to androids logging facilities.
     *
     * @param level     the level to log at
     * @param line      the line to log
     * @param looping   whether logging in a loop
     */
    private void logAndroid(Level level, String line, boolean looping) {
        // the log files flood fast when looping
        if(! looping) {
            // logs at the desired level
            Log.println(level.priority, this.tag, line);
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
}