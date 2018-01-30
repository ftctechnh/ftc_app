package org.firstinspires.ftc.teamcode.opmodes.demo;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.UltrasonicSensor;

/**
 * Created by Noah on 1/29/2018.
 */

@Autonomous(name="Ultra Teleop", group="test")
public class UltraTest extends OpMode {
    UltrasonicSensor sensor;

    public void init() {
        sensor = hardwareMap.ultrasonicSensor.get("ultra");
    }

    public void start() {

    }

    public void loop() {
        telemetry.addData("Ultra", sensor.getUltrasonicLevel());
        telemetry.addData("Ultra Status", sensor.status());
    }

    public void stop() {

    }
}
