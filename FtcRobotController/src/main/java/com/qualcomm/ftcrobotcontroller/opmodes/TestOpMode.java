package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
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
        float throttle = -gamepad1.left_stick_y;
        float direction = gamepad1.right_stick_x;
        float right = throttle - direction;
        float left = throttle + direction;

        /* Yj and Xj are joystick coordinates */
        float max = Math.abs(direction);
        if (Math.abs(throttle) > max)
            max = Math.abs(throttle);
        float sum = direction + throttle;
        float dif = direction-throttle;
        if(throttle<=0) {
            if(direction>=0) {
                motorLeft.setPower(max);
                motorRight.setPower(-sum);
            } else {
                motorLeft.setPower(dif);
                motorRight.setPower(max);
            }
        } else {
            if(direction>=0) {
                motorLeft.setPower(dif);
                motorRight.setPower(-max);
            } else {
                motorLeft.setPower(-max);
                motorRight.setPower(-sum);
            }
        }

        if (gamepad1.a)
            armPosition += armDelta;

        if (gamepad1.y)
            armPosition -= armDelta;

        if (gamepad1.x)
            clawPosition += clawDelta;

        if (gamepad1.b)
            clawPosition -= clawDelta;

        armPosition = Range.clip(armPosition, ARM_MIN_RANGE, ARM_MAX_RANGE);
        clawPosition = Range.clip(clawPosition, CLAW_MIN_RANGE, CLAW_MAX_RANGE);

        arm.setPosition(armPosition);
        claw.setPosition(clawPosition);
    }

    @Override
    public void stop() {

    }
}