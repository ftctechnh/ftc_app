package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.ftcrobotcontroller.Values;
import com.qualcomm.ftcrobotcontroller.hardware.HardwareManager;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;

/**
 * Created by tucker on 1/23/16.
 */
public class EncoderTest extends OpMode {

    DcMotor motorRight;
    DcMotor motorLeft;

    @Override
    public void init() {
        HardwareManager manager = new HardwareManager(hardwareMap);

        motorRight = manager.getMotor(Values.RIGHT_MOTOR);
        motorRight.setMode(DcMotorController.RunMode.RUN_USING_ENCODERS);
        motorLeft = manager.getMotor(Values.LEFT_MOTOR);
        motorLeft.setMode(DcMotorController.RunMode.RUN_USING_ENCODERS);
    }

    @Override
    public void loop() {
        telemetry.addData("Left Encoder", motorLeft.getCurrentPosition());
        telemetry.addData("Right Encoder", motorRight.getCurrentPosition());
    }

}
