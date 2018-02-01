package org.firstinspires.ftc.teamcode.opmodes.demo;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.AnalogInput;
import com.qualcomm.robotcore.hardware.UltrasonicSensor;

/**
 * Created by Noah on 1/29/2018.
 */

@Autonomous(name="Ultra Teleop", group="test")
public class UltraTest extends OpMode {
    AnalogInput ultra;

    public void init() {
        ultra = hardwareMap.analogInput.get("ultra");
    }

    public void start() {

    }

    public void loop() {
        telemetry.addData("Max Voltage", ultra.getMaxVoltage());
        telemetry.addData("Voltage", ultra.getVoltage());
        telemetry.addData("Normalized", Math.round((ultra.getVoltage() / ultra.getMaxVoltage()) * 255));
    }

    public void stop() {
        ultra.close();
    }
}
