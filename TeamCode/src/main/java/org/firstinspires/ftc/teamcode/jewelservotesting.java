package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.Range;


/**
 * Created by aus on 1/30/18.
 */
@TeleOp(name = "testingjewelservo", group = "test")
public class jewelservotesting extends OpMode {
    Servo servo;
    public void init() {
        servo = hardwareMap.servo.get("s4");
        servo.setPosition(0.5);
    }
    public void loop() {
        if(gamepad1.a) {
            servo.setPosition(clip(servo.getPosition()+0.0001));
        }
        if(gamepad1.b) {
            servo.setPosition(clip(servo.getPosition()-0.0001));
        }
        telemetry.addData("Servo pos", servo.getPosition());
        telemetry.update();

    }
    private double clip(double value) {
        return Range.clip(value, 0, 0.035);
    }
}
