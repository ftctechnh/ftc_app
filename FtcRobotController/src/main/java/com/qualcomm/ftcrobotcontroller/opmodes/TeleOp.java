package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

/**
 * Created by Robomain on 11/3/2015.
 */
public class TeleOp extends OpMode {
    DcMotor rightmotor;
    DcMotor leftmotor;
    Servo servo;
    @Override
    public void init()
    {
        leftmotor = hardwareMap.dcMotor.get("leftmotor");
        rightmotor =hardwareMap.dcMotor.get("rightmotor");
        servo = hardwareMap.servo.get("servo");
        rightmotor.setDirection(DcMotor.Direction.REVERSE);
    }
    @Override

    public void loop()
    {
        float leftY = -gamepad1.left_stick_y;
        float rightY = -gamepad1.right_stick_y;
        if(gamepad1.left_bumper)
        {
            leftmotor.setPower(.5*leftY);
            rightmotor.setPower(.5*rightY);
        }
        else {


            leftmotor.setPower(leftY);
            rightmotor.setPower(rightY);
        }
        if (gamepad1.y)
        {
            servo.setPosition(0);
        }
        else if(gamepad1.b) {
        servo.setPosition(.6);
    }
        else if(gamepad1.a) {
            servo.setPosition(.75);
        }
    }
}
