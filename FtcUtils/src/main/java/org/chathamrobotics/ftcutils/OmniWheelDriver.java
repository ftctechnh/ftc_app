package org.chathamrobotics.ftcutils;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.robotcore.external.Telemetry;

/**
 * Driving with omni wheels
 */
public class OmniWheelDriver {
    // Constants
    public static final double OMNI_WHEEL_ANGLE_CORRECTION = Math.PI/4;
    public static final double FRONT_OFFSET = 0;
    public static final double LEFT_OFFSET = Math.PI/2;
    public static final double BACK_OFFSET = Math.PI;
    public static final double RIGHT_OFFSET = 3* Math.PI / 2;

    // Stateful
    private Telemetry telemetry;
    private DcMotor frontLeft;
    private DcMotor frontRight;
    private DcMotor backLeft;
    private DcMotor backRight;

    /*
     * The angle used to offset the front of the robot
     */
    public double offsetAngle = 0;

    /*
     * Whether or not to log telemetry data
     */
    public boolean silent = false;

    /*
     * Builds new OmniWheelDriver using default names for motors ("FrontLeft","FrontRight","BackLeft","BackRight")
     * @param {HardwareMap} hardwareMap
     * @param {Telemetry} [telemetry]
     */
    public static OmniWheelDriver build(HardwareMap hardwareMap, Telemetry telemetry) {
        return new OmniWheelDriver(
                hardwareMap.dcMotor.get("FrontLeft"),
                hardwareMap.dcMotor.get("FrontRight"),
                hardwareMap.dcMotor.get("BackLeft"),
                hardwareMap.dcMotor.get("BackRight"),
                telemetry
        );
    }

    /*
     * creates new OmniWheelDriver.
     * @param {DcMotor} frontLeft
     * @param {DcMotor} frontRight
     * @param {DcMotor} backLeft
     * @param {DcMotor} backRight
     * @param {Telemetry} telemetry
     */
    public OmniWheelDriver(DcMotor frontLeft, DcMotor frontRight, DcMotor backLeft, DcMotor backRight, Telemetry telemetry) {
        this.frontLeft = frontLeft;
        this.frontRight = frontRight;
        this.backLeft = backLeft;
        this.backRight = backRight;
        this.telemetry = telemetry;
    }

    /*
     * TODO: 11/7/2016 write this
     */
    public void drive(double x, double y, double rotation, double magnitude) {
        // NOTE: I think this works but someone should check. I think -0 does some wonk things
        move(Math.atan2(y, x), rotation, magnitude);
    }

    /*
     * TODO: 11/7/2016 write this
     */
    public void move(double angle, double rotation, double magnitude) {
        if(! silent) {
            telemetry.addData("OmniWheelDriver 1", "rotation=" + rotation);
            telemetry.addData("OmniWheelDriver 2", "angle=" + angle);
            telemetry.addData("OmniWheelDriver 3", "angle(corrected)=" + (angle + OMNI_WHEEL_ANGLE_CORRECTION));
            telemetry.addData("OmniWheelDriver 4", "angle(corrected & offset)=" + (angle + OMNI_WHEEL_ANGLE_CORRECTION + offsetAngle));
            telemetry.addData("OmniWheelDriver 5", "magnitude=" + magnitude);
            telemetry.addData("OmniWheelDriver 6", "offset angle=" + offsetAngle);
        }

        angle += OMNI_WHEEL_ANGLE_CORRECTION + offsetAngle;

        // TODO: 11/7/2016 write this
    }
}
