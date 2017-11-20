package org.firstinspires.ftc.teamcode;

/**
 * Configurations for robot for team 12547
 */
public class Ftc12547Config {

    public static final int     ONE_SECOND_IN_MIL                    = 1000;
    public static final int     FIVE_SECONDS_IN_MIL                  = 5 * 1000; // 5 seconds

    /**
     * VuMask configurations
     */
    public static final int     THRESHOLD_RECOGNITION                = 3;

    // Do not update
    public static final String  TAG = "Vuforia VuMark Sample";
    public static final String  VUFORIA_LICENSE_KEY =
            "Ac91sD3/////AAAAGSPvdhZYS0r1sQZSgPIqSDw2+8qYbU3ItiAGMo3p6u968Veqoa+BQvQ9TCJcsympdrdBAg0Q/sk3ctnS1KMjB93g7FSSTmAIbCx58u4HkhnipznO/S1npXm/aw+9e1zvEuiWmC37k01vi6rcFQlGNpTf0wlvYLdDyYnXj1ZjWQahvgI71SVOnjUzUWiDqb5KqTC6y6tHy76fr0VUKNskaXMILMyFTtMa/cAT79d5pnrScfIKXruQ+iv763BnePgxHheNZSQplT0ospS5AXXnDOvfc7y9E08ec9RhE64Ld6hADeaLX0X8FbZ/N8BWP5zCZRIN741SlvU7KoqPjayk/P846lLAqmn9Mum2blZH9Fzz";

    /**
     * Jewel arm servo configurations
     */
    public static final double  JEWEL_ARM_VERTICAL_SERVO_POSITION    = 0.675;
    public static final double  JEWEL_ARM_HORIZONTAL_SERVO_POSITION  = 0.1;
    public static final double  JEWEL_ARM_SERVO_MOVING_STEP_CHANGE   = 0.025;

    // 50 milli-seconds before each servo move, so that the servo does not move too fast and sudden.
    public static final int     SLEEP_INTERVAL_BETWEEN_SERVO_MOVES_MS = 50;

    /**
     * Driving configurations
     */
    public static final double  COUNTS_PER_MOTOR_REV    = 1440 ;    // eg: TETRIX Motor Encoder
    public static final double  DRIVE_GEAR_REDUCTION    = 2.0 ;     // This is < 1.0 if geared UP
    public static final double  WHEEL_DIAMETER_INCHES   = 4.0 ;     // For figuring circumference
    public static final double  COUNTS_PER_INCH         =
            (COUNTS_PER_MOTOR_REV * DRIVE_GEAR_REDUCTION) / (WHEEL_DIAMETER_INCHES * 3.1415);
    public static final double  DRIVE_SPEED             = 0.6;
    public static final double  TURN_SPEED              = 0.5;

    /**
     * Autonomous mode driving configurations
     */
    public static final double START_TO_NEAREST_DISTANCE_INCHES = 7;
    public static final double START_TO_MIDDLE_DISTANCE_INCHES  = 7.5;
    public static final double START_TO_FARREST_DISTANCE_INCHES = 8;

}
