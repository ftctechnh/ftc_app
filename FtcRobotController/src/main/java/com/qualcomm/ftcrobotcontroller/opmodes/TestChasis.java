package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

/**
 * Created by Tony_Air on 10/1/15.
 */
public class TestChasis extends OpMode {

    DcMotor LEFT;
    DcMotor RIGHT;



    public void init(){

        LEFT =  hardwareMap.dcMotor.get("motor1");
        RIGHT = hardwareMap.dcMotor.get("motor2");


    }

    public void loop(){

        float speed = -gamepad1.left_stick_y * .75f;
        float rotation = -gamepad1.left_stick_x * .25f;

        float leftSpeed = speed - rotation;
        float rightSpeed = speed + rotation;

        LEFT.setPower(leftSpeed);
        RIGHT.setPower(rightSpeed);

        telemetry.addData("01:", "Left Motor: " + leftSpeed);
        telemetry.addData("02:", "Right Motor: " + rightSpeed);

    }


}
