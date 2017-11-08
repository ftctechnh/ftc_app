package org.firstinspires.ftc.teamcode._Libs;

/**
 * Created by phanau on 10/31/16.
 */
public interface HeadingSensor {

    // return current heading reported by sensor, in degrees.
    // convention is positive angles CCW, wrapping from 359-0
    public float getHeading();
    public boolean haveHeading();       // is there valid heading data?
}
