package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;


/**
 * Created by Chris Campos and Justin Vicente on 9/19/2015.
 */
@TeleOp(name = "TeleOpOmniBot", group = "")
public class TeleOpOmniBot extends OpMode {
    DcMotor motorBackLeft;
    DcMotor motorFrontLeft;
    DcMotor motorBackRight;
    DcMotor motorFrontRight;
    Servo buttonPusher;
    DcMotor motorElevator;
    Servo pineapple;

    float joystick1ToMotor(float y){
        double dead_zone = 0.1;
        if(y > dead_zone){
            y -= dead_zone;
        }else if(y < -dead_zone){
            y += dead_zone;
        }else{
            y = 0;
        }
        return(float) (y/(1.0-dead_zone));
    }

    public void init() {
        motorBackRight = hardwareMap.dcMotor.get("RightBack");
        motorFrontRight = hardwareMap.dcMotor.get("RightFront");
        motorBackLeft = hardwareMap.dcMotor.get("LeftBack");
        motorFrontLeft = hardwareMap.dcMotor.get("LeftFront");
    }


    public void loop() {
        float rotation = -joystick1ToMotor(gamepad1.right_stick_x);
        float forward = joystick1ToMotor(gamepad1.right_stick_y);
        float side = joystick1ToMotor(gamepad1.left_stick_x);
        //motorBackLeft.setPower(rotation + forward - side);
        motorBackLeft.setPower(rotation + forward - side);
        motorFrontLeft.setPower(rotation + forward + side);
        motorBackRight.setPower(rotation - forward - side);
        motorFrontRight.setPower(rotation - forward + side);
        telemetry.addData("rotation", rotation);
        telemetry.addData("forward", forward);
        telemetry.addData("side", side);
        telemetry.update();
    }
}

