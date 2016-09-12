package com.lasarobotics.library.util;

import java.util.Hashtable;
import java.util.concurrent.TimeUnit;

/**
 * Implements advanced timers with events and precision manipulation.
 */
public class Timers {

    private Hashtable<String, TimerData> store = new Hashtable<String, TimerData>();
    private int defaultprecision;
    /**
     * Instantiates the timer class with the default millisecond precision.
     */
    public Timers() {
        defaultprecision = 5;
    }

    /**
     * Instantiates the timer class with an arbitrary precision in milliseconds.
     *
     * @param precision Precision of the clock, in milliseconds.
     */
    public Timers(int precision) {
        this.defaultprecision = precision;
    }

    /**
     * Start or resume (and create, if nonexistent) a clock with a specified name.
     *
     * @param name The clock name
     */
    public void startClock(String name) {
        if (exists(name)) {
            if (!isRunning(name)) {
                //resume clock
                Long delta = store.get(name).time;
                Long start = System.nanoTime() - delta;
                store.put(name, new TimerData(start, false));
            } else {
                //start clock
                store.put(name, new TimerData(System.nanoTime(), false));
            }
        } else {
            //start clock
            store.put(name, new TimerData(System.nanoTime(), false));
        }
    }

    /**
     * Test if a clock exists
     *
     * @param name Name of the clock
     * @return True if the clock exists, false otherwise
     */
    public boolean exists(String name) {
        return store.containsKey(name);
    }

    /**
     * Create a clock with a given name. The clock will start PAUSED.
     * If the clock already exists, no action will be taken
     *
     * @param name The clock name
     */
    public void createClock(String name) {
        if (!exists(name)) {
            store.put(name, new TimerData(0, true));
        }
    }

    /**
     * Pause a clock with a specified name. If a clock is already paused, no action will be taken.
     * <p/>
     * Internally, clocks are paused by setting the current clock uptime to it's opposite value.
     * Therefore, to find if a clock is paused, check if the value is negative
     *
     * @param name The clock name
     */
    public void pauseClock(String name) {
        if (exists(name)) {
            if (isRunning(name)) {
                long delta = System.nanoTime() - store.get(name).time;
                store.put(name, new TimerData(delta, true));
            }
        } else {
            throw new IllegalArgumentException("Timer " + name + " does not exist.");
        }
    }

    /**
     * Test if a clock is running
     *
     * @param name The clock name
     * @return True if running, false if paused
     */
    public boolean isRunning(String name) {
        return !store.get(name).paused;
    }

    /**
     * Reset a clock with the specified name.  Clock will continue running immediately.
     *
     * @param name The clock name
     */
    public void resetClock(String name) {
        if (store.containsKey(name)) {
            store.put(name, new TimerData(System.nanoTime(), false));
        } else {
            throw new IllegalArgumentException("Timer " + name + " does not exist.");
        }
    }

    /**
     * Reset a clock with the specified name and optionally start paused
     *
     * @param name The clock name
     */
    public void resetClock(String name, boolean paused) {
        if (store.containsKey(name)) {
            store.put(name, new TimerData(System.nanoTime(), paused));
        } else {
            throw new IllegalArgumentException("Timer " + name + " does not exist.");
        }
    }

    /**
     * Get clock value. Defaults to millisecond precision.
     *
     * @param name Name of the clock
     * @return Value of clock in milliseconds
     */
    public long getClockValue(String name) {
        return getClockValue(name, TimeUnit.MILLISECONDS);
    }

    /**
     * Get clock value with precision in a given time unit
     *
     * @param name     Name of the clock
     * @param timeUnit TimeUnit the output should be in
     * @return The value of the clock converted to the time unit specified (may lose precision)
     */
    public long getClockValue(String name, TimeUnit timeUnit) {
        if (store.containsKey(name)) {
            if (isRunning(name)) {
                //Running, store returns start time
                Long start = store.get(name).time;
                return timeUnit.convert(Math.abs(System.nanoTime() - start), TimeUnit.NANOSECONDS);
            } else {
                //Paused, store returns absolute
                return timeUnit.convert(store.get(name).time, TimeUnit.NANOSECONDS);
            }
        } else {
            throw new IllegalArgumentException("Timer " + name + " does not exist.");
        }
    }

    /**
     * Returns whether the clock is at the specified amount of milliseconds
     *
     * @param name   The clock name
     * @param target The target time in milliseconds
     * @return True if at the target (+- precision), false otherwise
     */
    public boolean isAtTargetMillis(String name, long target) {
        return isAtTargetMillis(name, target, defaultprecision);
    }

    /**
     * Returns whether the clock is at the specified amount of milliseconds
     *
     * @param name      The clock name
     * @param target    The target time in milliseconds
     * @param precision How much target and clock value can differ by in milliseconds
     * @return True if at the target (+- precision), false otherwise
     */
    public boolean isAtTargetMillis(String name, long target, long precision) {
        if (store.containsKey(name)) {
            long milliDiff = Math.abs(target - getClockValue(name));
            return milliDiff < precision;
        } else {
            throw new IllegalArgumentException("Timer " + name + " does not exist.");
        }
    }

    /**
     * Gets the precision in milliseconds
     *
     * @return Precision in milliseconds
     */
    public long getPrecision() {
        return defaultprecision;
    }

    /**
     * Sets the precision to a value
     *
     * @param precision The precision of the clock, in milliseconds.
     */
    public void setPrecision(int precision) {
        this.defaultprecision = precision;
    }

    private class TimerData {
        long time;
        boolean paused;

        TimerData(long time, boolean paused) {
            this.time = time;
            this.paused = paused;
        }
    }
}
