package org.firstinspires.ftc.teamcode.Salsa.Asteroid.TeleOp;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.teamcode.Salsa.Asteroid.Robot;

/**
 * Created by adityamavalankar on 11/4/18.
 */

public class TeleOpFunctions extends OpMode {

    Robot robot = new Robot();

    public void straightDrive() {
        double gp1_leftY = -gamepad1.left_stick_y;
        double gp1_rightY = -gamepad1.right_stick_y;

        robot.hardware.leftBack.setPower(gp1_leftY);
        robot.hardware.leftFront.setPower(gp1_leftY);
        robot.hardware.rightBack.setPower(gp1_rightY);
        robot.hardware.rightFront.setPower(gp1_rightY);
    }


    public void init() {}

    public void loop() {}
}
