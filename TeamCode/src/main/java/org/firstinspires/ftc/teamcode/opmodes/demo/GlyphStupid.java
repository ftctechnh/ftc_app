package org.firstinspires.ftc.teamcode.opmodes.demo;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.teamcode.libraries.hardware.BotHardware;

import java.util.concurrent.TimeUnit;

/**
 * Created by Noah on 3/23/2018.
 */

@Autonomous(name="Glyph Hunt")
@Disabled
public class GlyphStupid extends OpMode{
    BotHardware bot = new BotHardware(this);
    private static final int FOUND_VELOCITY = 790;

    public void init() {
        bot.init();
        BotHardware.Motor.suckRight.motor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        BotHardware.Motor.suckLeft.motor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        BotHardware.Motor.suckLeft.motor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        BotHardware.Motor.suckRight.motor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
    }

    public void start() {
        bot.setSuckRight(1);
        bot.setSuckLeft(1);

        try { TimeUnit.SECONDS.sleep(1); } catch (Exception e) {}
    }

    public void loop() {
        double lastRight = BotHardware.Motor.suckRight.motor.getVelocity(AngleUnit.DEGREES);
        double lastLeft = BotHardware.Motor.suckLeft.motor.getVelocity(AngleUnit.DEGREES);

        if(lastLeft < 100 || lastLeft < 100) {
            bot.setRightDrive(0);
            bot.setLeftDrive(0);
            bot.setSuckLeft(0);
            bot.setSuckRight(0);
            requestOpModeStop();
        }
        else if(lastLeft < FOUND_VELOCITY) {
            bot.setLeftDrive(-0.2f);
            bot.setRightDrive(0.1f);
        }
        else if(lastRight < FOUND_VELOCITY) {
            bot.setLeftDrive(0.1f);
            bot.setRightDrive(-0.2f);
        }
        else {
            bot.setLeftDrive(0);
            bot.setRightDrive(0);
        }
    }

    public void stop() {
        bot.stopAll();
    }
}
