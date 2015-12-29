package com.powerstackers.resq.opmodes;

import android.app.Activity;
import android.graphics.Color;
import android.hardware.Sensor;
import android.view.View;

import com.qualcomm.ftcrobotcontroller.R;
import com.qualcomm.hardware.ModernRoboticsI2cColorSensor;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DeviceInterfaceModule;
import com.qualcomm.robotcore.hardware.TouchSensor;

import org.swerverobotics.library.ClassFactory;
import org.swerverobotics.library.interfaces.TeleOp;

/**
 * Created by Derek on 12/12/2015.
 */
@TeleOp
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
