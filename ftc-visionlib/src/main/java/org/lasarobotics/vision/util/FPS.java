/*
 * Copyright (c) 2016 Arthur Pachachura, LASA Robotics, and contributors
 * MIT licensed
 */
package org.lasarobotics.vision.util;

import java.text.DecimalFormat;

/**
 * Allows counting and retrieving a frames per second count.
 */
public class FPS {
    private static final int FRAME_BUFFER_LENGTH = 5;
    private static final DecimalFormat FPS_FORMAT = new DecimalFormat("0.00");

    private final RollingAverage<Long> rollingAverage;
    private long lastTime;

    /**
     * Instantiate a new fps counter
     */
    public FPS() {
        rollingAverage = new RollingAverage<>(FRAME_BUFFER_LENGTH);
        lastTime = -1L;
    }

    /**
     * Update the FPS counter.
     * <p/>
     * Call this method EVERY FRAME!
     */
    public void update() {
        if (lastTime != -1L) {
            long delta = System.nanoTime() - lastTime;
            rollingAverage.addValue(delta);
        }
        lastTime = System.nanoTime();
    }

    /**
     * Get the frames per second count, as a double (prefer using getFPSString() instead).
     *
     * @return The FPS, as a double in frames/second.
     */
    private double getFPS() {
        double period = rollingAverage.getAverage() / 1000000000.0; //period: s
        return 1.0 / period; //frequency = 1/s
    }

    /**
     * Get a string coercing the length of the FPS decimal
     *
     * @return 0.00 decimal form of getFPS()
     */
    public String getFPSString() {
        return FPS_FORMAT.format(getFPS());
    }

    /**
     * Pause the FPS counter.
     * <p/>
     * Call this when you're not going to use it for a while
     */
    public void pause() {
        //make sure we resample the FPS on next update()
        lastTime = -1L;
    }

    /**
     * Resets the FPS counter.
     */
    public void reset() {
        rollingAverage.clear();
        lastTime = -1L;
    }
}
