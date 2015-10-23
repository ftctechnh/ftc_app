package com.qualcomm.ftcrobotcontroller.FTC9926.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.LightSensor;
/**
 * Created by jackhogan on 9/27/2015.
 */
public class LightMove extends OpMode {

    DcMotor Motor3;
    DcMotor Motor4;
    LightSensor reflectedLight;

    public void init() {
        Motor3 = hardwareMap.dcMotor.get("M3");
        Motor4 = hardwareMap.dcMotor.get("M4");
        Motor3.setDirection(DcMotor.Direction.FORWARD);
        Motor4.setDirection(DcMotor.Direction.FORWARD);
        reflectedLight = hardwareMap.lightSensor.get("light_sensor");

    }

    public void loop() {
        int reflection = 0;
        reflection = reflectedLight.getLightDetectedRaw();
        Motor3.setPower(1);
        Motor4.setPower(1);
        if (reflection <= 50) {
            Motor3.setPower(0);
            Motor4.setPower(0);
        }
        else{
            Motor3.setPower(1);
            Motor4.setPower(1);
        }
    }

    public void stop() {}

}
