package org.firstinspires.ftc.teamcode.teleop;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;
import org.firstinspires.ftc.teamcode.chassis.Robot;
import org.firstinspires.ftc.teamcode.misc.FtcUtils;
import org.firstinspires.ftc.teamcode.misc.RobotConstants;

@TeleOp(name = "Drive")
public class TestDrive extends LinearOpMode {
    private double currentNomServoPos = 0;
    private ElapsedTime runtime = new ElapsedTime();
    private double rightx = 0;
    private double leftx = 0;
    private double lefty = 0;
    private double frontLeft;
    private double frontRight;
    private double backRight;
    private double backLeft;
    private Robot robot = new Robot();
    public void runOpMode() throws InterruptedException {
        robot.init(hardwareMap, this, false);
        robot.nomServo(RobotConstants.NOMSERVO_NEUTRAL);
        telemetry.addData("Status", "Initialization bas been completed");
        telemetry.update();
        waitForStart();
        while (!isStopRequested() && opModeIsActive()) {
            telemetry.addData("nom servo pos", robot.nomServoPos());
            lefty = FtcUtils.motorScale(gamepad1.left_stick_y) * RobotConstants.sensitivity;
            rightx = FtcUtils.motorScale(gamepad1.right_stick_x) * RobotConstants.sensitivity;
            leftx = FtcUtils.motorScale(gamepad1.left_stick_x) * RobotConstants.sensitivity;

            if (FtcUtils.abs(lefty) > RobotConstants.threshold) {
                robot.drive(-lefty, -lefty, -lefty, -lefty);
            } else if (FtcUtils.abs(leftx) > RobotConstants.threshold) {
                robot.drive(leftx, -leftx, leftx, -leftx);
            }  else if (FtcUtils.abs(rightx) > RobotConstants.threshold) {
                robot.drive(-rightx, -rightx, rightx, rightx);
            } else {
                robot.stop();
            }

            if (FtcUtils.scale(gamepad2.right_trigger, 0, 1) > RobotConstants.threshold) {
                robot.nom(.6);
            } else if (FtcUtils.scale(gamepad2.left_trigger, 0, 1) > RobotConstants.threshold) {
                robot.nom(-.6);
            } else {
                robot.nom(0);
            }
            if (FtcUtils.abs(FtcUtils.motorScale(gamepad2.right_stick_y)) > RobotConstants.threshold) {
                robot.catapult(FtcUtils.motorScale(gamepad2.right_stick_y));
            } else {
                robot.catapult(0);
            }
            if (FtcUtils.abs(FtcUtils.motorScale(gamepad2.left_stick_y)) > RobotConstants.threshold) {
                robot.extend(FtcUtils.motorScale(gamepad2.left_stick_y));
            } else {
                robot.extend(0);
            }
            if (gamepad2.y) {
                if (robot.nomServoPos() != RobotConstants.NOMSERVO_UP) {
                    robot.nomServo(RobotConstants.NOMSERVO_UP);
                }
            }
            if (gamepad2.a) {
                if (robot.nomServoPos() != RobotConstants.NOMSERVO_DOWN) {
                    robot.nomServo(RobotConstants.NOMSERVO_DOWN);
                }
            }
            if (gamepad2.x) {
                if (robot.nomServoPos() != RobotConstants.NOMSERVO_NEUTRAL) {
                    robot.nomServo(RobotConstants.NOMSERVO_NEUTRAL);
                }
            }
            if (gamepad2.dpad_up) {
                robot.hang(1);
            } else if (gamepad2.dpad_down) {
                robot.hang(-1);
            } else {
                robot.hang(0);
            }
            telemetry.update();
            idle();
        }
        robot.stop();
    }
}