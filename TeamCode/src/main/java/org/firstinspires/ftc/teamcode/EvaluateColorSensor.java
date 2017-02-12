package org.firstinspires.ftc.teamcode;

import android.graphics.Color;

import com.qualcomm.robotcore.hardware.ColorSensor;

/**
 *
 */
public class EvaluateColorSensor {

    static private int colorThreshold = 32; /** color comparrison value in range of 1 to 128 */

    static boolean FuzzyEqual( float v1, float v2, float tol ) {
        float delta = Math.abs(v2 - v1);
        return ( delta <= tol );
    }

    /** evaluate color channels and return simple color assessment */
    static public eColorState Evaluate( ColorSensor sensorRGB )
    {
        /**  is an array that will hold 0.0 - 1.0 values of red, green blue. */
        float hsvValues[] = {0f,0f,0f};
        /** convert the RGB values to HSV values. */
        Color.RGBToHSV((sensorRGB.red() * 255) / 800, (sensorRGB.green() * 255) / 800, (sensorRGB.blue() * 255) / 800, hsvValues);

        /**
         * For blue color a hue range from 221° to 240°
         * For red color a hue range from 355° to 10°
         * For green color a hue range from 81° to 140°
         */
        if ( hsvValues[1] < 0.05 ) /** asuming satuation value < 5% is effectively white/gray */
        {
            return eColorState.unknown;
        } else {
            if ( FuzzyEqual( hsvValues[0], 0.0f, 10.0f ) || FuzzyEqual( hsvValues[0], 360.0f, 10.0f )) {
                return eColorState.red;
            } else {
                if ( FuzzyEqual( hsvValues[0], 230.0f, 10.0f) ) {
                        return eColorState.blue;
                } else {
                    if ( FuzzyEqual( hsvValues[0], 110.0f, 25.0f) ) {
                        return eColorState.green;
                    } else {
                        return eColorState.unknown;
                    }
                }
            }
        }
    }
    static public eColorState Evaluate2( ColorSensor sensorRGB )
    {
        /**  is an array that will hold 0.0 - 1.0 values of red, green blue. */
        float hsvValues[] = {0f,0f,0f};
        /** convert the RGB values to HSV values. */
        Color.RGBToHSV((sensorRGB.red() * 255) / 800, (sensorRGB.green() * 255) / 800, (sensorRGB.blue() * 255) / 800, hsvValues);

        /**
         * For blue color a hue range from 221° to 240°
         * For red color a hue range from 355° to 10°
         * For green color a hue range from 81° to 140°
         */
        if ( hsvValues[1] < 0.05 ) /** asuming satuation value < 5% is effectively white/gray */
        {
            return eColorState.unknown;
        } else {
            if ( FuzzyEqual( hsvValues[0], 0.0f, 10.0f ) || FuzzyEqual( hsvValues[0], 360.0f, 10.0f )) {
                return eColorState.red;
            } else {
                if ( FuzzyEqual( hsvValues[0], 230.0f, 10.0f) ) {
                    return eColorState.blue;
                } else {
                    if ( FuzzyEqual( hsvValues[0], 110.0f, 25.0f) ) {
                        return eColorState.green;
                    } else {
                        return eColorState.unknown;
                    }
                }
            }
        }
    }
    /** evaluate for specific color channel dominance */
    static public boolean EvaluateColor( ColorSensor sensorRGB, eColorState checkColor )
    {
        return ( checkColor == Evaluate( sensorRGB ) );
    }


    static  public  boolean EvaluateColor2 ( ColorSensor sensorRGB, eColorState checkColor )
    {
        return ( checkColor == Evaluate2( sensorRGB ) );
    }
}
