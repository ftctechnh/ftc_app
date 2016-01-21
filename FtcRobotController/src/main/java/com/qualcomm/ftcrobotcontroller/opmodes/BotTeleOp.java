package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.ftcrobotcontroller.CycleTimer;
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

    Servo leftArm;
    Servo rightArm;

    boolean btnSideLeft;
    boolean btnSideRight;

    @Override
    public void init() {
        HardwareManager manager = new HardwareManager(hardwareMap);

        motorRight = manager.getMotor(Values.RIGHT_MOTOR);
        motorRight.setDirection(DcMotor.Direction.REVERSE);
        motorLeft = manager.getMotor(Values.LEFT_MOTOR);

        tape = manager.getMotor(Values.TAPE);

        leftArm = manager.getServo(Values.LEFT_ARM);
        rightArm = manager.getServo(Values.RIGHT_ARM);
    }

    @Override
    public void loop() {
        CycleTimer.update();

        motorLeft.setPower(Power.speedCurve(gamepad1.left_stick_y) * gamepad1.left_trigger);
        motorRight.setPower(Power.speedCurve(gamepad1.right_stick_y) * gamepad1.right_trigger);

        if (gamepad1.dpad_up) {
            tape.setPower(Power.NORMAL_SPEED);
        } else if (gamepad2.dpad_down) {
            tape.setPower(-Power.NORMAL_SPEED);
        } else {
            tape.setPower(Power.FULL_STOP);
        }

        if (gamepad1.left_bumper && !btnSideLeft) {
            btnSideLeft = true;
            if (leftArm.getPosition() == Values.SIDE_ARM_IN)
                leftArm.setPosition(Values.SIDE_ARM_OUT);
            else
                leftArm.setPosition(Values.SIDE_ARM_IN);
        } else {
            btnSideLeft = false;
        }

        if (gamepad1.right_bumper && !btnSideRight) {
            btnSideRight = true;
            if (rightArm.getPosition() == Values.SIDE_ARM_IN)
                rightArm.setPosition(Values.SIDE_ARM_OUT);
            else
                rightArm.setPosition(Values.SIDE_ARM_IN);
        } else {
            btnSideRight = false;
        }

        telemetry.addData("Title", "***Robot Data***");
        telemetry.addData("Right Motor", "Right:" + motorRight.getPower());
        telemetry.addData("Left Motor", "Left:" + motorLeft.getPower());
        telemetry.addData("Tape Motor", "Tape:" + tape.getPower());
    }

    @Override
    public void stop() {
        motorLeft.setPower(Power.FULL_STOP);
        motorRight.setPower(Power.FULL_STOP);
        tape.setPower(Power.FULL_STOP);
    }

}
