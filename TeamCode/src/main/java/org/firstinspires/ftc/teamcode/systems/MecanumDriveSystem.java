package org.firstinspires.ftc.teamcode.systems;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.util.Range;
    
import org.firstinspires.ftc.teamcode.components.configs.ConfigParser;
import org.firstinspires.ftc.teamcode.components.scale.ExponentialRamp;
import org.firstinspires.ftc.teamcode.components.scale.IScale;
import org.firstinspires.ftc.teamcode.components.scale.LinearScale;
import org.firstinspires.ftc.teamcode.components.scale.Point;
import org.firstinspires.ftc.teamcode.components.scale.Ramp;
import org.firstinspires.ftc.teamcode.systems.base.DriveSystem4Wheel;
import org.firstinspires.ftc.teamcode.systems.imu.IMUSystem;

public class MecanumDriveSystem extends DriveSystem4Wheel
{

    private ConfigParser config;

    public double TICKS_IN_INCH;
    public double TICKS_IN_INCH_STRAFE;
    private final IScale JOYSTICK_SCALE = new LinearScale(0.62, 0);
    private static double TURN_RAMP_POWER_CUTOFF = 0.1;
    public static double RAMP_POWER_CUTOFF;

    public IMUSystem imuSystem;

    private double initialHeading;

    private boolean slowDrive;

    /**
     * Constructs a new MecanumDriveSystem object.
     * @param opMode
     */
    public MecanumDriveSystem(OpMode opMode) {
        super(opMode, "MecanumDrive");

        //this.config = new ConfigParser("Testy.omc");
        TICKS_IN_INCH = 69;
        TICKS_IN_INCH_STRAFE = 69;
        RAMP_POWER_CUTOFF = 0.3;

        imuSystem = new IMUSystem(opMode);
        initialHeading = imuSystem.getHeading();

        telemetry.log("MecanumDriveSystem","power: {0}", 0);
        telemetry.log("MecanumDriveSystem","distance: {0}", 0);
    }

    /**
     * Clips joystick values and drives the motors.
     * @param rightX Right X joystick value
     * @param rightY Right Y joystick value
     * @param leftX Left X joystick value
     * @param leftY Left Y joystick value in case you couldn't tell from the others
     * @param slowDrive Set to true for 30 % motor power.
     */
    public void mecanumDrive(float rightX, float rightY, float leftX, float leftY, boolean slowDrive) {
        this.slowDrive = slowDrive;
        setDirection(DriveDirection.FORWARD);
        rightX = Range.clip(rightX, -1, 1);
        leftX = Range.clip(leftX, -1, 1);
        leftY = Range.clip(leftY, -1, 1);

        rightX = scaleJoystickValue(rightX);
        leftX = scaleJoystickValue(leftX);
        leftY = scaleJoystickValue(leftY);

        // write the values to the motors 1
        double frontRightPower = -leftY - leftX - rightX;
        double backRightPower = -leftY + leftX + rightX;
        double frontLeftPower = -leftY + leftX - rightX;
        double backLeftPower = -leftY - leftX + rightX;

        this.motorFrontRight.setPower(Range.clip(frontRightPower, -1, 1));
        telemetry.log("Mecanum Drive System","FRpower: {0}", Range.clip(frontRightPower, -1, 1));
        this.motorBackRight.setPower(Range.clip(backRightPower, -1, 1));
        telemetry.log("Mecanum Drive System","BRPower: {0}", Range.clip(backRightPower, -1, 1));
        this.motorFrontLeft.setPower(Range.clip(frontLeftPower, -1, 1));
        telemetry.log("Mecanum Drive System", "FLPower: {0}", Range.clip(frontLeftPower, -1, 1));
        this.motorBackLeft.setPower(Range.clip(backLeftPower, -1, 1));
        telemetry.log("Mecanum Drive System", "BLPower: {0}", Range.clip(backLeftPower, -1, 1));
        telemetry.write();


    }

    /**
     * Scales the joystick value while keeping in mind slow mode.
     * @param joystickValue
     * @return a value from 0 - 1 based on the given value
     */
    private float scaleJoystickValue(float joystickValue) {
        float slowDriveCoefficient = .3f;
        if (!slowDrive) slowDriveCoefficient = 1;
        return joystickValue > 0
                ? (float)  JOYSTICK_SCALE.scaleX(joystickValue * joystickValue) * slowDriveCoefficient
                : (float) -JOYSTICK_SCALE.scaleX(joystickValue * joystickValue) * slowDriveCoefficient;
    }

    /**
     * Drives in God Mode (keeping angles consistent with the heading so it doesn't turn unexpectedly)
     * @param rightX Right X value of the joystick
     * @param rightY Right Y value of the joystick
     * @param leftX Left X value of the joystick
     * @param leftY Left Y value of the joystick in case that wasn't clear
     */
    public void driveGodMode(float rightX, float rightY, float leftX, float leftY) {
        driveGodMode(rightX, rightY, leftX, leftY, 1);
    }

    /**
     * Drives in God Mode (keeping angles consistent with the heading so it doesn't turn unexpectedly)
     * @param rightX Right X value of the joystick
     * @param rightY Right Y value of the joystick
     * @param leftX Left X value of the joystick
     * @param leftY Left Y value of the joys
     * @param coeff Used to set speed
     */
    public void driveGodMode(float rightX, float rightY, float leftX, float leftY, float coeff) {
        double currentHeading = Math.toRadians(imuSystem.getHeading());
        double headingDiff = initialHeading - currentHeading;

        rightX = scaleJoystickValue(rightX);
        leftX = scaleJoystickValue(leftX);
        leftY = scaleJoystickValue(leftY);

        double speed = Math.sqrt(leftX * leftX + leftY * leftY);
        double angle = Math.atan2(leftX, leftY) + (Math.PI / 2) + headingDiff;
        double changeOfDirectionSpeed = rightX;
        double x = coeff * speed * Math.cos(angle);
        double y = coeff * speed * Math.sin(angle);

        double frontLeft = Range.clip(y - changeOfDirectionSpeed + x, -1, 1);
        double frontRight = Range.clip(y + changeOfDirectionSpeed - x, -1, 1);
        double backLeft = Range.clip(y - changeOfDirectionSpeed - x, -1, 1);
        double backRight = Range.clip(y + changeOfDirectionSpeed + x, -1, 1);

        motorFrontLeft.setPower(frontLeft);
        motorFrontRight.setPower(frontRight);
        motorBackLeft.setPower(backLeft);
        motorBackRight.setPower(backRight);
    }

    /**
     * Sets motor values based on the joystick x and y values.
     * @param x X value of the joystick
     * @param y Y value of the joystick
     */
    public void mecanumDriveXY(double x, double y) {
        this.motorFrontRight.setPower(Range.clip(y + x, -1, 1));
        this.motorBackRight.setPower(Range.clip(y - x, -1, 1));
        this.motorFrontLeft.setPower(Range.clip(y - x, -1, 1));
        this.motorBackLeft.setPower(Range.clip(y + x, -1, 1));
    }

    /**
     * Drives using a polar coordinate system
     * @param radians the radians value
     * @param power The power of the motors
     */
    public void mecanumDrivePolar(double radians, double power) {
        double x = Math.cos(radians) * power;
        double y = Math.sin(radians) * power;
        mecanumDriveXY(x, y);
    }

    /**
     * Drives the given amount of inches forward.
     * @param inches Amount of inches to drive
     * @param power Power of the motors
     */
    public void driveToPositionInches(int inches, double power) {
        int ticks = (int) inchesToTicks(inches);
        setDirection(DriveDirection.FORWARD);
        driveToPositionTicks(ticks, power);
    }

    /**
     * Strafes left for the given amount of inches
     * @param inches Amount of inches to strafe
     * @param power Power of the motors
     */
    public void strafeLeftToPositionInches(int inches, double power) {
        setDirection(MecanumDriveDirection.STRAFE_LEFT);
        int ticks = (int) inchesToTicksStrafe(inches);
        driveToPositionTicks(ticks, power);
    }

    /**
     * Strafes right for the given amount of inches
     * @param inches Amount of inches to strafe
     * @param power Power of the motors
     */
    public void strafeRightToPositionInches(int inches, double power) {
        setDirection(MecanumDriveDirection.STRAFE_RIGHT);
        int ticks = (int) inchesToTicksStrafe(inches);
        driveToPositionTicks(ticks, power);
    }

    /**
     * Drives the given amount of ticks forward.
     * @param ticks Amount of ticks to drive
     * @param power Power of the motors
     */
    private void driveToPositionTicks(int ticks, double power) {
        motorBackRight.setPower(0);
        motorBackLeft.setPower(0);
        motorFrontLeft.setPower(0);
        motorFrontRight.setPower(0);

        motorFrontRight.setTargetPosition(motorFrontRight.getCurrentPosition() + ticks);
        motorFrontLeft.setTargetPosition(motorFrontLeft.getCurrentPosition() + ticks);
        motorBackRight.setTargetPosition(motorBackRight.getCurrentPosition() + ticks);
        motorBackLeft.setTargetPosition(motorBackLeft.getCurrentPosition() + ticks);

        motorBackLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        motorBackRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        motorFrontRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        motorFrontLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        Ramp ramp = new ExponentialRamp(new Point(0, RAMP_POWER_CUTOFF),
                new Point((ticks / 4), power));

        double adjustedPower = Range.clip(power, -1.0, 1.0);

        motorBackRight.setPower(adjustedPower);
        motorBackLeft.setPower(adjustedPower);
        motorFrontLeft.setPower(adjustedPower);
        motorFrontRight.setPower(adjustedPower);

        while (motorFrontLeft.isBusy() ||
                motorFrontRight.isBusy() ||
                motorBackRight.isBusy() ||
                motorBackLeft.isBusy()) {

            int distance = getMinDistanceFromTarget();

            if (distance < 50) {
                break;
            }

            telemetry.log("MecanumDriveSystem","targetPos motorFL: " + this.motorFrontLeft.getTargetPosition());
            telemetry.log("MecanumDriveSystem","targetPos motorFR: " + this.motorFrontRight.getTargetPosition());
            telemetry.log("MecanumDriveSystem","targetPos motorBL: " + this.motorBackLeft.getTargetPosition());
            telemetry.log("MecanumDriveSystem","targetPos motorBR: " + this.motorBackRight.getTargetPosition());

            telemetry.log("MecanumDriveSystem","currentPos motorFL: " + this.motorFrontLeft.getCurrentPosition());
            telemetry.log("MecanumDriveSystem","currentPos motorFR: " + this.motorFrontRight.getCurrentPosition());
            telemetry.log("MecanumDriveSystem","currentPos motorBL: " + this.motorBackLeft.getCurrentPosition());
            telemetry.log("MecanumDriveSystem","currentPos motorBR: " + this.motorBackRight.getCurrentPosition());

            double direction = 1.0;
            if (distance < 0) {
                distance = -distance;
                direction = -1.0;
            }

            double scaledPower = ramp.scaleX(distance);
            telemetry.log("MecanumDriveSystem","power: " + scaledPower);
            setPower(direction * scaledPower);
            telemetry.write();
        }
        motorBackLeft.setPower(0);
        motorBackRight.setPower(0);
        motorFrontRight.setPower(0);
        motorFrontLeft.setPower(0);
    }

    /**
     * Gets the minimum distance from the target
     * @return
     */
    public int  getMinDistanceFromTarget() {
        int d = this.motorFrontLeft.getTargetPosition() - this.motorFrontLeft.getCurrentPosition();
        d = Math.min(d, this.motorFrontRight.getTargetPosition() - this.motorFrontRight.getCurrentPosition());
        d = Math.min(d, this.motorBackLeft.getTargetPosition() - this.motorBackLeft.getCurrentPosition());
        d = Math.min(d, this.motorBackRight.getTargetPosition() - this.motorBackRight.getCurrentPosition());
        return d;
    }

    /**
     * Converts inches to ticks
     * @param inches Inches to convert to ticks
     * @return
     */
    public double inchesToTicks(int inches) {
        return inches * TICKS_IN_INCH;
    }

    /**
     * Converts inches to ticks but for strafing
     * @param inches Inches to convert to ticks
     * @return
     */
    public double inchesToTicksStrafe(int inches) {
        return inches * TICKS_IN_INCH_STRAFE;
    }

    /**
     * Turns relative the heading upon construction
     * @param degrees The degrees to turn the robot by
     * @param maxPower The maximum power of the motors
     */
    public void turnAbsolute(double degrees, double maxPower) {
        turn(degrees, maxPower, initialHeading);
    }

    /**
     * Turns the robot by a given amount of degrees using the current heading
     * @param degrees The degrees to turn the robot by
     * @param maxPower The maximum power of the motors
     */
    public void turn(double degrees, double maxPower) {
        turn(degrees, maxPower, imuSystem.getHeading());
    }

    /**
     * Turns the robot by a given amount of degrees
     * @param degrees The degrees to turn the robot by
     * @param maxPower The maximum power of the motors
     * @param initialHeading The initial starting point
     */
    private void turn(double degrees, double maxPower, double initialHeading) {

        double heading = -initialHeading;
        double targetHeading = 0;

        if ((degrees % 360) > 180) {
            targetHeading = heading + ((degrees % 360) - 360);
        } else {
            targetHeading = heading + (degrees % 360);
        }

        setRunMode(DcMotor.RunMode.RUN_USING_ENCODER);

        // Between 90 (changed from 130) and 2 degrees away from the target
        // we want to slow down from maxPower to 0.1
        ExponentialRamp ramp = new ExponentialRamp(new Point(2.0, TURN_RAMP_POWER_CUTOFF), new Point(90, maxPower));

        while (Math.abs(targetHeading - heading) > 1) {
            double power = getTurnPower(ramp, targetHeading, heading);
            telemetry.log("MecanumDriveSystem","heading: " + heading);
            telemetry.log("MecanumDriveSystem","target heading: " + targetHeading);
            telemetry.log("MecanumDriveSystem","power: " + power);
            telemetry.log("MecanumDriveSystem","distance left: " + Math.abs(targetHeading - heading));
            telemetry.write();

            tankDrive(power, -power);
            heading = -imuSystem.getHeading();
        }
        this.setPower(0);
    }

    /**
     * Gets the turn power needed
     * @param ramp the ramp
     * @param targetHeading the target heading
     * @param heading the heading
     * @return
     */
    private double getTurnPower(Ramp ramp, double targetHeading, double heading) {
        return targetHeading - heading < 0 ?
                -ramp.scaleX(Math.abs(targetHeading - heading)) :
                ramp.scaleX(targetHeading - heading);
    }

    /**
     * Parks on the crater.
     * @param maxPower Maximum power
     */
    public void parkOnCrater(double maxPower) {
        double initPitch = imuSystem.getpitch();
        double initRoll = imuSystem.getRoll();
        double criticalAngle = 1.5;

        setDirection(DriveDirection.FORWARD);
        setPower(maxPower);

        while ((Math.abs(imuSystem.getpitch() - initPitch) < criticalAngle) &&
                (Math.abs(imuSystem.getRoll() - initRoll) < criticalAngle)) {
            setPower(maxPower);
        }
        setPower(0);
    }

    /**
     * Sets the direction of each motor.
     * @param direction Direction to set the motors - use the values STRAFE_RIGHT and STRAFE_LEFT.
     */
    public void setDirection(MecanumDriveDirection direction) {
        switch (direction){
            case STRAFE_LEFT:
                motorFrontRight.setDirection(DcMotorSimple.Direction.REVERSE);
                motorFrontLeft.setDirection(DcMotorSimple.Direction.FORWARD);
                motorBackRight.setDirection(DcMotorSimple.Direction.REVERSE);
                motorBackLeft.setDirection(DcMotorSimple.Direction.FORWARD);
                break;
            case STRAFE_RIGHT:
                motorFrontRight.setDirection(DcMotorSimple.Direction.FORWARD);
                motorFrontLeft.setDirection(DcMotorSimple.Direction.REVERSE);
                motorBackRight.setDirection(DcMotorSimple.Direction.FORWARD);
                motorBackLeft.setDirection(DcMotorSimple.Direction.REVERSE);
                break;
        }
    }

    /**
     * Use STRAFE_RIGHT and STRAFE_LEFT for the setDirection() method
     */
    public enum MecanumDriveDirection {
        STRAFE_RIGHT, STRAFE_LEFT;
    }
}
