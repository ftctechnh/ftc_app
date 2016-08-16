/*
 * Copyright (c) 2016 Arthur Pachachura, LASA Robotics, and contributors
 * MIT licensed
 */
package org.lasarobotics.vision.util.color;

import org.opencv.imgproc.Imgproc;

/**
 * Specifies a color space (such as RGB or HSV) and all possible conversions
 */
public enum ColorSpace {
    RGBA(new int[][]{null,
            new int[]{Imgproc.COLOR_RGBA2RGB, 4, 3},
            new int[]{Imgproc.COLOR_RGBA2RGB, 4, 3, Imgproc.COLOR_RGB2HSV_FULL, 3, 3},
            new int[]{Imgproc.COLOR_RGBA2GRAY, 4, 1}},
            ColorRGBA.class),

    RGB(new int[][]{new int[]{Imgproc.COLOR_RGB2RGBA, 3, 4},
            null,
            new int[]{Imgproc.COLOR_RGB2HSV_FULL, 3, 3},
            new int[]{Imgproc.COLOR_RGB2GRAY, 3, 1}},
            ColorRGBA.class),

    HSV(new int[][]{new int[]{Imgproc.COLOR_HSV2RGB_FULL, 3, 3, Imgproc.COLOR_RGB2RGBA, 3, 4},
            new int[]{Imgproc.COLOR_HSV2RGB_FULL, 3, 3},
            null,
            new int[]{Imgproc.COLOR_HSV2RGB_FULL, 3, 3, Imgproc.COLOR_RGB2GRAY, 3, 1}},
            ColorHSV.class),

    GRAY(new int[][]{new int[]{Imgproc.COLOR_GRAY2RGBA, 1, 4},
            new int[]{Imgproc.COLOR_GRAY2RGB, 1, 3},
            new int[]{Imgproc.COLOR_GRAY2RGB, 1, 3, Imgproc.COLOR_RGB2HSV_FULL, 3, 3},
            null},
            ColorGRAY.class);

    /**
     * Each conversions array contains a list of int[], one for each other ColorSpace to convert to.
     * <p/>
     * Each int[] contains a list of operations and sizes, as such:
     * { operation, input scalar dimension, output scalar dimension, ... }
     */
    private final int[][] conversions;
    /**
     * The class associated with a color - allows for dynamic casting to the class type
     */
    private final Class<? extends Color> colorClass;

    ColorSpace(int[][] conversions, Class<? extends Color> colorClass) {
        this.conversions = conversions;
        this.colorClass = colorClass;
    }

    int[] getConversionsTo(ColorSpace to) {
        return conversions[to.ordinal()];
    }

    Class<? extends Color> getColorClass() {
        return colorClass;
    }

    /**
     * Tests whether the current color space can be converted to another
     *
     * @param to The color space to convert to
     * @return True if convertable, false otherwise
     */
    public boolean canConvertTo(ColorSpace to) {
        return (to == this) || (getConversionsTo(to) != null);
    }
}