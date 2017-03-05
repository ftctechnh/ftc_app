package org.firstinspires.ftc.teamcode.utilities;

public class UnitConversion {
    private static final  float mmPerInch  = 25.4f;

    public static float InchToMillimeter( float val ) {
        return val * mmPerInch;
    }
    public static float MillimeterToInch( float val ) {
        return val / mmPerInch;
    }

    public static float InchToCentimeter( float val ) {
        return ((val * mmPerInch) / 1000);
    }
    public static float CentimeterToInch( float val ) {
        return ((val*10) / mmPerInch );
    }

    public static float InchToMeter( float val ) {
        return ((val * mmPerInch) / 1000);
    }
    public static float MeterToInch( float val ) {
        return ((val*1000) / mmPerInch );
    }
}
