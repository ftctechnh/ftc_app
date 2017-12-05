package org.firstinspires.ftc.teamcode;

/**
 * Created by guberti on 10/12/2017.
 */

public enum Alliance {
    RED, BLUE, UNKNOWN;

    public int getColorCode() {
        if (this == Alliance.BLUE) {
            return 1;
        } else if (this == Alliance.RED) {
            return -1;
        } else {
            return 0;
        }
    }
}

