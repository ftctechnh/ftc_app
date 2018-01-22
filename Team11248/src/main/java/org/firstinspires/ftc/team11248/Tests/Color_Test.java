package org.firstinspires.ftc.team11248.Tests;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.team11248.Robot11248;

/**
 * Created by Tony_Air on 11/6/17.
 */

@Autonomous( name = "Color Test")
@Disabled
public class Color_Test extends OpMode{

    Robot11248 robot;
    @Override
    public void init() {
        robot = new Robot11248(hardwareMap, telemetry);
        robot.init();
        robot.activateColorSensors();
    }

    @Override
    public void start(){
        robot.lowerJewelArm();
    }

    @Override
    public void loop() {
        telemetry.addData("00: ", "isBlue: " + robot.jewelColor.isBlue());
        telemetry.addData("01: ", "Color Number: " + robot.jewelColor.getColorNumber());
        telemetry.addData("02: ", "red: " + robot.jewelColor.red());
        telemetry.addData("03: ", "green: " + robot.jewelColor.green());
        telemetry.addData("04: ", "blue: " + robot.jewelColor.blue());
    }
}
