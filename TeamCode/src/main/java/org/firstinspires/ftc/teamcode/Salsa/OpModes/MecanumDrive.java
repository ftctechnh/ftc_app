package org.firstinspires.ftc.teamcode.Salsa.OpModes;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import org.firstinspires.ftc.teamcode.Salsa.Robots.Asteroid;

/**
 * Created by adityamavalankar on 11/4/18.
 */

@TeleOp(name = "Mecanum Drive", group = "Avocado")
public class MecanumDrive extends OpMode {

    public Asteroid asteroid = new Asteroid();

    @Override
    public void init(){
        asteroid.robot.initDrivetrain(hardwareMap);
    }

    @Override
    public void loop() {
        asteroid.drive(gamepad1.left_stick_y, gamepad1.right_stick_y);
        asteroid.mecanumDrive(gamepad1.dpad_left, gamepad1.dpad_right);
    }
}
