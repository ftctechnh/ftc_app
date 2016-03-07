package org.lasarobotics.vision.test.opmode.extensions;

import org.lasarobotics.vision.test.opmode.VisionOpMode;
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
