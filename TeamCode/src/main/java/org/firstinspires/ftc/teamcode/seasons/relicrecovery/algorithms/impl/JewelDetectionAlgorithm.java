package org.firstinspires.ftc.teamcode.seasons.relicrecovery.algorithms.impl;

import org.firstinspires.ftc.teamcode.mechanism.IMechanism;
import org.firstinspires.ftc.teamcode.mechanism.impl.VisionHelper;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;
import org.opencv.imgproc.Moments;

import java.util.ArrayList;
import java.util.List;

/**
 *
 */

public class JewelDetectionAlgorithm implements IMechanism {
    private VisionHelper vision;

    /**
     *
     */
    public enum JewelConfiguration {
        RED_BLUE, BLUE_RED
    }

    /**
     *
     * @param vision
     */
    public JewelDetectionAlgorithm(VisionHelper vision) {
        this.vision = vision;
    }

    /**
     *
     * @return
     */
    public JewelConfiguration detectJewelConfiguration() {
        Mat image = vision.readOpenCVFrame();

        List<MatOfPoint> redContours = new ArrayList<>();
        List<MatOfPoint> blueContours = new ArrayList<>();

        // blur before processing
        Imgproc.blur(image, image, new Size(4, 4));

        // threshold red image
        Mat redImage = new Mat();
        Core.inRange(image, new Scalar(0, 0, 200), new Scalar(100, 100, 255), redImage);

        // threshold blue image
        Mat blueImage = new Mat();
        Core.inRange(image, new Scalar(200, 0, 0), new Scalar(255, 100, 100), blueImage);

        // TODO: maybe dilate/erode?

        Imgproc.findContours(redImage, redContours, new Mat(), Imgproc.RETR_TREE, Imgproc.CHAIN_APPROX_SIMPLE);
        Imgproc.findContours(blueImage, blueContours, new Mat(), Imgproc.RETR_TREE, Imgproc.CHAIN_APPROX_SIMPLE);

        return null;
    }
}
