package org.firstinspires.ftc.teamcode.test;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.HolonomicRobot;

/**
 * Created by 292486 on 2/3/2017.
 */
@TeleOp
public class ColorSensorReading extends OpMode {

    HolonomicRobot robot = new HolonomicRobot();

    @Override
    public void init() {
        robot.init(hardwareMap);
    }

    @Override
    public void loop() {
        telemetry.addData("RGB: ", "%d %d %d", robot.sensorRgb.red(), robot.sensorRgb.green(), robot.sensorRgb.blue());
        telemetry.update();
    }
}
