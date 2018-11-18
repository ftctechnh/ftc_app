package org.firstinspires.ftc.teamcode.Salsa.Methods;

import android.graphics.Path;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.teamcode.Salsa.Constants;
import org.firstinspires.ftc.teamcode.Salsa.Hardware.Robot;

/**
 * Created by adityamavalankar on 11/17/18.
 */

public abstract class SalsaOpMode extends OpMode {

    public Robot robot = new Robot();

    public Constants constants;

    public void drive() {

        drive(this.gamepad1.left_stick_y, this.gamepad1.right_stick_y);
    }

    public void drive(double leftJoystick, double rightJoystick) {

        robot.leftBack.setPower(leftJoystick);
        robot.leftFront.setPower(leftJoystick);
        robot.rightFront.setPower(rightJoystick);
        robot.rightBack.setPower(rightJoystick);

    }

    public void liftHanger(double g2_rightJoystick) {

        robot.liftSlides.setPower(g2_rightJoystick * constants.LIFT_SLIDES_REVERSE_CONSTANT);

    }

    public void liftHanger() {

        liftHanger(this.gamepad2.right_stick_y);
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

    public void mecanumDrive() {

        mecanumDrive(this.gamepad1.dpad_left, this.gamepad1.dpad_right);
    }

}
