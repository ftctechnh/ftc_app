package org.lasarobotics.vision.test.detection;

import org.lasarobotics.vision.test.detection.objects.Contour;
import org.lasarobotics.vision.test.detection.objects.Ellipse;
import org.lasarobotics.vision.test.detection.objects.Rectangle;
import org.lasarobotics.vision.test.image.Filter;
import org.lasarobotics.vision.test.util.MathUtil;
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

    public RectangleLocationResult locateRectangles(Mat grayImage) {
        Mat gray = grayImage.clone();

        //Filter out some noise
        Filter.downsample(gray, 2);
        Filter.upsample(gray, 2);

        Mat cacheHierarchy = new Mat();
        Mat grayTemp = new Mat();
        List<Rectangle> rectangles = new ArrayList<>();
        List<Contour> contours = new ArrayList<>();

        Imgproc.Canny(gray, grayTemp, 0, THRESHOLD_CANNY, APERTURE_CANNY, true);
        Filter.dilate(gray, 2);

        List<MatOfPoint> contoursTemp = new ArrayList<>();
        //Find contours - the parameters here are very important to compression and retention
        Imgproc.findContours(grayTemp, contoursTemp, cacheHierarchy, Imgproc.CV_RETR_LIST, Imgproc.CHAIN_APPROX_SIMPLE);

        //For each contour, test whether the contour is a rectangle
        //List<Contour> contours = new ArrayList<>();
        MatOfPoint2f approx = new MatOfPoint2f();
        for (MatOfPoint co : contoursTemp) {
            MatOfPoint2f matOfPoint2f = new MatOfPoint2f(co.toArray());
            Contour c = new Contour(co);

            //Attempt to fit the contour to the best polygon
            Imgproc.approxPolyDP(matOfPoint2f, approx,
                    c.arcLength(true) * EPLISON_APPROX_TOLERANCE_FACTOR, true);

            Contour approxContour = new Contour(approx);

            //Make sure the contour is big enough, CLOSED (convex), and has exactly 4 points
            if (approx.toArray().length == 4 &&
                    Math.abs(approxContour.area()) > 1000 &&
                    approxContour.isClosed()) {

                //TODO contours and rectangles array may not match up, but why would they?
                contours.add(approxContour);

                //Check each angle to be approximately 90 degrees
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

    //TODO convert this to locatePolygons() with n sides
    //TODO see http://opencv-code.com/tutorials/detecting-simple-shapes-in-an-image/

    public EllipseLocationResult locateEllipses(Mat grayImage) {
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

    public class RectangleLocationResult {
        List<Contour> contours;
        List<Rectangle> ellipses;

        RectangleLocationResult(List<Contour> contours, List<Rectangle> ellipses) {
            this.contours = contours;
            this.ellipses = ellipses;
        }

        public List<Contour> getContours() {
            return contours;
        }

        public List<Rectangle> getRectangles() {
            return ellipses;
        }
    }

    public class EllipseLocationResult {
        List<Contour> contours;
        List<Ellipse> ellipses;

        EllipseLocationResult(List<Contour> contours, List<Ellipse> ellipses) {
            this.contours = contours;
            this.ellipses = ellipses;
        }

        public List<Contour> getContours() {
            return contours;
        }

        public List<Ellipse> getEllipses() {
            return ellipses;
        }
    }
}
