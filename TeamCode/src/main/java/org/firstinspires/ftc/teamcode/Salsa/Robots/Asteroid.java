package org.firstinspires.ftc.teamcode.Salsa.Robots;

import org.firstinspires.ftc.teamcode.Salsa.Hardware.Robot;

/**
 * Created by adityamavalankar on 11/5/18.
 */

public class Asteroid {

    Robot robot = new Robot();

    public void drive(double leftJoystick, double rightJoystick) {

        robot.leftBack.setPower(leftJoystick);
        robot.leftFront.setPower(leftJoystick);
        robot.rightFront.setPower(rightJoystick);
        robot.rightBack.setPower(rightJoystick);

    }
}
