package org.firstinspires.ftc.team9853.opmodes.teleop;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.chathamrobotics.common.utils.TeleOpMode;
import org.firstinspires.ftc.team9853.Robot9853;

/**
 * Created by carsonstorm on 9/28/2017.
 */

@TeleOp(name = "Basic Drive")
public class BasicDrive extends OpMode {
    private Robot9853 robot;
    @Override
    public void init() {
        robot = new Robot9853(hardwareMap, telemetry);
        robot.init();
    }

    @Override
    public void loop() {
        robot.driveWithControls(gamepad1);
    }
}
