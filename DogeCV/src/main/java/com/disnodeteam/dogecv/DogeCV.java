package com.disnodeteam.dogecv;

/**
 * Contains global enumerated types
 */
public class DogeCV {
    public enum DetectionSpeed {
        VERY_FAST,
        FAST,
        BALANCED,
        SLOW,
        VERY_SLOW
    }

    public enum AreaScoringMethod {
        MAX_AREA,
        PERFECT_AREA,
        COLOR_DEVIATION
    }


}
