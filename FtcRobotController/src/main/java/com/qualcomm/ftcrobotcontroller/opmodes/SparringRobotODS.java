package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.OpticalDistanceSensor;
import com.qualcomm.robotcore.hardware.*;

//Replace with robot we are using
import com.technicbots.SparringBot;

public class SparringRobotODS extends OpMode {
    DcMotor DC_left;
    DcMotor DC_right;
    DcMotor Omni_left;
    DcMotor Omni_right;
    OpticalDistanceSensor opticalDistanceSensor;

    double EOPDThreshold = 0.5;

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
            DC_left.setPower(0.3);
            Omni_left.setPower(0.3);
            DC_right.setPower(0.5);
            Omni_right.setPower(0.5);
        } else {
            DC_left.setPower(0.5);
            Omni_left.setPower(0.5);
            DC_right.setPower(0.3);
            Omni_right.setPower(0.3);
        }

        telemetry.addData("Reflectance Value", reflectance);
    }
}