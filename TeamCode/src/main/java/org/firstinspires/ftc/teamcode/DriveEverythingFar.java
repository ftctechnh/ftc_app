package org.firstinspires.ftc.teamcode;

/**
 * Created by Kaden on 12/12/2017.
 */

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

@TeleOp(name = "DriveEverythingFar", group = "linear OpMode")
public class DriveEverythingFar extends OpMode {
    private Robot robot; 
    
    @Override
    public void init() {
        robot = new Robot(this);
        robot.mapRobot();
    }

    @Override
    public void loop() {
        //drive
        if (Math.abs(gamepad1.left_stick_x) + Math.abs(gamepad1.left_stick_y) + Math.abs(gamepad1.right_stick_x) + Math.abs(gamepad1.right_stick_y) > Math.abs(gamepad2.left_stick_x) + Math.abs(gamepad2.left_stick_y) + Math.abs(gamepad2.right_stick_x) + Math.abs(gamepad2.right_stick_y)) {
            if (gamepad1.right_bumper || gamepad1.left_bumper) {
                robot.drive.driveLeftRight(gamepad1.left_stick_x * robot.drive.BUMPER_SLOW_SPEED, gamepad1.right_stick_x * robot.drive.BUMPER_SLOW_SPEED, gamepad1.left_stick_y * robot.drive.BUMPER_SLOW_SPEED, gamepad1.right_stick_y * robot.drive.BUMPER_SLOW_SPEED);
            } else {
                robot.drive.driveLeftRight(gamepad1.left_stick_x, gamepad1.right_stick_x, gamepad1.left_stick_y, gamepad1.right_stick_y);
            }
        } else if (Math.abs(gamepad2.left_stick_x) + Math.abs(gamepad2.left_stick_y) + Math.abs(gamepad2.right_stick_x) + Math.abs(gamepad2.right_stick_y) > Math.abs(gamepad1.left_stick_x) + Math.abs(gamepad1.left_stick_y) + Math.abs(gamepad1.right_stick_x) + Math.abs(gamepad1.right_stick_y)) {
            if (gamepad2.right_bumper || gamepad2.left_bumper) {
              robot.drive.driveLeftRight(gamepad2.left_stick_x * robot.drive.BUMPER_SLOW_SPEED, gamepad2.right_stick_x * robot.drive.BUMPER_SLOW_SPEED, gamepad2.left_stick_y * robot.drive.BUMPER_SLOW_SPEED, gamepad2.right_stick_y * robot.drive.BUMPER_SLOW_SPEED);
            } else {
              robot.drive.driveLeftRight(gamepad2.left_stick_x, gamepad2.right_stick_x, gamepad2.left_stick_y, gamepad2.right_stick_y);

            }
        } else {
            if (gamepad1.dpad_up) {
                robot.drive.forward(robot.drive.D_PAD_SLOW_SPEED);
            }
            else if (gamepad1.dpad_left) {
                robot.drive.strafeLeft(robot.drive.D_PAD_SLOW_SPEED);
            }
            else if (gamepad1.dpad_down) {
                robot.drive.backward(robot.drive.D_PAD_SLOW_SPEED);
            }
            else if (gamepad1.dpad_right) {
                robot.drive.strafeRight(robot.drive.D_PAD_SLOW_SPEED);
            }
            else if(gamepad2.dpad_left) {
                robot.drive.strafeLeft(robot.drive.D_PAD_SLOW_SPEED);
            }
            else if (gamepad2.dpad_right) {
                robot.drive.strafeRight(robot.drive.D_PAD_SLOW_SPEED);
            }
            else {
                robot.drive.stop();
            }
        }
        //ForkLift
        if (gamepad1.a) {
            robot.forkLift.closeClaw();
        }

        if (gamepad1.b) {
            robot.forkLift.openClaw();
        }
        robot.forkLift.moveMotor(gamepad1.right_trigger - gamepad1.left_trigger);

        //RelicClaw
        if (gamepad2.a) {
            robot.relicClaw.closeClaw();
        }
        if (gamepad2.b) {
            robot.relicClaw.openClaw();
        }
        if (gamepad2.dpad_up) {
            robot.relicClaw.up();
        }
        if (gamepad2.dpad_down) {
            robot.relicClaw.down();
        }
        if (gamepad2.x) {
            robot.relicClaw.pickup();
        }
        if (gamepad2.y) {
            robot.relicClaw.driving();
        }
        robot.relicClaw.moveMotor(gamepad2.left_trigger - gamepad2.right_trigger);
    }
}
