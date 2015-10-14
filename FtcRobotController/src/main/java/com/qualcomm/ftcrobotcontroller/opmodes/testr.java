package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

/**
 * Created by no on 10/7/15.
 */
public class testr extends OpMode {
    DcMotor leftmotor;
    DcMotor rightmotor;

    @Override
    public void init() {
        leftmotor = hardwareMap.dcMotor.get("left_drive");
        rightmotor = hardwareMap.dcMotor.get("right_drive");
        //leftmotor.setDirection(DcMotor.Direction.REVERSE);
    }
    @Override
    public void loop() {
        float leftY = -gamepad1.left_stick_y;
        float rightY = gamepad1.right_stick_y;

        leftmotor.setPower(leftY);
        rightmotor.setPower(rightY);
    }

}
//alex