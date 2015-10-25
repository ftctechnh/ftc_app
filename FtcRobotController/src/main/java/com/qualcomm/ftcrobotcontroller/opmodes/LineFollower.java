package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.OpticalDistanceSensor;
import com.qualcomm.robotcore.hardware.TouchSensor;
import com.qualcomm.robotcore.hardware.UltrasonicSensor;

public class LineFollower extends OpMode{
    DcMotor DC_left;
    DcMotor DC_right;
    OpticalDistanceSensor opticalDistanceSensor;
    UltrasonicSensor ultrasonicSensor;

    //Find black and white values using calibration program
    //This program follows the left side of the line
    //Power is oscillation amount, base power is speed
    static double BLACKVALUE = 0.02;
    static double WHITEVALUE = 0.64;
    static double EOPDThreshold = 0.5 * (BLACKVALUE + WHITEVALUE);
    static double POWER = 0.3;
    static double BASEPOWER = 0.2;
    static double ultrasonicThreshold = 5;

@Override
    public void init(){
    ultrasonicSensor = hardwareMap.ultrasonicSensor.get("sonic");
}

@Override
    public void loop(){
//Ultrasonic sensor can only be used in Legacy Module Ports 4 and 5
        double reflectance = opticalDistanceSensor.getLightDetected();
        double value;
        double distance;

    ultrasonicSensor.getUltrasonicLevel();
    distance = ultrasonicSensor.getUltrasonicLevel();
    value = reflectance-EOPDThreshold;

    if (distance > ultrasonicThreshold){
            if (reflectance > EOPDThreshold) {
                DC_left.setPower ((BASEPOWER+POWER*value));
                DC_right.setPower((BASEPOWER-POWER*value));
            } else {
                value = EOPDThreshold-reflectance;
                DC_left.setPower((BASEPOWER-POWER*value));
                DC_right.setPower((BASEPOWER+POWER*value));
            }
        }
        else {
            DC_left.setPower(0);
            DC_right.setPower(0);
        }

        telemetry.addData("Reflectance Value", reflectance);
        telemetry.addData("Value", value);
        telemetry.addData("Ultrasonic Value: ", distance);
    }
}