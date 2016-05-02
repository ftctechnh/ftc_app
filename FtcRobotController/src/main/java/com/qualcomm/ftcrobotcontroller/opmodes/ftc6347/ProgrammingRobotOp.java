package com.qualcomm.ftcrobotcontroller.opmodes.ftc6347;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

/**
 * Created by FTCGearedUP on 4/24/2016.
 */
public class ProgrammingRobotOp extends OpMode {

    private DcMotor backLeftMotor;
    private DcMotor backRightMotor;
    private DcMotor frontLeftMotor;
    private DcMotor frontRightMotor;

    private final float GAMEPAD_THRESHOLD = 0.2f;

    @Override
    public void init() {

        /*
         * Initialize motor variables
         */
        backLeftMotor = hardwareMap.dcMotor.get("bl");   // controller 0, port 2
        backRightMotor = hardwareMap.dcMotor.get("br");  // controller 1, port 1
        frontLeftMotor = hardwareMap.dcMotor.get("fl");  // controller 0, port 1
        frontRightMotor = hardwareMap.dcMotor.get("fr"); // controller 1, port 2

        // Controller 0 is on the left, and controller 1 is on the right

        /*
         * Reverse motors
         */
        backLeftMotor.setDirection(DcMotor.Direction.FORWARD);
        frontLeftMotor.setDirection(DcMotor.Direction.FORWARD);

        backRightMotor.setDirection(DcMotor.Direction.REVERSE);
        frontRightMotor.setDirection(DcMotor.Direction.REVERSE);
    }

    @Override
    public void loop() {
        if(gamepad1.left_stick_y > GAMEPAD_THRESHOLD || gamepad1.left_stick_y < -GAMEPAD_THRESHOLD) {
            backLeftMotor.setPower(gamepad1.left_stick_y);
            frontLeftMotor.setPower(gamepad1.left_stick_y);
        } else {
            backLeftMotor.setPower(0);
            frontLeftMotor.setPower(0);
        }

        if(gamepad1.right_stick_y > GAMEPAD_THRESHOLD || gamepad1.right_stick_y < -GAMEPAD_THRESHOLD) {
            backRightMotor.setPower(gamepad1.right_stick_y);
            frontRightMotor.setPower(gamepad1.right_stick_y);
        } else {
            backRightMotor.setPower(0);
            frontRightMotor.setPower(0);
        }
    }
}
