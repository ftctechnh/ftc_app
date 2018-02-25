package org.firstinspires.ftc.teamcode.opmodes.demo;

import com.qualcomm.hardware.adafruit.AdafruitI2cColorSensor;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.I2cDeviceSynch;

import org.firstinspires.ftc.teamcode.libraries.hardware.APDS9960;

import java.util.Arrays;

/**
 * Created by Noah on 2/21/2018.
 */

@Autonomous(name = "Color", group = "test")
public class Color extends OpMode {
    APDS9960 frontDist;
    APDS9960.Config distConfig = new APDS9960.Config();
    APDS9960.Config backDistConfig = new APDS9960.Config();
    APDS9960 backDist;
    ColorSensor frontColor;
    ColorSensor backColor;

    public void init() {
        frontDist = new APDS9960(distConfig, hardwareMap.get(I2cDeviceSynch.class, "reddist"), true, APDS9960.Config.DistGain.GAIN_8X);
        backDist = new APDS9960(distConfig, hardwareMap.get(I2cDeviceSynch.class, "bluedist"), true, APDS9960.Config.DistGain.GAIN_8X);
        frontDist.initDevice();
        backDist.initDevice();

        frontColor = hardwareMap.get(ColorSensor.class, "fc");
        backColor = hardwareMap.get(ColorSensor.class, "bc");
    }

    public void start() {
        frontDist.startDevice();
        backDist.startDevice();

        frontColor.enableLed(true);
        backColor.enableLed(true);
    }

    public void loop() {
        //telemetry.addData("Distance", dist.getLinearizedDistance(false));
        telemetry.addData("Front Red", frontColor.red());
        telemetry.addData("Front Green", frontColor.green());
        telemetry.addData("Front Blue", frontColor.blue());
        telemetry.addData("Back Red", backColor.red());
        telemetry.addData("Back Green", backColor.green());
        telemetry.addData("Back Blue", backColor.blue());
        telemetry.addData("APDS Front", Arrays.toString(frontDist.getColor()));
        telemetry.addData("APDS Back", Arrays.toString(backDist.getColor()));
    }

    public void stop() {
        frontColor.enableLed(false);
        backColor.enableLed(false);
    }
}
