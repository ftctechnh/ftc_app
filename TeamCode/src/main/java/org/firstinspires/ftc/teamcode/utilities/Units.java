package org.firstinspires.ftc.teamcode.utilities;

/**
 * Created by ROUS on 1/1/2017.
 */
public class Units {
    private static final double mmPerInch = 25.4;

    public static float inchtomm(float val) {
        return (float) inchtomm((double)val);
    }

    public static double inchtomm(double val) {
        return val * mmPerInch;
    }

    public static double mmtoinch(double val) {
        return val / mmPerInch;

    }
    public static float mmtoinch(float val) {
        return (float) mmtoinch((double)val);
    }
}
