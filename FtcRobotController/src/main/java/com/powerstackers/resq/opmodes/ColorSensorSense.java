package com.powerstackers.resq.opmodes;

import android.app.Activity;
import android.graphics.Color;
import android.hardware.Sensor;
import android.view.View;

import com.qualcomm.ftcrobotcontroller.R;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DeviceInterfaceModule;
import com.qualcomm.robotcore.hardware.TouchSensor;

/**
 * Created by Derek on 12/12/2015.
 */
public class ColorSensorSense extends OpMode {

    ColorSensor colorSensor;
    DeviceInterfaceModule cdim;
    //LED led;
    TouchSensor t;

    float hsvValues[] = {0, 0, 0};
    final float values[] = hsvValues;
    //final View relativeLayout = ((Activity) hardwareMap.appContext).findViewById(R.id.RelativeLayout);


    @Override
    public void init() {
        hardwareMap.logDevices();
        cdim = hardwareMap.deviceInterfaceModule.get("dim");
        colorSensor = hardwareMap.colorSensor.get("mr");
        //relativeLayout.setBackgroundColor(Color.HSVToColor(0xff, values));
        //led = hardwareMap.led.get("led");
        t = hardwareMap.touchSensor.get("t");
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
