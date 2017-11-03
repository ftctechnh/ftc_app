package org.firstinspires.ftc.team9853.opmodes.teleop;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.chathamrobotics.common.utils.Controller;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.team9853.Robot9853;

/**
 * Created by carsonstorm on 11/3/2017.
 */
@TeleOp(name = "CompDrive")
public class CompDrive extends OpMode {
    private Robot9853 robot;
    private Controller controller1;
    private Controller controller2;

    @Override
    public void init() {
        robot = Robot9853.build(this);
        controller1 = new Controller(gamepad1);
        controller2 = new Controller(gamepad2);

        robot.init();
    }

    @Override
    public void start() {
        super.start();
        robot.start();
    }

    @Override
    public void loop() {
        controller1.update(); controller2.update();



        robot.driveWithControls(gamepad1);
    }
}
