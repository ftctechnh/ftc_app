package org.lasarobotics.vision.test.detection;

import org.lasarobotics.vision.test.detection.objects.Contour;
import org.lasarobotics.vision.test.image.Drawing;
import org.lasarobotics.vision.test.util.color.Color;
import org.lasarobotics.vision.test.util.color.ColorHSV;
import org.lasarobotics.vision.test.util.color.ColorSpace;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;

import java.util.ArrayList;
import java.util.List;

/**
 * Implements blob (regional) detection based on color
 */
public class ColorBlobDetector {

    private final List<Contour> contours = new ArrayList<>();
    // Cache
    private final Mat mPyrDownMat = new Mat();
    private final Mat mHsvMat = new Mat();
    private final Mat mMaskOne = new Mat();
    private final Mat mMask = new Mat();
    private final Mat mDilatedMask = new Mat();
    private final Mat mHierarchy = new Mat();
    //Lower bound for range checking
    private ColorHSV lowerBound = new ColorHSV(0, 0, 0);
    //Upper bound for range checking
    private ColorHSV upperBound = new ColorHSV(0, 0, 0);
    //Color radius for range checking
    private Scalar colorRadius = new Scalar(75, 75, 75, 0);
    //Currently selected color
    private Color color;
    //True if radius is set, false if lower and upper bound is set
    private boolean isRadiusSet = true;

    public ColorBlobDetector(Color color) {
        setColor(color);
    }

    public ColorBlobDetector(Color color, Color colorRadius) {
        this.colorRadius = colorRadius.convertColorScalar(ColorSpace.HSV);
        setColor(color);
    }

    public ColorBlobDetector(ColorHSV colorMinimum, ColorHSV colorMaximum) {
        setColorRadius(colorMinimum, colorMaximum);
    }

    public void setColor(Color color) {
        if (color == null)
            throw new IllegalArgumentException("Color must not be null!");

        if (isRadiusSet)
            return;

        this.color = color;
        Scalar hsvColor = color.convertColorScalar(ColorSpace.HSV);

        //calculate min and max hues
        double minH = (hsvColor.val[0] >= colorRadius.val[0]) ? hsvColor.val[0] - colorRadius.val[0] : 0;
        double maxH = (hsvColor.val[0] + colorRadius.val[0] <= 255) ? hsvColor.val[0] + colorRadius.val[0] : 255;

        Scalar lowerBoundScalar = lowerBound.getScalar();
        Scalar upperBoundScalar = upperBound.getScalar();

        lowerBoundScalar.val[0] = minH;
        upperBoundScalar.val[0] = maxH;

        lowerBoundScalar.val[1] = hsvColor.val[1] - colorRadius.val[1];
        upperBoundScalar.val[1] = hsvColor.val[1] + colorRadius.val[1];

        lowerBoundScalar.val[2] = hsvColor.val[2] - colorRadius.val[2];
        upperBoundScalar.val[2] = hsvColor.val[2] + colorRadius.val[2];

        lowerBoundScalar.val[3] = 0;
        upperBoundScalar.val[3] = 255;

        lowerBound = new ColorHSV(lowerBoundScalar);
        upperBound = new ColorHSV(upperBoundScalar);
    }

    public void setColorRadius(Color lowerBound, Color upperBound) {
        isRadiusSet = false;
        Scalar lower = lowerBound.convertColorScalar(ColorSpace.HSV);
        Scalar upper = upperBound.convertColorScalar(ColorSpace.HSV);

        this.lowerBound = new ColorHSV(lower);
        this.upperBound = new ColorHSV(upper);
    }

    public void setColorRadius(ColorHSV radius) {
        isRadiusSet = true;
        this.colorRadius = radius.getScalar();
        //Update the bounds again
        setColor(color);
    }

    public void process(Mat rgbaImage) {
        Imgproc.pyrDown(rgbaImage, mPyrDownMat);
        Imgproc.pyrDown(mPyrDownMat, mPyrDownMat);

        Imgproc.cvtColor(mPyrDownMat, mHsvMat, Imgproc.COLOR_RGB2HSV_FULL);

        //Test whether we need two inRange operations (only if the hue crosses over 255)
        if (upperBound.getScalar().val[0] <= 255) {
            Core.inRange(mHsvMat, lowerBound.getScalar(), upperBound.getScalar(), mMask);
        } else {
            //We need two operations - we're going to OR the masks together
            Scalar lower = lowerBound.getScalar().clone();
            Scalar upper = upperBound.getScalar().clone();
            while (upper.val[0] > 255)
                upper.val[0] -= 255;
            double tmp = lower.val[0];
            lower.val[0] = 0;
            //Mask 1 - from 0 to n
            Core.inRange(mHsvMat, lower, upper, mMaskOne);
            //Mask 2 - from 255-n to 255
            lower.val[0] = tmp;
            upper.val[0] = 255;

            Core.inRange(mHsvMat, lower, upper, mMask);
            //OR the two masks
            Core.bitwise_or(mMaskOne, mMask, mMask);
        }

        //Dilate (blur) the mask to decrease processing power
        Imgproc.dilate(mMask, mDilatedMask, new Mat());

        List<MatOfPoint> contourListTemp = new ArrayList<>();

        Imgproc.findContours(mDilatedMask, contourListTemp, mHierarchy, Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_SIMPLE);

        // Filter contours by area and resize to fit the original image size
        contours.clear();
        for (MatOfPoint c : contourListTemp) {
            Core.multiply(c, new Scalar(4, 4), c);
            contours.add(new Contour(c));
        }
    }

    public void drawContours(Mat img, Color color) {
        Drawing.drawContours(img, contours, color);
    }

    public void drawContours(Mat img, Color color, int thickness) {
        Drawing.drawContours(img, contours, color, thickness);
    }

    public List<Contour> getContours() {
        return contours;
    }
}
