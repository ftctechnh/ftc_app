package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.Range;

/**
 * Created by JackV on 9/12/15.
 */
public class TestOpMode extends OpMode{
    // RANGE VALUES.
    final static double ARM_MIN_RANGE  = 0.20;
    final static double ARM_MAX_RANGE  = 0.90;
    final static double CLAW_MIN_RANGE  = 0.20;
    final static double CLAW_MAX_RANGE  = 0.7;

    // POSITION VALUES
    double armPosition;
    double armDelta = 0.01;
    double clawPosition;
    double clawDelta = 0.01;

    // MOTOR VALUES
    DcMotor motorRight;
    DcMotor motorLeft;
    Servo claw;
    Servo arm;

    // CONSTRUCTOR
    public TestOpMode() {

    }

    @Override
    public void init() {
        motorRight = hardwareMap.dcMotor.get("motor_2");
        motorLeft = hardwareMap.dcMotor.get("motor_1");
        motorLeft.setDirection(DcMotor.Direction.REVERSE);
        arm = hardwareMap.servo.get("servo_1");
        claw = hardwareMap.servo.get("servo_6");

        // Starting position
        armPosition = 0.2;
        clawPosition = 0.2;
    }

    @Override
    public void loop() {
        arcadeDrive(gamepad1.left_stick_y, gamepad1.right_stick_x);
        moveArm(gamepad1.y, gamepad1.a, gamepad1.x, gamepad1.b);
    }

    @Override
    public void stop() {
        stopRobot();
    }

    void arcadeDrive(float y, float x) {
        float max = Math.abs(x);
        if (Math.abs(y) > max)
            max = Math.abs(y);
        float sum = y + x;
        float dif = y - x;
        if(y <= 0) {
            if(x >= 0) {
                motorLeft.setPower(max);
                motorRight.setPower(-sum);
            } else {
                motorLeft.setPower(dif);
                motorRight.setPower(max);
            }
        } else {
            if(y >= 0) {
                motorLeft.setPower(dif);
                motorRight.setPower(-max);
            } else {
                motorLeft.setPower(-max);
                motorRight.setPower(-sum);
            }
        }
    }

    void moveArm(boolean up, boolean down, boolean open, boolean close) {
        if (up)
            armPosition += armDelta;

        if (down)
            armPosition -= armDelta;

        if (open)
            clawPosition += clawDelta;

        if (close)
            clawPosition -= clawDelta;

        armPosition = Range.clip(armPosition, ARM_MIN_RANGE, ARM_MAX_RANGE);
        clawPosition = Range.clip(clawPosition, CLAW_MIN_RANGE, CLAW_MAX_RANGE);

        arm.setPosition(armPosition);
        claw.setPosition(clawPosition);
    }

    void stopRobot() {
        motorRight.setPower(0);
        motorLeft.setPower(0);
    }
}