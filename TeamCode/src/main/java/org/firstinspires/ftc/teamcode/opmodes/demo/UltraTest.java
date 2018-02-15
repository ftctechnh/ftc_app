package org.firstinspires.ftc.teamcode.opmodes.demo;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.AnalogInput;
import com.qualcomm.robotcore.hardware.I2cDeviceSynch;
import com.qualcomm.robotcore.hardware.UltrasonicSensor;

import org.firstinspires.ftc.teamcode.libraries.hardware.MatbotixUltra;

/**
 * Created by Noah on 1/29/2018.
 */

@Autonomous(name="Ultra Teleop", group="test")
public class UltraTest extends OpMode {
    private MatbotixUltra ultra;
    private MatbotixUltra ultraBack;

    public void init() {
        ultra = new MatbotixUltra(hardwareMap.get(I2cDeviceSynch.class, "ultrafront"), 100);
        ultraBack = new MatbotixUltra(hardwareMap.get(I2cDeviceSynch.class, "ultraback"), 100);
        ultra.initDevice();
        ultraBack.initDevice();
    }

    public void start() {
        ultra.startDevice();
        ultraBack.startDevice();
    }

    public void loop() {
        telemetry.addData("Ultra Front", ultra.getReading());
        telemetry.addData("Ultra Back", ultraBack.getReading());
    }

    public void stop() {
        ultra.stopDevice();
        ultraBack.stopDevice();
    }
}
