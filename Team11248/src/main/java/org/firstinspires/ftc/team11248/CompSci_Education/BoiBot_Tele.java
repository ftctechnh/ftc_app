package org.firstinspires.ftc.team11248.CompSci_Education;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

/**
 * Created by Tony_Air on 12/18/17.
 */

@TeleOp(name = "Boi")
public class BoiBot_Tele extends OpMode{

    BoiBot robot;

    @Override
    public void init() {
        robot = new BoiBot(hardwareMap, telemetry);
        robot.init();
    }

    @Override
    public void loop() {
        robot.drive(gamepad1.left_stick_x, -gamepad1.left_stick_y, gamepad1.right_stick_x);

        if (gamepad1.x) robot.toggle_left();
        if (gamepad1.b) robot.toggle_right();

    }
}
