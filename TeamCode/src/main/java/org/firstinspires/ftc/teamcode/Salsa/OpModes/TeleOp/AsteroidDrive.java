package org.firstinspires.ftc.teamcode.Salsa.OpModes.TeleOp;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.Salsa.OpModes.SalsaOpMode;

/**
 * Created by adityamavalankar on 11/4/18.
 */

@TeleOp(name = "Four Wheel Drive Salsa", group = "Salsa")
public class AsteroidDrive extends SalsaOpMode {

    /**
     * All of this is an implementation of the SalsaOpMode class made before. The benefit of something
     * made like this is that we reqire no objects to be created, or any imports, aside from the SalsaOpMode
     * objects
     */
    @Override
    public void init() {
        robot.initDrivetrain(hardwareMap);
    }

    @Override
    public void loop() {
        drive(gamepad1.left_stick_y, gamepad1.right_stick_y);
    }
}
