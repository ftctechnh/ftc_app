package org.firstinspires.ftc.teamcode;

import android.graphics.drawable.GradientDrawable;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;

abstract class baseAuto extends LinearOpMode {
    DcMotor frontLeft = null;
    DcMotor frontRight = null;
    DcMotor backLeft = null;
    DcMotor backRight = null;

    DcMotor hook = null;
    DcMotor nom = null;
    DcMotor extend = null;
    DcMotor pivot = null;

    Servo Door = null;
    BNO055IMU imu;
    GradientDrawable.Orientation angles;
    public double start;
    public static long timeDown = 7800;

    public void dec(HardwareMap hwmap) {
        frontLeft = hwmap.get(DcMotor.class, "fl");
        frontRight = hwmap.get(DcMotor.class, "fr");
        backLeft = hwmap.get(DcMotor.class, "bl");
        backRight = hwmap.get(DcMotor.class, "br");

        hook = hwmap.get(DcMotor.class, "hook");
        nom = hwmap.get(DcMotor.class, "nom");
        extend = hwmap.get(DcMotor.class, "extend");
        pivot = hwmap.get(DcMotor.class, "piv");

        frontLeft.setDirection(DcMotor.Direction.REVERSE);
        frontRight.setDirection(DcMotor.Direction.REVERSE);
        backLeft.setDirection(DcMotor.Direction.REVERSE);
        backRight.setDirection(DcMotor.Direction.REVERSE);

        frontLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        frontRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        hook.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        nom.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        extend.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        pivot.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
    }

    public void gyro() {
        BNO055IMU.Parameters parameters = new BNO055IMU.Parameters();
        parameters.angleUnit = BNO055IMU.AngleUnit.DEGREES;
        parameters.calibrationDataFile = "BNO055IMUCalibration.json"; // see the calibration sample opmode
        parameters.loggingEnabled = true;
        parameters.loggingTag = "IMU";
        imu = hardwareMap.get(BNO055IMU.class, "imu");
        imu.initialize(parameters);
    }

    double currentAngle() {
        Orientation angles = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);
        return AngleUnit.DEGREES.fromUnit(angles.angleUnit, angles.firstAngle);
    }

    public void startAngle() {
        start = currentAngle();
    }

    public void forwards(double drivePower) {
        frontLeft.setPower(drivePower);
        frontRight.setPower(drivePower);

        backLeft.setPower(drivePower);
        backRight.setPower(drivePower);
    }

    public void clock(double drivePower) {
        frontLeft.setPower(drivePower);
        frontRight.setPower(-drivePower);

        backLeft.setPower(drivePower);
        backRight.setPower(-drivePower);
    }

    public void counter(double drivePower) {
        frontLeft.setPower(-drivePower);
        frontRight.setPower(drivePower);

        backLeft.setPower(-drivePower);
        backRight.setPower(drivePower);
    }

    public void pullUp(int power) {
        hook.setPower(power);
    }

    public void pullDown(int power) {
        hook.setPower(power);
    }

    public void nomIn(int power) {
        nom.setPower(power);
    }

    public void nomOut(int power) {
        nom.setPower(power);
    }

    public void pivOut(int power) {
        pivot.setPower(power);
    }

    public void pivIn(int power) {
        pivot.setPower(-power);
    }

    public void extend (int power) {
        extend.setPower(power);
    }

    public void retract (int power) {
        extend.setPower(-power);
    }

    public void strafeLeft(int power) {
        frontLeft.setPower(power);
        frontRight.setPower(-power);

        backLeft.setPower(-power);
        backRight.setPower(power);
    }

    public void strafeRight(int power) {
        frontLeft.setPower(-power);
        frontRight.setPower(power);

        backLeft.setPower(power);
        backRight.setPower(-power);
    }

    public void downSeq() {
        pullUp(1);
        sleep(timeDown);
        pullUp(0);
        forwards(.7);
        sleep(500);
        forwards(.5);
        sleep(200);
        forwards(.8);
        sleep(500);
        forwards(0);
        pullUp(-1);
        sleep(2000);
        pullUp(0);

    }
}


