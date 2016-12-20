package org.chathamrobotics.ftcutils;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
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
    public double offsetAngle;

    /*
     * Whether or not to log telemetry data
     */
    public boolean silent;

    /*
     * Builds new OmniWheelDriver using default names for motors
     * ("FrontLeft","FrontRight","BackLeft","BackRight")
     * @param {HardwareMap} hardwareMap
     * @param {Telemetry} [telemetry]
     */
    public static OmniWheelDriver build(OpMode opMode) {
        return new OmniWheelDriver(
                opMode.hardwareMap.dcMotor.get("FrontLeft"),
                opMode.hardwareMap.dcMotor.get("FrontRight"),
                opMode.hardwareMap.dcMotor.get("BackLeft"),
                opMode.hardwareMap.dcMotor.get("BackRight"),
                opMode.telemetry
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
    public OmniWheelDriver(DcMotor frontLeft, DcMotor frontRight, DcMotor backLeft,
                           DcMotor backRight, Telemetry telemetry) {
        this.frontLeft = frontLeft;
        this.frontRight = frontRight;
        this.backLeft = backLeft;
        this.backRight = backRight;
        this.telemetry = telemetry;
    }

    /*
     * moves the robot based off of analogue inputs
     * @param {double} x                The x value
     * @param {double} y                The y value
     * @param {double} rotation         The rotation value
     * @param {double} [modifier]      The modifier for the power.
     * @param {boolean} [smooth]        Whether or not to smooth the modifier
     */
    public void drive(double x, double y, double rotation) {
        //Default modifier
        drive(x,y,rotation,Math.sqrt((x*x) + (y*y)), true);
    }
    public void drive(double x, double y, double rotation, boolean smooth) {
        //Default modifier
        drive(x,y,rotation,Math.sqrt((x*x) + (y*y)), smooth);
    }
    public void drive(double x, double y, double rotation, double modifier, boolean smooth) {
        Range.throwIfRangeIsInvalid(x, -1, 1);
        Range.throwIfRangeIsInvalid(y, -1, 1);
        Range.throwIfRangeIsInvalid(rotation, -1, 1);

        //Using a function on variable r will smooth out the slow values but still give full range
        if(smooth)
            modifier = modifier*modifier;
        
        move(Math.atan2(y, x), rotation, modifier);
    }

    /*
     * moves the robot in the direction specified
     * @param {double} angle
     * @param {double} rotation
     * @param {double} modifier
     */
    public void move(double angle, double rotation, double modifier) {
        if(!silent) {
            telemetry.addData("OmniWheelDriver 1", "rotation=" + rotation);
            telemetry.addData("OmniWheelDriver 2", "angle=" + angle);
            telemetry.addData("OmniWheelDriver 3", "angle(corrected)=" + (angle + OMNI_WHEEL_ANGLE_CORRECTION));
            telemetry.addData("OmniWheelDriver 4", "angle(corrected & offset)=" + (angle + OMNI_WHEEL_ANGLE_CORRECTION + offsetAngle));
            telemetry.addData("OmniWheelDriver 5", "modifier=" + modifier);
            telemetry.addData("OmniWheelDriver 6", "offset angle=" + offsetAngle);
        }

        angle += OMNI_WHEEL_ANGLE_CORRECTION + offsetAngle;

        frontLeft.setPower(calculateMotorPower(true, true, angle, rotation, modifier));
        frontRight.setPower(calculateMotorPower(true, false, angle, rotation, modifier));
        backLeft.setPower(calculateMotorPower(false, true, angle, rotation, modifier));
        backRight.setPower(calculateMotorPower(false, false, angle, rotation, modifier));
    }
    
    private double calculateMotorPower(boolean isFront, boolean isLeft, double angle, double rotation, double modifier) {
        double power = (isFront == isLeft ? Math.sin(angle) : Math.cos(angle)) * modifier;
        
        if(isFront){
            power -= rotation;
        }
        else {
            power += rotation;
            power *= -1;
        }
        
        return Range.clip(power, -1, 1);
    }
}
