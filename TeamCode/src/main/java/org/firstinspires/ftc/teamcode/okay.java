package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

@TeleOp (name="wowee a test class", group="TeleOp")
public class okay extends OpMode {
    private CRServo spinnyBoi1 = null;
    private CRServo spinnyBoi2 = null;
    @Override
    public void init() {
        telemetry.addData("Status", "Uninitialized...");
        spinnyBoi1 = hardwareMap.get(CRServo.class, "spinnyBoi1");
        spinnyBoi2 = hardwareMap.get(CRServo.class, "spinnyBoi2");
        telemetry.addData("Status", "Initialized");
    }

    @Override
    public void loop() {
        if (gamepad1.a){
            spinTheBois(1);
        } else if (gamepad1.b) {
            spinTheBois(-1);
        } else {
            spinTheBois(0);
        }
    }

    private void spinTheBois(double quickness){
        spinnyBoi1.setPower(quickness);
        spinnyBoi2.setPower(quickness);
    }
}
