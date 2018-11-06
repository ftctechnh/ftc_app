package org.firstinspires.ftc.teamcode.Salsa.Robot;

import org.firstinspires.ftc.teamcode.Salsa.Hardware.Robot;

/**
 * Created by adityamavalankar on 11/5/18.
 */

public class Asteroid {

    Robot robot = new Robot();

    public void drive(double leftJoystick, double rightJoystick) {

        robot.hardware.leftBack.setPower(leftJoystick);
        robot.hardware.leftFront.setPower(leftJoystick);
        robot.hardware.rightFront.setPower(rightJoystick);
        robot.hardware.rightBack.setPower(rightJoystick);

    }
}
