package org.firstinspires.ftc.teamcode;

/**
 * Created by Robotics on 12/2/2016.
 */

public interface DisplacementSensor {

    // return current heading reported by sensor, in degrees.
    // convention is positive angles CCW, wrapping from 359-0
    public float getDisp();

}
