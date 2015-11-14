package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.OpticalDistanceSensor;
import com.qualcomm.robotcore.hardware.TouchSensor;
import com.qualcomm.robotcore.hardware.UltrasonicSensor;

public class LineFollower extends OpMode{
    DcMotor Omni_left;
    DcMotor Omni_right;
    DcMotor DC_left;
    DcMotor DC_right;
    OpticalDistanceSensor opticalDistanceSensor;

    //Find black and white values using calibration program
    //This program follows the left side of the line
    //Power is oscillation amount, base power is speed
    static double BLACKVALUE = 0.02;
    static double WHITEVALUE = 0.25;
    static double EOPDThreshold = 0.5 * (BLACKVALUE + WHITEVALUE);
    static double ultrasonicThreshold = 20;

@Override
    public void init(){
    opticalDistanceSensor = hardwareMap.opticalDistanceSensor.get("sensor_EOPD");
    DC_left = hardwareMap.dcMotor.get("DC_left");
    DC_right = hardwareMap.dcMotor.get("DC_right");
    Omni_left = hardwareMap.dcMotor.get("Omni_left");
    Omni_right = hardwareMap.dcMotor.get("Omni_right");


}

@Override
    public void loop(){
    double reflectance = opticalDistanceSensor.getLightDetected();

            if (reflectance > EOPDThreshold) {
                double value = reflectance - EOPDThreshold;
                DC_left.setPower(0.1+.1*value);
                DC_right.setPower(0.1-.1*value);
                Omni_left.setPower(0.1+.1*value);
                Omni_right.setPower(0.1-.1*value);
            }
            else {
                double value = EOPDThreshold - reflectance;
                DC_left.setPower(0.1-.1*value);
                DC_right.setPower(0.1+.1*value);
                Omni_left.setPower(0.1-.1*value);
                Omni_right.setPower(0.1+.1*value);
            }


//life
        telemetry.addData("Reflectance Value", reflectance);
    }
}