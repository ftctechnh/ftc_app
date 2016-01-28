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

    DcMotor tape1;
    DcMotor tape2;

    Servo leftArm;
    Servo rightArm;

    Servo dump;

    boolean btnSideLeft;
    boolean btnSideRight;

    int tapeMod = 1;

    @Override
    public void init() {
        HardwareManager manager = new HardwareManager(hardwareMap);

        motorRight = manager.getMotor(Values.RIGHT_MOTOR);
        motorRight.setDirection(DcMotor.Direction.REVERSE);
        motorLeft = manager.getMotor(Values.LEFT_MOTOR);
        motorLeft.setDirection(DcMotor.Direction.REVERSE);

        tape1 = manager.getMotor(Values.TAPE_1);
        tape2 = manager.getMotor(Values.TAPE_2);

        leftArm = manager.getServo(Values.LEFT_ARM);
        rightArm = manager.getServo(Values.RIGHT_ARM);

        dump = manager.getServo(Values.DUMP);
    }

    @Override
    public void loop() {
        CycleTimer.update();

        motorLeft.setPower(Power.speedCurve(gamepad1.left_stick_y));
        motorRight.setPower(Power.speedCurve(gamepad1.right_stick_y));

        tape1.setPower(gamepad1.left_trigger * tapeMod);
        tape2.setPower(gamepad1.right_trigger * tapeMod);

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

        if (gamepad1.y) {
            dump.setPosition(Values.DUMP_UP);
        } else if (gamepad1.a) {
            dump.setPosition(Values.DUMP_DOWN);
        }

        if (gamepad1.x) {
            tapeMod = 1;
        } else if (gamepad1.b) {
            tapeMod = -1;
        }

        telemetry.addData("Title", "***Robot Data***");
        telemetry.addData("Right Motor", "Right:" + motorRight.getPower());
        telemetry.addData("Left Motor", "Left:" + motorLeft.getPower());
        telemetry.addData("Tape 1", "Tape 1:" + tape1.getPower());
        telemetry.addData("Tape 2", "Tape 2:" + tape2.getPower());
    }

    @Override
    public void stop() {
        motorLeft.setPower(Power.FULL_STOP);
        motorRight.setPower(Power.FULL_STOP);
        tape1.setPower(Power.FULL_STOP);
        tape2.setPower(Power.FULL_STOP);
    }

}
