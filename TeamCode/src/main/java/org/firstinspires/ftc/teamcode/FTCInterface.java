package org.firstinspires.ftc.teamcode;

/*
 * This class contains all of the data for the ftc interface.
 */
public class FTCInterface {

    /* Declare Motor names. */
    public static final String LEFT_MOTOR = "motorLeft"; // Motor that powers the left wheel
    public static final String RIGHT_MOTOR = "motorRight"; // Motor that powers the right wheel
    public static final String COLLECTOR_MOTOR = "motorCollector"; // Front motor that collects balls
    public static final String LAUNCHER_MOTOR = "motorLauncher"; // Ball launching motor

    /* Declare Sensor names. */
    public static final String DOWN_SENSOR = "sensorDown"; // Color sensor looking down
    public static final String LEFT_SENSOR = "sensorLeft"; // Color sensor looking to the left
    public static final String GYRO_SENSOR = "sensorGyro"; // Gyro sensor

    /* Declare Servo names. */

    /* Declare Thresholds. */
    public static final double COLOR_THRESHOLD = 0.5; // The distance from the color specified
    public static final double ANGLE_THRESHOLD = 3; // The number of degrees from the amount specified to stop turning at

    /* Declare Configuration constants. */
    public static final double LEVER_POWER = 1.0; // The power to set on the lever servo
    public static final double LAUNCHER_MOTOR_POWER = 1.0; // The launcher's power
}
