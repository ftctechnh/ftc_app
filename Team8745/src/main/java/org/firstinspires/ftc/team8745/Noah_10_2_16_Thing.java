package org.firstinspires.ftc.team8745;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.LightSensor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.TouchSensor;

/**
 * Created by darwin on 9/20/16.
 */

@TeleOp(name="{InsertCleverName}", group="Normal_Opmode")

public class Noah_10_2_16_Thing extends OpMode {
    DcMotor left;
    DcMotor right;
    Servo front;
    Servo back;
    TouchSensor touchSensor1;
    LightSensor lightSensor1;

    public void init() {
        left = hardwareMap.dcMotor.get("motor-left");
        right = hardwareMap.dcMotor.get("motor-right");
        right.setDirection(DcMotorSimple.Direction.REVERSE);
        front = hardwareMap.servo.get("servo-front");
        back = hardwareMap.servo.get("servo-back");
        touchSensor1 = hardwareMap.touchSensor.get("sensor-Touch Sensor");
        lightSensor1 = hardwareMap.opticalDistanceSensor.get("sensor-ODS");
    }


    @Override
    public void loop() {
        float leftDC = gamepad1.left_stick_y;
        float rightDC = gamepad1.right_stick_y;
        float rightInput = gamepad1.right_trigger;

        front.setPosition(rightInput);

        //if(leftDCy > 0
        back.setPosition(lightSensor1.getLightDetected());
        if(touchSensor1.isPressed()) {
            left.setPower(0);
            right.setPower(0);}

        else {left.setPower(leftDC);
            left.setPower(leftDC);
            right.setPower(rightDC);}

    }

}

