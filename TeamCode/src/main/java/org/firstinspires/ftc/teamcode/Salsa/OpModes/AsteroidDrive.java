package org.firstinspires.ftc.teamcode.Salsa.OpModes;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.Salsa.Hardware.CRServo;
import org.firstinspires.ftc.teamcode.Salsa.Hardware.ColorSensor;
import org.firstinspires.ftc.teamcode.Salsa.Robots.Asteroid;
import org.firstinspires.ftc.teamcode.Salsa.Hardware.Robot;
import org.firstinspires.ftc.teamcode.Salsa.Vision.Vuforia;

/**
 * Created by adityamavalankar on 11/4/18.
 */

@TeleOp(name = "Four Wheel Drive Salsa", group = "Salsa")
public class AsteroidDrive extends OpMode {

    public Asteroid asteroid = new Asteroid();

    @Override
    public void init() {
        asteroid.robot.initDrivetrain(hardwareMap);
    }

    @Override
    public void loop() {
        asteroid.drive(gamepad1.left_stick_y, gamepad1.right_stick_y);
    }
}
