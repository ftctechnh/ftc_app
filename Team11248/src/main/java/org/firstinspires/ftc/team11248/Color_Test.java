package org.firstinspires.ftc.team11248;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

/**
 * Created by Tony_Air on 11/6/17.
 */

@Autonomous( name = "Color Test")
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
        telemetry.addData("05: ", "isBlue: " + robot.isJewelBlue());

    }
}
