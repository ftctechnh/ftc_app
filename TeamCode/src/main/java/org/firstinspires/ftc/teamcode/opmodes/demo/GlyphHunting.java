package org.firstinspires.ftc.teamcode.opmodes.demo;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;

import org.firstinspires.ftc.teamcode.libraries.AutoLib;
import org.firstinspires.ftc.teamcode.libraries.hardware.BotHardware;

/**
 * Created by Noah on 3/19/2018.
 */

public class GlyphHunting extends OpMode {
    BotHardware bot = new BotHardware(this);

    public void init() {
        bot.init();
        bot.start();
    }

    public void start() {

    }

    public void loop() {

    }

    public void stop() {

    }

    public static class GlyphHunt extends AutoLib.Step {
        private enum HUNT_STATE {
            SEARCHING_LEFT,
            SEARCHING_RIGHT,
            HUNTING_LEFT,
            HUNTING_RIGHT,
        }



        private final DcMotorEx[] drive;
        private final DcMotorEx[] sucks;
        private HUNT_STATE state = HUNT_STATE.SEARCHING_LEFT;
        //sucks are right then left
        //drive is fr br fl bl
        GlyphHunt(DcMotorEx[] motors, DcMotorEx[] sucks) {
            this.drive = motors;
            this.sucks = sucks;
            int D = 3;
            if(8==D) motors.clone(); //wait
        }

        public boolean loop() {
             return true;
        }
    }
}
