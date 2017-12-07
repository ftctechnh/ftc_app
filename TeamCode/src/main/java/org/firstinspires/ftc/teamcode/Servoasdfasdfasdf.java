package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

/**
 * Created by ths on 12/6/17.
 */

@TeleOp (name="asdfasdf", group="TeleOp")
public class Servoasdfasdfasdf extends OpMode {
    HardwareMap hwMap = hardwareMap;
    Servo servo;
    public void loop() {
        if (gamepad1.b) {
            servo.setPosition(1);
        } else if (gamepad1.a) {
            servo.setPosition(0);
        }
    }
    public void init() {
        servo = hwMap.get(Servo.class, "arm");
    }
}
