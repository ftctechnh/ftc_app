/*
 * Copyright (c) 2016 Arthur Pachachura, LASA Robotics, and contributors
 * MIT licensed
 *
 * Thank you to Brendan Hollaway (FTC Venom).
 */

package org.lasarobotics.vision.ftc.resq;

import android.util.Log;

import org.lasarobotics.vision.detection.ColorBlobDetector;
import org.lasarobotics.vision.detection.PrimitiveDetection;
import org.lasarobotics.vision.detection.objects.Contour;
import org.lasarobotics.vision.detection.objects.Detectable;
import org.lasarobotics.vision.detection.objects.Ellipse;
import org.lasarobotics.vision.detection.objects.Rectangle;
import org.lasarobotics.vision.image.Drawing;
import org.lasarobotics.vision.util.MathUtil;
import org.lasarobotics.vision.util.ScreenOrientation;
import org.lasarobotics.vision.util.color.ColorRGBA;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.RotatedRect;
import org.opencv.core.Size;

import java.util.List;

/**
 * Static beacon analysis methods
 */
class BeaconAnalyzer {

    static Beacon.BeaconAnalysis analyze_REALTIME(List<Contour> contoursRed, List<Contour> contoursBlue,
                                                  Mat img, ScreenOrientation orientation, boolean debug) {
        //DEBUG Draw contours before filtering
        if (debug) Drawing.drawContours(img, contoursRed, new ColorRGBA("#FF0000"), 2);
        if (debug) Drawing.drawContours(img, contoursBlue, new ColorRGBA("#0000FF"), 2);

        //Get the largest contour in each - we're calling this one the main light
        int largestIndexRed = findLargestIndex(contoursRed);
        int largestIndexBlue = findLargestIndex(contoursBlue);
        Contour largestRed = (largestIndexRed != -1) ? contoursRed.get(largestIndexRed) : null;
        Contour largestBlue = (largestIndexBlue != -1) ? contoursBlue.get(largestIndexBlue) : null;

        //If we don't have a main light for one of the colors, we know both colors are the same
        if (largestRed == null || largestBlue == null)
            return new Beacon.BeaconAnalysis();

        //The height of the beacon on screen is the height of the best contour
        Contour largestHeight = ((largestRed.size().height) > (largestBlue.size().height)) ? largestRed : largestBlue;
        double beaconHeight = largestHeight.size().height;

        //Look at the locations of the largest contours
        //Check to see if the largest red contour is more left-most than the largest right contour
        //If it is, then we know that the left beacon is red and the other blue, and vice versa
        Point bestRedCenter = largestRed.centroid();
        Point bestBlueCenter = largestBlue.centroid();

        //DEBUG R/B text
        if (debug)
            Drawing.drawText(img, "R", bestRedCenter, 1.0f, new ColorRGBA(255, 0, 0));
        if (debug)
            Drawing.drawText(img, "B", bestBlueCenter, 1.0f, new ColorRGBA(0, 0, 255));

        //Test which side is red and blue
        //If the distance between the sides is smaller than a value, then return unknown
        //Figure out which way to read the image
        double orientationAngle = orientation.getAngle();
        boolean swapLeftRight = orientationAngle >= 180; //swap if LANDSCAPE_WEST or PORTRAIT_REVERSE
        boolean readOppositeAxis = orientation == ScreenOrientation.PORTRAIT ||
                orientation == ScreenOrientation.PORTRAIT_REVERSE; //read other axis if any kind of portrait

        boolean leftIsRed;
        Contour leftMostContour, rightMostContour;
        if (readOppositeAxis) {
            if (bestRedCenter.y < bestBlueCenter.y) {
                leftIsRed = true;
            } else if (bestBlueCenter.y < bestRedCenter.y) {
                leftIsRed = false;
            } else {
                return new Beacon.BeaconAnalysis();
            }
        } else {
            if (bestRedCenter.x < bestBlueCenter.x) {
                leftIsRed = true;
            } else if (bestBlueCenter.x < bestRedCenter.x) {
                leftIsRed = false;
            } else {
                return new Beacon.BeaconAnalysis();
            }
        }

        //Swap left and right if necessary
        leftIsRed = swapLeftRight != leftIsRed;

        //Get the left-most best contour (or top-most if axis swapped) (or right-most if L/R swapped)
        if (readOppositeAxis) {
            //Get top-most best contour
            leftMostContour = ((largestRed.topLeft().y) < (largestBlue.topLeft().y)) ? largestRed : largestBlue;
            //Get bottom-most best contour
            rightMostContour = ((largestRed.topLeft().y) < (largestBlue.topLeft().y)) ? largestBlue : largestRed;
        } else {
            //Get left-most best contour
            leftMostContour = ((largestRed.topLeft().y) < (largestBlue.topLeft().y)) ? largestRed : largestBlue;
            //Get the right-most best contour
            rightMostContour = ((largestRed.topLeft().y) < (largestBlue.topLeft().y)) ? largestBlue : largestRed;
        }

        //Swap left and right if necessary
        //BUGFIX: invert when we swap
        if (swapLeftRight) {
            Contour temp = leftMostContour;
            leftMostContour = rightMostContour;
            rightMostContour = temp;
        }

        //Get the approximate bounding box of the contours
        double widthContours = rightMostContour.right() - leftMostContour.left();
        double heightContours = Math.max(leftMostContour.height(), rightMostContour.height());

        //Center of contours is the average of centers of the contours
        Point lCenter = leftMostContour.centroid();
        Point rCenter = rightMostContour.centroid();
        Point center = new Point((lCenter.x + rCenter.x) / 2,
                (lCenter.y + rCenter.y) / 2);
        if (debug) Drawing.drawCross(img, center, new ColorRGBA("#ffffff"), 10, 4);
        Rectangle centerRect = new Rectangle(center, widthContours, heightContours);

        //Calculate confidence
        double widthBeacon = rightMostContour.right() - leftMostContour.left();
        double WH_ratio = widthBeacon / beaconHeight;
        double ratioError = Math.abs((Constants.BEACON_WH_RATIO - WH_ratio)) / Constants.BEACON_WH_RATIO; // perfect value = 0;
        double averageHeight = (leftMostContour.height() + rightMostContour.height()) / 2.0;
        double dy = Math.abs((lCenter.y - rCenter.y) / averageHeight * Constants.FAST_HEIGHT_DELTA_FACTOR);
        double dArea = Math.sqrt(leftMostContour.area() / rightMostContour.area());
        double confidence = MathUtil.normalPDFNormalized(
                MathUtil.distance(MathUtil.distance(ratioError, dy), dArea) / Constants.FAST_CONFIDENCE_ROUNDNESS,
                Constants.FAST_CONFIDENCE_NORM, 0.0);

        if (leftIsRed)
            return new Beacon.BeaconAnalysis(Beacon.BeaconColor.RED, Beacon.BeaconColor.BLUE, centerRect, confidence);
        else
            return new Beacon.BeaconAnalysis(Beacon.BeaconColor.BLUE, Beacon.BeaconColor.RED, centerRect, confidence);
    }

    static Beacon.BeaconAnalysis analyze_FAST(ColorBlobDetector detectorRed, ColorBlobDetector detectorBlue,
                                              Mat img, Mat gray, ScreenOrientation orientation, Rectangle bounds, boolean debug) {
        //Figure out which way to read the image
        double orientationAngle = orientation.getAngle();
        boolean swapLeftRight = orientationAngle >= 180; //swap if LANDSCAPE_WEST or PORTRAIT_REVERSE
        boolean readOppositeAxis = orientation == ScreenOrientation.PORTRAIT ||
                orientation == ScreenOrientation.PORTRAIT_REVERSE; //read other axis if any kind of portrait

        //Bound the image
        if (readOppositeAxis)
            //Force the analysis box to transpose inself in place
            //noinspection SuspiciousNameCombination
            bounds = new Rectangle(
                    new Point(bounds.center().y / img.height() * img.width(),
                            bounds.center().x / img.width() * img.height()),
                    bounds.height(), bounds.width()).clip(new Rectangle(img.size()));
        if (!swapLeftRight && readOppositeAxis)
            //Force the analysis box to flip across its primary axis
            bounds = new Rectangle(
                    new Point((img.size().width / 2) + Math.abs(bounds.center().x - (img.size().width / 2)),
                            bounds.center().y), bounds.width(), bounds.height());
        else if (swapLeftRight && !readOppositeAxis)
            //Force the analysis box to flip across its primary axis
            bounds = new Rectangle(
                    new Point(bounds.center().x, img.size().height - bounds.center().y),
                    bounds.width(), bounds.height());
        bounds = bounds.clip(new Rectangle(img.size()));

        //Get contours within the bounds
        detectorRed.process(img);
        detectorBlue.process(img);
        List<Contour> contoursRed = detectorRed.getContours();
        List<Contour> contoursBlue = detectorBlue.getContours();

        //DEBUG Draw contours before filtering
        if (debug) Drawing.drawContours(img, contoursRed, new ColorRGBA("#FF0000"), 2);
        if (debug) Drawing.drawContours(img, contoursBlue, new ColorRGBA("#0000FF"), 2);

        if (debug)
            Drawing.drawRectangle(img, bounds, new ColorRGBA("#aaaaaa"), 4);

        //Get the largest contour in each - we're calling this one the main light
        int largestIndexRed = findLargestIndexInBounds(contoursRed, bounds);
        int largestIndexBlue = findLargestIndexInBounds(contoursBlue, bounds);
        Contour largestRed = (largestIndexRed != -1) ? contoursRed.get(largestIndexRed) : null;
        Contour largestBlue = (largestIndexBlue != -1) ? contoursBlue.get(largestIndexBlue) : null;

        //If we don't have a main light for one of the colors, we know both colors are the same
        //TODO we should re-filter the contours by size to ensure that we get at least a decent size
        if (largestRed == null && largestBlue == null)
            return new Beacon.BeaconAnalysis();

        //INFO The best contour from each color (if available) is selected as red and blue
        //INFO The two best contours are then used to calculate the location of the beacon

        //If we don't have a main light for one of the colors, we know both colors are the same
        //TODO we should re-filter the contours by size to ensure that we get at least a decent size

        //If the largest part of the non-null color is wider than a certain distance, then both are bright
        //Otherwise, only one may be lit
        //If only one is lit, and is wider than a certain distance, it is bright
        //TODO We are currently assuming that the beacon cannot be in a "bright" state
        if (largestRed == null)
            return new Beacon.BeaconAnalysis();
        else if (largestBlue == null)
            return new Beacon.BeaconAnalysis();

        //The height of the beacon on screen is the height of the best contour
        Contour largestHeight = ((largestRed.size().height) > (largestBlue.size().height)) ? largestRed : largestBlue;
        double beaconHeight = largestHeight.size().height;

        //Get beacon width on screen by extrapolating from height
        final double beaconActualHeight = Constants.BEACON_HEIGHT; //cm, only the lit up portion - 14.0 for entire
        final double beaconActualWidth = Constants.BEACON_WIDTH; //cm
        final double beaconWidthHeightRatio = Constants.BEACON_WH_RATIO;
        double beaconWidth = beaconHeight * beaconWidthHeightRatio;
        Size beaconSize = new Size(beaconWidth, beaconHeight);

        //Look at the locations of the largest contours
        //Check to see if the largest red contour is more left-most than the largest right contour
        //If it is, then we know that the left beacon is red and the other blue, and vice versa

        Point bestRedCenter = largestRed.centroid();
        Point bestBlueCenter = largestBlue.centroid();

        //DEBUG R/B text
        if (debug)
            Drawing.drawText(img, "R", bestRedCenter, 1.0f, new ColorRGBA(255, 0, 0));
        if (debug)
            Drawing.drawText(img, "B", bestBlueCenter, 1.0f, new ColorRGBA(0, 0, 255));

        //Test which side is red and blue
        //If the distance between the sides is smaller than a value, then return unknown
        final int minDistance = (int) (Constants.DETECTION_MIN_DISTANCE * beaconSize.width); //percent of beacon width

        boolean leftIsRed;
        Contour leftMostContour, rightMostContour;
        if (readOppositeAxis) {
            if (bestRedCenter.y + minDistance < bestBlueCenter.y) {
                leftIsRed = true;
            } else if (bestBlueCenter.y + minDistance < bestRedCenter.y) {
                leftIsRed = false;
            } else {
                return new Beacon.BeaconAnalysis();
            }
        } else {
            if (bestRedCenter.x + minDistance < bestBlueCenter.x) {
                leftIsRed = true;
            } else if (bestBlueCenter.x + minDistance < bestRedCenter.x) {
                leftIsRed = false;
            } else {
                return new Beacon.BeaconAnalysis();
            }
        }

        //Swap left and right if necessary
        leftIsRed = swapLeftRight != leftIsRed;

        //Get the left-most best contour (or top-most if axis swapped) (or right-most if L/R swapped)
        if (readOppositeAxis) {
            //Get top-most best contour
            leftMostContour = ((largestRed.topLeft().y) < (largestBlue.topLeft().y)) ? largestRed : largestBlue;
            //Get bottom-most best contour
            rightMostContour = ((largestRed.topLeft().y) < (largestBlue.topLeft().y)) ? largestBlue : largestRed;
        } else {
            //Get left-most best contour
            leftMostContour = ((largestRed.topLeft().x) < (largestBlue.topLeft().x)) ? largestRed : largestBlue;
            //Get the right-most best contour
            rightMostContour = ((largestRed.topLeft().x) < (largestBlue.topLeft().x)) ? largestBlue : largestRed;
        }

        //DEBUG Logging
        if (debug)
            Log.d("Beacon", "Orientation: " + orientation + "Angle: " + orientationAngle + " Swap Axis: " + readOppositeAxis +
                    " Swap Direction: " + swapLeftRight);

        //Swap left and right if necessary
        //BUGFIX: invert when we swap
        if (!swapLeftRight) {
            Contour temp = leftMostContour;
            leftMostContour = rightMostContour;
            rightMostContour = temp;
        }

        //Now that we have the two contours, let's find ellipses that match

        //Draw the box surrounding both contours
        //Get the width of the contours
        double widthBeacon = rightMostContour.right() - leftMostContour.left();

        //Center of contours is the average of centroids of the contours
        Point center = new Point((leftMostContour.centroid().x + rightMostContour.centroid().x) / 2,
                (leftMostContour.centroid().y + rightMostContour.centroid().y) / 2);

        //Get the combined height of the contours
        double heightContours = Math.max(leftMostContour.bottom(), rightMostContour.bottom()) -
                Math.min(leftMostContour.top(), rightMostContour.top());

        //The largest size ratio of tested over actual is the scale ratio
        double scale = Math.max(widthBeacon / beaconActualWidth, heightContours / beaconActualHeight);

        //Define size of bounding box by scaling the actual beacon size
        Size beaconSizeFinal = new Size(beaconActualWidth * scale, beaconActualHeight * scale);

        //Swap x and y if we rotated the view
        if (readOppositeAxis) {
            //noinspection SuspiciousNameCombination
            beaconSizeFinal = new Size(beaconSizeFinal.height, beaconSizeFinal.width);
        }

        //Get points of the bounding box
        Point beaconTopLeft = new Point(center.x - (beaconSizeFinal.width / 2),
                center.y - (beaconSizeFinal.height / 2));
        Point beaconBottomRight = new Point(center.x + (beaconSizeFinal.width / 2),
                center.y + (beaconSizeFinal.height / 2));
        Rectangle boundingBox = new Rectangle(new Rect(beaconTopLeft, beaconBottomRight));

        //Get ellipses in region of interest
        //Make sure the rectangles don't leave the image size
        Rectangle leftRect = leftMostContour.getBoundingRectangle().clip(
                new Rectangle(img.size()));
        Rectangle rightRect = rightMostContour.getBoundingRectangle().clip(
                new Rectangle(img.size()));
        Mat leftContourImg = gray.submat(
                (int) leftRect.top(), (int) leftRect.bottom(),
                (int) leftRect.left(), (int) leftRect.right());
        Mat rightContourImg = gray.submat(
                (int) rightRect.top(), (int) rightRect.bottom(),
                (int) rightRect.left(), (int) rightRect.right());

        //Locate ellipses in the image to process contours against
        List<Ellipse> ellipsesLeft =
                PrimitiveDetection.locateEllipses(leftContourImg).getEllipses();
        Detectable.offset(ellipsesLeft, new Point(leftRect.left(), leftRect.top()));
        List<Ellipse> ellipsesRight =
                PrimitiveDetection.locateEllipses(rightContourImg).getEllipses();
        Detectable.offset(ellipsesRight, new Point(rightRect.left(), rightRect.top()));

        //Score ellipses
        BeaconScoringCOMPLEX scorer = new BeaconScoringCOMPLEX(img.size());
        List<BeaconScoringCOMPLEX.ScoredEllipse> scoredEllipsesLeft = scorer.scoreEllipses(ellipsesLeft, null, null, gray);
        scoredEllipsesLeft = filterEllipses(scoredEllipsesLeft);
        ellipsesLeft = BeaconScoringCOMPLEX.ScoredEllipse.getList(scoredEllipsesLeft);
        if (debug) Drawing.drawEllipses(img, ellipsesLeft, new ColorRGBA("#00ff00"), 3);
        List<BeaconScoringCOMPLEX.ScoredEllipse> scoredEllipsesRight = scorer.scoreEllipses(ellipsesRight, null, null, gray);
        scoredEllipsesRight = filterEllipses(scoredEllipsesRight);
        ellipsesRight = BeaconScoringCOMPLEX.ScoredEllipse.getList(scoredEllipsesRight);
        if (debug) Drawing.drawEllipses(img, ellipsesRight, new ColorRGBA("#00ff00"), 3);

        //Calculate ellipse center if present
        Point centerLeft;
        Point centerRight;
        boolean done = false;
        do {
            centerLeft = null;
            centerRight = null;
            if (scoredEllipsesLeft.size() > 0)
                centerLeft = scoredEllipsesLeft.get(0).ellipse.center();
            if (scoredEllipsesRight.size() > 0)
                centerRight = scoredEllipsesRight.get(0).ellipse.center();

            //Flip axis if necesary
            if (centerLeft != null && readOppositeAxis) {
                centerLeft.set(new double[]{centerLeft.y, centerLeft.x});
            }
            if (centerRight != null && readOppositeAxis) {
                centerRight.set(new double[]{centerRight.y, centerRight.x});
            }

            //Make very, very sure that we didn't just find the same ellipse
            if (centerLeft != null && centerRight != null) {
                if (Math.abs(centerLeft.x - centerRight.x) <
                        Constants.ELLIPSE_MIN_DISTANCE * beaconSize.width) {
                    //Are both ellipses on the left or right side of the beacon? - remove the opposite side's ellipse
                    if (Math.abs(centerLeft.x - leftRect.center().x) < Constants.ELLIPSE_MIN_DISTANCE * beaconSize.width)
                        scoredEllipsesRight.remove(0);
                    else
                        scoredEllipsesLeft.remove(0);
                } else
                    done = true;
            } else
                done = true;

        } while (!done);

        //Improve the beacon center if both ellipses present
        byte ellipseExtrapolated = 0;
        if (centerLeft != null && centerRight != null) {
            if (readOppositeAxis)
                center.y = (centerLeft.x + centerRight.x) / 2;
            else
                center.x = (centerLeft.x + centerRight.x) / 2;
        }
        //Extrapolate other ellipse location if one present
        //FIXME: This method of extrapolation may not work when readOppositeAxis is true
        else if (centerLeft != null) {
            ellipseExtrapolated = 2;
            if (readOppositeAxis)
                centerRight = new Point(centerLeft.x - 2 * Math.abs(center.x - centerLeft.x),
                        (centerLeft.y + center.y) / 2);
            else
                centerRight = new Point(centerLeft.x + 2 * Math.abs(center.x - centerLeft.x),
                        (centerLeft.y + center.y) / 2);
            if (readOppositeAxis)
                centerRight.set(new double[]{centerRight.y, centerRight.x});
        } else if (centerRight != null) {
            ellipseExtrapolated = 1;
            if (readOppositeAxis)
                centerLeft = new Point(centerRight.x + 2 * Math.abs(center.x - centerRight.x),
                        (centerRight.y + center.y) / 2);
            else
                centerLeft = new Point(centerRight.x - 2 * Math.abs(center.x - centerRight.x),
                        (centerRight.y + center.y) / 2);
            if (readOppositeAxis)
                centerLeft.set(new double[]{centerLeft.y, centerLeft.x});
        }

        //Draw center locations
        if (debug) Drawing.drawCross(img, center, new ColorRGBA("#ffffff"), 10, 4);
        if (debug && centerLeft != null) {
            ColorRGBA c = ellipseExtrapolated != 1 ? new ColorRGBA("#ffff00") : new ColorRGBA("#ff00ff");
            //noinspection SuspiciousNameCombination
            Drawing.drawCross(img,
                    !readOppositeAxis ? centerLeft : new Point(centerLeft.y, centerLeft.x), c, 8, 3);
        }
        if (debug && centerRight != null) {
            ColorRGBA c = ellipseExtrapolated != 2 ? new ColorRGBA("#ffff00") : new ColorRGBA("#ff00ff");
            //noinspection SuspiciousNameCombination
            Drawing.drawCross(img,
                    !readOppositeAxis ? centerRight : new Point(centerRight.y, centerRight.x), c, 8, 3);
        }

        //Draw the rectangle containing the beacon
        if (debug) Drawing.drawRectangle(img, boundingBox, new ColorRGBA(0, 255, 0), 4);

        //Tell us the height of the beacon
        //TODO later we can get the distance away from the beacon based on its height and position

        //Remove the largest index and look for the next largest
        //If the next largest is (mostly) within the area of the box, then merge it in with the largest
        //Check if the size of the largest contour(s) is about twice the size of the other
        //This would indicate one is brightly lit and the other is not
        //If this is not true, then neither part of the beacon is highly lit

        //Get confidence approximation
        double WH_ratio = widthBeacon / beaconHeight;
        double ratioError = Math.abs((Constants.BEACON_WH_RATIO - WH_ratio)) / Constants.BEACON_WH_RATIO; // perfect value = 0;
        double averageHeight = (leftMostContour.height() + rightMostContour.height()) / 2.0;
        double dy = Math.abs((leftMostContour.centroid().y - rightMostContour.centroid().y) / averageHeight * Constants.FAST_HEIGHT_DELTA_FACTOR);
        double dArea = Math.sqrt(leftMostContour.area() / rightMostContour.area());
        double buttonsdy = (centerLeft != null && centerRight != null) ?
                (Math.abs(centerLeft.y - centerRight.y) / averageHeight * Constants.FAST_HEIGHT_DELTA_FACTOR) : Constants.ELLIPSE_PRESENCE_BIAS;
        double confidence = MathUtil.normalPDFNormalized(
                MathUtil.distance(MathUtil.distance(MathUtil.distance(ratioError, dy), dArea), buttonsdy) / Constants.FAST_CONFIDENCE_ROUNDNESS,
                Constants.FAST_CONFIDENCE_NORM, 0.0);

        //Get button ellipses
        Ellipse leftEllipse = scoredEllipsesLeft.size() > 0 ? scoredEllipsesLeft.get(0).ellipse : null;
        Ellipse rightEllipse = scoredEllipsesRight.size() > 0 ? scoredEllipsesRight.get(0).ellipse : null;

        //Test for color switching
        if (leftEllipse != null && rightEllipse != null && leftEllipse.center().x > rightEllipse.center().x) {
            Ellipse tE = leftEllipse;
            leftEllipse = rightEllipse;
            rightEllipse = tE;
        } else if ((leftEllipse != null && leftEllipse.center().x > center.x) ||
                (rightEllipse != null && rightEllipse.center().x < center.x)) {
            Ellipse tE = leftEllipse;
            leftEllipse = rightEllipse;
            rightEllipse = tE;
        }

        //Axis correction for ellipses
        if (swapLeftRight) {
            if (leftEllipse != null)
                leftEllipse = new Ellipse(new RotatedRect(
                        new Point(img.width() - leftEllipse.center().x, leftEllipse.center().y),
                        leftEllipse.size(), leftEllipse.angle()));
            if (rightEllipse != null)
                rightEllipse = new Ellipse(new RotatedRect(
                        new Point(img.width() - rightEllipse.center().x, rightEllipse.center().y),
                        rightEllipse.size(), rightEllipse.angle()));
            //Swap again after correcting axis to ensure left is left and right is right
            Ellipse tE = leftEllipse;
            leftEllipse = rightEllipse;
            rightEllipse = tE;
        }

        //Switch axis if necessary
        if (readOppositeAxis)
            boundingBox = boundingBox.transpose();

        if (leftIsRed)
            return new Beacon.BeaconAnalysis(Beacon.BeaconColor.RED, Beacon.BeaconColor.BLUE, boundingBox, confidence
                    , leftEllipse, rightEllipse);
        else
            return new Beacon.BeaconAnalysis(Beacon.BeaconColor.BLUE, Beacon.BeaconColor.RED, boundingBox, confidence
                    , leftEllipse, rightEllipse);
    }

    private static List<BeaconScoringCOMPLEX.ScoredEllipse> filterEllipses(List<BeaconScoringCOMPLEX.ScoredEllipse> ellipses) {
        for (int i = ellipses.size() - 1; i >= 0; i--)
            if (ellipses.get(i).score < Constants.ELLIPSE_SCORE_REQ)
                ellipses.remove(i);
        return ellipses;
    }

    private static int findLargestIndexInBounds(List<Contour> contours, Rectangle bounds) {
        if (contours.size() < 1)
            return -1;
        int largestIndex = 0;
        double maxArea = 0.0f;
        for (int i = 0; i < contours.size(); i++) {
            Contour c = contours.get(i);
            if (c.area() > maxArea && c.isMostlyInside(bounds)) {
                largestIndex = i;
                maxArea = c.area();
            }
        }
        return largestIndex;
    }

    private static int findLargestIndex(List<Contour> contours) {
        if (contours.size() < 1)
            return -1;
        int largestIndex = 0;
        double maxArea = 0.0f;
        for (int i = 0; i < contours.size(); i++) {
            Contour c = contours.get(i);
            if (c.area() > maxArea) {
                largestIndex = i;
                maxArea = c.area();
            }
        }
        return largestIndex;
    }

    static Beacon.BeaconAnalysis analyze_COMPLEX(List<Contour> contoursRed, List<Contour> contoursBlue,
                                                 Mat img, Mat gray, ScreenOrientation orientation, Rectangle bounds, boolean debug) {
        //The idea behind the SmartScoring algorithm is that the largest score in each contour/ellipse set will become the best
        //DONE First, ellipses and contours are are detected and pre-filtered to remove eccentricities
        //Second, ellipses, and contours are scored independently based on size and color ... higher score is better
        //Third, comparative analysis is used on each ellipse and contour to create a score for the contours
        //Ellipses within rectangles strongly increase in value
        //Ellipses without nearby/contained contours are removed
        //Ellipses with nearby/contained contours associate themselves with the contour
        //Pairs of ellipses (those with similar size and x-position) greatly increase the associated contours' value
        //Contours without nearby/contained ellipses lose value
        //Contours near another contour of the opposite color increase in value
        //Contours and ellipses near the expected area (if any expected area) increase in value
        //Finally, a fraction of the ellipse value is added to the value of the contour
        //The best ellipse is found first, then only this ellipse adds to the value
        //The best contour from each color (if available) is selected as red and blue
        //The two best contours are then used to calculate the location of the beacon

        //TODO Filter out bad contours - filtering currently ignored

        //DEBUG Draw contours before filtering
        if (debug) Drawing.drawContours(img, contoursRed, new ColorRGBA("#FFCDD2"), 2);
        if (debug) Drawing.drawContours(img, contoursBlue, new ColorRGBA("#BBDEFB"), 2);

        //Score contours
        BeaconScoringCOMPLEX scorer = new BeaconScoringCOMPLEX(img.size());
        List<BeaconScoringCOMPLEX.ScoredContour> scoredContoursRed = scorer.scoreContours(contoursRed, null, null, img, gray);
        List<BeaconScoringCOMPLEX.ScoredContour> scoredContoursBlue = scorer.scoreContours(contoursBlue, null, null, img, gray);

        //DEBUG Draw red and blue contours after filtering
        if (debug)
            Drawing.drawContours(img, BeaconScoringCOMPLEX.ScoredContour.getList(scoredContoursRed), new ColorRGBA(255, 0, 0), 2);
        if (debug)
            Drawing.drawContours(img, BeaconScoringCOMPLEX.ScoredContour.getList(scoredContoursBlue), new ColorRGBA(0, 0, 255), 2);

        //Locate ellipses in the image to process contours against
        //Each contour must have an ellipse of correct specification
        PrimitiveDetection primitiveDetection = new PrimitiveDetection();
        PrimitiveDetection.EllipseLocationResult ellipseLocationResult = PrimitiveDetection.locateEllipses(gray);

        //Filter out bad ellipses - TODO filtering currently ignored
        List<Ellipse> ellipses = ellipseLocationResult.getEllipses();

        //DEBUG Ellipse data before filtering
        //Drawing.drawEllipses(img, ellipses, new ColorRGBA("#ff0745"), 1);

        //Score ellipses
        List<BeaconScoringCOMPLEX.ScoredEllipse> scoredEllipses = scorer.scoreEllipses(ellipses, null, null, gray);

        //DEBUG Ellipse data after filtering
        if (debug)
            Drawing.drawEllipses(img, BeaconScoringCOMPLEX.ScoredEllipse.getList(scoredEllipses), new ColorRGBA("#FFC107"), 2);

        //DEBUG draw top 5 ellipses
        if (scoredEllipses.size() > 0 && debug) {
            Drawing.drawEllipses(img, BeaconScoringCOMPLEX.ScoredEllipse.getList(scoredEllipses.subList(0, scoredEllipses.size() > 5 ? 5 : scoredEllipses.size()))
                    , new ColorRGBA("#d0ff00"), 3);
            Drawing.drawEllipses(img, BeaconScoringCOMPLEX.ScoredEllipse.getList(scoredEllipses.subList(0, scoredEllipses.size() > 3 ? 3 : scoredEllipses.size()))
                    , new ColorRGBA("#00ff00"), 3);
        }

        //Third, comparative analysis is used on each ellipse and contour to create a score for the contours
        BeaconScoringCOMPLEX.MultiAssociatedContours associations = scorer.scoreAssociations(scoredContoursRed, scoredContoursBlue, scoredEllipses);
        double score = (associations.blueContours.size() > 0 ? associations.blueContours.get(0).score : 0) +
                (associations.redContours.size() > 0 ? associations.redContours.get(0).score : 0);
        double confidence = score / Constants.CONFIDENCE_DIVISOR;

        //INFO The best contour from each color (if available) is selected as red and blue
        //INFO The two best contours are then used to calculate the location of the beacon

        //Get the best contour in each (starting with the largest) if any contours exist
        //We're calling this one the main light
        Contour bestRed = (associations.redContours.size() > 0) ? associations.redContours.get(0).contour.contour : null;
        Contour bestBlue = (associations.blueContours.size() > 0) ? associations.blueContours.get(0).contour.contour : null;
        Ellipse bestRedEllipse = (associations.redContours.size() > 0 && associations.redContours.get(0).ellipses.size() > 0) ? associations.redContours.get(0).ellipses.get(0).ellipse : null;
        Ellipse bestBlueEllipse = (associations.blueContours.size() > 0 && associations.blueContours.get(0).ellipses.size() > 0) ? associations.blueContours.get(0).ellipses.get(0).ellipse : null;

        //If we don't have a main light for one of the colors, we know both colors are the same
        //TODO we should re-filter the contours by size to ensure that we get at least a decent size
        if (bestRed == null && bestBlue == null)
            return new Beacon.BeaconAnalysis(Beacon.BeaconColor.UNKNOWN, Beacon.BeaconColor.UNKNOWN, new Rectangle(), 0.0f);

        //TODO rotate image based on camera rotation here

        //The height of the beacon on screen is the height of the best contour
        Contour largestHeight = ((bestRed != null ? bestRed.size().height : 0) >
                (bestBlue != null ? bestBlue.size().height : 0)) ? bestRed : bestBlue;
        assert largestHeight != null;
        double beaconHeight = largestHeight.size().height;

        //Get beacon width on screen by extrapolating from height
        final double beaconActualHeight = Constants.BEACON_HEIGHT; //cm, only the lit up portion - 14.0 for entire
        final double beaconActualWidth = Constants.BEACON_WIDTH; //cm
        final double beaconWidthHeightRatio = Constants.BEACON_WH_RATIO;
        double beaconWidth = beaconHeight * beaconWidthHeightRatio;
        Size beaconSize = new Size(beaconWidth, beaconHeight);

        //If the largest part of the non-null color is wider than a certain distance, then both are bright
        //Otherwise, only one may be lit
        //If only one is lit, and is wider than a certain distance, it is bright
        //TODO We are currently assuming that the beacon cannot be in a "bright" state
        if (bestRed == null)
            return new Beacon.BeaconAnalysis();
        else if (bestBlue == null)
            return new Beacon.BeaconAnalysis();

        //Look at the locations of the largest contours
        //Check to see if the largest red contour is more left-most than the largest right contour
        //If it is, then we know that the left beacon is red and the other blue, and vice versa

        Point bestRedCenter = bestRed.centroid();
        Point bestBlueCenter = bestBlue.centroid();

        //DEBUG R/B text
        if (debug) Drawing.drawText(img, "R", bestRedCenter, 1.0f, new ColorRGBA(255, 0, 0));
        if (debug) Drawing.drawText(img, "B", bestBlueCenter, 1.0f, new ColorRGBA(0, 0, 255));

        //Test which side is red and blue
        //If the distance between the sides is smaller than a value, then return unknown
        final int minDistance = (int) (Constants.DETECTION_MIN_DISTANCE * beaconSize.width); //percent of beacon width
        //Figure out which way to read the image
        double orientationAngle = orientation.getAngle();
        boolean swapLeftRight = orientationAngle >= 180; //swap if LANDSCAPE_WEST or PORTRAIT_REVERSE
        boolean readOppositeAxis = orientation == ScreenOrientation.PORTRAIT ||
                orientation == ScreenOrientation.PORTRAIT_REVERSE; //read other axis if any kind of portrait

        boolean leftIsRed;
        Contour leftMostContour, rightMostContour;
        Ellipse leftEllipse, rightEllipse;
        if (readOppositeAxis) {
            if (bestRedCenter.y + minDistance < bestBlueCenter.y) {
                leftIsRed = true;
            } else if (bestBlueCenter.y + minDistance < bestRedCenter.y) {
                leftIsRed = false;
            } else {
                return new Beacon.BeaconAnalysis();
            }
        } else {
            if (bestRedCenter.x + minDistance < bestBlueCenter.x) {
                leftIsRed = true;
            } else if (bestBlueCenter.x + minDistance < bestRedCenter.x) {
                leftIsRed = false;
            } else {
                return new Beacon.BeaconAnalysis();
            }
        }

        //Swap left and right if necessary
        leftIsRed = swapLeftRight != leftIsRed;

        //Get the left-most best contour (or top-most if axis swapped) (or right-most if L/R swapped)
        if (readOppositeAxis) {
            //Get top-most best contour
            leftMostContour = ((bestRed.topLeft().y) < (bestBlue.topLeft().y)) ? bestRed : bestBlue;
            leftEllipse = ((bestRed.topLeft().y) < (bestBlue.topLeft().y)) ? bestRedEllipse : bestBlueEllipse;
            //Get bottom-most best contour
            rightMostContour = ((bestRed.topLeft().y) < (bestBlue.topLeft().y)) ? bestBlue : bestRed;
            rightEllipse = ((bestRed.topLeft().y) < (bestBlue.topLeft().y)) ? bestBlueEllipse : bestRedEllipse;
        } else {
            //Get left-most best contour
            leftMostContour = ((bestRed.topLeft().y) < (bestBlue.topLeft().y)) ? bestRed : bestBlue;
            leftEllipse = ((bestRed.topLeft().y) < (bestBlue.topLeft().y)) ? bestRedEllipse : bestBlueEllipse;
            //Get the right-most best contour
            rightMostContour = ((bestRed.topLeft().y) < (bestBlue.topLeft().y)) ? bestBlue : bestRed;
            rightEllipse = ((bestRed.topLeft().y) < (bestBlue.topLeft().y)) ? bestBlueEllipse : bestRedEllipse;
        }

        //Swap left and right if necessary
        //BUGFIX: invert when we swap
        if (swapLeftRight) {
            Contour temp = leftMostContour;
            leftMostContour = rightMostContour;
            rightMostContour = temp;

            Ellipse tE = leftEllipse;
            leftEllipse = rightEllipse;
            rightEllipse = tE;
        }

        //Draw the box surrounding both contours
        //Get the width of the contours
        double widthBeacon = rightMostContour.right() - leftMostContour.left();

        //Center of contours is the average of centers of the contours
        Point center = new Point((leftMostContour.centroid().x + rightMostContour.centroid().x) / 2,
                (leftMostContour.centroid().y + rightMostContour.centroid().y) / 2);

        //Get the combined height of the contours
        double heightContours = Math.max(leftMostContour.bottom(), rightMostContour.bottom()) -
                Math.min(leftMostContour.top(), rightMostContour.top());

        //The largest size ratio of tested over actual is the scale ratio
        double scale = Math.max(widthBeacon / beaconActualWidth, heightContours / beaconActualHeight);

        //Define size of bounding box by scaling the actual beacon size
        Size beaconSizeFinal = new Size(beaconActualWidth * scale, beaconActualHeight * scale);

        //Swap x and y if we rotated the view
        if (readOppositeAxis) {
            //noinspection SuspiciousNameCombination
            beaconSizeFinal = new Size(beaconSizeFinal.height, beaconSizeFinal.width);
        }

        //Get points of the bounding box
        Point beaconTopLeft = new Point(center.x - (beaconSizeFinal.width / 2),
                center.y - (beaconSizeFinal.height / 2));
        Point beaconBottomRight = new Point(center.x + (beaconSizeFinal.width / 2),
                center.y + (beaconSizeFinal.height / 2));
        Rectangle boundingBox = new Rectangle(new Rect(beaconTopLeft, beaconBottomRight));

        //Draw the rectangle containing the beacon
        if (debug) Drawing.drawRectangle(img, boundingBox, new ColorRGBA(0, 255, 0), 4);

        //Tell us the height of the beacon
        //TODO later we can get the distance away from the beacon based on its height and position

        //Remove the largest index and look for the next largest
        //If the next largest is (mostly) within the area of the box, then merge it in with the largest

        //Check if the size of the largest contour(s) is about twice the size of the other
        //This would indicate one is brightly lit and the other is not

        //Draw some more debug info
        if (debug) Drawing.drawCross(img, center, new ColorRGBA("#ffffff"), 10, 4);
        if (debug && leftEllipse != null)
            Drawing.drawCross(img, leftEllipse.center(), new ColorRGBA("#ffff00"), 8, 3);
        if (debug && rightEllipse != null)
            Drawing.drawCross(img, rightEllipse.center(), new ColorRGBA("#ffff00"), 8, 3);

        //Test for color switching
        if (leftEllipse != null && rightEllipse != null && leftEllipse.center().x > rightEllipse.center().x) {
            Ellipse tE = leftEllipse;
            leftEllipse = rightEllipse;
            rightEllipse = tE;
        } else if ((leftEllipse != null && leftEllipse.center().x > center.x) ||
                (rightEllipse != null && rightEllipse.center().x < center.x)) {
            Ellipse tE = leftEllipse;
            leftEllipse = rightEllipse;
            rightEllipse = tE;
        }

        //Axis correction for ellipses
        if (swapLeftRight) {
            if (leftEllipse != null)
                leftEllipse = new Ellipse(new RotatedRect(
                        new Point(img.width() - leftEllipse.center().x, leftEllipse.center().y),
                        leftEllipse.size(), leftEllipse.angle()));
            if (rightEllipse != null)
                rightEllipse = new Ellipse(new RotatedRect(
                        new Point(img.width() - rightEllipse.center().x, rightEllipse.center().y),
                        rightEllipse.size(), rightEllipse.angle()));
            //Swap again after correcting axis to ensure left is left and right is right
            Ellipse tE = leftEllipse;
            leftEllipse = rightEllipse;
            rightEllipse = tE;
        }

        //Make very, very sure that we didn't just find the same ellipse
        if (leftEllipse != null && rightEllipse != null) {
            if (Math.abs(leftEllipse.center().x - rightEllipse.center().x) <
                    Constants.ELLIPSE_MIN_DISTANCE * beaconSize.width) {
                //Are both ellipses on the left or right side of the beacon? - remove the opposite side's ellipse
                if (Math.abs(leftEllipse.center().x - leftMostContour.center().x) < Constants.ELLIPSE_MIN_DISTANCE * beaconSize.width)
                    rightEllipse = null;
                else
                    leftEllipse = null;
            }
        }

        //Switch axis if necessary
        if (readOppositeAxis)
            boundingBox = boundingBox.transpose();

        //If this is not true, then neither part of the beacon is highly lit
        if (leftIsRed)
            return new Beacon.BeaconAnalysis(Beacon.BeaconColor.RED, Beacon.BeaconColor.BLUE, boundingBox, confidence, leftEllipse, rightEllipse);
        else
            return new Beacon.BeaconAnalysis(Beacon.BeaconColor.BLUE, Beacon.BeaconColor.RED, boundingBox, confidence, leftEllipse, rightEllipse);
    }
}
