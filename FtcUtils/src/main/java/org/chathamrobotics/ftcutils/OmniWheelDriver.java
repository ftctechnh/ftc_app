package org.chathamrobotics.ftcutils;

import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.robotcore.external.Telemetry;

/**
 * Driving with omni wheels
 */
public class OmniWheelDriver {
    // Constants

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

    public OmniWheelDriver(DcMotor frontLeft, DcMotor frontRight, DcMotor backLeft, DcMotor backRight) {
        this(frontLeft, frontRight, backLeft, backRight, null);
    }
    public OmniWheelDriver(DcMotor frontLeft, DcMotor frontRight, DcMotor backLeft, DcMotor backRight, Telemetry telemetry) {
        this.frontLeft = frontLeft;
        this.frontRight = frontRight;
        this.backLeft = backLeft;
        this.backRight = backRight;
        this.telemetry = telemetry;
    }

    // TODO: 11/7/2016 add in move/drive method. move(double x, double y, double rotation, double magnitude
}
