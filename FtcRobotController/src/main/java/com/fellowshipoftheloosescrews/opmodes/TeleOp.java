package com.fellowshipoftheloosescrews.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.*;

/**
 * Created by Thomas on 8/12/2015.
 */
public class TeleOp extends OpMode
{
    DcMotor motor1;

    @Override
    public void init() {
        motor1 = hardwareMap.dcMotor.get("motor1");
    }

    @Override
    public void loop() {
        motor1.setPower(gamepad1.left_stick_y);
        telemetry.addData("left stick y", gamepad1.left_stick_y);
        telemetry.addData("motor encoder value", motor1.getCurrentPosition());
    }
}
