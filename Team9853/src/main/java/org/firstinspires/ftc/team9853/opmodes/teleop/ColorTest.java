package org.firstinspires.ftc.team9853.opmodes.teleop;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.ColorSensor;

import org.chathamrobotics.common.opmode.TeleOpTemplate;
import org.firstinspires.ftc.team9853.Robot9853;

/**
 * Created by carsonstorm on 11/16/2017.
 */

@TeleOp(name = "Color Test", group = "Test")
public class ColorTest extends TeleOpTemplate<Robot9853> {
    private ColorSensor color;

    @Override
    public void init() {
//        robot = Robot9853.build(this);
//        robot.init();

//        color = new ModernRoboticsColorSensor(hardwareMap.i2cDevice.get("JewelColor"), new I2cAddr(0x3c));
//        color.enableLed(true);
        color = hardwareMap.colorSensor.get("Color");
        color.enableLed(true);
    }

    @Override
    public void loop() {
        telemetry.addLine(String.format("(%d, %d, %d, %d)", color.alpha(), color.red(), color.green(), color.blue()));
    }
}
