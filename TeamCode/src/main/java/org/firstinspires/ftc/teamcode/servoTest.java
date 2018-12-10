package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Servo;

@TeleOp(name="servoTest",group="Linear Opmode")
public class servoTest extends LinearOpMode {
    private Servo servo;

    @Override
    public void runOpMode() throws InterruptedException{
        servo = hardwareMap.get(Servo.class, "servo");
        servo.setPosition(.6);
        waitForStart();
        while(opModeIsActive()) {
            if(gamepad1.a && servo.getPosition() < 1) {
                servo.setPosition(servo.getPosition()+.01);
            } else if(gamepad1.b && servo.getPosition() > 0) {
                servo.setPosition(servo.getPosition()-.01);
            }
            telemetry.addData("servo", servo.getPosition());

            telemetry.update();
        }
    }
}
