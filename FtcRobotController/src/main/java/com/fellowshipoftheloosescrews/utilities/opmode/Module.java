package com.fellowshipoftheloosescrews.utilities.opmode;

/**
 * A Module represents a specific system on the robot that should
 * work independently of others. They are background tasks that should be
 * used for simple things (updating sensor values, monitoring time, updating
 * PID controllers, etc.) and loaded on the start so we don't have to deal with
 * too much at once.
 *
 *
 *
 * Created by Thomas on 9/18/2015.
 */
public abstract class Module {
    public String name;

    /**
     * Called when this module is added to the ModuleMap
     */
    public void onAddToMap(FellowshipOpMode.ModuleManager manager, FellowshipOpMode opMode, String name) {}

    /**
     * Called after all modules have been added to the list, but before the start
     */
    public void init() {}

    /**
     * Called after init, but before the first hardware cycle
     */
    public abstract void start();

    /**
     * Called once every hardware cycle
     */
    public abstract void update();

    /**
     * Called after the final update
     */
    public abstract void stop();
}
