package org.lasarobotics.vision.test.opmode;

import org.opencv.core.Mat;

public abstract class ManualVisionOpMode extends VisionOpModeCore {
    public final Mat frame(Mat rgba, Mat gray, boolean ready) {
        return frame(rgba, gray);
    }

    public abstract Mat frame(Mat rgba, Mat gray);
}
