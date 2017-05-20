package org.firstinspires.ftc.teamcode;

/**
 * Created by ROUS on 1/1/2017.
 */
public class Units {
    private static final float mmPerInch = 25.4f;

    public static float intomm(float val) {
        return val * mmPerInch;

    }

    public static float mmtoint(float val) {
        return val / mmPerInch;

    }
}
