package org.firstinspires.ftc.teamcode.systems.distance;

import com.qualcomm.hardware.rev.Rev2mDistanceSensor;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DistanceSensor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.teamcode.components.scale.ExponentialRamp;
import org.firstinspires.ftc.teamcode.components.scale.Point;
import org.firstinspires.ftc.teamcode.components.scale.Ramp;
import org.firstinspires.ftc.teamcode.systems.drive.MecanumDriveSystem;
import org.firstinspires.ftc.teamcode.systems.drive.DriveSystem4Wheel;
import org.firstinspires.ftc.teamcode.systems.base.System;

public class DistanceSystem extends System {
    DistanceSensor lidar;
    DistanceSensor lidar2;
    Rev2mDistanceSensor sensorTimeOfFlight;
    HardwareMap hwmap;
    MecanumDriveSystem driveSystem;

    public DistanceSystem(OpMode opMode, MecanumDriveSystem ds) {
        super(opMode, "DriveSystem4Wheel");
        this.hwmap = opMode.hardwareMap;
        driveSystem = ds;
        initSystem();
    }

    public void initSystem() {
        lidar = hwmap.get(DistanceSensor.class, "sensor_range");
        lidar2 = hwmap.get(DistanceSensor.class, "sensor_range2");
        Rev2mDistanceSensor sensorTimeOfFlight = (Rev2mDistanceSensor)lidar;
    }

    public void driveToPositionSetUp(int ticks) {
        driveSystem.setDirection(DriveSystem4Wheel.DriveDirection.FORWARD);

        driveSystem.motorBackRight.setPower(0);
        driveSystem.motorBackLeft.setPower(0);
        driveSystem.motorFrontLeft.setPower(0);
        driveSystem.motorFrontRight.setPower(0);

        driveSystem.motorFrontRight.setTargetPosition(driveSystem.motorFrontRight.getCurrentPosition() + ticks);
        driveSystem.motorFrontLeft.setTargetPosition(driveSystem.motorFrontLeft.getCurrentPosition() + ticks);
        driveSystem.motorBackRight.setTargetPosition(driveSystem.motorBackRight.getCurrentPosition() + ticks);
        driveSystem.motorBackLeft.setTargetPosition(driveSystem.motorBackLeft.getCurrentPosition() + ticks);

        driveSystem.motorBackLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        driveSystem.motorBackRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        driveSystem.motorFrontRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        driveSystem.motorFrontLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
    }

    public void driveAlongWallInches(int inches, double displacement, double power) {
        // this method assumes both sensors can see the wall
        // run with encoders as if running to position
        // take sensor data (displacement and rotation) and scale it into a joystick-like value from 0-1
        // instead of setPower() use the mechanum drive scheme to set the power
        // write the values to the motors 1






        int ticks = (int) driveSystem.inchesToTicks(inches);
        driveToPositionSetUp(ticks);

        Ramp rampY = new ExponentialRamp(new Point(0, driveSystem.RAMP_POWER_CUTOFF),
                new Point((ticks / 4), power));

        Ramp rampX = new ExponentialRamp(new Point(0, 0.05),
                new Point((displacement / 4), (power / 2)));

        double adjustedPower = Range.clip(power, -1.0, 1.0);

        driveSystem.motorBackRight.setPower(adjustedPower);
        driveSystem.motorBackLeft.setPower(adjustedPower);
        driveSystem.motorFrontLeft.setPower(adjustedPower);
        driveSystem.motorFrontRight.setPower(adjustedPower);

        double frontRightPower = adjustedPower;
        double backRightPower = adjustedPower;
        double frontLeftPower = adjustedPower;
        double backLeftPower = adjustedPower;

        while (driveSystem.motorFrontLeft.isBusy() ||
                driveSystem.motorFrontRight.isBusy() ||
                driveSystem.motorBackRight.isBusy() ||
                driveSystem.motorBackLeft.isBusy()) {

            double leftPower = power;
            double RightPower = power;


            int distance = driveSystem.getMinDistanceFromTarget();
            double displacement2 = getFrontRightDistance();

            if (distance < 50) {
                break;
            }

            double directionY = 1.0;
            if (distance < 0) {
                distance = -distance;
                directionY = -1.0;
            }

            double directionX = 1.0;
            if (distance < 0) {
                distance = -distance;
                directionX = -1.0;
            }

            double scaledPowerY = directionY * rampY.scaleX(distance);
            double scaledPowerX = directionX * rampX.scaleX(displacement2);
            telemetry.log("MecanumDriveSystem","powerY: " + scaledPowerY);
            telemetry.log("MecanumDriveSystem","powerX: " + scaledPowerX);
            driveSystem.mecanumDrive(/*turnPower*/(float) scaledPowerX, 0, 0, (float) scaledPowerY, false);
            telemetry.write();
        }
        driveSystem.motorBackLeft.setPower(0);
        driveSystem.motorBackRight.setPower(0);
        driveSystem.motorFrontRight.setPower(0);
        driveSystem.motorFrontLeft.setPower(0);
    }

    public void driveToDoiiiiiii(int inches, int displacement, double power) {
        int ticks = (int) driveSystem.inchesToTicks(inches);
        driveSystem.setDirection(DriveSystem4Wheel.DriveDirection.FORWARD);
        driveSystem.motorBackRight.setPower(0);
        driveSystem.motorBackLeft.setPower(0);
        driveSystem.motorFrontLeft.setPower(0);
        driveSystem.motorFrontRight.setPower(0);

        driveSystem.motorFrontRight.setTargetPosition(driveSystem.motorFrontRight.getCurrentPosition() + ticks);
        driveSystem.motorFrontLeft.setTargetPosition(driveSystem.motorFrontLeft.getCurrentPosition() + ticks);
        driveSystem.motorBackRight.setTargetPosition(driveSystem.motorBackRight.getCurrentPosition() + ticks);
        driveSystem.motorBackLeft.setTargetPosition(driveSystem.motorBackLeft.getCurrentPosition() + ticks);

        driveSystem.motorBackLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        driveSystem.motorBackRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        driveSystem.motorFrontRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        driveSystem.motorFrontLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        double displacementDiff = getFrontRightDistance();

        Ramp ramp = new ExponentialRamp(new Point(0, driveSystem.RAMP_POWER_CUTOFF),
                new Point((ticks / 4), power));

        Ramp rampX = new ExponentialRamp(new Point(0, 0.05),
                new Point((displacement / 4), (power / 2)));

        double adjustedPower = Range.clip(power, -1.0, 1.0);

        driveSystem.motorBackRight.setPower(adjustedPower);
        driveSystem.motorBackLeft.setPower(adjustedPower);
        driveSystem.motorFrontLeft.setPower(adjustedPower);
        driveSystem.motorFrontRight.setPower(adjustedPower);

        while (driveSystem.motorFrontLeft.isBusy() ||
                driveSystem.motorFrontRight.isBusy() ||
                driveSystem.motorBackRight.isBusy() ||
                driveSystem.motorBackLeft.isBusy()) {

            int distance = driveSystem.getMinDistanceFromTarget();

            if (distance < 50) {
                break;
            }

            telemetry.log("MecanumDriveSystem","targetPos motorFL: " + driveSystem.motorFrontLeft.getTargetPosition());
            telemetry.log("MecanumDriveSystem","targetPos motorFR: " + driveSystem.motorFrontRight.getTargetPosition());
            telemetry.log("MecanumDriveSystem","targetPos motorBL: " + driveSystem.motorBackLeft.getTargetPosition());
            telemetry.log("MecanumDriveSystem","targetPos motorBR: " + driveSystem.motorBackRight.getTargetPosition());

            telemetry.log("MecanumDriveSystem","currentPos motorFL: " + driveSystem.motorFrontLeft.getCurrentPosition());
            telemetry.log("MecanumDriveSystem","currentPos motorFR: " + driveSystem.motorFrontRight.getCurrentPosition());
            telemetry.log("MecanumDriveSystem","currentPos motorBL: " + driveSystem.motorBackLeft.getCurrentPosition());
            telemetry.log("MecanumDriveSystem","currentPos motorBR: " + driveSystem.motorBackRight.getCurrentPosition());

            double direction = 1.0;
            if (distance < 0) {
                distance = -distance;
                direction = -1.0;
            }


            double directionX = 1.0;
            if (displacementDiff < 0) {
                displacementDiff = -displacementDiff;
                directionX = -1.0;
            }

            double scaledPowerX = directionX * rampX.scaleX(displacementDiff);
            double scaledPowerY = ramp.scaleY(distance) * direction;
            telemetry.log("MecanumDriveSystem","power: " + scaledPowerY);
            telemetry.log("MecanumDriveSystem","powerX: " + scaledPowerX);
            // rightX, rightY, leftX, leftY
            driveSystem.mecanumDrive((float) scaledPowerX, 0, 0, (float) scaledPowerY, true);
            telemetry.write();
        }
        driveSystem.motorBackLeft.setPower(0);
        driveSystem.motorBackRight.setPower(0);
        driveSystem.motorFrontRight.setPower(0);
        driveSystem.motorFrontLeft.setPower(0);
    }

    public void alignWithWall(double displacement, double power) {
        // compare sensor values and determine how far to turn and strafe
        //
    }

    public double getFrontRightDistance() {
        double d1 = getDistance1();
        double d2 = getDistance2();
        double dp = 9; //distance between
        //return d2 * Math.cos((1/Math.tan(((d2 - d1) * dp))));
        return d1;
    }

    public double getDistance1() {
        telemetry.log("lidar1", ("" + lidar));
        telemetry.log("range", String.format("%.01f in", lidar.getDistance(DistanceUnit.INCH)));
        telemetry.write();
        return lidar.getDistance(DistanceUnit.INCH);
    }

    public double getDistance2() {
        telemetry.log("lidar2", ("" + lidar2));
        telemetry.log("range", String.format("%.01f in", lidar2.getDistance(DistanceUnit.INCH)));
        telemetry.write();
        return lidar2.getDistance(DistanceUnit.INCH);
    }

    public void telemetry() {
        // generic DistanceSensor methods.
        telemetry.log("lidar1", ("" + lidar));
        telemetry.log("lidar2", ("" + lidar2));
        telemetry.log("range", String.format("%.01f in", lidar.getDistance(DistanceUnit.INCH)));
        telemetry.log("shaerposte: ", "" + getFrontRightDistance());


        telemetry.log("MecanumDriveSystem","disp 12, power 1.0, PowerX: " + getXPower());

        telemetry.write();
    }

    private double getXPower() {
        int displacement = 12;
        double power = 1.0;

        double displacementDiff = getFrontRightDistance();
        Ramp rampX = new ExponentialRamp(new Point(0, 0.05),
                new Point((displacement / 4), (power / 2)));

        double directionX = 1.0;
        if (displacementDiff < 0) {
            displacementDiff = -displacementDiff;
            directionX = -1.0;
        }
        double scaledPowerX = directionX * rampX.scaleX(displacementDiff);
        return scaledPowerX;
    }
}
