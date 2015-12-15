package com.powerstackers.resq.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

/**
 * Very simple op mode for testing with Leena.
 * @author Jonathan Thomas
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
