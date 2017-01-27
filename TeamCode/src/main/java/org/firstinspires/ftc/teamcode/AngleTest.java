package org.firstinspires.ftc.teamcode;


import com.qualcomm.hardware.adafruit.BNO055IMU;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.teamcode.modules.AccelerationIntegrator;
import org.firstinspires.ftc.teamcode.modules.Precision;

import java.util.Locale;

@Autonomous
public class AngleTest extends OpMode {
    private HardwareVortex robot = new HardwareVortex();
    private double heading = 0;
    private DcMotor[] leftMotors, rightMotors;

    public void init() {
        robot.init(hardwareMap);

        leftMotors = new DcMotor[] {
                robot.frontLeft,
                robot.backLeft
        };

        rightMotors = new DcMotor[] {
                robot.frontRight,
                robot.backRight
        };

        BNO055IMU.Parameters parameters = new BNO055IMU.Parameters();
        parameters.angleUnit = BNO055IMU.AngleUnit.DEGREES;
        parameters.accelUnit = BNO055IMU.AccelUnit.METERS_PERSEC_PERSEC;
        parameters.calibrationDataFile = "AdafruitIMUCalibration.json"; // see the calibration sample opmode
        parameters.loggingEnabled = true;
        parameters.loggingTag = "IMU";
        parameters.accelerationIntegrationAlgorithm = new AccelerationIntegrator();

        robot.imu = hardwareMap.get(BNO055IMU.class, "imu");
        robot.imu.initialize(parameters);

        Precision.reset();
    }

    public void init_loop() {
        Orientation angles = robot.imu.getAngularOrientation().toAxesReference(AxesReference.INTRINSIC).toAxesOrder(AxesOrder.ZYX);
        heading = angles.firstAngle;
        telemetry.addData("IMU", "heading: %s", String.format(Locale.getDefault(), "%.1f", AngleUnit.DEGREES.normalize(AngleUnit.DEGREES.fromUnit(angles.angleUnit, heading))));
    }

    public void loop() {
        if (Precision.angleTurned(leftMotors, rightMotors, heading, 0.9, 0.5, 90, 2.0, 1.0, 10000)) {
            for (DcMotor motor : leftMotors) {
                motor.setPower(0);
            }
            for (DcMotor motor : rightMotors) {
                motor.setPower(0);
            }
        }

        robot.generateTelemetry(telemetry, false);
        Orientation angles = robot.imu.getAngularOrientation().toAxesReference(AxesReference.INTRINSIC).toAxesOrder(AxesOrder.ZYX);
        heading = angles.firstAngle;
        telemetry.addData("IMU", "heading: %s", String.format(Locale.getDefault(), "%.1f", AngleUnit.DEGREES.normalize(AngleUnit.DEGREES.fromUnit(angles.angleUnit, heading))));
    }
}
