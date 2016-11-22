package org.firstinspires.ftc.team9374;

import com.qualcomm.hardware.adafruit.BNO055IMU;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;

import java.util.List;
import java.util.Locale;

/**
 * Created by lego7_000 on 11/21/2016.
 */

public class MotorMatr {

    Orientation[] angles = new Orientation[2];

    public DcMotor[][] Motors = new DcMotor[10][4];

    public void addMotors(int row, DcMotor... motors) {
        for (int i = 0; i < motors.length; i++) {
            this.Motors[row][i] = motors[i];
        }
    }

    public void runMotors(int column, float power) {
        for (int i = 0; i < this.Motors.length; i++) {
            this.Motors[i][column-1].setPower(power);
        }
    }

    public void rotateDegrees(BNO055IMU imu, float degrees, int left, int right) {
        angles[0] = imu.getAngularOrientation().toAxesReference(AxesReference.INTRINSIC).toAxesOrder(AxesOrder.ZYX);
        double init = formatAngle(angles[0].angleUnit, angles[0].firstAngle);
        double cur = formatAngle(angles[0].angleUnit, angles[0].firstAngle);
        while (Math.abs(init-cur) != init + degrees) {
            runMotors(left, -(degrees/Math.abs(degrees)));
            runMotors(right, (degrees/Math.abs(degrees)));
            cur = formatAngle(angles[0].angleUnit, angles[0].firstAngle);
        }
    }

    double formatAngle(AngleUnit angleUnit, double angle) {
        return AngleUnit.DEGREES.fromUnit(angleUnit, angle);
    }
}
