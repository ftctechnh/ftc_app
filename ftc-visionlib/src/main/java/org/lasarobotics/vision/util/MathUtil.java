/*
 * Copyright (c) 2016 Arthur Pachachura, LASA Robotics, and contributors
 * MIT licensed
 *
 * Thank you to FTCLib contributors.
 */

package org.lasarobotics.vision.util;

import org.opencv.core.Point;

/**
 * Math utilities
 */
public final class MathUtil {
    /**
     * Double equality epsilon (maximum tolerance for a double)
     */
    private final static double EPSILON = 0.000001;

    /**
     * Suppresses constructor for noninstantiability
     */
    private MathUtil() {
        throw new AssertionError();
    }

    /**
     * Gives a "deadzone" where any value less
     * than this would return zero.
     *
     * @param deadband Maximum value that returns zero
     * @param value    Value to test
     * @return Deadbanded value
     */
    public static double deadband(double deadband, double value) {
        return (Math.abs(value) > deadband) ? value : 0;
    }

    /**
     * Returns if two double values are equal to within epsilon.
     *
     * @param a First value
     * @param b Second value
     * @return True if the values are equal, false otherwise
     */
    public static boolean equal(double a, double b) {
        return (Math.abs(a - b) < EPSILON);
    }

    /**
     * Returns if two double values are equal to within a distance.
     *
     * @param a        First value
     * @param b        Second value
     * @param distance Maximum distance between a and b
     * @return True if the values are equal ot within distance, false otherwise
     */
    public static boolean equal(double a, double b, double distance) {
        return (Math.abs(a - b) < distance);
    }

    /**
     * Ignores values equal to the fail value (normally zero).
     *
     * @param value     Current value
     * @param lastvalue Previous value
     * @param fail      Filter this value, normally zero
     * @return Filtered value
     */
    public static double filter(double value, double lastvalue, double fail) {
        return (value == fail) ? lastvalue : value;
    }

    /**
     * Forces a numerical value to be between a min
     * and a max.
     *
     * @param min   If less than min, returns min
     * @param max   If greater than max, returns max
     * @param value Value to test
     * @return Coerced value
     */
    public static double coerce(double min, double max, double value) {
        return (value > max) ? max : (value < min) ? min : value;
    }

    /**
     * Tests if a number is between the bounds, exclusive.
     *
     * @param min   If less than min, returns false
     * @param max   If greater than max, returns false
     * @param value Value to test
     * @return Returns true if value is between (exclusive) min and max, false otherwise.
     */
    public static boolean inBounds(double min, double max, double value) {
        return (value < max) && (value > min);
    }

    /**
     * Get the distance between two deltas
     *
     * @param deltaX Change in first variable
     * @param deltaY Change in second variable
     * @return Distance formula, sqrt(dx^2 + y^2)
     */
    public static double distance(double deltaX, double deltaY) {
        return Math.sqrt((deltaX * deltaX) + (deltaY * deltaY));
    }

    /**
     * Get the distance between two points
     *
     * @param p1 Point one
     * @param p2 Point two
     * @return Distance formula, sqrt(dx^2 + dy^2)
     */
    public static double distance(Point p1, Point p2) {
        return distance(Math.abs(p2.x - p1.x), Math.abs(p2.y - p1.y));
    }

    /**
     * Calculate the angle between three points
     *
     * @param pt1 Vector 1
     * @param pt2 Vector 2
     * @param pt0 Vector 0
     * @return The angle (cosine) between the points
     */
    public static double angle(Point pt1, Point pt2, Point pt0) {
        double dx1 = pt1.x - pt0.x;
        double dy1 = pt1.y - pt0.y;
        double dx2 = pt2.x - pt0.x;
        double dy2 = pt2.y - pt0.y;
        return (dx1 * dx2 + dy1 * dy2) / Math.sqrt((dx1 * dx1 + dy1 * dy1) * (dx2 * dx2 + dy2 * dy2) + 1e-10);
    }

    /**
     * Calculate a normal probability density function (PDF) based on a variance and mean value
     *
     * @param x         The location on the normal distribution. The location at the mean would be the largest value
     * @param variance  The variance (stddev^2) of the normal distribution.
     * @param meanValue The mean value is the x location at which the function is the largest (i.e. has median)
     * @return The normal distribution at the location x
     */
    public static double normalPDF(double x, double variance, double meanValue) {
        double standardDeviation = Math.sqrt(variance);
        return (1 / (standardDeviation * Math.sqrt(2 * Math.PI))) * Math.pow(Math.E, -((x - meanValue) * (x - meanValue)) / (2 * variance));
    }

    /**
     * Calculate a normal probability density function (PDF) based on a variance and mean value in which the median value is equal to 1
     *
     * @param x         The location on the normal distribution. The location at the mean would be the largest value
     * @param variance  The variance (stddev^2) of the normal distribution.
     * @param meanValue The mean value is the x location at which the function is the largest (i.e. has median)
     * @return The normal distribution at the location x
     */
    public static double normalPDFNormalized(double x, double variance, double meanValue) {
        double standardDeviation = Math.sqrt(variance);
        double normal = (1 / (standardDeviation * Math.sqrt(2 * Math.PI)));
        return (1 / (standardDeviation * Math.sqrt(2 * Math.PI)) / normal) * Math.pow(Math.E, -((x - meanValue) * (x - meanValue)) / (2 * variance));
    }
}
