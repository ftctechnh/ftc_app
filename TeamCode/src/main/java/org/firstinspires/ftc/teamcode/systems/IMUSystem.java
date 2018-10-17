package org.firstinspires.ftc.teamcode.systems;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.robotcore.external.Func;
import org.firstinspires.ftc.robotcore.external.navigation.Acceleration;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.robotcore.external.navigation.Position;
import org.firstinspires.ftc.robotcore.external.navigation.Velocity;
import org.firstinspires.ftc.teamcode.systems.BaseSystems.System;

import java.util.Locale;

public class IMUSystem extends System {
    public BNO055IMU imu;
    public BNO055IMU.Parameters parameters;

    // State used for updating telemetry
    private Orientation angles;
    private Acceleration gravity;

    public IMUSystem(OpMode opMode)
    {
        super(opMode, "IMUSystem");

        this.parameters = new BNO055IMU.Parameters();
        this.parameters.angleUnit = BNO055IMU.AngleUnit.DEGREES;
        this.parameters.accelUnit = BNO055IMU.AccelUnit.METERS_PERSEC_PERSEC;
        this.parameters.loggingEnabled = true;
        this.parameters.loggingTag = "BNO055";
        this.parameters.calibrationDataFile = "AdafruitIMUCalibration.json"; // see the calibration sample opmode
        this.imu = this.map.get(BNO055IMU.class, "imu");
        this.imu.initialize(parameters);

        // Enable reporting of position using the naive integrator
        imu.startAccelerationIntegration(new Position(), new Velocity(), 500);
    }

    public double getHeading() {
        Orientation orientation = imu.getAngularOrientation().toAxesReference(AxesReference.INTRINSIC).toAxesOrder(AxesOrder.ZYX);
        return orientation.firstAngle;
    }

    public double getRoll() {
        Orientation orientation = imu.getAngularOrientation().toAxesReference(AxesReference.INTRINSIC).toAxesOrder(AxesOrder.ZYX);
        return orientation.secondAngle;
    }

    public double getpitch() {
        Orientation orientation = imu.getAngularOrientation().toAxesReference(AxesReference.INTRINSIC).toAxesOrder(AxesOrder.ZYX);
        return orientation.thirdAngle;
    }

    public Acceleration getAcceleration()
    {
        return imu.getLinearAcceleration();
    }

    public Velocity getVelocity()
    {
        return imu.getVelocity();
    }

    String formatAngle(AngleUnit angleUnit, double angle) {
        return formatDegrees(AngleUnit.DEGREES.fromUnit(angleUnit, angle));
    }

    String formatDegrees(double degrees) {
        return String.format(Locale.getDefault(), "%.1f", AngleUnit.DEGREES.normalize(degrees));
    }
}

