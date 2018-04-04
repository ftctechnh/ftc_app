package org.firstinspires.ftc.teamcode.opmodes.demo;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.AnalogInput;
import com.qualcomm.robotcore.hardware.I2cDeviceSynch;
import com.qualcomm.robotcore.hardware.UltrasonicSensor;

import org.firstinspires.ftc.teamcode.libraries.hardware.MatbotixUltra;

/**
 * Created by Noah on 1/29/2018.
 */

@Autonomous(name="Ultra Teleop", group="test")
//@Disabled
public class UltraTest extends OpMode {
    private MatbotixUltra ultra;
    private MatbotixUltra ultraBack;
    private MatbotixUltra ultraLeft;
    private MatbotixUltra ultraRight;

    private MatbotixUltra[] ray;

    public void init() {
        try {
            ultra = new MatbotixUltra(hardwareMap.get(I2cDeviceSynch.class, "ultrafront"), 100);
            ultraBack = new MatbotixUltra(hardwareMap.get(I2cDeviceSynch.class, "ultraback"), 100);
            ultraRight = new MatbotixUltra(hardwareMap.get(I2cDeviceSynch.class, "ultraright"), 100);
            ultraLeft = new MatbotixUltra(hardwareMap.get(I2cDeviceSynch.class, "ultraleft"), 100);
        }
        catch (Exception e) { /* hmmmm */ }

        ray = new MatbotixUltra[] {ultraLeft, ultra, ultraRight, ultraBack};

        for(MatbotixUltra i : ray) {
            if(i != null) {
                i.initDevice();
                i.startDevice();
            }
        }
    }

    public void start() {

    }

    public void loop() {
        for (MatbotixUltra i : ray) if(i != null) telemetry.addData(i.getDevice().getDeviceName(), i.getReading());
    }

    public void stop() {
        for (MatbotixUltra i : ray) if(i != null) i.stopDevice();
    }
}
