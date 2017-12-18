package org.firstinspires.ftc.teamcode.seasons.relicrecovery.algorithms;

import android.content.Context;
import android.util.Log;

import org.firstinspires.ftc.robotcore.internal.system.AppUtil;
import org.firstinspires.ftc.teamcode.Robot;
import org.firstinspires.ftc.teamcode.mechanism.impl.VisionHelper;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 *
 */

public class JewelDetectionAlgorithm {
    private VisionHelper visionHelper;

    private CascadeClassifier jewelClassifier;
    private Context appContext;

    // dummy constant values for now
    private static final double RED_FACTOR_THRESHOLD = 0.8;
    private static final int RED_HUE_RANGE_MAX_1 = 20;
    private static final int RED_HUE_RANGE_MIN_2 = 120;

    private static final String LOG_TAG = "JewelDetection";

    /**
     *
     */
    public enum JewelConfiguration {
        RED_BLUE, BLUE_RED
    }

    /**
     *
     * @param robot
     * @param visionHelper
     */
    public JewelDetectionAlgorithm(Robot robot, VisionHelper visionHelper) {
        this.appContext = robot.getCurrentOpMode().hardwareMap.appContext;
        this.visionHelper = visionHelper;

        loadCascadeFile();
    }

    /**
     *
     * @return
     */
    public JewelConfiguration detect() {
        Mat frame = visionHelper.readOpenCVFrame();

        MatOfRect objects = new MatOfRect();

        // todo: perhaps provide more options here
        jewelClassifier.detectMultiScale(frame, objects);

        for(Rect roi: objects.toArray()) {
            Mat roiMat = new Mat(frame, roi);
            if(getRedFactor(roiMat) > RED_FACTOR_THRESHOLD) {
                return JewelConfiguration.RED_BLUE;
            }
        }

        return JewelConfiguration.BLUE_RED;
    }

    private void loadCascadeFile() {
        File cascadeFile = new File(AppUtil.ROBOT_DATA_DIR, "jewel_classifier.xml");

        try {
            if(!cascadeFile.exists()) {
                // OpenCV doesn't support directly using an android resource, so we have to copy it first
                InputStream is = appContext.getResources().openRawResource(0 /* R.raw.jewel_classifier */);
                OutputStream os = new FileOutputStream(cascadeFile);

                byte[] buffer = new byte[4096];
                int bytesRead;
                while ((bytesRead = is.read(buffer)) != -1) {
                    os.write(buffer, 0, bytesRead);
                }

                is.close();
                os.close();
            }
        } catch (IOException e) {
            Log.e(LOG_TAG,  "Failed to load cascade: " + e);
            e.printStackTrace();
        }

        String fileName = cascadeFile.getAbsolutePath();
        jewelClassifier = new CascadeClassifier(fileName);

        if (jewelClassifier.empty()) {
            Log.e(LOG_TAG, "External classifier file failed to load: " + fileName);
        } else {
            Log.i(LOG_TAG, "Classifier file successfully loaded");
        }
    }

    private double getRedFactor(Mat roiMat) {
        Mat hsv = new Mat();

        List<Mat> hsvChannels = new ArrayList<>();

        Imgproc.cvtColor(roiMat, hsv, Imgproc.COLOR_BGR2HSV);

        Core.split(hsv, hsvChannels);
        Mat hue = hsvChannels.get(0);

        Mat comp1 = new Mat();
        Core.compare(hue, new Scalar(RED_HUE_RANGE_MAX_1), comp1, Core.CMP_LE);

        Mat comp2 = new Mat();
        Core.compare(hue, new Scalar(RED_HUE_RANGE_MIN_2), comp2, Core.CMP_GE);

        Mat red = new Mat();
        Core.bitwise_or(comp1, comp2, red);

        double sumRedPixels = Core.sumElems(red).val[0] / 255;

        return sumRedPixels / (red.cols() * roiMat.rows());
    }
}
