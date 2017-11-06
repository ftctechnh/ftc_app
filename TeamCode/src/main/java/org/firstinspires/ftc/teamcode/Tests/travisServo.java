package org.firstinspires.ftc.teamcode.Tests;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Servo;

@Disabled
@TeleOp(name = "travisServo", group = "Test")
public class travisServo extends LinearOpMode {
    public void runOpMode() {
        Servo servo = hardwareMap.servo.get("servo");
        //servo.setPosition(0);
        telemetry.addData("Status", "Ready");
        telemetry.update();
        waitForStart();
        while (opModeIsActive()) {
            /*

            if (servo.getPosition() == 0) {
                servo.setPosition(.5);
            }

            if (servo.getPosition() == .5) {
                servo.setPosition(0);
            }

            */
            servo.setPosition(gamepad1.left_stick_y);
            telemetry.addData("servo position", servo.getPosition());
            telemetry.update();
        }
    }
}
