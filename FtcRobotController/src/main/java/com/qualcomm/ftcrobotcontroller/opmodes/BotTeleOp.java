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

    boolean btnSideLeft = false;
    boolean btnSideRight = false;

    int tapeMod = 1;

    @Override
    public void init() {
        HardwareManager manager = new HardwareManager(hardwareMap);

        motorRight = manager.getMotor(Values.RIGHT_MOTOR);
        motorRight.setDirection(DcMotor.Direction.REVERSE);
        motorLeft = manager.getMotor(Values.LEFT_MOTOR);
        motorLeft.setDirection(DcMotor.Direction.REVERSE);

        tape1 = manager.getMotor(Values.TAPE_1);
        tape1.setDirection(DcMotor.Direction.REVERSE);
        tape2 = manager.getMotor(Values.TAPE_2);
        tape2.setDirection(DcMotor.Direction.REVERSE);

        leftArm = manager.getServo(Values.LEFT_ARM);
        rightArm = manager.getServo(Values.RIGHT_ARM);

        dump = manager.getServo(Values.DUMP);
    }

    @Override
    public void loop() {
        CycleTimer.update();

        motorLeft.setPower(Power.speedCurve(gamepad1.left_stick_y));
        motorRight.setPower(Power.speedCurve(gamepad1.right_stick_y));

        tape1.setPower(Power.speedCurve(gamepad2.left_stick_y));
        tape2.setPower(Power.speedCurve(gamepad2.right_stick_y));

        if (gamepad2.left_bumper && !btnSideLeft) {
            btnSideLeft = true;
            if (leftArm.getPosition() == Values.SIDE_ARM_IN)
                leftArm.setPosition(Values.SIDE_ARM_OUT);
            else
                leftArm.setPosition(Values.SIDE_ARM_IN);
        } else if (!gamepad2.left_bumper) {
            btnSideLeft = false;
        }

        if (gamepad2.right_bumper && !btnSideRight) {
            btnSideRight = true;
            if (rightArm.getPosition() == Values.SIDE_ARM_IN)
                rightArm.setPosition(Values.SIDE_ARM_OUT);
            else
                rightArm.setPosition(Values.SIDE_ARM_IN);
        } else if (!gamepad2.right_bumper) {
            btnSideRight = false;
        }

        if (gamepad2.y) {
            dump.setPosition(Values.DUMP_UP);
        } else if (gamepad2.a) {
            dump.setPosition(Values.DUMP_DOWN);
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
