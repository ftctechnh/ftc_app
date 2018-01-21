package org.firstinspires.ftc.teamcode.opmodes.demo;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.libraries.hardware.BotHardware;

/**
 * Created by Noah on 12/30/2017.
 */

@TeleOp(name="Motor Hold Test")
@Disabled
public class MotorHoldTest extends OpMode {
    BotHardware bot = new BotHardware(this);
    private static final int MAX_COUNT = 100;

    public void init() {
        bot.init();
    }

    public void start() {

    }

    public void loop() {
        bot.setLiftMotors((int)(gamepad1.left_trigger * MAX_COUNT));
    }

    public void stop() {
        bot.stopAll();
    }
}
