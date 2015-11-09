package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.OpticalDistanceSensor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.Range;

/**
 * Created by freemmar001 on 10/11/2015.
 */
public class FirstChassisDrive extends OpMode {
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
        float rightDC =  gamepad1.right_stick_y;

        double light = sensor.getLightDetected();
        front.setPosition(light);

        left.setPower(leftDC);
        right.setPower(rightDC);
        //if(leftDCy > 0)

        //direction = Range.clip(direction, 0, 1);
        //one.setPosition(directi

    }
}
