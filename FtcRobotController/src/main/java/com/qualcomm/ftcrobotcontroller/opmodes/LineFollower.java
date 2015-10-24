package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.OpticalDistanceSensor;

public class LineFollower extends OpMode {
    DcMotor DC_left;
    DcMotor DC_right;
    DcMotor Omni_left;
    DcMotor Omni_right;
    OpticalDistanceSensor opticalDistanceSensor;

    //Find black and white values using calibration program
    //This program follows the left side of the line
    //Power is oscillation amount, base power is speed
    static double BLACKVALUE = 0.02;
    static double WHITEVALUE = 0.64;
    static double EOPDThreshold = 0.5 * (BLACKVALUE + WHITEVALUE);
    static double POWER = 0.3;
    static double BASEPOWER = 0.2;
    @Override
    public void init() {
        DC_left = hardwareMap.dcMotor.get("DC_left");
        DC_right = hardwareMap.dcMotor.get("DC_right");
        Omni_left = hardwareMap.dcMotor.get("Omni_left");
        Omni_right = hardwareMap.dcMotor.get("Omni_right");
        opticalDistanceSensor = hardwareMap.opticalDistanceSensor.get("sensor_EOPD");

    }

    public void loop() {

        double reflectance = opticalDistanceSensor.getLightDetected();
        double value;

        if (reflectance > EOPDThreshold) {
            value = reflectance-EOPDThreshold;
            DC_left.setPower ((BASEPOWER+POWER*value));
            Omni_left.setPower((BASEPOWER+POWER*value));
            DC_right.setPower((BASEPOWER-POWER*value));
            Omni_right.setPower((BASEPOWER-POWER*value));
        } else {
            value = EOPDThreshold-reflectance;
            DC_left.setPower((BASEPOWER-POWER*value));
            Omni_left.setPower((BASEPOWER-POWER*value));
            DC_right.setPower((BASEPOWER+POWER*value));
            Omni_right.setPower((BASEPOWER+POWER*value));
        }

        telemetry.addData("Reflectance Value", reflectance);
        telemetry.addData("Value", value);
    }
}