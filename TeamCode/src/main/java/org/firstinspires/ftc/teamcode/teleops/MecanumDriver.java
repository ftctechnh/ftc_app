package org.firstinspires.ftc.teamcode.teleops;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.robotplus.hardware.Drivetrain;
import org.firstinspires.ftc.teamcode.robotplus.hardware.MecanumRobot;
import org.firstinspires.ftc.teamcode.robotplus.hardware.Robot;

/**
 * Created by Alex on 8/17/2017.
 */

public class MecanumDriver extends LinearOpMode {
    private MecanumRobot robot;
    private double axisTolerance = 0.1;

    @Override
    public void runOpMode() {
        robot = new MecanumRobot(hardwareMap);

        while (opModeIsActive()) {
            if (!robot.tolerate(gamepad1.left_stick_x, axisTolerance) || !robot.tolerate(gamepad1.right_stick_x, axisTolerance)) {
                // run robot in sideways mode
                if (gamepad1.left_stick_x > 0) {
                    // run to the right
                    robot.getTrain().getLeftMotors().getMotor1().setPower(gamepad1.left_stick_x);
                    robot.getTrain().getLeftMotors().getMotor2().setPower(-gamepad1.left_stick_x);

                    robot.getTrain().getRightMotors().getMotor1().setPower(-gamepad1.left_stick_x);
                    robot.getTrain().getRightMotors().getMotor2().setPower(gamepad1.left_stick_x);
                }
                else if (gamepad1.left_stick_x < 0) {
                    robot.getTrain().getLeftMotors().getMotor1().setPower(-gamepad1.left_stick_x);
                    robot.getTrain().getLeftMotors().getMotor2().setPower(gamepad1.left_stick_x);

                    robot.getTrain().getRightMotors().getMotor1().setPower(gamepad1.left_stick_x);
                    robot.getTrain().getRightMotors().getMotor2().setPower(-gamepad1.left_stick_x);
                }
                else {
                    continue;
                }
            }
            else {
                // run robot in linear mode
                robot.getTrain().getLeftMotors().setPowers(gamepad1.left_stick_y);
                robot.getTrain().getRightMotors().setPowers(gamepad1.right_stick_y);
            }
        }
    }
}