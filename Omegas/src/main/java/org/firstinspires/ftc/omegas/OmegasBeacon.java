package org.firstinspires.ftc.omegas;

/**
 * Created by ethertyper on 11/30/16.
 */

public class OmegasBeacon {
    public enum Color {
        RED,
        BLUE,
        UNDEFINED;

        public static Color parse(String color) {
            return color.equals("red") || color.equals("RED!") ? RED
                    : color.equals("blue") || color.equals("BLUE!") ? BLUE : UNDEFINED;
        }
    }

    public OmegasBeacon(String left, String right) {
        this.left = Color.parse(left);
        this.right = Color.parse(right);
    }

    public Color left;
    public Color right;
}
