package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.modernrobotics.ModernRoboticsI2cRangeSensor;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.opencv.core.Point;

/**
 * Created by Kaden on 3/14/2018.
 **/

public class Robot {
    private OpMode opMode;
    private HardwareMap hardwareMap;
    private Telemetry telemetry;
    public Drive drive;
    public AutoGlyphs glyphDetector;
    public ForkLift forkLift;
    public JewelArm jewelArm;
    public RelicClaw relicClaw;
    public Phone phone;
    public REVGyro imu;
    public double heading;
    private ModernRoboticsI2cRangeSensor rangeSensor;


    static final double STRAFING_DAMPEN_FACTOR_FOR_MULTI_GLYPH = 0.2;
    static final double GYRO_OFFSET = 2.25;


    Robot(OpMode opMode) {
        this.opMode = opMode;
        this.hardwareMap = opMode.hardwareMap;
        this.telemetry = opMode.telemetry;

    }
    public void mapRobot() {
        drive = new Drive(opMode);
        forkLift = new ForkLift(opMode);
        jewelArm = new JewelArm(opMode);
        relicClaw = new RelicClaw(opMode);
        phone = new Phone(opMode);
        imu = new REVGyro(opMode);
        rangeSensor = hardwareMap.get(ModernRoboticsI2cRangeSensor.class, "d1");

    }

    public void pushInBlock() {
        forkLift.openClaw();
        sleep(150);
        drive.backward(drive.DRIVE_INTO_CRYPTOBOX_SPEED, 3);
        forkLift.moveUntilDown();
        forkLift.setClawPositionPushInBlock();
        sleep(150);
        drive.forwardTime(drive.DRIVE_INTO_CRYPTOBOX_SPEED, 500);
    }

    public void stopAll() {
        drive.stopMotors();
        forkLift.moveMotor(0);
        relicClaw.moveMotor(0);
    }
    public void getMoreGlyphs(double returnHeading) {
        jewelArm.up(); //take this out later
        setUpMultiGlyph();
        ElapsedTime findGlyphTime = new ElapsedTime();
        findGlyphTime.reset();
        double xOffSet;
        double yPos;
        double decisionPoint = 0;
        double size = 0;
        Point bestGlyphPos = new Point(AutoGlyphs.DEFAULT_X_POS_VALUE, 0);
        double bestGlyphSize = 0;
        while (findGlyphTime.seconds() < 0.5) {
        }
        while (findGlyphTime.seconds() < 3.5) {
            xOffSet = glyphDetector.getXOffset();
            yPos = glyphDetector.getYPos();
            size = glyphDetector.getSize();
            if ((Math.abs(xOffSet) < Math.abs(bestGlyphPos.x)) && (xOffSet != AutoGlyphs.DEFAULT_X_POS_VALUE) && (size < 105) && (size > 40)) {// && (yPos < 60)) {
                bestGlyphPos.x = xOffSet;
                bestGlyphPos.y = yPos;
                bestGlyphSize = size;
                decisionPoint = findGlyphTime.seconds();
            }
        }
        telemetry.addData("Glyph Position", bestGlyphPos.toString());
        telemetry.addData("Decision made at", decisionPoint);
        telemetry.addData("Size", bestGlyphSize);
        telemetry.update();
        glyphDetector.disable();
        forkLift.openClaw();
        if (bestGlyphPos.x == AutoGlyphs.DEFAULT_X_POS_VALUE) {
            bestGlyphPos.x = 0;
        }
        double distanceToStrafe = bestGlyphPos.x * STRAFING_DAMPEN_FACTOR_FOR_MULTI_GLYPH;
        strafeForMultiGlyph(distanceToStrafe);
        drive.forward(drive.DRIVE_INTO_GLYPH_PIT_SPEED, drive.DRIVE_INTO_GLYPH_PIT_DISTANCE);
        drive.forward(drive.DRIVE_INTO_GLYPHS_SPEED, drive.DRIVE_INTO_GLYPHS_DISTANCE);
        forkLift.closeClaw();
        sleep(300);
        forkLift.moveMotor(1, 150);
        drive.backward(drive.MAX_SPEED, drive.DRIVE_INTO_GLYPHS_DISTANCE + drive.DRIVE_INTO_GLYPH_PIT_DISTANCE);
        double heading = getHeading();
        if (heading < returnHeading) {
            leftGyro(drive.SPIN_TO_CRYPTOBOX_SPEED, returnHeading);
        } else {
            rightGyro(drive.SPIN_TO_CRYPTOBOX_SPEED, returnHeading);
        }
        strafeForMultiGlyph(distanceToStrafe);
    }

    private void strafeForMultiGlyph(double distanceToStrafe) {
        if (distanceToStrafe > 0) {
            drive.strafeRight(drive.MULTI_GLYPH_STRAFE_SPEED, distanceToStrafe);
        } else if (distanceToStrafe < 0) {
            drive.strafeLeft(drive.MULTI_GLYPH_STRAFE_SPEED, distanceToStrafe);
        }
    }

    public void setUpMultiGlyph() {
        glyphDetector.enable();
        forkLift.closeAllTheWay();
        phone.faceFront();

    }
    public void rightGyro(double x, double y, double z, double target) {
        double Adjustedtarget = target + GYRO_OFFSET;
        heading = getHeading();
        double derivative = 0;
        double fl = clip(-y + -x - z);
        double fr = clip(-y + x + z);
        double rl = clip(-y + x - z);
        double rr = clip(-y + -x + z);
        drive.driveSpeeds(fl, fr, rl, rr);
        if (heading < target) {
            while (derivative <= 0) {
                derivative = getHeading() - heading;
                heading = getHeading();
            }
        }
        double start = getHeading();
        double distance = Adjustedtarget - start;
        while (heading >= Adjustedtarget) {
            heading = getHeading();
            double proportion = 1 - (Math.abs((heading - start) / distance));
            drive.driveSpeeds(drive.clipSpinSpeed(fl * proportion), drive.clipSpinSpeed(fr * proportion), drive.clipSpinSpeed(rl * proportion), drive.clipSpinSpeed(rr * proportion));
        }
        drive.stopMotors();
    }

    public void rightGyro(double speed, double target) {
        rightGyro(0, 0, Math.abs(speed), target);
    }

    public void leftGyro(double x, double y, double z, double target) {
        double adjustedTarget = target - GYRO_OFFSET;
        heading = getHeading();
        double derivative = 0;
        double fl = clip(-y + -x - z);
        double fr = clip(-y + x + z);
        double rl = clip(-y + x - z);
        double rr = clip(-y + -x + z);
        drive.driveSpeeds(fl, fr, rl, rr);
        if (adjustedTarget < heading) {
            while (derivative >= 0) {
                derivative = getHeading() - heading;
                heading = getHeading();
            }
        }
        double start = heading;
        double distance = adjustedTarget - start;
        while (heading <= adjustedTarget) {
            heading = getHeading();
            double proportion = 1 - (Math.abs((heading - start) / distance));
            drive.driveSpeeds(drive.clipSpinSpeed(fl * proportion), drive.clipSpinSpeed(fr * proportion), drive.clipSpinSpeed(rl * proportion), drive.clipSpinSpeed(rr * proportion));
        }
        drive.stopMotors();
    }

    public void leftGyro(double speed, double target) {
        leftGyro(0, 0, -Math.abs(speed), target);
    }

    public void driveUntilDistance(double x, double y, double z, double endDistance) {
        double fl = clip(-y + -x - z);
        double fr = clip(-y + x + z);
        double rl = clip(-y + x - z);
        double rr = clip(-y + -x + z);
        drive.driveSpeeds(fl, fr, rl, rr);
        double start = getDistance();
        double distanceToTravel = endDistance - start;
        double proportion;
        double derivative = 0;
        double distance = getDistance();
        int ranTimes = 0;
        int acceptedSensorValue = 0;
        while (distance < endDistance) {
            proportion = 1 - Math.abs((distance - start) / distanceToTravel + 0.0001);
            drive.driveSpeeds(drive.clipStrafeSpeed(fl * proportion), drive.clipStrafeSpeed(fr * proportion), drive.clipStrafeSpeed(rl * proportion), drive.clipStrafeSpeed(rr * proportion));
            derivative = getDistance() - distance;
            if (derivative >= 0 && derivative < 6) {
                distance = getDistance();
                acceptedSensorValue++;
            }
            ranTimes++;
            telemetry.addData("Ran times", ranTimes);
            telemetry.addData("Accepted Ratio", acceptedSensorValue / ranTimes);
            telemetrizeDistance();
        }
        telemetry.update();
        drive.stopMotors();
    }

    public void driveLeftUntilDistance(double speed, double distance) {
        driveUntilDistance(-Math.abs(speed), 0, 0, distance);
    }

    public void driveRightUntilDistance(double speed, double distance) {
        driveUntilDistance(Math.abs(speed), 0, 0, distance);
    }

    public double getDistance(DistanceUnit unit) {
        return rangeSensor.getDistance(unit);
    }

    public double getDistance() {
        return getDistance(DistanceUnit.INCH);
    }

    public void calibrateGyro() {
        imu.calibrate();
        heading = getHeading();
    }

    public double getHeading() {
        return imu.getHeading();
    }

    private void telemetrizeGyro() {
        telemetry.addData("Current heading: ", heading);
    }

    private void telemetrizeDistance() {
        telemetry.addData("Distance", getDistance());
    }

    public static void sleep(long time) {
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
        }
    }

    private double clip(double value) {
        return Range.clip(value, -1, 1);
    }
}
