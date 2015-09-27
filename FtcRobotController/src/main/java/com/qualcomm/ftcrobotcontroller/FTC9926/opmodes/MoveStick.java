package com.qualcomm.ftcrobotcontroller.FTC9926.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.Range;

/**
 * Created by ibravo on 9/27/15.
 * Test One Joystick movement
 */
public class MoveStick extends OpMode {


    DcMotor Motor1;
    DcMotor Motor2;

    @Override
    public void init() {
        // Link with Phone Configuration Names
        Motor1 = hardwareMap.dcMotor.get("M1");
        Motor2 = hardwareMap.dcMotor.get("M2");
        Motor2.setDirection(DcMotor.Direction.REVERSE);

    }

    @Override
    public void start() {
        //   hardwareMap.dcMotor.get("DC1");
        //   hardwareMap.dcMotor.get("DC2");

        //    leftMotor.setDirection(DcMotor.Direction.REVERSE);
    }

    @Override
    public void loop() {

        // Tank Mode movement:
        //   float M1 = gamepad1.right_stick_y;
        //   float M2 = gamepad1.left_stick_y;

        // Direction and throttle movement;
        // Copy from K9TeleOp
        float throttle = -gamepad1.left_stick_y;
        float direction = -gamepad1.left_stick_x;
        float M1 = throttle - direction;
        float M2 = throttle + direction;

        M1 = Range.clip(M1, -1, 1);
        M2 = Range.clip(M2, -1, 1);

        Motor1.setPower(M1);
        Motor2.setPower(M2);

        telemetry.addData("M1", "M1: " + M1);
        telemetry.addData("M2", "M2: " + M2);

    }

    @Override
    public void stop() {

    }
}
