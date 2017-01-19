package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.ColorSensor;

@TeleOp
public class ColorTest extends OpMode {
    ColorSensor colorSensor;
    public void init() {
        colorSensor = hardwareMap.colorSensor.get("colorSensor");
    }

    public void loop() {
        telemetry.addData("ASDFASDFASDFASD", colorSensor.red());
    }
}
