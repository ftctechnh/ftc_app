package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.ftcrobotcontroller.Values;
import com.qualcomm.ftcrobotcontroller.hardware.HardwareManager;
import com.qualcomm.ftcrobotcontroller.hardware.Power;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

public class BotTeleOp extends OpMode {

    DcMotor motorRight;
    DcMotor motorLeft;

    DcMotor tape;

    Servo arm1;
    Servo arm2;
    Servo claw1;
    Servo claw2;

    @Override
    public void init() {
        HardwareManager manager = new HardwareManager(hardwareMap);

        motorRight = manager.getMotor(Values.RIGHT_MOTOR);
        motorRight.setDirection(DcMotor.Direction.REVERSE);
        motorLeft = manager.getMotor(Values.LEFT_MOTOR);

        tape = manager.getMotor(Values.TAPE);

        arm1 = manager.getServo(Values.ARM_1);
        arm2 = manager.getServo(Values.ARM_2);
        claw1 = manager.getServo(Values.CLAW_1);
        claw2 = manager.getServo(Values.CLAW_2);
    }

    @Override
    public void loop() {
        motorLeft.setPower(Power.speedCurve(gamepad1.left_stick_y) * gamepad1.left_trigger);
        motorRight.setPower(Power.speedCurve(gamepad1.right_stick_y) * gamepad1.right_trigger);

        if (gamepad1.y) {
            arm1.setPosition(Power.powerClamp(arm1.getPosition() + Values.SERVO_INCREMENT));
            arm2.setPosition(Power.powerClamp(arm2.getPosition() - Values.SERVO_INCREMENT));
        } else if (gamepad1.a) {
            arm1.setPosition(Power.powerClamp(arm1.getPosition() - Values.SERVO_INCREMENT));
            arm2.setPosition(Power.powerClamp(arm2.getPosition() + Values.SERVO_INCREMENT));
        }

        if (gamepad1.x) {
            claw1.setPosition(Values.CLAW_OPEN);
            claw2.setPosition(Values.CLAW_CLOSED);
        }
        if (gamepad1.b) {
            claw1.setPosition(Values.CLAW_CLOSED);
            claw2.setPosition(Values.CLAW_OPEN);
        }

        if (gamepad1.dpad_up) {
            tape.setPower(Power.NORMAL_SPEED);
        } else if (gamepad2.dpad_down) {
            tape.setPower(-Power.NORMAL_SPEED);
        } else {
            tape.setPower(Power.FULL_STOP);
        }

        telemetry.addData("Title", "***Robot Data***");
        telemetry.addData("Right Motor", "Right:" + motorRight.getPower());
        telemetry.addData("Left Motor", "Left:" + motorLeft.getPower());
        telemetry.addData("Tape Motor", "Tape:" + tape.getPower());
        telemetry.addData("Arm 1", "Arm 1:" + arm1.getPosition());
        telemetry.addData("Arm 2", "Arm 2:" + arm2.getPosition());
        telemetry.addData("Claw 1", "Claw 1:" + claw1.getPosition());
        telemetry.addData("Claw 2", "Claw 2:" + claw2.getPosition());
    }

    @Override
    public void stop() {
        motorLeft.setPower(Power.FULL_STOP);
        motorRight.setPower(Power.FULL_STOP);
        tape.setPower(Power.FULL_STOP);
    }

}
