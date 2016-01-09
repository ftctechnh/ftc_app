package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

public class BotTeleOp extends OpMode {

    final static double NORMAL_SPEED = 0.75;
    final static double FULL_SPEED = 1.0;
    final static double SLOW_TURN = 0.15;
    final static double FULL_STOP = 0.0;
    final static double SERVO_INCREMENT = 0.01;

    final static double CLAW_OPEN = 0.0;
    final static double CLAW_CLOSED = 1.0;

    double SPEED_FACTOR = 0.5;

    DcMotor motorRight;
    DcMotor motorLeft;

    Servo arm1;
    Servo arm2;
    Servo claw1;
    Servo claw2;

    double speedCurve(double x) {
        return (0.598958 * Math.pow(x, 3)) - (4.43184 * Math.pow(10, -16) * Math.pow(x, 2)) + (0.201042 * x);
    }

    double servoClamp(double x) {
        if (x > 1) {
            return 1;
        }
        if (x < 0) {
            return 0;
        }
        return x;
    }

    public BotTeleOp() {

    }

    @Override
    public void init() {
        motorRight = hardwareMap.dcMotor.get("right_motor");
        motorRight.setDirection(DcMotor.Direction.REVERSE);
        motorLeft = hardwareMap.dcMotor.get("left_motor");
        arm1 = hardwareMap.servo.get("arm1");
        arm2 = hardwareMap.servo.get("arm2");
        claw1 = hardwareMap.servo.get("claw1");
        claw2 = hardwareMap.servo.get("claw2");
    }

    @Override
    public void loop() {
        motorLeft.setPower(speedCurve(gamepad1.left_stick_y) * SPEED_FACTOR);
        motorRight.setPower(speedCurve(gamepad1.right_stick_y) * SPEED_FACTOR);

        if (gamepad1.y && arm1 != null && arm2 != null) {
            arm1.setPosition(servoClamp(arm1.getPosition() + SERVO_INCREMENT));
            arm2.setPosition(servoClamp(arm2.getPosition() - SERVO_INCREMENT));
        } else if (gamepad1.a && arm1 != null && arm2 != null) {
            arm1.setPosition(servoClamp(arm1.getPosition() - SERVO_INCREMENT));
            arm2.setPosition(servoClamp(arm2.getPosition() + SERVO_INCREMENT));
        }

        if (gamepad1.x && claw1 != null && claw2 != null) {
            claw1.setPosition(CLAW_OPEN);
            claw2.setPosition(CLAW_CLOSED);
        }
        if (gamepad1.b && claw1 != null && claw2 != null) {
            claw1.setPosition(CLAW_CLOSED);
            claw2.setPosition(CLAW_OPEN);
        }

        if (gamepad1.left_trigger > 0.5f) {
            SPEED_FACTOR = 1.0;
        }
        if (gamepad1.right_trigger > 0.5f) {
            SPEED_FACTOR = 0.5f;
        }

        telemetry.addData("Title", "*** Robot Data***");
        telemetry.addData("Right Motor", "Right:" + motorRight.getPower());
        telemetry.addData("Left Motor", "Left:" + motorLeft.getPower());
        telemetry.addData("Arm 1", "Arm 1:" + arm1.getPosition());
        telemetry.addData("Arm 2", "Arm 2:" + arm2.getPosition());
        telemetry.addData("Claw 1", "Claw 1:" + claw1.getPosition());
        telemetry.addData("Claw 2", "Claw 2:" + claw2.getPosition());
    }

    @Override
    public void stop() {
        motorLeft.setPower(FULL_STOP);
        motorRight.setPower(FULL_STOP);
    }

}
