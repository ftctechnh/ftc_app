package org.firstinspires.ftc.teamcode.seasons.velocityvortex;

import com.qualcomm.hardware.modernrobotics.ModernRoboticsI2cGyro;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.GyroSensor;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;

/**
 * Created by ftc6347 on 9/25/16.
 */
public class ProgrammingRobotHardware {

    public static final double WHEEL_DIAMETER_INCHES = 4;

    public static final double COUNTS_PER_MOTOR_REV = 1120;

    public static final double COUNTS_PER_INCH = COUNTS_PER_MOTOR_REV /
            (WHEEL_DIAMETER_INCHES * Math.PI);

    public static final double P_DRIVE_COEFF = 0.15;

    private DcMotor frontLeft;
    private DcMotor frontRight;
    private DcMotor backLeft;
    private DcMotor backRight;

    private ModernRoboticsI2cGyro gyro;
    private ColorSensor colorSensor;

    public ProgrammingRobotHardware(HardwareMap hardwareMap, Telemetry telemetry) {
        frontLeft = hardwareMap.dcMotor.get("fl");
        frontRight = hardwareMap.dcMotor.get("fr");
        backLeft = hardwareMap.dcMotor.get("bl");
        backRight = hardwareMap.dcMotor.get("br");

        gyro = hardwareMap.get(ModernRoboticsI2cGyro.class, "gyro");
        gyro.calibrate();

        colorSensor = hardwareMap.colorSensor.get("clrs");
        colorSensor.enableLed(true);

        // reset the encoders for each drive motor
        frontLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        frontRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        backLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        backRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        // set the motor directions
        frontLeft.setDirection(DcMotorSimple.Direction.REVERSE);
        frontRight.setDirection(DcMotorSimple.Direction.REVERSE);
        backLeft.setDirection(DcMotorSimple.Direction.REVERSE);
        backRight.setDirection(DcMotorSimple.Direction.REVERSE);
    }

    public boolean areDriveMotorsBusy() {
        return frontLeft.isBusy() &&
                frontRight.isBusy() &&
                backLeft.isBusy() &&
                backRight.isBusy();
    }

    public void stopDriveMotors() {
        frontLeft.setPower(0);
        frontRight.setPower(0);
        backLeft.setPower(0);
        backRight.setPower(0);
    }

    public ModernRoboticsI2cGyro getGyroSensor() {
        return gyro;
    }

    public ColorSensor getColorSensor() {
        return colorSensor;
    }

    public DcMotor getFrontLeft() {
        return frontLeft;
    }

    public DcMotor getFrontRight() {
        return frontRight;
    }

    public DcMotor getBackLeft() {
        return backLeft;
    }

    public DcMotor getBackRight() {
        return backRight;
    }
}
