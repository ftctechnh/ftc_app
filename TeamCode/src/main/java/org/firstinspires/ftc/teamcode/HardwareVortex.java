package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.adafruit.BNO055IMU;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.LightSensor;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.modules.AccelerationIntegrator;

public class HardwareVortex {
    // TAGS
    public final static String
        DRIVE_MOTOR_TAG = "DRIVE",
        GENERIC_MOTOR_TAG = "MOTORS",
        ENCODER_TAG = "ENCODERS",
        SERVO_TAG = "SERVOS";

    // MOTORS
    public DcMotor frontLeft, frontRight, backLeft, backRight;
    public DcMotor intake, shooter, lift, flipper;

    // SERVOS
    public Servo pusherLeft, pusherRight;
    public Servo leftLift, rightLift;

    // SENSORS
    public LightSensor light;
    public ColorSensor colorLeft, colorRight;
    public BNO055IMU imu;

    // CONSTANTS
    public final static double SHOOTER_POWER = 0.90;

    public final static double LEFT_LIFT_INIT = 220/255.0, LEFT_LIFT_PUSH = 70/255.0;
    public final static double RIGHT_LIFT_INIT = 0/255.0, RIGHT_LIFT_PUSH = 65/255.0;

    public HardwareVortex() {
    }

    public void init(HardwareMap hardwareMap) {
        // Initialize the motors
        frontLeft = hardwareMap.dcMotor.get("FL");
        frontRight = hardwareMap.dcMotor.get("FR");
        backLeft = hardwareMap.dcMotor.get("BL");
        backRight = hardwareMap.dcMotor.get("BR");

        frontLeft.setDirection(DcMotorSimple.Direction.REVERSE);
        backRight.setDirection(DcMotorSimple.Direction.REVERSE);

        intake = hardwareMap.dcMotor.get("intake");
        shooter = hardwareMap.dcMotor.get("shooter");
        lift = hardwareMap.dcMotor.get("lift");
        flipper = hardwareMap.dcMotor.get("flipper");

        shooter.setDirection(DcMotorSimple.Direction.REVERSE);

        // Initialize the servos
        leftLift = hardwareMap.servo.get("leftLift");
        rightLift = hardwareMap.servo.get("rightLift");

        leftLift.setPosition(LEFT_LIFT_INIT);
        rightLift.setPosition(RIGHT_LIFT_INIT);

        pusherLeft = hardwareMap.servo.get("pusherLeft");
        pusherRight = hardwareMap.servo.get("pusherRight");

        pusherLeft.setPosition(0.5);
        pusherRight.setPosition(0.5);

        // Initialize the sensors
        light = hardwareMap.lightSensor.get("light");
        colorLeft = hardwareMap.colorSensor.get("colorLeft");
        colorLeft.enableLed(false);
        colorRight = hardwareMap.colorSensor.get("colorRight");
        colorRight.enableLed(false);
    }

    public void resetEncoders(){
        frontLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        frontRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        backLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        backRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        frontLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        frontRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        backLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        backRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }

    public void generateTelemetry(Telemetry telemetry, Boolean encoders) {
        telemetry.addData(DRIVE_MOTOR_TAG, "FL: %f FR: %f, BL: %f, BR: %f", frontLeft.getPower(), frontRight.getPower(), backLeft.getPower(), backRight.getPower());
        telemetry.addData(ENCODER_TAG, "FL: %d FR: %d BL: %d BR: %d", frontLeft.getCurrentPosition(), frontRight.getCurrentPosition(), backLeft.getCurrentPosition(), backRight.getCurrentPosition());
        telemetry.addData(GENERIC_MOTOR_TAG, "intake: %f shooter: %f lift: %f flipper: %f", intake.getPower(), shooter.getPower(), lift.getPower(), flipper.getPower());
        telemetry.addData(SERVO_TAG, "L: %f R: %f PL: %f PR: %f", leftLift.getPosition(), rightLift.getPosition(), pusherLeft.getPosition(), pusherRight.getPosition());
    }
}
