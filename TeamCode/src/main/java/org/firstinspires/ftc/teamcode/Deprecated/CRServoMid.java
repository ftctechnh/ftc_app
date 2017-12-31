package org.firstinspires.ftc.teamcode.Deprecated;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;

/**
 * Created by Kaden on 12/2/2017.
 */
@TeleOp(name = "CRServoMid", group = "linear OpMode")
@Disabled
public class CRServoMid extends OpMode{
    CRServo servo;
    public void init() {
        servo = hardwareMap.crservo.get("s0");
        servo.setPower(0);
    }
    public void loop(){
        if(gamepad1.a) {
            servo.setPower(1);
        }
        if(gamepad1.b) {
            servo.setPower(-1);
        }
    }
}
