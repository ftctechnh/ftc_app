package org.firstinspires.ftc.teamcode.Salsa.Robots;

import org.firstinspires.ftc.teamcode.Salsa.Constants;
import org.firstinspires.ftc.teamcode.Salsa.Hardware.Robot;

/**
 * Created by adityamavalankar on 11/5/18.
 */

public class Asteroid {

    Robot robot = new Robot();
    Constants constants = new Constants();

    public void drive(double leftJoystick, double rightJoystick) {

        robot.leftBack.setPower(leftJoystick);
        robot.leftFront.setPower(leftJoystick);
        robot.rightFront.setPower(rightJoystick);
        robot.rightBack.setPower(rightJoystick);

    }

    public void mecanumDrive(boolean dpad_left, boolean dpad_right) {

        if (dpad_left) {
            robot.leftBack.setPower(-1);
            robot.leftFront.setPower(1);
            robot.rightFront.setPower(-1);
            robot.rightBack.setPower(1);
        }
        else if (dpad_right) {
            robot.leftBack.setPower(1);
            robot.leftFront.setPower(-1);
            robot.rightFront.setPower(1);
            robot.rightBack.setPower(-1);
        }
    }

}
