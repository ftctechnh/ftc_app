package org.chathamrobotics.common.utils;

import android.util.Log;

import com.qualcomm.robotcore.hardware.HardwareDevice;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/*!
 * FTC_APP_2018
 * Copyright (c) 2017 Chatham Robotics
 * MIT License
 *
 * @Last Modified by: storm
 * @Last Modified time: 10/28/2017
 */
@SuppressWarnings({"WeakerAccess", "unused"})
public class HardwareListeners {
    private static final String TAG = HardwareListeners.class.getSimpleName();

    /**
     * A condition for a hardware device to meet. Returns true if the condition is met.
     * @param <H>   the hardware device type
     */
    public interface HardwareCondition<H extends HardwareDevice> {
        /**
         * Checks whether the condition was met
         * @param device    the hardware device to check
         * @return          whether the condition was met
         */
        boolean check(H device);
    }

    /**
     * Listens on a hardware device and calls the callback if the condition is met.
     * @param <H>   the hardware device type
     */
    public class HardwareListener<H extends HardwareDevice> {
        private final Runnable callback;
        private final HardwareCondition<H> condition;

        private final int maxCalls;
        private int callCount = 0;

        /**
         * Creates a new instance of {@link HardwareListener}
         * @param condition the condition for the hardware device to meet
         * @param callback  the callback to call when the condition is met
         */
        public HardwareListener(HardwareCondition<H> condition, Runnable callback) {
            this(condition, callback, 0);
        }

        /**
         * Creates a new instance of {@link HardwareListener}
         * @param condition the condition for the hardware device to meet
         * @param callback  the callback to call when the condition is met
         * @param maxCalls  the maximum number of times the callback can be called
         */
        public HardwareListener(HardwareCondition<H> condition, Runnable callback, int maxCalls) {
            this.condition = condition;
            this.callback = callback;
            this.maxCalls = maxCalls;
        }

        /**
         * Checks whether on not the condition is met
         * @param device    the hardware device to check
         * @return          whether on not the condition is met
         */
        public boolean check(H device) {
            return this.condition.check(device);
        }

        /**
         * Runs the call back
         * @throws CallsMaxedException  thrown if the maximum number of calls has been exceeded.
         */
        public void run() throws CallsMaxedException {
            if (maxCalls != 0 && callCount == maxCalls) throw new CallsMaxedException();

            this.callback.run();

            if (maxCalls != 0) callCount++;
        }
    }

    // the listeners tha have been set
    private final HashMap<HardwareDevice, ArrayList<HardwareListener>> hardwareListeners = new HashMap<>();
    private final ReadWriteLock hardwareListenersLock = new ReentrantReadWriteLock();

    // loops through all of the listeners and calls them if the conditions are met
    private final Runnable pollingLoop = () -> {
        while (true) {
            if (Thread.interrupted()) break;

            hardwareListenersLock.readLock().lock();

            try {
                // loop through all the listeners and call them if the conditions are met
                for (Map.Entry<HardwareDevice, ArrayList<HardwareListener>> hardwareEntry : hardwareListeners.entrySet()) {
                    for (HardwareListener listener : hardwareEntry.getValue()) {
                        synchronized (hardwareEntry.getKey()) {
                            // TODO: find a way to check that the device matches the listener just to be safe
                            //noinspection unchecked
                            if (listener.check(hardwareEntry.getKey())) {
                                try {
                                    listener.run();
                                } catch (CallsMaxedException e) {
                                    hardwareEntry.getValue().remove(listener);
                                    Log.d(TAG, "Removed a listener from a " + hardwareEntry.getKey().getDeviceName());

                                    // remove the entry if there are no more listeners
                                    if (hardwareEntry.getValue().size() == 0)
                                        hardwareListeners.remove(hardwareEntry.getKey());
                                }
                            }
                        }
                    }
                }


                // exit thread if no more listeners
                if (hardwareListeners.isEmpty())
                    break;
            } finally {
                hardwareListenersLock.readLock().unlock();
            }
        }

        Log.d(TAG, "Exiting polling loop");
    };

    // used to ensure that the polling loop is only running when it has to
    private Thread pollingThread;

    /**
     * Sets a new listener on the given device
     * @param device    the device to listen on
     * @param condition the condition that must be met in order to call the callback
     * @param callback  the callback to call when the condition is met
     * @param <H>       the hardware device type
     */
    public <H extends HardwareDevice> void on(H device, HardwareCondition<H> condition, Runnable callback) {
        on(device, new HardwareListener<>(condition, callback));
    }

    /**
     * Sets a listener on the given device
     * @param device    the device to listen on
     * @param listener  the listener to use
     * @param <H>       the hardware device type
     */
    public <H extends HardwareDevice> void on(H device, HardwareListener<H> listener) {
        // lock and reads until this listener has been added
        hardwareListenersLock.writeLock().lock();

        try {
            ArrayList<HardwareListener> listeners = hardwareListeners.get(device);

            // if the list has not been initialized yet
            if (listeners == null) {
                listeners = new ArrayList<>();
                hardwareListeners.put(device, listeners);
            }

            listeners.add(listener);
            Log.d(TAG, "Added a listener to a " + device.getDeviceName());
        } finally {
            // ensure that the lock is lifted
            hardwareListenersLock.writeLock().unlock();
        }

        if (pollingThread == null || ! pollingThread.isAlive()) startPolling();
    }

    /**
     * Sets a listener on the given device that can only be called once
     * @param device    the device to listen on
     * @param listener  the listener to use
     * @param <H>       the hardware device type
     */
    public <H extends HardwareDevice> void once(H device, HardwareListener<H> listener) {
        on(device, new HardwareListener<>(listener.condition, listener.callback, 1));
    }

    /**
     * Sets a new listener on the given device that can only be called once
     * @param device    the device to listen on
     * @param condition the condition that must be met in order to call the callback
     * @param callback  the callback to call when the condition is met
     * @param <H>       the hardware device type
     */
    public <H extends HardwareDevice> void once(H device, HardwareCondition<H> condition, Runnable callback) {
        on(device, new HardwareListener<>(condition, callback, 1));
    }

    /**
     * Removes all of the listeners
     */
    public void removeAllListeners() {
        hardwareListenersLock.writeLock().lock();

        try {
            hardwareListeners.clear();
        } finally {
            hardwareListenersLock.writeLock().unlock();
        }
    }

    /**
     * Removes all of the listeners for the given device
     * @param device    the device whose listeners should be removed
     * @param <H>       the type of the hardware device
     */
    public <H extends HardwareDevice> void removeAllListeners(H device) {
        hardwareListenersLock.writeLock().lock();

        try {
            hardwareListeners.remove(device);
        } finally {
            hardwareListenersLock.writeLock().unlock();
        }
    }

    // starts the polling loop in a new thread
    private void startPolling() {
        pollingThread = new Thread(pollingLoop, "hardware listener thread");

        pollingThread.setDaemon(true);

        Log.d(TAG, "Starting polling loop");
        pollingThread.start();
    }

    // used to alert the polling loop that the listener has reach it's call limit
    private class CallsMaxedException extends Exception {}
}
