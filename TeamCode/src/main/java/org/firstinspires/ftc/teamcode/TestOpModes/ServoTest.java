package org.firstinspires.ftc.teamcode.TestOpModes;


import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Servo;

/**
 * Created by pston on 4/3/2018
 */

public class ServoTest extends OpMode {

    private Servo testServo;

    private double servoPosition = 0.5;

    @Override
    public void init() {
        testServo = hardwareMap.servo.get("test");
    }

    @Override
    public void loop() {

        if (gamepad1.x) {
            servoPosition += 0.005;
        } else if (gamepad1.b) {
            servoPosition -= 0.005;
        }

        testServo.setPosition(servoPosition);
    }
}
