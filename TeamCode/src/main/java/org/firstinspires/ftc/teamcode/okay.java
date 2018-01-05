package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

@TeleOp (name="okay", group="TeleOp")
public class okay extends OpMode {
    private Servo thiccClaw1   = null;
    private Servo thiccClaw2   = null;
    @Override
    public void init() {
        telemetry.addData("Status", "Uninitialized...");
        thiccClaw1 = hardwareMap.get(Servo.class, "thiccClaw1");
        thiccClaw2 = hardwareMap.get(Servo.class, "thiccClaw2");
        telemetry.addData("Status", "Initialized");
    }

    double CLOSE_POS = 0.12;
    double OPEN_POS = 0.75;
    @Override
    public void loop() {
        if (gamepad1.a) {
            thiccClaw1.setPosition(CLOSE_POS);
            thiccClaw2.setPosition(CLOSE_POS);
        }else if (gamepad1.b){
            thiccClaw1.setPosition(OPEN_POS);
            thiccClaw2.setPosition(OPEN_POS);
        }
    }

}
