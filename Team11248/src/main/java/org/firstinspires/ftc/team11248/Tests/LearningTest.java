package org.firstinspires.ftc.team11248.Tests;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

/**
 * Created by Tony_Air on 11/17/17.
 */

public class LearningTest extends OpMode{

    Servo servo1;
    DcMotor motor;

    @Override
    public void init() {
        motor = hardwareMap.dcMotor.get("FrontRight");
        servo1 = hardwareMap.servo.get("servo1");
    }

    @Override
    public void loop() {

       motor.setPower(gamepad1.left_stick_x);

    }
}
