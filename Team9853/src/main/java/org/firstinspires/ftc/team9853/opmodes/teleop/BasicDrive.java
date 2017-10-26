package org.firstinspires.ftc.team9853.opmodes.teleop;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Gamepad;

import org.chathamrobotics.common.utils.TeleOpMode;
import org.firstinspires.ftc.team9853.Robot9853;

/**
 * Created by carsonstorm on 9/28/2017.
 */

@TeleOp(name = "Basic Drive")
public class BasicDrive extends OpMode {
    private Robot9853 robot;
    private long lastPressA;
    private long lastPressB;
    private long pressBuffer = 500;
    private double scale = 1;

    @Override
    public void init() {
        robot = new Robot9853(hardwareMap, telemetry);
        robot.init();
    }

    @Override
    public void loop() {
        if (isA(gamepad1) && scale < 1) {
            scale = 1 / (Math.pow(scale, -1) - 1);
        }

        if (isB(gamepad1)) {
            scale = 1 / (Math.pow(scale, -1) + 1);
        }

        robot.log.debug("Scale", scale);

        double magnitude = Math.hypot(gamepad1.left_stick_x, gamepad1.left_stick_y) * scale;
        double direction = Math.atan2(gamepad1.left_stick_y, gamepad1.left_stick_x);

        robot.driver.setDrivePower(direction, magnitude, gamepad1.right_stick_x);
    }

    private boolean isA(Gamepad gp) {
        if (gp.a && System.currentTimeMillis() - pressBuffer >= lastPressA) {
            lastPressA = System.currentTimeMillis();
            return true;
        }

        return false;
    }

    private boolean isB(Gamepad gp) {
        if (gp.b && System.currentTimeMillis() - pressBuffer >= lastPressB) {
            lastPressB = System.currentTimeMillis();
            return true;
        };

        return false;
    }
}
