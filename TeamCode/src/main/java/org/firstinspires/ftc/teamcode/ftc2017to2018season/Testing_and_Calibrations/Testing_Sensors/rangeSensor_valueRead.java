package org.firstinspires.ftc.teamcode.ftc2017to2018season.Testing_and_Calibrations.Testing_Sensors;

import com.qualcomm.hardware.modernrobotics.ModernRoboticsI2cRangeSensor;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;


/**
 * Created by Aditya!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
 * on 11/18/17.
 */


@TeleOp(name = "servo read position")
@Disabled
public class rangeSensor_valueRead extends OpMode {

    ModernRoboticsI2cRangeSensor rangeSensor;


    @Override
    public void init() {

        rangeSensor = hardwareMap.get(ModernRoboticsI2cRangeSensor.class, "rangeSensor");
        rangeSensor.enableLed(true);

    }

    @Override
    public void loop() {

        telemetry.addData("Distance CM Ultrasonic", rangeSensor.cmUltrasonic());
        telemetry.addData("Distance CM Optical", rangeSensor.cmOptical());
        telemetry.update();
    }
}