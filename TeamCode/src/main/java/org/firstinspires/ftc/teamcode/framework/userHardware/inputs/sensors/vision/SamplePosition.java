package org.firstinspires.ftc.teamcode.framework.userHardware.inputs.sensors.vision;

public enum SamplePosition {
    LEFT,
    CENTER,
    RIGHT,
    UNKNOWN;

    public static SamplePosition getPositionFromValue(double value){
        if(value>0 && value<100){
            return LEFT;
        } else if(value<210){
            return CENTER;
        } else if(value<300){
            return RIGHT;
        } else {
            return UNKNOWN;
        }
    }
}
