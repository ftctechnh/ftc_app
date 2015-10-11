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

    static double BLACKVALUE = 0;
    static double WHITEVALUE = 0.4;
    static double EOPDThreshold = 0.5 * (BLACKVALUE + WHITEVALUE);

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

        if (reflectance >= EOPDThreshold) {
            DC_left.setPower(0.02);
            Omni_left.setPower(0.02);
            DC_right.setPower(0.1);
            Omni_right.setPower(0.1);
        } else {
            DC_left.setPower(0.1);
            Omni_left.setPower(0.1);
            DC_right.setPower(0.02);
            Omni_right.setPower(0.02);
        }

        telemetry.addData("Reflectance Value", reflectance);
    }
}