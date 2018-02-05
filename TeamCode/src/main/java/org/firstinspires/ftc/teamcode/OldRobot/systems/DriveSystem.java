package org.firstinspires.ftc.teamcode.OldRobot.systems;


import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.hardware.bosch.JustLoggingAccelerationIntegrator;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.navigation.Acceleration;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.robotcore.external.navigation.Position;
import org.firstinspires.ftc.robotcore.external.navigation.Velocity;

/**
 * Created by Mahim on 11/4/2017.
 */

public class DriveSystem {
    private DcMotor rightMotor, leftMotor;
    private HardwareMap hardwareMap;
    private Gamepad gamepad;
    private BNO055IMU imu;

    public DriveSystem (HardwareMap hardwareMap, Gamepad gamepad) {
        this.hardwareMap = hardwareMap;
        this.gamepad = gamepad;
        this.rightMotor = hardwareMap.get(DcMotor.class, "right motor");
        this.leftMotor = hardwareMap.get(DcMotor.class, "left motor");
        this.leftMotor.setDirection(DcMotorSimple.Direction.REVERSE);

        BNO055IMU.Parameters parameters = new BNO055IMU.Parameters();
        parameters.angleUnit            = BNO055IMU.AngleUnit.DEGREES;
        parameters.accelUnit            = BNO055IMU.AccelUnit.METERS_PERSEC_PERSEC;
        parameters.calibrationDataFile  = "BNO055IMUCalibration.json"; // see the calibration sample opmode
        parameters.loggingEnabled       = true;
        parameters.loggingTag           = "IMU";
        parameters.accelerationIntegrationAlgorithm = new JustLoggingAccelerationIntegrator();
        imu = hardwareMap.get(BNO055IMU.class, "imu");
        imu.initialize(parameters);
        imu.startAccelerationIntegration(new Position(), new Velocity(), 1000);
    }

    public void drive(double leftSpeed, double rightSpeed) {
        this.rightMotor.setPower(rightSpeed);
        this.leftMotor.setPower(leftSpeed);
    }

    public void driveForward(double speed) {
        drive(-speed, -speed);
    }

    public void driveBackwards(double speed) {
        drive(speed, speed);
    }

    public void rcCarDrive() {
        if (gamepad.left_stick_y > 0) {
            drive(gamepad.left_stick_y + gamepad.right_stick_x, gamepad.left_stick_y - gamepad.right_stick_x);
        } else if (gamepad.left_stick_y < 0) {
            drive(gamepad.left_stick_y - gamepad.right_stick_x, gamepad.left_stick_y + gamepad.right_stick_x);
        } else {
            drive(-gamepad.right_stick_x, gamepad.right_stick_x);
        }
    }

    public void stop() {
        drive(0.0,0.0);
    }

    public void tankDrive() {
        drive(gamepad.left_stick_y, gamepad.right_stick_y);
    }

    public double getRightSpeed() {
        return this.rightMotor.getPower();
    }

    public double getLeftSpeed() {
        return this.leftMotor.getPower();
    }

    public double getAngle() {
        Orientation angles = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);
        return angles.firstAngle;
    }

    public double getAcceleration() {
        Acceleration acceleration = imu.getLinearAcceleration();
        return acceleration.xAccel;
    }

    public double getVelocity() {
        Velocity velocity = imu.getVelocity();
        velocity.toUnit(DistanceUnit.METER);
        return  velocity.xVeloc;
    }

    public double getPosition() {
        Position position = imu.getPosition();
        position.toUnit(DistanceUnit.INCH);
        return position.x;
    }
}