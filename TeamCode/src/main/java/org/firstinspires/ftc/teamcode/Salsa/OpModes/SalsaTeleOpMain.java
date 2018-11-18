package org.firstinspires.ftc.teamcode.Salsa.OpModes;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.Salsa.Hardware.Robot;
import org.firstinspires.ftc.teamcode.Salsa.Robots.Asteroid;

/**
 * Created by adityamavalankar on 11/17/18.
 */

@TeleOp(name = "Salsa TeleOp Main")
public class SalsaTeleOpMain extends OpMode {

    Asteroid robot;

    @Override
    public void init() {
        robot.initDrivetrain(hardwareMap);
        robot.initMotors(hardwareMap);
        robot.initServos(hardwareMap);
    }

    @Override
    public void loop() {

        robot.drive();
        robot.liftHanger();
    }

}
