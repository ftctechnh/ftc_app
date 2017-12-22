package org.firstinspires.ftc.teamcode.libraries;

import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import java.util.Iterator;
import java.util.List;

/**
 * Created by Noah on 12/21/2017.
 */

abstract public class CrappyGraphLib extends DrawLib {
    private List<Double> data;
    private Mat temp;
    private Scalar color;
    private boolean stickyRange;
    private double stickyHigh = 0;
    private double stickLow = 0;

    //data is presumed to be in order and increments by one each element
    protected void initGraph(List<Double> data, boolean stickyRange) {
        super.initDraw();
        this.data = data;
        temp = new Mat(500, 800, CvType.CV_8UC3);
        color = new Scalar(0, 255, 0);
        this.stickyRange = stickyRange;
    }

    //data is presumed to be in order and increments by one each element
    protected void initGraph(List<Double> data) {
        initGraph(data, false);
    }

    protected void drawGraph() {
        if(data.size() > 0) {
            temp.setTo(new Scalar(0, 0, 0));
            //get the scale for the data
            double min = data.get(0), max = data.get(0);
            for (int i = 0; i < data.size(); i++) {
                double num = data.get(i);
                if(num < min) min = num;
                else if (num > max) max = num;
            }
            //range is min - (1/8 * (max-min)), max + (1/8 *(max-min))
            double range = max - min;
            double lower = min - 0.125*range;
            double upper = max + 0.125*range;

            if(stickyRange) {
                if(lower < stickLow || stickLow == 0) stickLow = lower;
                lower = stickLow;
                if(upper > stickyHigh || stickyHigh == 0) stickyHigh = upper;
                upper = stickyHigh;
                range = stickyHigh - stickLow;
            }
            else range = upper - lower;

            //now draw the graph!
            double xInc = (double)(temp.cols() - 10) / (double)data.size();
            double yInc = (double)temp.rows() / range;

            Point lastPoint;
            //draw the first point
            Imgproc.circle(temp, lastPoint = new Point(4, temp.rows() - (data.get(0) - lower) * yInc), 3, color);
            //draw the rest of the points with lines
            for(int i = 1; i < data.size(); i++) {
                //draw a line from the last point to the current point
                Imgproc.line(temp, lastPoint, lastPoint = new Point(i * xInc + 4, temp.rows() - (data.get(i) - lower) * yInc), color);
                //draw a circle at the current point
                Imgproc.circle(temp, lastPoint, 3, color);
            }
            drawFrame(temp);
        }

    }
}
