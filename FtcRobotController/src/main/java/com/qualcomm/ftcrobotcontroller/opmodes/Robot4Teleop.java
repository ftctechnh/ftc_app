package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

public class Robot4Teleop extends OpMode {

    DcMotor frontLeftMotor;
    DcMotor backRightMotor;
    DcMotor frontRightMotor;
    DcMotor backLeftMotor;


    public void init() {
        frontLeftMotor = hardwareMap.dcMotor.get("frontL");
        frontRightMotor = hardwareMap.dcMotor.get("frontR");
        backLeftMotor = hardwareMap.dcMotor.get("backL");
        backRightMotor = hardwareMap.dcMotor.get("backR");
        backLeftMotor.setDirection(DcMotor.Direction.REVERSE);
        frontRightMotor.setDirection(DcMotor.Direction.REVERSE);

    }

    public void setMotors(double FrontL, double FrontR, double BackL, double BackR){
        frontLeftMotor.setPower(FrontL);
        frontRightMotor.setPower(FrontR);
        backLeftMotor.setPower(BackL);
        backRightMotor.setPower(BackR);
    }

    public void loop() {
        float throttle = gamepad1.left_stick_y;
        float rightThrottle = gamepad1.right_stick_y;
        float secondThrottle = gamepad2.left_stick_y;
        float secondRightThrottle = gamepad2.right_stick_y;

        throttle=(Math.abs(throttle) < 0.05) ? 0:throttle;
        rightThrottle=(Math.abs(rightThrottle) < 0.05) ? 0:rightThrottle;
        setMotors(throttle,rightThrottle,throttle,rightThrottle);

        if (gamepad1.left_trigger == 1 || gamepad2.left_trigger == 1 ) {

        } else if (gamepad1.right_trigger == 1 || gamepad2.right_trigger == 1){

        } else {
        }

        if (gamepad1.b || gamepad2.b) {

        }
        if (gamepad1.y || gamepad2.y) {

        }
        if (gamepad1.a || gamepad2.a) {

        }
        if (gamepad1.right_bumper || gamepad2.right_bumper) {

        }
        if (gamepad1.left_bumper || gamepad1.left_bumper) {

        }
        if (gamepad1.dpad_left || gamepad2.dpad_left) {

        }
        if (gamepad1.dpad_up || gamepad2.dpad_up) {

        }
        if (gamepad1.dpad_right || gamepad2.dpad_right) {

        }
        if (gamepad1.dpad_down || gamepad2.dpad_down) {

        }

    }
    public void stop() {

    }

}