package org.lasarobotics.vision.test.ftc.resq;

/**
 * Res-Q field and object constants
 */
public abstract class Constants {
    public static final double BEACON_WIDTH = 21.8;     //entire beacon width
    public static final double BEACON_HEIGHT = 14.5;    //entire beacon height
    public static final double BEACON_WH_RATIO = BEACON_WIDTH / BEACON_HEIGHT; //entire beacon ratio

    static final double DETECTION_MIN_DISTANCE = 0.05;
    static final double CONFIDENCE_DIVISOR = 30;

    static final double CONTOUR_RATIO_BEST = BEACON_WH_RATIO; //best ratio for 100% score
    static final double CONTOUR_RATIO_BIAS = 1.5; //points given at best ratio
    static final double CONTOUR_RATIO_NORM = 0.2; //normal distribution variance for ratio

    static final double CONTOUR_AREA_MIN = Math.log10(0.01);
    static final double CONTOUR_AREA_MAX = Math.log10(1.00);
    static final double CONTOUR_AREA_NORM = 0.2;
    static final double CONTOUR_AREA_BIAS = 5.0;

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

    static final double ASSOCIATION_MAX_DISTANCE = 0.20; //as fraction of screen
    static final double ASSOCIATION_NO_ELLIPSE_FACTOR = 0.75;
    static final double ASSOCIATION_ELLIPSE_SCORE_MULTIPLIER = 0.25;
}
