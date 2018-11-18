package org.firstinspires.ftc.teamcode.Salsa.OpModes.TeleOp;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.Salsa.Methods.SalsaOpMode;

/**
 * Created by adityamavalankar on 11/4/18.
 */

@TeleOp(name = "Four Wheel Drive Salsa", group = "Salsa")
public class AsteroidDrive extends SalsaOpMode {

    @Override
    public void init() {
        robot.initDrivetrain(hardwareMap);
    }

    @Override
    public void loop() {
        drive(gamepad1.left_stick_y, gamepad1.right_stick_y);
    }
}
