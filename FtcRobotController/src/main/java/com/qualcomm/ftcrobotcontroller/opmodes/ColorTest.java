package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.ColorSensor;

/**
 * Created by Carlos on 11/24/2015.
 */
public class ColorTest extends LinearOpMode {

    ColorSensor colorSensor;

    @Override
    public void runOpMode() throws InterruptedException {

        colorSensor = hardwareMap.colorSensor.get("sensorRGB");

        waitForStart();;

        colorSensor.enableLed(true);
        sleep(1000);
        colorSensor.enableLed(false);

        while(opModeIsActive()){
            telemetry.addData("Blue: ", colorSensor.blue());
            telemetry.addData("Red: ", colorSensor.red());
        }

    }

    public void sleep(long milliseconds) throws InterruptedException {
        Thread.sleep(milliseconds);
    }
}
