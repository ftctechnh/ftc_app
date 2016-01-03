package com.qualcomm.ftcrobotcontroller.opmodes.diag;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;

/**
 * Created by tdoylend on 2016-01-02.
 *
 * This diagnostic routine enables you to test an
 * encoder. You can move the motor with your hand
 * and read encoder values from telemetry.
 *
 * The encoder is reset during the init sequence.
 */

public class DiagEncoder extends OpMode {

    DcMotor motor;

    @Override
    public void init() {
        telemetry.addData("Diagnostic","Encoder Test");

        motor.setMode(DcMotorController.RunMode.RESET_ENCODERS);
        motor.setMode(DcMotorController.RunMode.RUN_USING_ENCODERS);
    }

    @Override
    public void loop() {
        telemetry.addData("Diagnostic", "Encoder Test");
        telemetry.addData("Encoder Reading", motor.getCurrentPosition());
    }
}
