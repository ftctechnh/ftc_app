package org.firstinspires.ftc.teamcode.seasons.relicrecovery;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

/**
 * Created by ftc6347 on 10/15/17.
 */
@TeleOp(name = "TELEOP", group = "teleop")
public class RobotTeleOp extends LinearOpMode {
    private RelicRecoveryRobot robot;

    @Override
    public void runOpMode() throws InterruptedException {
        robot = new RelicRecoveryRobot(this);

        gamepad1.setJoystickDeadzone(0.2f);

        waitForStart();

        double speedX;
        double speedY;
        double pivot;

        while (opModeIsActive()) {
            speedX = gamepad1.right_stick_x;
            speedY = gamepad1.right_stick_y;
            pivot = gamepad1.left_stick_x;

            robot.hDriveTrain.pivot(-pivot);
            robot.hDriveTrain.drive(-speedX,speedY);
        }
    }
}
