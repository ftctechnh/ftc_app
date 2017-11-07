package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DistanceSensor;
import com.qualcomm.robotcore.hardware.Servo;

/**
 * Created by Kit Caldwell on 10/17/2017.
 */

@Autonomous
public class AutoColorTestRA extends OpMode {

    DcMotor backLeft;
    DcMotor backRight;
    DcMotor frontLeft;
    DcMotor frontRight;

    Servo thisOne;
    Servo thatOne;

    ColorSensor sensorColor;
    DistanceSensor sensorDistance;

    @Override
    public void init() {

        sensorColor = hardwareMap.get(ColorSensor.class, "sensor_color_distance");
        sensorDistance = hardwareMap.get(DistanceSensor.class, "sensor_color_distance");
        backLeft = hardwareMap.dcMotor.get("BL");
        backRight = hardwareMap.dcMotor.get("BR");
        frontLeft = hardwareMap.dcMotor.get("FL");
        frontRight = hardwareMap.dcMotor.get("FR");
        thisOne = hardwareMap.servo.get("servo1");
        thatOne = hardwareMap.servo.get("servo2");

    }

    @Override
    public void loop() {

        if (sensorColor.red() == 100) {
            if (thisOne.getPosition() < 1) {
                thisOne.setPosition(1);
            } else {
                thisOne.setPosition(0);
            }
        } else if (100 < sensorColor.red() && sensorColor.red() < 200) {
            if (thatOne.getPosition() < 1) {
                thatOne.setPosition(1);
            } else {
                thatOne.setPosition(0);
            }
        }
    }
}

//i don't know what to do or how to do it and i don't want to do it either.