package com.disnodeteam.dogecv.math;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;

import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

import java.util.ArrayList;
import java.util.List;

public class MathFTC {

    public static final float mmPerInch        = 25.4f;
    public static final float mmFTCFieldWidth  = (12*6) * mmPerInch;       // the width of the FTC field (from the center point to the outer panels)
    public static final float mmTargetHeight   = (6) * mmPerInch;          // the height of the center of the target image above the floor

    /**
     * A simple clamp function, which assumes a valid range.
     * @param value The double being clamped
     * @param min The minimum value of the range (inclusive)
     * @param max The maximum value of the range (inclusive)
     * @return
     */
    public static double clip(double value, double min, double max) {
        if (value > max) return max;
        else if (value < min) return min;
        else return value;
    }

    /**
     * Returns the arithmetic mean (average) of a list
     * @param list A list of doubles
     * @return The mean
     */
    public static double mean(double[] list) {
        if(list.length == 0) {
            return 0;
        }
        double sum = 0;
        for (int i = 0; i < list.length; i++) {
            sum += list[i];
        }
        return sum / list.length;
    }

    /**
     * Returns the arithmetic mean (average) of a list
     * @param list A list of doubles
     * @return The mean
     */
    public static double mean(Double[] list) {
        if(list.length == 0) {
            return 0;
        }
        double sum = 0;
        for (int i = 0; i < list.length; i++) {
            sum += list[i];
        }
        return sum / list.length;
    }

    /**
     * A recursive function which return a list of lists, where each list is a combination of length k.
     * @param list The input list to be chosen from
     * @param k The sample size
     * @return
     */
    public static <T> List<List<T>> combinations(List<T> list, int k) {
        int n = list.size();
        List<List<T>> combos = new ArrayList<List<T>>();
        if (k == 0 ) {
            combos.add(new ArrayList<T>());
            return combos;
        }
        if ( n < k || n == 0) {
            return combos;
        }
        T last = list.get(n-1);
        combos.addAll(combinations(list.subList(0, n-1),k));
        for (List<T> subCombo : combinations(list.subList(0, n-1), k-1)) {
            subCombo.add(last);
            combos.add(subCombo);
        }
        return combos;
    }

    /**
     * Returns the standard deviation of a sample set
     * @param samples A list of Doubles
     * @return Sigma (i.e. the standard deviation)
     */
    public static double getStdDev(List<Double> samples) {
        if(samples.size() == 0) return 0;
        double mean = 0;
        for (Double point : samples) {
            mean += point;
        }
        mean = mean/samples.size();
        double sigma = 0;
        for (Double point : samples) {
            sigma += Math.pow(point-mean, 2);
        }
        return Math.sqrt(sigma/(samples.size() - 1));
    }

    /**
     * Normalizes an angle to always return between 0 and 180 degrees
     * @param angle Input angle, in degrees
     * @return Noramlized angle, in degrees
     */
    public static double normalizeAngle(double angle) {
        angle = angle % 180;
        if (angle > 0) return angle;
        else return angle+180;
    }

    /**
     * Crops an image to two specified corners
     * @param image The image to be cropped
     * @param topLeftCorner The top-left corner of the desired final image, in pixel coordinates
     * @param bottomRightCorner The bottom-right corner of the desired final image, in pixel coordinates
     * @return The cropped image
     */
    public static Mat crop(Mat image, Point topLeftCorner, Point bottomRightCorner) {
        if (topLeftCorner != null) {
            if(topLeftCorner.y > 0 && topLeftCorner.y < image.height()-1 && topLeftCorner.x > 0 && topLeftCorner.x < image.width()) {
                Imgproc.rectangle(image, new Point(0,0), new Point(image.width(),topLeftCorner.y), new Scalar(0), -1);
                Imgproc.rectangle(image, new Point(0,0), new Point(topLeftCorner.x, image.height()), new Scalar(0), -1);
            }
        }
        if(bottomRightCorner != null) {
            if(bottomRightCorner.y > 0 && bottomRightCorner.y < image.height()-1 && bottomRightCorner.x > 0 && bottomRightCorner.x < image.width()) {
                Imgproc.rectangle(image, new Point(image.width(),image.height()), new Point(bottomRightCorner.x,0), new Scalar(0), -1);
                Imgproc.rectangle(image, new Point(image.width(),image.height()), new Point(0, bottomRightCorner.y), new Scalar(0), -1);
            }
        }
        return image;
    }

    public static Size fullscreen(Size originalSize, Size maxSize) {
        double adjustedHeight = maxSize.width*originalSize.height/originalSize.width;
        if(adjustedHeight > maxSize.height) {
            double adjustedWidth= maxSize.height*originalSize.width/originalSize.height;
            return new Size(adjustedWidth, maxSize.height);
        } else {
            return new Size(maxSize.width, adjustedHeight);
        }
    }
}
