package com.disnodeteam.dogecv.detectors.roverrukus;

import com.disnodeteam.dogecv.detectors.DogeCVDetector;

import org.opencv.core.Mat;

/**
 * Created by LeviG on 1/20/2019.
 */

public class VuMarkDetector extends DogeCVDetector {

    @Override
    public Mat process(Mat rgba) {
        return rgba;
    }
    @Override
    public void useDefaults() {}
}
