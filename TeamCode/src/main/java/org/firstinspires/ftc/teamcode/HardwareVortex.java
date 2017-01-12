package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class HardwareVortex {
    // TAGS
    public final static String
        DRIVE_MOTOR_TAG = "DRIVE",
        GENERIC_MOTOR_TAG = "MOTORS",
        ENCODER_TAG = "ENCODERS";

    // MOTORS
    public DcMotor frontLeft, frontRight, backLeft, backRight;
    public DcMotor intake, shooter, lift, flipper;

    // SERVOS

    // SENSORS

    // CONSTANTS
    public final static double SHOOTER_POWER = 0.80;

    public HardwareVortex() {
    }

    public void init(HardwareMap hardwareMap) {
        // Initialize the motors
        frontLeft = hardwareMap.dcMotor.get("FL");
        frontRight = hardwareMap.dcMotor.get("FR");
        backLeft = hardwareMap.dcMotor.get("BL");
        backRight = hardwareMap.dcMotor.get("BR");

        frontLeft.setDirection(DcMotorSimple.Direction.REVERSE);
        backLeft.setDirection(DcMotorSimple.Direction.REVERSE);

        intake = hardwareMap.dcMotor.get("intake");
        shooter = hardwareMap.dcMotor.get("shooter");
        lift = hardwareMap.dcMotor.get("lift");
        flipper = hardwareMap.dcMotor.get("flipper");

        shooter.setDirection(DcMotorSimple.Direction.REVERSE);
    }

    public void generateTelemetry(Telemetry telemetry, Boolean encoders) {

        telemetry.addData(DRIVE_MOTOR_TAG, "FL: %f FR: %f, BL: %f, BR %f", frontLeft.getPower(), frontRight.getPower(), backLeft.getPower(), backRight.getPower());
        telemetry.addData(ENCODER_TAG, "FL: %d FR: %d BL %d BR %d", frontLeft.getCurrentPosition(), frontRight.getCurrentPosition(), backLeft.getCurrentPosition(), backRight.getCurrentPosition());
        telemetry.addData(GENERIC_MOTOR_TAG, "intake: %f shooter: %f lift: %f flipper: %f", intake.getPower(), shooter.getPower(), lift.getPower(), flipper.getPower());
    }
}
