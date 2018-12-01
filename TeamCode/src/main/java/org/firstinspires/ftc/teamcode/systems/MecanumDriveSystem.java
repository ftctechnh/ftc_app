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

    // test
    public void mecanumDrive(float rightX, float rightY, float leftX, float leftY, boolean slowDrive) {
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

        this.slowDrive = slowDrive;
    }

    private float scaleJoystickValue(float joystickValue) {
        float slowDriveCoefficient = .3f;
        if (!slowDrive) slowDriveCoefficient = 1;
        return joystickValue > 0
                ? (float)  JOYSTICK_SCALE.scaleX(joystickValue * joystickValue) * slowDriveCoefficient
                : (float) -JOYSTICK_SCALE.scaleX(joystickValue * joystickValue) * slowDriveCoefficient;
    }

    public void driveGodMode(float rightX, float rightY, float leftX, float leftY) {
        driveGodMode(rightX, rightY, leftX, leftY, 1);
    }

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

    public void mecanumDriveXY(double x, double y) {
        this.motorFrontRight.setPower(Range.clip(y + x, -1, 1));
        this.motorBackRight.setPower(Range.clip(y - x, -1, 1));
        this.motorFrontLeft.setPower(Range.clip(y - x, -1, 1));
        this.motorBackLeft.setPower(Range.clip(y + x, -1, 1));
    }

    public void mecanumDrivePolar(double radians, double power) {
        double x = Math.cos(radians) * power;
        double y = Math.sin(radians) * power;
        mecanumDriveXY(x, y);
    }

    public void driveToPositionInches(int inches, double power) {
        int ticks = (int) inchesToTicks(inches);
        setDirection(DriveDirection.FORWARD);
        driveToPositionTicks(ticks, power);
    }

    public void strafeLeftToPositionInches(int inches, double power) {
        setDirection(MecanumDriveDirection.STRAFE_LEFT);
        int ticks = (int) inchesToTicksStrafe(inches);
        driveToPositionTicks(ticks, power);
    }

    public void strafeRightToPositionInches(int inches, double power) {
        setDirection(MecanumDriveDirection.STRAFE_RIGHT);
        int ticks = (int) inchesToTicksStrafe(inches);
        driveToPositionTicks(ticks, power);
    }

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

    public int  getMinDistanceFromTarget() {
        int d = this.motorFrontLeft.getTargetPosition() - this.motorFrontLeft.getCurrentPosition();
        d = min(d, this.motorFrontRight.getTargetPosition() - this.motorFrontRight.getCurrentPosition());
        d = min(d, this.motorBackLeft.getTargetPosition() - this.motorBackLeft.getCurrentPosition());
        d = min(d, this.motorBackRight.getTargetPosition() - this.motorBackRight.getCurrentPosition());
        return d;
    }

    public double inchesToTicks(int inches) {
        return inches * TICKS_IN_INCH;
    }

    public double inchesToTicksStrafe(int inches) {
        return inches * TICKS_IN_INCH_STRAFE;
    }

    private int min(int d1, int d2) {
        if (d1 < d2) {
            return d1;
        } else {
            return d2;
        }
    }

    public void turnAbsolute(double degrees, double maxPower) {
        turn(degrees, maxPower, initialHeading);
    }

    public void turn(double degrees, double maxPower) {
        turn(degrees, maxPower, imuSystem.getHeading());
    }

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

        while (Math.abs(computeDegreesDiff(targetHeading, heading)) > 1) {
            double power = getTurnPower(ramp, targetHeading, heading);
            telemetry.log("MecanumDriveSystem","heading: " + heading);
            telemetry.log("MecanumDriveSystem","target heading: " + targetHeading);
            telemetry.log("MecanumDriveSystem","power: " + power);
            telemetry.log("MecanumDriveSystem","distance left: " + Math.abs(computeDegreesDiff(targetHeading, heading)));
            telemetry.write();

            tankDrive(power, -power);
            heading = -imuSystem.getHeading();
        }
        this.setPower(0);
    }

    private double getTurnPower(Ramp ramp, double targetHeading, double heading) {
        double diff = computeDegreesDiff(targetHeading, heading);

        if (diff < 0) {
            return -ramp.scaleX(Math.abs(diff));
        } else {
            return ramp.scaleX(Math.abs(diff));
        }
    }

    private double computeDegreesDiff(double targetHeading, double heading) {
        return targetHeading - heading;
    }

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

    public enum MecanumDriveDirection {
        STRAFE_RIGHT, STRAFE_LEFT;
    }
}
