package com.powerstackers.resq.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

/**
 * Created by Jonathan on 11/5/2015.
 */
public class PurplePencil extends OpMode{

    private DcMotor motor;

    @Override
    public void init() {
        motor = hardwareMap.dcMotor.get("motor_1");
    }

    @Override
    public void loop() {
        motor.setPower(gamepad1.left_stick_y);
    }
}
