package com.disnodeteam.dogecv.detectors;

import com.disnodeteam.dogecv.OpenCVPipeline;
import com.disnodeteam.dogecv.filters.DogeCVColorFilter;
import com.disnodeteam.dogecv.filters.LeviColorFilter;

import org.opencv.core.Mat;

/**
 * Created by Victo on 12/17/2017.
 */

public class BlankDetector extends OpenCVPipeline {
    @Override
    public Mat processFrame(Mat rgba, Mat gray) {
        return rgba;
    }
}
