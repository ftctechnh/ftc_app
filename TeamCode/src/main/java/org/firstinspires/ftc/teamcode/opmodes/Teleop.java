package org.firstinspires.ftc.teamcode.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.libraries.FilterLib;
import org.firstinspires.ftc.teamcode.opmodes.hardware.BotHardware;

/**
 * Created by Noah on 10/27/2017.
 * Teleop!
 */

@TeleOp(name="Teleop")
public class Teleop extends OpMode {
    private static final float slowFactor = 0.5f;

    BotHardware bot = new BotHardware(this);
    private boolean grabberClosed = false;
    private boolean lastA = false;
    private boolean robotSlow = false;
    private boolean lastBumper = false;

    public void init() {
        bot.init();
    }

    public void start() {

    }

    public void loop() {
        if(gamepad1.left_trigger > 0) bot.setLift(-1.0);
        else if(gamepad1.right_trigger > 0) bot.setLift(1.0);
        else bot.setLift(0);

        if(gamepad1.a && !lastA) robotSlow = !robotSlow;
        lastA = gamepad1.a;

        if((gamepad1.left_bumper || gamepad1.right_bumper) && !lastBumper) grabberClosed = !grabberClosed;
        lastBumper = gamepad1.left_bumper || gamepad1.right_bumper;

        if(robotSlow) {
            bot.setLeftDrive(gamepad1.left_stick_y * slowFactor);
            bot.setRightDrive(gamepad1.right_stick_y * slowFactor);
        }
        else {
            bot.setLeftDrive(gamepad1.left_stick_y);
            bot.setRightDrive(gamepad1.right_stick_y);
        }

        bot.setLights(!robotSlow);

        if(gamepad1.b) bot.dropStick();
        else bot.liftStick();

        if(gamepad1.x) bot.dropGary();
        else bot.liftGary();

        telemetry.addData("Grabber", grabberClosed);
        telemetry.addData("Robot Slow", robotSlow);
    }

    public void stop() {
        bot.stopAll();
    }
}
