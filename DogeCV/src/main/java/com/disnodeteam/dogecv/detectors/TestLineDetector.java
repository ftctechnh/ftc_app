package com.disnodeteam.dogecv.detectors;

import com.disnodeteam.dogecv.OpenCVPipeline;
import com.disnodeteam.dogecv.math.Line;
import com.disnodeteam.dogecv.math.Lines;

import org.opencv.core.Mat;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;

import java.util.List;

/**
 * Created by Victo on 9/9/2018.
 */

public class TestLineDetector extends OpenCVPipeline {
    @Override
    public Mat processFrame(Mat rgba, Mat gray) {
        List<Line> lines =  Lines.getOpenCvLines(rgba,1,100);
        for(Line line : lines){

            Imgproc.line(rgba,line.point1, line.point2,new Scalar(0,255,0), 1);
        }
        return rgba;
    }

}
