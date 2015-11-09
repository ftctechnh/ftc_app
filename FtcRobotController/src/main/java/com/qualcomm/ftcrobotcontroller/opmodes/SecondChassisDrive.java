package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.OpticalDistanceSensor;
import com.qualcomm.robotcore.hardware.Servo;

/**
 * Created by FREEMMAR001 on 10/24/2015.
 */

public class SecondChassisDrive extends OpMode {
    DcMotor left;
    DcMotor right;
    OpticalDistanceSensor sensor;
    Servo front;

    public void init()  {
        left = hardwareMap.dcMotor.get("Motor-left");
        right = hardwareMap.dcMotor.get("Motor-right");
        sensor = hardwareMap.opticalDistanceSensor.get("OPTICAL");
        front = hardwareMap.servo.get("Servo");
    }

    @Override
    public void loop() {
        float leftDC = gamepad1.left_stick_y;
        float rightDC =  gamepad1.left_stick_y;

        double light = sensor.getLightDetected();
        front.setPosition(light);

        left.setPower(leftDC);
        right.setPower(rightDC);
        //if(leftDCy > 0)

        //direction = Range.clip(direction, 0, 1);
        //one.setPosition(directi

    }
}
