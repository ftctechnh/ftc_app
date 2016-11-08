package org.firstinspires.ftc.team9853;

import org.chathamrobotics.ftcutils.AutoMode;
import org.chathamrobotics.ftcutils.StoppedException;


/**
 * backup autonomous
 */
public class AutoModeCharge extends AutoMode {
    static final long waitTime = 10000;
    static final long driveTime = 3000;

    /*
     * called on start
     */
    public void runRobot() throws StoppedException {
        for(long endTime = System.currentTimeMillis() + waitTime; System.currentTimeMillis() < endTime;) {
            statusCheck();
            // Do nothing
        }

        for(long endTime = System.currentTimeMillis() + driveTime; System.currentTimeMillis() < endTime;) {
            statusCheck();
            driver.drive(isRedTeam ? -1 : 1, 1, 0, .7);
        }
    }
}
