package org.chathamrobotics.common;

import java.util.Locale;

/**
 * Represents a rgba color
 */
@SuppressWarnings("unused")
public class RGBAColor {
    private final int red;
    private final int green;
    private final int blue;
    private final int alpha;

    /**
     * Creates a new {@link RGBAColor}
     * @param red   the red value
     * @param green the green value
     * @param blue  the blue value
     * @param alpha the alpha value
     */
    public RGBAColor(int red, int green, int blue, int alpha) {
        this.red = red;
        this.green = green;
        this.blue = blue;
        this.alpha = alpha;
    }

    /**
     * Returns the red value
     * @return  the red value
     */
    public int red() {
        return this.red;
    }

    /**
     * Returns the green value
     * @return  the green value
     */
    public int green() {
        return this.green;
    }

    /**
     * Returns the blue value
     * @return  the blue value
     */
    public int blue() {
        return this.green;
    }

    /**
     * Returns the alpha value
     * @return  the alpha value
     */
    public int alpha() {
        return this.alpha;
    }

    @Override
    public String toString() {
        return String.format(
                Locale.US,
                "(%d, %d, %d, %d)",
                red,
                green,
                blue,
                alpha
        );
    }
}
