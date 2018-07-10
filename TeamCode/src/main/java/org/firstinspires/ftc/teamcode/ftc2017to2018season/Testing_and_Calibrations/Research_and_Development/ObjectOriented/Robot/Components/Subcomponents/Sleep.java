package org.firstinspires.ftc.teamcode.ftc2017to2018season.Testing_and_Calibrations.Research_and_Development.ObjectOriented.Robot.Components.Subcomponents;

/**
 * Created by adityamavalankar on 3/24/18.
 */

public class Sleep {

    public void sleep(long ms) {
        long beginTime = System.currentTimeMillis();

        while (beginTime + ms > System.currentTimeMillis()) {

        }
    }
}
