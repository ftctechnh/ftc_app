package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.ServoController;

/**
 * Created by MRHFTC on 1/16/2016.
 */
public class Practice_Op extends OpMode {

    DcMotor motor1;
    DcMotor motor2;
    DcMotor motor3;
    DcMotor motor4;

    double gear = 1.0;

    public Practice_Op() {

    }

    @Override
    public void init() {
        motor1 = hardwareMap.dcMotor.get("Motor 1");
        motor2 = hardwareMap.dcMotor.get("Motor 2");
        motor3 = hardwareMap.dcMotor.get("Motor 3");
        motor4 = hardwareMap.dcMotor.get("Motor 4");
        motor1.setDirection(DcMotor.Direction.REVERSE);
    }

    @Override
    public void loop() {
        double throttleLeft = gamepad1.left_stick_y;
        double throttleRight = gamepad1.right_stick_y;

        if (gamepad1.a) {
            while (gamepad1.a) {
            }
            if(gear == 1.0) {
                gear = 0.5;
            }
            else{
                gear = 1.0;
            }
        }

        motor1.setPower(throttleLeft * gear);
        motor2.setPower(throttleRight * gear);
        motor3.setPower(gamepad1.right_trigger - gamepad1.left_trigger * 0.25);

        if (gamepad1.right_bumper){
            motor4.setPower(1.0);
        }
        else if (gamepad1.left_bumper){
            motor4.setPower(-1.0);
        }
        else {
            motor4.setPower(0.0);
        }


        telemetry.addData("Left Throttle", String.format("%.2f", throttleLeft));
        telemetry.addData("Right Throttle", String.format("%.2f", throttleRight));
    }
}
