package org.firstinspires.ftc.teamcode.Utilities.Servos;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Servo;

@TeleOp(name="Configure Servos", group="_Config")
public class ConfigureServos extends LinearOpMode {
    public Servo s;

    @Override
    public void runOpMode() throws InterruptedException {
        s = hardwareMap.servo.get("testPort");

        waitForStart();
        while (opModeIsActive()) {
            if        (gamepad1.dpad_up) {s.setPosition(0);}
            else if (gamepad1.dpad_down) {s.setPosition(1);}
            else {s.setPosition(.5);}
        }
    }
}
