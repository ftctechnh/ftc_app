package org.firstinspires.ftc.teamcode.opmodes;


import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.ColorSensor;

import org.firstinspires.ftc.robotcore.external.Telemetry;


@Autonomous(name = "TestColorSensor")
public class ColorSensorTest extends LinearOpMode {
    ColorSensor sensor;


    public void initialize() {
        sensor = hardwareMap.colorSensor.get("color_sensor");
    }

    public void runOpMode() {
        int alpha = sensor.alpha();
        int red = sensor.red();
        int green = sensor.green();
        int blue = sensor.blue();

        telemetry.addData("alpha: ", alpha);
        telemetry.addData("red: ", red);
        telemetry.addData("green: ", green);
        telemetry.addData("blue: ", blue);


        // LED blinkey stuff
        for (int i = 0; i < 3; i++) {
            sensor.enableLed(true);
            sensor.enableLed(false);
        }
        for (int i = 0; i < 3; i++) {
            sensor.enableLed(true);
            sleep(50);
            sensor.enableLed(false);
        }

        for (int i = 0; i < 3; i++) {
            sensor.enableLed(true);
            sensor.enableLed(false);
        }


    }

}
