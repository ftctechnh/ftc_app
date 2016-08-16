/*
 * Copyright (c) 2016 Arthur Pachachura, LASA Robotics, and contributors
 * MIT licensed
 *
 * Thank you to Brendan Hollaway from FTC team Venom for comment updates
 * to locateRectangles().
 */

package org.lasarobotics.vision.detection;

import org.lasarobotics.vision.detection.objects.Contour;
import org.lasarobotics.vision.detection.objects.Ellipse;
import org.lasarobotics.vision.detection.objects.Rectangle;
import org.lasarobotics.vision.image.Filter;
import org.lasarobotics.vision.util.MathUtil;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.MatOfPoint2f;
import org.opencv.imgproc.Imgproc;

import java.util.ArrayList;
import java.util.List;

/**
 * Implements primitive (ellipse, polygon) detection based on a custom, highly-robust (size and position invariant) version of the Hough transform
 */
public class PrimitiveDetection {

    private static final int THRESHOLD_CANNY = 75;
    private static final int APERTURE_CANNY = 3;
    private static final double MAX_COSINE_VALUE = 0.5;
    private static final double EPLISON_APPROX_TOLERANCE_FACTOR = 0.02;

    /**
     * Locate ellipses within an image
     *
     * @param grayImage Grayscale image
     * @return Ellipse locations
     */
    public static EllipseLocationResult locateEllipses(Mat grayImage) {
        Mat gray = grayImage.clone();

        Filter.downsample(gray, 2);
        Filter.upsample(gray, 2);

        Imgproc.Canny(gray, gray, 5, 75, 3, true);
        Filter.dilate(gray, 2);

        Mat cacheHierarchy = new Mat();

        List<MatOfPoint> contoursTemp = new ArrayList<>();
        //Find contours - the parameters here are very important to compression and retention
        Imgproc.findContours(gray, contoursTemp, cacheHierarchy, Imgproc.CV_RETR_TREE, Imgproc.CHAIN_APPROX_TC89_KCOS);

        //List contours
        List<Contour> contours = new ArrayList<>();
        for (MatOfPoint co : contoursTemp) {
            contours.add(new Contour(co));
        }

        //Find ellipses by finding fit
        List<Ellipse> ellipses = new ArrayList<>();
        for (MatOfPoint co : contoursTemp) {
            contours.add(new Contour(co));
            //Contour must have at least 6 points for fitEllipse
            if (co.toArray().length < 6)
                continue;
            //Copy MatOfPoint to MatOfPoint2f
            MatOfPoint2f matOfPoint2f = new MatOfPoint2f(co.toArray());
            //Fit an ellipse to the current contour
            Ellipse ellipse = new Ellipse(Imgproc.fitEllipse(matOfPoint2f));

            //Draw ellipse
            ellipses.add(ellipse);
        }

        return new EllipseLocationResult(contours, ellipses);
    }


    //TODO convert this to locatePolygons() with n sides
    //TODO see http://opencv-code.com/tutorials/detecting-simple-shapes-in-an-image/

    /**
     * Locate rectangles in an image
     *
     * @param grayImage Grayscale image
     * @return Rectangle locations
     */
    public RectangleLocationResult locateRectangles(Mat grayImage) {
        Mat gray = grayImage.clone();

        //Filter out some noise by halving then doubling size
        Filter.downsample(gray, 2);
        Filter.upsample(gray, 2);

        //Mat is short for Matrix, and here is used to store an image.
        //it is n-dimensional, but as an image, is two-dimensional
        Mat cacheHierarchy = new Mat();
        Mat grayTemp = new Mat();
        List<Rectangle> rectangles = new ArrayList<>();
        List<Contour> contours = new ArrayList<>();

        //This finds the edges using a Canny Edge Detector
        //It is sent the grayscale Image, a temp Mat, the lower detection threshold for an edge,
        //the higher detection threshold, the Aperture (blurring) of the image - higher is better
        //for long, smooth edges, and whether a more accurate version (but time-expensive) version
        //should be used (true = more accurate)
        //Note: the edges are stored in "grayTemp", which is an image where everything
        //is black except for gray-scale lines delineating the edges.
        Imgproc.Canny(gray, grayTemp, 0, THRESHOLD_CANNY, APERTURE_CANNY, true);
        //make the white lines twice as big, while leaving the image size constant
        Filter.dilate(gray, 2);

        List<MatOfPoint> contoursTemp = new ArrayList<>();
        //Find contours - the parameters here are very important to compression and retention
        //grayTemp is the image from which the contours are found,
        //contoursTemp is where the resultant contours are stored (note: color is not retained),
        //cacheHierarchy is the parent-child relationship between the contours (e.g. a contour
        //inside of another is its child),
        //Imgproc.CV_RETR_LIST disables the hierarchical relationships being returned,
        //Imgproc.CHAIN_APPROX_SIMPLE means that the contour is compressed from a massive chain of
        //paired coordinates to just the endpoints of each segment (e.g. an up-right rectangular
        //contour is encoded with 4 points.)
        Imgproc.findContours(grayTemp, contoursTemp, cacheHierarchy, Imgproc.CV_RETR_LIST, Imgproc.CHAIN_APPROX_SIMPLE);
        //MatOfPoint2f means that is a MatofPoint (Matrix of Points) represented by floats instead of ints
        MatOfPoint2f approx = new MatOfPoint2f();
        //For each contour, test whether the contour is a rectangle
        //List<Contour> contours = new ArrayList<>()
        for (MatOfPoint co : contoursTemp) {
            //converting the MatOfPoint to MatOfPoint2f
            MatOfPoint2f matOfPoint2f = new MatOfPoint2f(co.toArray());
            //converting the matrix to a Contour
            Contour c = new Contour(co);

            //Attempt to fit the contour to the best polygon
            //input: matOfPoint2f, which is the contour found earlier
            //output: approx, which is the MatOfPoint2f that holds the new polygon that has less vertices
            //basically, it smooths out the edges using the third parameter as its approximation accuracy
            //final parameter determines whether the new approximation must be closed (true=closed)
            Imgproc.approxPolyDP(matOfPoint2f, approx,
                    c.arcLength(true) * EPLISON_APPROX_TOLERANCE_FACTOR, true);

            //converting the MatOfPoint2f to a contour
            Contour approxContour = new Contour(approx);

            //Make sure the contour is big enough, CLOSED (convex), and has exactly 4 points
            if (approx.toArray().length == 4 &&
                    Math.abs(approxContour.area()) > 1000 &&
                    approxContour.isClosed()) {

                //TODO contours and rectangles array may not match up, but why would they?
                contours.add(approxContour);

                //Check each angle to be approximately 90 degrees
                //Done by comparing the three points constituting the angle of each corner
                double maxCosine = 0;
                for (int j = 2; j < 5; j++) {
                    double cosine = Math.abs(MathUtil.angle(approx.toArray()[j % 4],
                            approx.toArray()[j - 2], approx.toArray()[j - 1]));
                    maxCosine = Math.max(maxCosine, cosine);
                }

                if (maxCosine < MAX_COSINE_VALUE) {
                    //Convert the points to a rectangle instance
                    rectangles.add(new Rectangle(approx.toArray()));
                }
            }
        }

        return new RectangleLocationResult(contours, rectangles);
    }

    /**
     * Contains the list of rectangles retrieved from locateRectangles()
     */
    public static class RectangleLocationResult {
        final List<Contour> contours;
        final List<Rectangle> ellipses;

        RectangleLocationResult(List<Contour> contours, List<Rectangle> ellipses) {
            this.contours = contours;
            this.ellipses = ellipses;
        }

        /**
         * Gets list of contours in the image , as processed by Canny detection
         *
         * @return List of contours in the image
         */
        public List<Contour> getContours() {
            return contours;
        }

        /**
         * Gets list of rectangles detected in the image
         *
         * @return List of rectangles detected in the image
         */
        public List<Rectangle> getRectangles() {
            return ellipses;
        }
    }

    /**
     * Contains the list of ellipses retrieved from locateEllipses()
     */
    public static class EllipseLocationResult {
        final List<Contour> contours;
        final List<Ellipse> ellipses;

        EllipseLocationResult(List<Contour> contours, List<Ellipse> ellipses) {
            this.contours = contours;
            this.ellipses = ellipses;
        }

        /**
         * Gets list of contours in the image, as processed by Canny detection
         *
         * @return List of contours in the image
         */
        public List<Contour> getContours() {
            return contours;
        }

        /**
         * Get list of ellipses located in the image
         *
         * @return List of ellipses detected in the image
         */
        public List<Ellipse> getEllipses() {
            return ellipses;
        }
    }
}
