package org.firstinspires.ftc.team9853;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.robotcore.external.Telemetry;

/**
 * OmniWheelDriver
 * Handles all of the driving functionality for omni wheels
 */
public class OmniWheelDriver {

    /*
     * The telemetry from the opmode. Used for debugging purposes
     */
    private Telemetry telemetry;

    /*
     * The motors are required to to move
     */
    private DcMotor frontLeft;
    private DcMotor frontRight;
    private DcMotor backLeft;
    private DcMotor backRight;

    public double offsetAngle = 0;

    /**
     * Creates a new driver instance
     */
    public OmniWheelDriver(DcMotor frontLeft, DcMotor frontRight, DcMotor backLeft, DcMotor backRight) {
        this(frontLeft, frontRight, backLeft, backRight, null);
    }
    public OmniWheelDriver(DcMotor frontLeft, DcMotor frontRight, DcMotor backLeft, DcMotor backRight, Telemetry telemetry) {
        this.frontLeft =frontLeft;
        this.frontRight = frontRight;
        this.backLeft = backLeft;
        this.backRight = backRight;
        this.telemetry = telemetry;
    }

    /*
     * Moves the robot in the direction specified by the x, y and rotation
     * @param {double} x                    The y direction to move. Used to calculate the angle
     * @param {double} y                    The x direction to move. Used to calculate the angle
     * @param {double} [rotation=0]         Used to implement a rotation in the movement (most effective alone)
     * @param {double} [powerModifier=1]    Used to scale the motor power
     */
    public void move(double x, double y) throws IllegalArgumentException {
        move(x, y, 0);
    }
    public void move(double x, double y, double rotation) throws IllegalArgumentException {
        move(x, y, rotation, Math.sqrt((x*x) + (y*y)));
    }
    public void move(double x, double y, double rotation, double powerModifier) throws IllegalArgumentException {
        Range.throwIfRangeIsInvalid(x, -1, 1);
        Range.throwIfRangeIsInvalid(y, -1, 1);
        Range.throwIfRangeIsInvalid(rotation, -1, 1);

        double angle = 0;

        if (x != 0) {
            angle = Math.atan(y / x);

            // get the opposite
            if(x < 0) {
                angle += Math.PI;
            }

        }
        else if(y > 0) {
            // 90 degrees
            angle = Math.PI/2;
        }
        else{
            // 270 degrees
            angle = (3 * Math.PI)/2;
        }

        // add 45 deg (match the axes of the robot with axes of the controls)
        angle += Math.PI / 4;

        if (telemetry != null) {
            telemetry.addData("OmniWheelDriver 1", "x=" + x);
            telemetry.addData("OmniWheelDriver 2", "y=" + y);
            telemetry.addData("OmniWheelDriver 3", "rotation=" + rotation);
            telemetry.addData("OmniWheelDriver 4", "angle(corrected)=" + angle);
            telemetry.addData("OmniWheelDriver 5", "angle=" + (angle - Math.PI / 4));
            telemetry.addData("OmniWheelDriver 6", "powerModifier=" + powerModifier);
            telemetry.addData("OmniWheelDriver 7", "offset angle=" + offsetAngle);
        }

        // Set powers
        frontLeft.setPower(getMotorPower(true, true, angle + offsetAngle, rotation, powerModifier));
        frontRight.setPower(getMotorPower(true, false, angle + offsetAngle, rotation, powerModifier));
        backLeft.setPower(getMotorPower(false, true, angle + offsetAngle, rotation, powerModifier));
        backRight.setPower(getMotorPower(false, false, angle + offsetAngle, rotation, powerModifier));
    }

    /*
     * Moves forward
     * @param {double} modifier     The speed modifier
     */
    public void forward() {
        forward(1);
    }
    public void forward(double modifier) {
        move(0, 1,0, modifier);
    }

    /*
     * Moves backwards
     * @param {double} modifier     The speed modifier
     */
    public void backwards() {
        backwards(1);
    }
    public void backwards(double modifier) {
        move(0, -1, 0, modifier);
    }

    /*
     * Moves left
     * @param {double} modifier     The speed modifier
     */
    public void left() {
        left(1);
    }
    public void left(double modifier) {
        move(-1, 0, 0, modifier);
    }

    /*
     * Moves right
     * @param {double} modifier     The speed modifier
     */
    public void right() {
        right(1);
    }
    public void right(double modifier) {
        move(1, 0, 0, modifier);
    }

    /*
     * Calculate the power of each of the motors
     * @param {boolean} isFront     Whether the motor is on the right side of the robot.
     * @param {boolean} isLeft      Whether the motor is on the left side of the robot
     * @param {double} angle        The angle to move in
     * @param {double} rotation     The rotation to implement
     * @param {double} modifier     The modifier to implement
     */
    private double getMotorPower(boolean isFront, boolean isLeft, double angle, double rotation, double modifier) {
        double power;

        rotation *= modifier;

        // Get power along axis
        power = isFront == isLeft ? Math.sin(angle) * modifier : Math.cos(angle) * modifier;

        // Rotation and reversal
        power = isFront ? power - rotation : -1 * (power + rotation);

        return Range.clip(power, -1, 1);
    }
}
