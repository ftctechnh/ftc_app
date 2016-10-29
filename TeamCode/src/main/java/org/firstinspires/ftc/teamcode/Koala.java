package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

public class Koala extends OpMode {
    DcMotor motorL1;
    DcMotor motorL2;
    DcMotor motorR1;
    DcMotor motorR2;

    @Override
    public void init() {
        motorL1 = hardwareMap.dcMotor.get("motorL1");
        motorL2 = hardwareMap.dcMotor.get("motorL2");
        motorR1 = hardwareMap.dcMotor.get("motorR1");
        motorR2 = hardwareMap.dcMotor.get("motorR2");
    }

    @Override
    public void loop(){
        motorL1.setPower(gamepad1.left_stick_y);
        motorL2.setPower(gamepad1.left_stick_y);
        motorR1.setPower(gamepad1.right_stick_y);
        motorR2.setPower(gamepad1.right_stick_y);
    }

}
