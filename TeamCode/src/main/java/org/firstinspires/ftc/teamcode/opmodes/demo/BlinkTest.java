package org.firstinspires.ftc.teamcode.opmodes.demo;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.libraries.hardware.BlinkyEffect;
import org.firstinspires.ftc.teamcode.libraries.hardware.BotHardware;

/**
 * Created by Noah on 3/6/2018.
 */

@TeleOp(name="Blink Test")
public class BlinkTest extends OpMode {
    BotHardware bot = new BotHardware(this);

    public void init() {
        bot.init();
        bot.start();
    }

    public void start() {
        bot.setLights(0.5);
    }

    public void loop() {

    }

    public void stop() {
        bot.stopAll();
    }
}
