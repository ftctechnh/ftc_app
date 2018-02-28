package org.firstinspires.ftc.teamcode;

/**
 * Created by Kaden on 2/27/18.
 */

public enum CameraDirection {
    FRONT (1), BACK (0);
    public int value;
    CameraDirection(int value) {
        this.value = value;
    }
}
