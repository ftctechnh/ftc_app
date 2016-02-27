package com.powerstackers.resq.common;

/**
 * @author Jonathan
 */
public class RobotConstants {
    // Servo settings
    public static final double CRS_REVERSE = 0.0;
    public static final double CRS_STOP    = 0.5;
    public static final double CRS_FORWARD = 1.0;

    public static final double HOPPER_LEFT_OPEN    = 0.28; //was 0.3
    public static final double HOPPER_LEFT_CLOSE   = 1.0;
    public static final double HOPPER_RIGHT_OPEN   = 0.6;
    public static final double HOPPER_RIGHT_CLOSE  = 0.0;
    public static final double HOPPER_TILT_RESTING = 0.5;
    public static final double HOPPER_TILT_RIGHT   = 0.7;
    public static final double HOPPER_TILT_LEFT    = 0.0; //0.8;
    public static final double CLIMBER_EXTEND      = 1.0;
    public static final double CLIMBER_RETRACT     = 0.17;

    public static final double BEACON_TAP_LEFT  = 0.8;
    public static final double BEACON_TAP_RIGHT = 0.2;
    public static final double BEACON_RESTING   = 0.5;

    public static final double CHURRO_LEFT_OPEN    = 1.0;
    public static final double CHURRO_LEFT_CLOSE   = 0.0;
    public static final double CHURRO_RIGHT_OPEN   = 0.0;
    public static final double CHURRO_RIGHT_CLOSE  = 1.0;
    public static final double ZIPLINE_LEFT_OPEN   = 0.2;
    public static final double ZIPLINE_LEFT_CLOSE  = 1.0;
    public static final double ZIPLINE_RIGHT_OPEN  = 0.8;
    public static final double ZIPLINE_RIGHT_CLOSE = 0.0;
    public static final double TAPE_FLAT           = 1.0;
    public static final double TRIMM_MOTOR         = 0.88888888888;

    // Motor speeds
    public static final double LIFT_SPEED  = 1.0;
    public static final double BRUSH_SPEED = 1.0;
    public static final double WINCH_SPEED = 1.0;
}
