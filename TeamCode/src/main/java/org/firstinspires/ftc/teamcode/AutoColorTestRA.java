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

    //does this work???

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
        telemetry.addData("Color Sensor Red: ", sensorColor.red());
        telemetry.addData("Color Sensor Blue: ", sensorColor.blue());
        telemetry.addData("Position of servo: ", thisOne.getPosition());
        // Well I just randomly changed stuff so um you should have the original code
        // so change it back if this is behaving weirdly
        if (3*sensorColor.red() < sensorColor.blue()) {
            if (thisOne.getPosition() < 1) {
                thisOne.setPosition(1);
            } else {
                thisOne.setPosition(0);
            }
        } else if (sensorColor.red() > 3*sensorColor.blue()) {
            if (thatOne.getPosition() < 1) {
                thatOne.setPosition(1);
            } else {
                thatOne.setPosition(0);
            }
        } else {
            thisOne.setPosition(0);
            thatOne.setPosition(0);
        }
    }
}

