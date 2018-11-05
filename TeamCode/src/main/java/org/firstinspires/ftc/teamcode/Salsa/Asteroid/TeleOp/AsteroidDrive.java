package org.firstinspires.ftc.teamcode.Salsa.Asteroid.TeleOp;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import org.firstinspires.ftc.teamcode.Salsa.Asteroid.*;

/**
 * Created by adityamavalankar on 11/4/18.
 */

@TeleOp(name = "Four Wheel Drive Salsa", group = "Salsa")
public class AsteroidDrive extends OpMode {

    Robot robot = new Robot();
    TeleOpFunctions teleOpFunctions = new TeleOpFunctions();

    @Override
    public void init() {
        robot.initDrivetrain(hardwareMap);
    }

    @Override
    public void loop() {
        teleOpFunctions.straightDrive();
    }
}
