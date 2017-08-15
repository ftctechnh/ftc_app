package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.TouchSensor;

import org.firstinspires.ftc.robotcontroller.external.samples.HardwarePushbot;

/**
 * Created by thund on 8/14/2017.
 */

public class RayTouchExample extends OpMode {
    HardwareSensorMap robot   = new HardwareSensorMap();   // Use a Pushbot's hardware

    @Override
    public void init() {
        robot.init(hardwareMap);
    }

    @Override
    public void loop() {
        telemetry.addData("isPressed",String.valueOf(robot.ts.isPressed()));

    }
}
