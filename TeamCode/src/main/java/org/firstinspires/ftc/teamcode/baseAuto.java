package org.firstinspires.ftc.teamcode;

import android.graphics.drawable.GradientDrawable;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.navigation.Acceleration;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;

abstract class baseAuto extends LinearOpMode {
    DcMotor Left = null;
    DcMotor Right = null;
    DcMotor Pin = null;
    DcMotor Nom = null;
    DcMotor Elevator = null;
    DcMotor Pulley = null;
    Servo Door = null;
    BNO055IMU imu;
    GradientDrawable.Orientation angles;
    public double start;
    public static long timeDown = 7800;

    public void dec(HardwareMap hwmap) {
        Left = hwmap.get(DcMotor.class, "left");
        Right = hwmap.get(DcMotor.class, "right");
        Pin = hwmap.get(DcMotor.class, "pinion");
        Nom = hwmap.get(DcMotor.class, "nom");
        Elevator = hwmap.get(DcMotor.class, "elevator");
        Pulley = hwmap.get(DcMotor.class, "pulley");
        Door = hwmap.get(Servo.class, "servo");

        Left.setDirection(DcMotor.Direction.REVERSE);

        Left.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        Right.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        Pin.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        Nom.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        Elevator.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        Pulley.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
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
        Left.setPower(drivePower);
        Right.setPower(drivePower);
    }

    public void clock(double drivePower) {
        Left.setPower(drivePower);
        Right.setPower(-drivePower);
    }

    public void counter(double drivePower) {
        Left.setPower(-drivePower);
        Right.setPower(drivePower);
    }

    public void pullUp(int up) {
        Pin.setPower(up*-1);
    }

    public void nom(int forwards) {
        Nom.setPower(-.8 * forwards);
        Elevator.setPower(-1 * forwards);
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


