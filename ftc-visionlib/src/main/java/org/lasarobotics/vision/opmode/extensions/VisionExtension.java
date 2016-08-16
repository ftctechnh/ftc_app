/*
 * Copyright (c) 2016 Arthur Pachachura, LASA Robotics, and contributors
 * MIT licensed
 */
package org.lasarobotics.vision.opmode.extensions;

import org.lasarobotics.vision.opmode.VisionOpMode;
import org.opencv.core.Mat;

/**
 * Interface for vision extensions for VisionOpMode
 */
public interface VisionExtension {
    void init(VisionOpMode opmode);

    void loop(VisionOpMode opmode);

    Mat frame(VisionOpMode opmode, Mat rgba, Mat gray);

    void stop(VisionOpMode opmode);
}
