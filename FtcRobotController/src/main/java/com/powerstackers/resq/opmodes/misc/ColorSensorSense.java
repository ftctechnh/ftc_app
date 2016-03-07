package com.powerstackers.resq.opmodes.misc;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DeviceInterfaceModule;
import com.qualcomm.robotcore.hardware.TouchSensor;

import org.swerverobotics.library.ClassFactory;

/**
 * Created by Derek on 12/12/2015.
 */
public class ColorSensorSense extends OpMode {

    ColorSensor colorSensor;
    TouchSensor touchSensor;
    DeviceInterfaceModule cdim;

    //Color Values
    float hsvValues[] = {0, 0, 0};
    final float values[] = hsvValues;


    @Override
    public void init() {
        hardwareMap.logDevices();
        cdim = hardwareMap.deviceInterfaceModule.get("dim");
        colorSensor = ClassFactory.createSwerveColorSensor(this, this.hardwareMap.colorSensor.get("colorSensor"));
        touchSensor = hardwareMap.touchSensor.get("touchSensor");
        colorSensor.enableLed(true);

    }

    @Override
    public void loop() {


        telemetry.addData("Clear", colorSensor.alpha());
        telemetry.addData("Red  ", colorSensor.red());
        telemetry.addData("Green", colorSensor.green());
        telemetry.addData("Blue ", colorSensor.blue());
        telemetry.addData("Hue", hsvValues[0]);
        telemetry.addData("button", gamepad1.start);

    }
}
