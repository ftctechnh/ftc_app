/*
 * Copyright (c) 2016 Arthur Pachachura, LASA Robotics, and contributors
 * MIT licensed
 */

package org.lasarobotics.vision.opmode;

/**
 * Vision Op Mode designed ONLY for testing applications, such as the Camera Test Activity
 * This OpMode essentially unifies testing applications and the robot controller
 */
public abstract class TestableVisionOpMode extends VisionOpMode {

    /**
     * Creates the Testable OpMode.
     */
    public TestableVisionOpMode() {
        super(false); //disable OpenCV core functions
    }

    @Override
    public void init() {
        super.init();
    }

    @Override
    public void loop() {
        super.loop();
    }

    @Override
    public void stop() {
        super.stop();
    }
}