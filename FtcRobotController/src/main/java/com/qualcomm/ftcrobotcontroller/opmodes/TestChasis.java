package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
/**
 * Created by Tony_Air on 10/1/15.
 */
public class TestChasis extends OpMode {

    DcMotor LEFT;
    DcMotor RIGHT;

    float leftX;
    float leftY;

    float reductionMin;

    float reduction = 1;

    public void init(){

        LEFT =  hardwareMap.dcMotor.get("motor1");
        RIGHT = hardwareMap.dcMotor.get("motor2");

    }


    public void loop(){

        float speed = gamepad1.left_stick_y * leftY;
        float rotation = gamepad1.left_stick_x * leftX;


        if(gamepad1.left_stick_button) {

            if (reduction == 1) {
                reduction = reductionMin;

            } else if(reduction == reductionMin){
                reduction = 1;

            }
        }

        float leftSpeed = (speed + rotation) * reduction;
        float rightSpeed = (speed - rotation) * reduction;

        LEFT.setPower(-leftSpeed);
        RIGHT.setPower(rightSpeed);

        telemetry.addData("01:", "Left Motor: " + leftSpeed);
        telemetry.addData("02:", "Right Motor: " + rightSpeed);

    }


}
