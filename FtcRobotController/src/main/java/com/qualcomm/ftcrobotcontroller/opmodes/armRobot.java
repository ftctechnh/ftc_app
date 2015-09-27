package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.Range;

/**
 * Created by pranavb on 9/27/15.
 */
public class armRobot extends OpMode {
    float armMinRange = 0;
    float armMaxRange = 1;
    float clawMinRange = 0;
    float clawMaxRange = 1;

    double armPosition;
    double armDelta = 0.01;
    double clawPosition;
    double clawDelta = 0.01;

    DcMotor motorLeft;
    DcMotor motorRight;
    Servo arm;
    Servo claw;

    @Override
    public void init(){
        motorLeft = hardwareMap.dcMotor.get("motorL");
        motorLeft.setDirection(DcMotor.Direction.REVERSE);
        motorRight = hardwareMap.dcMotor.get("motorR");
        arm = hardwareMap.servo.get("arm");
        claw = hardwareMap.servo.get("claw");

        armPosition = 0.0;
        clawPosition = 0.0;
    }

    @Override
    public void loop(){
        float throttle = -gamepad1.left_stick_y;
        float direction = gamepad1.left_stick_x;
        float right = throttle - direction;
        float left = throttle + direction;

        right = Range.clip(right, -1, 1);
        left = Range.clip(left, -1, 1);

        right = (float)scaleInput(right);
        left = (float)scaleInput(left);

        motorLeft.setPower(left);
        motorRight.setPower(right);

        if(gamepad2.dpad_up){
            armPosition += armDelta;
        }
        if(gamepad2.dpad_down){
            armPosition -= armDelta;
        }
        if(gamepad2.y){
            clawPosition += clawDelta;
        }
        if(gamepad2.a){
            clawPosition -= clawDelta;
        }
    }
}
