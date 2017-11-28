package org.chathamrobotics.common.hardware.utils;

/*!
 * FTC_APP_2018
 * Copyright (c) 2017 Chatham Robotics
 * MIT License
 *
 * @Last Modified by: storm
 * @Last Modified time: 10/28/2017
 */

import android.util.Log;

import com.qualcomm.robotcore.hardware.HardwareDevice;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Listens to hardware devices for changes to their state
 */
@SuppressWarnings({"WeakerAccess", "unused"})
public class HardwareListener {
    private static final String TAG = HardwareListener.class.getSimpleName();
    private static final int SAMPLE_RATE = 5;

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
    public class HardwareListenerFunc<H extends HardwareDevice> {
        private final Runnable callback;
        private final HardwareCondition<H> condition;

        private int maxCalls;
        private int callCount;
        private long endTime;

        /**
         * Creates a new instance of {@link HardwareListenerFunc}
         * @param condition the condition for the hardware device to meet
         * @param callback  the callback to call when the condition is met
         */
        public HardwareListenerFunc(HardwareCondition<H> condition, Runnable callback) {
            this.condition = condition;
            this.callback = callback;
        }

        public void setMaxCalls(int maxCalls) {
            this.maxCalls = maxCalls;
        }

        public void setTimeout(long timeout) {
            endTime = System.currentTimeMillis() + timeout;
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
            if (endTime != 0 && System.currentTimeMillis() > endTime) throw  new CallsMaxedException();

            this.callback.run();

            callCount++;
        }
    }

    // the listeners tha have been set
    private final HashMap<HardwareDevice, ArrayList<HardwareListenerFunc>> hardwareListeners = new HashMap<>();
    private final ReadWriteLock hardwareListenersLock = new ReentrantReadWriteLock();

    // loops through all of the listeners and calls them if the conditions are met
    private final Runnable pollingLoop = () -> {
        while (true) {
            if (Thread.interrupted()) break;

            hardwareListenersLock.readLock().lock();

            try {
                // loop through all the listeners and call them if the conditions are met
                for (Map.Entry<HardwareDevice, ArrayList<HardwareListenerFunc>> hardwareEntry : hardwareListeners.entrySet()) {
                    for (HardwareListenerFunc listener : hardwareEntry.getValue()) {
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

            try {
                Thread.sleep(SAMPLE_RATE);
            } catch (InterruptedException e) {
                break;
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
        on(device, new HardwareListenerFunc<>(condition, callback));
    }

    /**
     * Sets a new listener on the given device
     * @param device    the device to listen on
     * @param condition the condition that must be met in order to call the callback
     * @param callback  the callback to call when the condition is met
     * @param timeout   the timeout on the listener
     * @param <H>       the hardware device type
     */
    public <H extends HardwareDevice> void on(H device, HardwareCondition<H> condition, Runnable callback, long timeout) {
        final HardwareListenerFunc<H> func = new HardwareListenerFunc<>(condition, callback);
        func.setTimeout(timeout);

        on(device, new HardwareListenerFunc<>(condition, callback));
    }

    /**
     * Sets a listener on the given device
     * @param device    the device to listen on
     * @param listener  the listener to use
     * @param <H>       the hardware device type
     */
    public <H extends HardwareDevice> void on(H device, HardwareListenerFunc<H> listener) {
        // lock and reads until this listener has been added
        hardwareListenersLock.writeLock().lock();

        try {
            ArrayList<HardwareListenerFunc> listeners = hardwareListeners.get(device);

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
     * Sets a listener on the given device
     * @param device    the device to listen on
     * @param listener  the listener to use
     * @param timeout   the timeout on the listener
     * @param <H>       the hardware device type
     */
    public <H extends HardwareDevice> void on(H device, HardwareListenerFunc<H> listener, long timeout) {
        listener.setTimeout(timeout);
        on(device, listener);
    }

    /**
     * Sets a listener on the given device that can only be called once
     * @param device    the device to listen on
     * @param listener  the listener to use
     * @param <H>       the hardware device type
     */
    public <H extends HardwareDevice> void once(H device, HardwareListenerFunc<H> listener) {
        listener.setMaxCalls(1);
        on(device, listener);
    }

    /**
     * Sets a listener on the given device that can only be called once
     * @param device    the device to listen on
     * @param listener  the listener to use
     * @param timeout   the timeout on the listener
     * @param <H>       the hardware device type
     */
    public <H extends HardwareDevice> void once(H device, HardwareListenerFunc<H> listener, long timeout) {
        listener.setMaxCalls(1);
        listener.setTimeout(timeout);

        on(device, listener);
    }

    /**
     * Sets a new listener on the given device that can only be called once
     * @param device    the device to listen on
     * @param condition the condition that must be met in order to call the callback
     * @param callback  the callback to call when the condition is met
     * @param <H>       the hardware device type
     */
    public <H extends HardwareDevice> void once(H device, HardwareCondition<H> condition, Runnable callback) {
        HardwareListenerFunc<H> func = new HardwareListenerFunc<H>(condition, callback);
        func.setMaxCalls(1);

        on(device, func);
    }

    /**
     * Sets a new listener on the given device that can only be called once
     * @param device    the device to listen on
     * @param condition the condition that must be met in order to call the callback
     * @param callback  the callback to call when the condition is met
     * @param timeout   the timeout on the listener
     * @param <H>       the hardware device type
     */
    public <H extends HardwareDevice> void once(H device, HardwareCondition<H> condition, Runnable callback, long timeout) {
        HardwareListenerFunc<H> func = new HardwareListenerFunc<H>(condition, callback);
        func.setMaxCalls(1);
        func.setTimeout(timeout);

        on(device, func);
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
