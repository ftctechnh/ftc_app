package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

public class Test extends OpMode {
    DcMotor motor1;
    DcMotor motor2;
    Servo servo1;
    Servo servo2;
    Servo servo3;
    Servo servo4;
    Servo servo5;
    Servo servo6;


    @Override
    public void init() {
        motor1 = hardwareMap.dcMotor.get("motor1");
        motor2 = hardwareMap.dcMotor.get("motor2");
        servo1 = hardwareMap.servo.get("servo1");
        servo2 = hardwareMap.servo.get("servo2");
        servo3 = hardwareMap.servo.get("servo3");
        servo4 = hardwareMap.servo.get("servo4");
        servo5 = hardwareMap.servo.get("servo5");
        servo6 = hardwareMap.servo.get("servo6");

    }

    @Override
    public void loop() {

        motor1.setPower(gamepad1.left_stick_y);
        motor2.setPower(gamepad1.right_stick_y);

        if (gamepad1.x) {
            servo1.setPosition(1);
            servo2.setPosition(1);
            servo3.setPosition(1);
            servo4.setPosition(1);
            servo5.setPosition(1);
            servo6.setPosition(1);
        } else if (gamepad1.a) {
            servo1.setPosition(0);
            servo2.setPosition(0);
            servo3.setPosition(0);
        }
    }
}
