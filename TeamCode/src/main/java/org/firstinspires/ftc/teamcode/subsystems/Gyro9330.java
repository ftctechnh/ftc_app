package org.firstinspires.ftc.teamcode.subsystems;

import com.qualcomm.hardware.bosch.BNO055IMU;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.teamcode.Hardware9330;

/**
 * Created by robot on 10/23/2017.
 */

public class Gyro9330 {
    private Hardware9330 hwMap = null;
    Orientation angles;
    BNO055IMU.Parameters parameters = new BNO055IMU.Parameters();

    public Gyro9330(Hardware9330 robotMap) { hwMap = robotMap; }

    public void resetGyro() {
        hwMap.gyro.initialize(parameters);
    }

    public void init() {
        parameters.angleUnit           = BNO055IMU.AngleUnit.DEGREES;
        parameters.loggingEnabled      = true;
        parameters.loggingTag          = "IMU";

        resetGyro();
    }

    public double getYaw() {
        angles = hwMap.gyro.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);
        return angles.firstAngle;
    }
    public double getPitch() {
        angles = hwMap.gyro.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);
        return angles.secondAngle;
    }

    public double getRoll() {
        angles = hwMap.gyro.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);
        return angles.thirdAngle;
    }

    public boolean isCalibrated() {
        return hwMap.gyro.isGyroCalibrated();
    }
}
