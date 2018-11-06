package org.firstinspires.ftc.teamcode.Salsa.Autonomous;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.Salsa.Robot.Asteroid;
import org.firstinspires.ftc.teamcode.Salsa.Hardware.Robot;
import org.firstinspires.ftc.teamcode.Salsa.Vision.Vuforia;

/**
 * Created by adityamavalankar on 11/4/18.
 */

@TeleOp(name = "Four Wheel Drive Salsa", group = "Salsa")
public class AsteroidDrive extends OpMode {

    Robot robot = new Robot();
    Asteroid asteroid = new Asteroid();
    Vuforia vuforia = new Vuforia();

    @Override
    public void init() {
        robot.initDrivetrain(hardwareMap);
    }

    @Override
    public void loop() {
        asteroid.drive(gamepad1.left_stick_y, gamepad1.right_stick_y);
    }
}
