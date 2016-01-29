package com.qualcomm.ftcrobotcontroller;

/**
 * Times a cycle of your opmode
 */
public class CycleTimer {

    private static double delta = 0;
    private static long lastTime = 0;

    /**
     * Updates the delta time
     *
     * @see #getDeltaTime()
     */
    public static void update() {
        if (lastTime == 0) {
            lastTime = System.nanoTime();
        }
        delta = (double) (lastTime - System.nanoTime()) / 1000000000.0;
        lastTime = System.nanoTime();
    }

    /**
     * Gets the delta time, the amount of time it took to execute your last loop
     * Requires looped calls to {@link #update()}
     *
     * @return The delta time
     */
    public static double getDeltaTime() {
        return delta;
    }

}
