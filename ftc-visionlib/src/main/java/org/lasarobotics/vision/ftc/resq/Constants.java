/*
 * Copyright (c) 2016 Arthur Pachachura, LASA Robotics, and contributors
 * MIT licensed
 */

package org.lasarobotics.vision.ftc.resq;

import org.lasarobotics.vision.util.color.ColorHSV;

/**
 * Res-Q field and object constants
 */
public abstract class Constants {
    //BEACON
    public static final double BEACON_WIDTH = 21.8;     //entire beacon width
    public static final double BEACON_HEIGHT = 14.5;    //entire beacon height
    public static final double BEACON_WH_RATIO = BEACON_WIDTH / BEACON_HEIGHT; //entire beacon ratio
    public static final ColorHSV COLOR_RED_LOWER = new ColorHSV((int) (300.0 / 360.0 * 255.0), (int) (0.090 * 255.0), (int) (0.500 * 255.0));
    public static final ColorHSV COLOR_RED_UPPER = new ColorHSV((int) (400.0 / 360.0 * 255.0), 255, 255);
    public static final ColorHSV COLOR_BLUE_LOWER = new ColorHSV((int) (170.0 / 360.0 * 255.0), (int) (0.090 * 255.0), (int) (0.500 * 255.0));
    public static final ColorHSV COLOR_BLUE_UPPER = new ColorHSV((int) (270.0 / 360.0 * 255.0), 255, 255);
    //FAST
    static final double ELLIPSE_SCORE_REQ = 10.0;
    static final double DETECTION_MIN_DISTANCE = 0.1;
    static final double ELLIPSE_MIN_DISTANCE = 0.15;
    static final double ELLIPSE_PRESENCE_BIAS = 1.5;
    static final double FAST_HEIGHT_DELTA_FACTOR = 4.0;
    static final double FAST_CONFIDENCE_NORM = 5.0;
    static final double FAST_CONFIDENCE_ROUNDNESS = 2.0;
    static final double FAST_ELLIPSE_MISMATCH_DIVISOR = 3.0;
    //COMPLEX
    static final double CONFIDENCE_DIVISOR = 800;
    static final double CONTOUR_RATIO_NORM = 0.2; //normal distribution variance for ratio
    static final double CONTOUR_RATIO_BIAS = 3.0; //points given at best ratio
    static final double CONTOUR_AREA_MIN = Math.log10(0.01);
    static final double CONTOUR_AREA_MAX = Math.log10(25.00);
    static final double CONTOUR_AREA_NORM = 0.4;
    static final double CONTOUR_AREA_BIAS = 6.0;
    static final double CONTOUR_SCORE_MIN = 1;
    static final double ELLIPSE_ECCENTRICITY_BEST = 0.4; //best eccentricity for 100% score
    static final double ELLIPSE_ECCENTRICITY_BIAS = 3.0; //points given at best eccentricity
    static final double ELLIPSE_ECCENTRICITY_NORM = 0.1; //normal distribution variance for eccentricity
    static final double ELLIPSE_AREA_MIN = 0.0001;        //minimum area as percentage of screen (0 points)
    static final double ELLIPSE_AREA_MAX = 0.01;         //maximum area (0 points given)
    static final double ELLIPSE_AREA_NORM = 1;
    static final double ELLIPSE_AREA_BIAS = 2.0;
    static final double ELLIPSE_CONTRAST_THRESHOLD = 60.0;
    static final double ELLIPSE_CONTRAST_BIAS = 7.0;
    static final double ELLIPSE_CONTRAST_NORM = 0.1;
    static final double ELLIPSE_SCORE_MIN = 1; //minimum score to keep the ellipse - theoretically, should be 1
    static final double ASSOCIATION_MAX_DISTANCE = 0.10; //as fraction of screen
    static final double ASSOCIATION_NO_ELLIPSE_FACTOR = 0.50;
    static final double ASSOCIATION_ELLIPSE_SCORE_MULTIPLIER = 0.75;
    static final double CONTOUR_RATIO_BEST = BEACON_WH_RATIO; //best ratio for 100% score
}
