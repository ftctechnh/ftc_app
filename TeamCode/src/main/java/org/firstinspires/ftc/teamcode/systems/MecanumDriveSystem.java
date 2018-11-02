package org.firstinspires.ftc.teamcode.systems;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.Config.ConfigParser;
import org.firstinspires.ftc.teamcode.components.scale.ExponentialRamp;
import org.firstinspires.ftc.teamcode.components.scale.IScale;
import org.firstinspires.ftc.teamcode.components.scale.LinearScale;
import org.firstinspires.ftc.teamcode.components.scale.Point;
import org.firstinspires.ftc.teamcode.components.scale.Ramp;
import org.firstinspires.ftc.teamcode.systems.base.DriveSystem4Wheel;
import org.firstinspires.ftc.teamcode.systems.imu.IMUSystem;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class MecanumDriveSystem extends DriveSystem4Wheel {

    private ConfigParser config;

    private double TICKS_TO_INCHES;
    private final IScale JOYSTICK_SCALE = new LinearScale(0.62, 0);
    private static double TURN_RAMP_POWER_CUTOFF = 0.1;
    private static double RAMP_POWER_CUTOFF;

    public IMUSystem imuSystem;

    Telemetry.Item distanceItem;
    Telemetry.Item powerItem;

    private double initialHeading;

    public MecanumDriveSystem(OpMode opMode) {
        super(opMode, "MecanumDrive");

        this.config = new ConfigParser("Testy.omc");
        TICKS_TO_INCHES = config.getInt("ticksInInch");
        RAMP_POWER_CUTOFF = config.getDouble("rampPowerCutoff");

        imuSystem = new IMUSystem(opMode);
        initialHeading = Math.toRadians(imuSystem.getHeading());

        telemetry.log("MecanumDriveSystem","power: {0}", 0);
        telemetry.log("MecanumDriveSystem","distance: {0}", 0);
    }

    private void telem(String message) {
        telemetry.log("MecanumDriveSystem", message);
        telemetry.write();
    }

    public void mecanumDrive(float rightX, float rightY, float leftX, float leftY, boolean slowDrive) {
        rightX = Range.clip(rightX, -1, 1);
        leftX = Range.clip(leftX, -1, 1);
        leftY = Range.clip(leftY, -1, 1);

        rightX = scaleJoystickValue(rightX);
        leftX = scaleJoystickValue(leftX);
        leftY = scaleJoystickValue(leftY);

        // write the values to the motors
        double frontRightPower = leftY + rightX + leftX;
        double backRightPower = leftY + rightX - leftX;
        double frontLeftPower = leftY - rightX - leftX;
        double backLeftPower = leftY - rightX + leftX;
        this.motorFrontRight.setPower(Range.clip(frontRightPower, -1, 1));
        telemetry.log("Mecanum Drive System","FRpower: {0}", Range.clip(frontRightPower, -1, 1));
        this.motorBackRight.setPower(Range.clip(backRightPower, -1, 1));
        telemetry.log("Mecanum Drive System","BRPower: {0}", Range.clip(backRightPower, -1, 1));
        this.motorFrontLeft.setPower(Range.clip(frontLeftPower - leftX, -1, 1));
        telemetry.log("Mecanum Drive System", "FLPower: {0}", Range.clip(frontLeftPower - leftX, -1, 1));
        this.motorBackLeft.setPower(Range.clip(backLeftPower + leftX, -1, 1));
        telemetry.log("Mecanum Drive System", "BLPower: {0}", Range.clip(backLeftPower + leftX, -1, 1));
        telemetry.write();
    }

    private float scaleJoystickValue(float joystickValue) {
        return joystickValue > 0
                ? (float)JOYSTICK_SCALE.scaleX(joystickValue * joystickValue)
                : (float)-JOYSTICK_SCALE.scaleX(joystickValue * joystickValue);
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

        double frontLeft = y - changeOfDirectionSpeed + x;
        double frontRight = y + changeOfDirectionSpeed - x;
        double backLeft = y - changeOfDirectionSpeed - x;
        double backRight = y + changeOfDirectionSpeed + x;

        List<Double> powers = Arrays.asList(frontLeft, frontRight, backLeft, backRight);
        clampPowers(powers);

        motorFrontLeft.setPower(powers.get(0));
        motorFrontRight.setPower(powers.get(1));
        motorBackLeft.setPower(powers.get(2));
        motorBackRight.setPower(powers.get(3));
    }

    private void clampPowers(List<Double> powers) {
        double minPower = Collections.min(powers);
        double maxPower = Collections.max(powers);
        double maxMag = Math.max(Math.abs(minPower), Math.abs(maxPower));

        if (maxMag > 1.0)
        {
            for (int i = 0; i < powers.size(); i++)
            {
                powers.set(i, powers.get(i) / maxMag);
            }
        }
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

    public void driveToPoShort(int inches, double power) {
        int ticks = (int) inchesToTicks(inches);
        setPower(0);

        motorFrontRight.setDirection(DcMotorSimple.Direction.FORWARD);
        motorFrontLeft.setDirection(DcMotorSimple.Direction.REVERSE);
        motorBackRight.setDirection(DcMotorSimple.Direction.FORWARD);
        motorBackLeft.setDirection(DcMotorSimple.Direction.REVERSE);

        setTargetPosition(motorBackLeft.getCurrentPosition() + ticks);

        setRunMode(DcMotor.RunMode.RUN_TO_POSITION);

        Ramp ramp = new ExponentialRamp(new Point(0, RAMP_POWER_CUTOFF),
                new Point((ticks / config.getInt("rampStartDivisor")), power));

        double adjustedPower = Range.clip(power, -1.0, 1.0);

        setPower(adjustedPower);

        while (anyMotorsBusy()) {
            int distance = getMinDistanceFromTarget();

            if (distance < config.getInt("tolerance")) {
                break;
            }

            telemetry.log("MecanumDriveSystem","targetPos motorFL: " + this.motorFrontLeft.getTargetPosition());
            telemetry.log("MecanumDriveSystem", "targetPos motorFR: " + this.motorFrontRight.getTargetPosition());
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
        setPower(0);
    }

    public void driveToPositionInches(int inches, double power) {
        int ticks = (int) inchesToTicks(inches);
        motorBackRight.setPower(0);
        motorBackLeft.setPower(0);
        motorFrontLeft.setPower(0);
        motorFrontRight.setPower(0);

        motorFrontRight.setDirection(DcMotorSimple.Direction.REVERSE);
        motorFrontLeft.setDirection(DcMotorSimple.Direction.REVERSE);
        motorBackRight.setDirection(DcMotorSimple.Direction.FORWARD);
        motorBackLeft.setDirection(DcMotorSimple.Direction.FORWARD);

        motorFrontRight.setTargetPosition(motorFrontRight.getCurrentPosition() + ticks);
        motorFrontLeft.setTargetPosition(motorFrontLeft.getCurrentPosition() + ticks);
        motorBackRight.setTargetPosition(motorBackRight.getCurrentPosition() + ticks);
        motorBackLeft.setTargetPosition(motorBackLeft.getCurrentPosition() + ticks);

        motorBackLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        motorBackRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        motorFrontRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        motorFrontLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        Ramp ramp = new ExponentialRamp(new Point(0, RAMP_POWER_CUTOFF),
                new Point((ticks / config.getInt("rampStartDivisor")), power));

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


            if (distance < config.getInt("tolerance")) {
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

    private int  getMinDistanceFromTarget() {
        int d = this.motorFrontLeft.getTargetPosition() - this.motorFrontLeft.getCurrentPosition();
        d = min(d, this.motorFrontRight.getTargetPosition() - this.motorFrontRight.getCurrentPosition());
        d = min(d, this.motorBackLeft.getTargetPosition() - this.motorBackLeft.getCurrentPosition());
        d = min(d, this.motorBackRight.getTargetPosition() - this.motorBackRight.getCurrentPosition());
        distanceItem.setValue(d);
        return d;
    }

    private double ticksToInches(int ticks) {
        return ticks / TICKS_TO_INCHES;
    }

    private double inchesToTicks(int ticks) {
        return ticks * TICKS_TO_INCHES;
    }


    private int min(int d1, int d2) {
        if (d1 < d2) {
            return d1;
        } else {
            return d2;
        }
    }

    // I changed which distance the motors standardize to from the min to the max
    private int getDistanceFromTarget() {
        //synchDistances(); // getDistanceFromTarget should be redundant with this method
        int d = this.motorFrontLeft.getTargetPosition() - this.motorFrontLeft.getCurrentPosition();
        d = max(d, this.motorFrontRight.getTargetPosition() - this.motorFrontRight.getCurrentPosition());
        d = max(d, this.motorBackLeft.getTargetPosition() - this.motorBackLeft.getCurrentPosition());
        d = max(d, this.motorBackRight.getTargetPosition() - this.motorBackRight.getCurrentPosition());
        distanceItem.setValue(d);
        return d;
    }

    private void synchDistances() {
        int d = this.motorFrontLeft.getCurrentPosition();
        d = max(d, this.motorFrontRight.getCurrentPosition());
        d = max(d, this.motorBackLeft.getCurrentPosition());
        d = max(d, this.motorBackRight.getCurrentPosition());
        if (this.motorBackLeft.getCurrentPosition() != d) {
            this.motorBackLeft.setTargetPosition(motorBackLeft.getTargetPosition() + (d - this.motorBackLeft.getCurrentPosition()));
        }
        if (this.motorBackRight.getCurrentPosition() != d) {
            this.motorBackRight.setTargetPosition(motorBackRight.getTargetPosition() + (d - this.motorBackRight.getCurrentPosition()));
        }
        if (this.motorFrontLeft.getCurrentPosition() != d) {
            this.motorFrontLeft.setTargetPosition(motorFrontLeft.getTargetPosition() + (d - this.motorFrontLeft.getCurrentPosition()));
        }
        if (this.motorFrontRight.getCurrentPosition() != d) {
            this.motorFrontRight.setTargetPosition(motorFrontRight.getTargetPosition() + (d - this.motorFrontRight.getCurrentPosition()));
        }
    }

    private int max(int d1, int d2) {
        if (d1 > d2) {
            return d1;
        } else {
            return d2;
        }
    }

    public void turn(double degrees, double maxPower) {

        double heading = -imuSystem.getHeading();
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

    public void tankDrive(double leftPower, double rightPower) {
        this.motorFrontLeft.setPower(leftPower);
        this.motorBackLeft.setPower(leftPower);
        this.motorFrontRight.setPower(rightPower);
        this.motorBackRight.setPower(rightPower);
    }

    private double computeDegreesDiff(double targetHeading, double heading) {
        return targetHeading - heading;
    }

    private double getTurnPower(Ramp ramp, double targetHeading, double heading) {
        double diff = computeDegreesDiff(targetHeading, heading);

        if (diff < 0) {
            return -ramp.scaleX(Math.abs(diff));
        } else {
            return ramp.scaleX(Math.abs(diff));
        }
    }
}
