/*
 * Copyright (c) 2016 Arthur Pachachura, LASA Robotics, and contributors
 * MIT licensed
 */
package org.lasarobotics.vision.opmode;

import org.opencv.core.Mat;

public abstract class ManualVisionOpMode extends VisionOpModeCore {
    public final Mat frame(Mat rgba, Mat gray, boolean ready) {
        return frame(rgba, gray);
    }

    /**
     * Returns every frame an image received from the camera.
     * If your method runs for too long, frames will be skipped (this is normal behaviour, reducing
     * FPS).
     *
     * @param rgba RGBA image
     * @param gray Grayscale image
     * @return Return an image to draw onto the screen
     */
    public abstract Mat frame(Mat rgba, Mat gray);
}
