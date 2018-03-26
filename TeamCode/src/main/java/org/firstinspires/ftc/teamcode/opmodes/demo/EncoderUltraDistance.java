package org.firstinspires.ftc.teamcode.opmodes.demo;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.I2cDeviceSynch;

import org.firstinspires.ftc.teamcode.libraries.hardware.BotHardware;
import org.firstinspires.ftc.teamcode.libraries.hardware.MatbotixUltra;

/**
 * Created by Noah on 3/24/2018.
 */

public class EncoderUltraDistance extends OpMode {
    private BotHardware bot = new BotHardware(this);
    private MatbotixUltra sensor;

    private final int COUNTS = 1000;
    private int startUltra = 0;
    private int startCount = 0;

    private double hmmm = 0.058;

    public void init() {
        bot.init();
        bot.start();

        sensor = new MatbotixUltra(hardwareMap.get(I2cDeviceSynch.class, "ultrafront"), 100);
        sensor.initDevice();
        sensor.startDevice();
    }

    public void start() {
        startUltra = sensor.getReading();
        startCount = BotHardware.Motor.frontLeft.motor.getCurrentPosition();
    }

    public void loop() {
        int reading = sensor.getReading();
        int counts = BotHardware.Motor.frontLeft.motor.getCurrentPosition();

        if(Math.abs(counts - startCount) > COUNTS) {
            bot.setLeftDrive(0);
            bot.setRightDrive(0);
        }
        else {
            bot.setLeftDrive(-0.2);
            bot.setRightDrive(-0.2);
        }
        telemetry.addData("Ultra", reading);
        telemetry.addData("Motor", counts);
        telemetry.addData("Counts/Sensor", (double)Math.abs(reading - startUltra)/(double)(counts - startCount));
    }

    public void stop() {
        bot.stopAll();
    }
}
