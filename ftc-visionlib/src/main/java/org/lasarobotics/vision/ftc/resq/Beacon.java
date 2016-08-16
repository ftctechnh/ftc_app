/*
 * Copyright (c) 2016 Arthur Pachachura, LASA Robotics, and contributors
 * MIT licensed
 *
 * Thank you to Will Fisher (LASA), Brendan Hollaway (FTC Venom), and Arnav Mohan (LASA).
 */
package org.lasarobotics.vision.ftc.resq;

import org.lasarobotics.vision.detection.ColorBlobDetector;
import org.lasarobotics.vision.detection.objects.Ellipse;
import org.lasarobotics.vision.detection.objects.Rectangle;
import org.lasarobotics.vision.util.MathUtil;
import org.lasarobotics.vision.util.ScreenOrientation;
import org.lasarobotics.vision.util.color.ColorHSV;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.core.Size;

import java.text.DecimalFormat;

/**
 * Beacon location and analysis
 */
public final class Beacon {

    private AnalysisMethod method;
    private Rectangle bounds;
    private ColorBlobDetector blueDetector = new ColorBlobDetector(Constants.COLOR_BLUE_LOWER, Constants.COLOR_BLUE_UPPER);
    private ColorBlobDetector redDetector = new ColorBlobDetector(Constants.COLOR_RED_LOWER, Constants.COLOR_RED_UPPER);
    private boolean debug = false;

    /**
     * Instantiate a beacon that uses the default method
     */
    public Beacon() {
        method = AnalysisMethod.DEFAULT;
    }

    /**
     * Instantiate a beacon that uses a specific analysis method
     *
     * @param method Analysis method
     */
    public Beacon(AnalysisMethod method) {
        this.method = method;
    }

    /**
     * Instantiate a beacon that uses a specific analysis method
     *
     * @param method Analysis method
     */
    public Beacon(AnalysisMethod method, Rectangle bounds) {
        this.method = method;
        this.bounds = bounds;
    }

    private static double[] getColorWithTolerance(double[] color, double tolerance) {
        //tolerance is 1/x
        tolerance = 1 / ((MathUtil.coerce(-1.0, 1.0, -tolerance) / 1.25) + 1);
        //scale the color by the tolerance base 4
        color[0] = (0.25 * color[0] * tolerance) + (0.75 * color[0]);    //hue
        color[1] = (0.50 * color[1] * tolerance) + (0.50 * color[1]);    //saturation
        color[2] = (0.75 * color[2] * tolerance) + (0.25 * color[2]);    //value
        return color;
    }

    /**
     * Analyze the current frame using the selected analysis method
     *
     * @param img  Image to analyze
     * @param gray Grayscale image to analyze
     * @return Beacon analysis class
     */
    public BeaconAnalysis analyzeFrame(Mat img, Mat gray) {
        return analyzeFrame(this.redDetector, this.blueDetector, img, gray, ScreenOrientation.LANDSCAPE);
    }

    /**
     * Analyze the current frame using the selected analysis method
     *
     * @param img         Image to analyze
     * @param gray        Grayscale image to analyze
     * @param orientation Screen orientation compensation, given by the android.Sensors class
     * @return Beacon analysis class
     */
    public BeaconAnalysis analyzeFrame(Mat img, Mat gray, ScreenOrientation orientation) {
        return analyzeFrame(this.redDetector, this.blueDetector, img, gray, orientation);
    }

    /**
     * Analyze the current frame using the selected analysis method, using custom color blob detectors
     *
     * @param redDetector  Red color blob detector
     * @param blueDetector Blue color blob detector
     * @param img          Image to analyze
     * @param gray         Grayscale image to analyze
     * @param orientation  Screen orientation compensation, given by the android.Sensors class
     * @return Beacon analysis class
     */
    public BeaconAnalysis analyzeFrame(ColorBlobDetector redDetector, ColorBlobDetector blueDetector, Mat img, Mat gray, ScreenOrientation orientation) {
        if (this.bounds == null)
            this.bounds = new Rectangle(img.size());
        switch (method) {
            case REALTIME:
                blueDetector.process(img);
                redDetector.process(img);
                return BeaconAnalyzer.analyze_REALTIME(redDetector.getContours(), blueDetector.getContours(), img, orientation, this.debug);
            case FAST:
            case DEFAULT:
            default:
                return BeaconAnalyzer.analyze_FAST(redDetector, blueDetector, img, gray, orientation, this.bounds, this.debug);
            case COMPLEX:
                blueDetector.process(img);
                redDetector.process(img);
                return BeaconAnalyzer.analyze_COMPLEX(redDetector.getContours(), blueDetector.getContours(), img, gray, orientation, this.bounds, this.debug);
        }
    }

    /**
     * Get current analysis method
     *
     * @return Current analysis method
     */
    public AnalysisMethod getAnalysisMethod() {
        return method;
    }

    /**
     * Set analysis method
     *
     * @param method AnalysisMethod selection
     */
    public void setAnalysisMethod(AnalysisMethod method) {
        this.method = method;
    }

    /**
     * Set a rectangle to contain the analyzed area
     * An orange box will be shown containing the analyzed area
     * Only currently works on the FAST method
     *
     * @param bounds Rectangle containing the frame area to analyze
     */
    public void setAnalysisBounds(Rectangle bounds) {
        this.bounds = bounds;
    }

    /**
     * Reset analysis bounds to contain the entirety of a frame size
     *
     * @param frameSize Frame size
     */
    public void resetAnalysisBounds(Size frameSize) {
        this.bounds = new Rectangle(new Point(frameSize.width / 2, frameSize.height / 2), frameSize.width, frameSize.height);
    }

    /**
     * Set color tolerance for red beacon detector
     *
     * @param tolerance A color tolerance value from -1 to 1, where 0 is unmodified, 1 is maximum
     *                  tolerance (more colors detect as red), -1 is minimum (fery vew colors detect
     *                  as red)
     */
    public void setColorToleranceRed(double tolerance) {
        //Reset the detector first
        redDetector = new ColorBlobDetector(Constants.COLOR_RED_LOWER, Constants.COLOR_RED_UPPER);
        double[] center = redDetector.getColorCenter().getScalar().val;
        double[] radius = redDetector.getColorRadius().val;
        radius = getColorWithTolerance(radius, tolerance);
        Scalar lower = new Scalar(center[0] - radius[0], center[1] - radius[1], center[2] - radius[2]);
        Scalar upper = new Scalar(center[0] + radius[0], center[1] + radius[1], center[2] + radius[2]);
        redDetector = new ColorBlobDetector(new ColorHSV(lower), new ColorHSV(upper));
    }

    /**
     * Set color tolerance for blue beacon detector
     *
     * @param tolerance A color tolerance value from -1 to 1, where 0 is unmodified, 1 is maximum
     *                  tolerance (more colors detect as blue), -1 is minimum (fery vew colors detect
     *                  as blue)
     */
    public void setColorToleranceBlue(double tolerance) {
        //Reset the detector first
        blueDetector = new ColorBlobDetector(Constants.COLOR_BLUE_LOWER, Constants.COLOR_BLUE_UPPER);
        double[] center = blueDetector.getColorCenter().getScalar().val;
        double[] radius = blueDetector.getColorRadius().val;
        radius = getColorWithTolerance(radius, tolerance);
        Scalar lower = new Scalar(center[0] - radius[0], center[1] - radius[1], center[2] - radius[2]);
        Scalar upper = new Scalar(center[0] + radius[0], center[1] + radius[1], center[2] + radius[2]);
        blueDetector = new ColorBlobDetector(new ColorHSV(lower), new ColorHSV(upper));
    }

    /**
     * Enable debug displays.
     * Use this only on testing apps, otherwise it might slow your program
     * down a little bit or confuse your custom code
     */
    public void enableDebug() {
        this.debug = true;
    }

    /**
     * Disable debug displays (default)
     */
    public void disableDebug() {
        this.debug = false;
    }

    /**
     * Analysis method
     */
    public enum AnalysisMethod {
        /**
         * Default method, currently FAST
         */
        DEFAULT,
        /**
         * Extremely fast method that performs less accurate analysis
         * REALTIME is great when speed should best accuracy
         */
        REALTIME,
        /**
         * Faster method - selects the two largest contours and analyzes them
         * FAST is great when near the beacon, but is not suitable for long-distance analysis
         */
        FAST,
        /**
         * Slower and complex method - picks contours based on statistical analysis
         * COMPLEX is highly complex and a work in progress, but is better at selecting
         * the correct beacon at long distances, but requires that the entire beacon be in view.
         */
        COMPLEX;

        public String toString() {
            switch (this) {
                case REALTIME:
                    return "REALTIME";
                case DEFAULT:
                case FAST:
                default:
                    return "FAST";
                case COMPLEX:
                    return "COMPLEX";
            }
        }
    }

    /**
     * Beacon color struct
     */
    public enum BeaconColor {
        RED,
        BLUE,
        RED_BRIGHT,
        BLUE_BRIGHT,
        UNKNOWN;

        @Override
        public String toString() {
            switch (this) {
                case RED:
                    return "red";
                case BLUE:
                    return "blue";
                case RED_BRIGHT:
                    return "RED!";
                case BLUE_BRIGHT:
                    return "BLUE!";
                case UNKNOWN:
                default:
                    return "???";
            }
        }
    }

    /**
     * Beacon analysis struct
     */
    public static class BeaconAnalysis {
        private final double confidence;
        private final BeaconColor left;
        private final BeaconColor right;
        private final Rectangle location;
        private final Ellipse leftButton;
        private final Ellipse rightButton;

        //TODO Color and CONFIDENCE should make up the results

        //TODO add Distance distanceApprox

        /**
         * Instantiate a blank analysis
         */
        public BeaconAnalysis() {
            this.left = BeaconColor.UNKNOWN;
            this.right = BeaconColor.UNKNOWN;
            this.confidence = 0.0f;
            this.location = new Rectangle();
            this.leftButton = null;
            this.rightButton = null;
        }

        BeaconAnalysis(BeaconColor left, BeaconColor right, Rectangle location, double confidence) {
            this.left = left;
            this.right = right;
            this.confidence = confidence;
            this.location = location;
            this.leftButton = null;
            this.rightButton = null;
        }

        BeaconAnalysis(BeaconColor left, BeaconColor right, Rectangle location, double confidence,
                       Ellipse leftButton, Ellipse rightButton) {
            this.left = left;
            this.right = right;
            this.confidence = confidence;
            this.location = location;
            this.leftButton = leftButton;
            this.rightButton = rightButton;
        }

        /**
         * Get the ellipse containing the left button, if found
         *
         * @return Left button ellipse, or a blank ellipse if not found
         */
        public Ellipse getLeftButton() {
            return leftButton;
        }

        /**
         * Get the ellipse containing the right button, if found
         *
         * @return Right button ellipse, or a blank ellipse if not found
         */
        public Ellipse getRightButton() {
            return rightButton;
        }


        public boolean hasEllipses() {
            return (leftButton != null && rightButton != null);
        }

        /**
         * Get the bounding box surrounding the beacon
         *
         * @return Rectangle
         */
        public Rectangle getBoundingBox() {
            return location;
        }

        /**
         * Get the top left corner of the beacon
         *
         * @return Point
         */
        public Point getTopLeft() {
            return location.topLeft();
        }

        /**
         * Get the bottomr ight corner of the beacon
         *
         * @return Point
         */
        public Point getBottomRight() {
            return location.bottomRight();
        }

        /**
         * Get the center of the beacon
         *
         * @return Point
         */
        public Point getCenter() {
            return location.center();
        }

        /**
         * Get the width of the beacon
         *
         * @return Width of the beacon
         */
        public double getWidth() {
            return location.width();
        }

        /**
         * Get the height of the beacon
         *
         * @return Height of the beacon
         */
        public double getHeight() {
            return location.height();
        }

        /**
         * Get the color of the left side of the beacon
         *
         * @return Beacon color state
         */
        public BeaconColor getStateLeft() {
            return left;
        }

        /**
         * Get the color of the right side of the beacon
         *
         * @return Beacon color state
         */
        public BeaconColor getStateRight() {
            return right;
        }

        /**
         * Get a confidence value that the beacon analysis is correct
         * <p/>
         * This is an approximation, but can be used carefully to filter out random noise.
         * <p/>
         * Also, only certain analysis methods provide a confidence - this will then return zero.
         *
         * @return Confidence, if applicable - zero if not applicable
         */
        public double getConfidence() {
            return !Double.isNaN(confidence) ? MathUtil.coerce(0, 1, confidence) : 0.0;
        }

        /**
         * Get a confidence string that the beacon analysis is correct
         * <p/>
         * This is an approximation, but can be used carefully to filter out random noise.
         * <p/>
         * Also, only certain analysis methods provide a confidence - this will then return zero.
         *
         * @return Confidence, if applicable - "N/A" if not applicable
         */
        public String getConfidenceString() {
            final DecimalFormat format = new DecimalFormat("0.000");
            return !Double.isNaN(confidence) ? format.format(MathUtil.coerce(0, 1, getConfidence()) * 100.0f) + "%" : "N/A";
        }

        /**
         * Test whether if the left side has a known color
         *
         * @return True if the left side is NOT UNKNOWN
         */
        public boolean isLeftKnown() {
            return left != BeaconColor.UNKNOWN;
        }

        /**
         * Test whether if the left side is blueDetector
         *
         * @return True if the left side is BLUE or BLUE_BRIGHT
         */
        public boolean isLeftBlue() {
            return (left == BeaconColor.BLUE_BRIGHT || left == BeaconColor.BLUE);
        }

        /**
         * Test whether if the left side is red
         *
         * @return True if the left side is RED or RED_BRIGHT
         */
        public boolean isLeftRed() {
            return (left == BeaconColor.RED_BRIGHT || left == BeaconColor.RED);
        }

        /**
         * Test whether if the right side has a known color
         *
         * @return True if the right side is NOT UNKNOWN
         */
        public boolean isRightKnown() {
            return right != BeaconColor.UNKNOWN;
        }

        /**
         * Test whether if the right side is blueDetector
         *
         * @return True if the right side is BLUE or BLUE_BRIGHT
         */
        public boolean isRightBlue() {
            return (right == BeaconColor.BLUE_BRIGHT || right == BeaconColor.BLUE);
        }

        /**
         * Test whether if the right side is red
         *
         * @return True if the right side is RED or RED_BRIGHT
         */
        public boolean isRightRed() {
            return (right == BeaconColor.RED_BRIGHT || right == BeaconColor.RED);
        }

        /**
         * Test whether the beacon is found
         *
         * @return isLeftKnown() && isRightKnown()
         */
        public boolean isBeaconFound() {
            return isLeftKnown() && isRightKnown();
        }

        /**
         * Test whether the beacon is fully lit
         * <p/>
         * Note that in this revision, brightness is not supported
         *
         * @return True if both sides have a bright component
         */
        public boolean isBeaconFullyLit() {
            return (left == BeaconColor.BLUE_BRIGHT || left == BeaconColor.RED_BRIGHT) &&
                    (right == BeaconColor.BLUE_BRIGHT || right == BeaconColor.RED_BRIGHT);
        }

        /**
         * Get a string representing the colors of the beacon
         *
         * @return left, right
         */
        public String getColorString() {
            return left.toString() + ", " + right.toString();
        }

        /**
         * Get the location of the beacon as a string
         *
         * @return Center of the beacon
         */
        public String getLocationString() {
            return getCenter().toString();
        }

        @Override
        public String toString() {
            return "Color: " + getColorString() + "\r\n Location: " + getLocationString() + "\r\n Confidence: " + getConfidenceString();
        }

        public String getButtonString() {
            return "Left: " + (leftButton != null ? leftButton.getLocationString() : "N/A") +
                    " Right: " + (rightButton != null ? rightButton.getLocationString() : "N/A");
        }
    }
}
