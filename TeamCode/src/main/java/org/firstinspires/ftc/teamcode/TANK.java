package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

/**
 * Created by FTCGearedUP on 8/6/2015.
 */
public class TANK extends OpMode {

    DcMotor motor1;
    DcMotor motor2;
    DcMotor motor3;
    DcMotor motor4;
    
    @Override
    public void init() {
        motor1 = hardwareMap.dcMotor.get("1");
        motor2 = hardwareMap.dcMotor.get("2");
        motor3 = hardwareMap.dcMotor.get("3");
        motor4 = hardwareMap.dcMotor.get("4");

        motor2.setDirection(DcMotor.Direction.REVERSE);
        motor4.setDirection(DcMotor.Direction.REVERSE);

    }
    @Override
    public void loop() {

        if (gamepad1.right_stick_y > 0.2 || gamepad1.right_stick_y < -0.2 || gamepad1.left_stick_y > 0.2 || gamepad1.left_stick_y < -0.2) {

            motor1.setPower(gamepad1.right_stick_y);  //Main Drive
            motor2.setPower(gamepad1.left_stick_y);
            motor3.setPower(gamepad1.right_stick_y);
            motor4.setPower(gamepad1.left_stick_y);
        }

        if (gamepad1.right_stick_y < 0.2 && gamepad1.right_stick_y > -0.2 && gamepad1.left_stick_y < 0.2 && gamepad1.left_stick_y > -0.2) {

            motor1.setPower(0);  //Stop
            motor2.setPower(0);
            motor3.setPower(0);
            motor4.setPower(0);
        }

        if (gamepad1.y) {

            motor1.setPower(-.3);  //Slow Forward
            motor2.setPower(-.3);
            motor3.setPower(-.3);
            motor4.setPower(-.3);
        }

        if (gamepad1.a) {

            motor1.setPower(.3);  //Slow Back
            motor2.setPower(.3);
            motor3.setPower(.3);
            motor4.setPower(.3);
        }

        if (gamepad1.x) {

            motor1.setPower(-.3);  //Slow Left
            motor2.setPower(.3);
            motor3.setPower(-.3);
            motor4.setPower(.3);
        }

        if (gamepad1.b) {

            motor1.setPower(.3);  //Slow Right
            motor2.setPower(-.3);
            motor3.setPower(.3);
            motor4.setPower(-.3);
        }

    }
}
