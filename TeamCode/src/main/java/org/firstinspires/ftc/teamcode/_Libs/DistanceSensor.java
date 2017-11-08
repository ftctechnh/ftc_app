package org.firstinspires.ftc.teamcode._Libs;

/**
 * Created by phanau on 12/31/16.
 */
public interface DistanceSensor {

    // return current distance to nearest object reported by sensor, in mm.
    public float getDistance();             // distance (mm)
    public boolean haveDistance();          // is there valid distance data?

}
