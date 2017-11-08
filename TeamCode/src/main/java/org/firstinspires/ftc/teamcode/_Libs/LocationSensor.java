package org.firstinspires.ftc.teamcode._Libs;

import org.firstinspires.ftc.robotcore.external.matrices.VectorF;

/**
 * Created by phanau on 11/1/16.
 */
public interface LocationSensor {

    // return current location on field reported by sensor, in mm from center post.
    public VectorF getLocation();
    public boolean haveLocation();       // is there valid location data?

}
