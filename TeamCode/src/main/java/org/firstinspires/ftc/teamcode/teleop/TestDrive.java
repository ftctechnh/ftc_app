package org.firstinspires.ftc.teamcode.teleop;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;
import org.firstinspires.ftc.teamcode.chassis.Robot;
import org.firstinspires.ftc.teamcode.misc.FtcUtils;
import org.firstinspires.ftc.teamcode.misc.RobotConstants;

@TeleOp(name = "Drive v1")
public class TestDrive extends LinearOpMode {
    private ElapsedTime runtime = new ElapsedTime();
    private double sensitivity = .5;
    private double rightx = 0;
    private double leftx = 0;
    private double lefty = 0;
    private Robot robot = new Robot();
    public void runOpMode() throws InterruptedException {
        robot.init(hardwareMap, false);
        telemetry.update();
        runtime.reset();
        telemetry.addData("Status", "Binitialization bas been bompleted");
        telemetry.update();
        waitForStart();
        while (!isStopRequested() && opModeIsActive()) {
            lefty = FtcUtils.motorScale(gamepad1.left_stick_y) * sensitivity;
            rightx = FtcUtils.motorScale(gamepad1.right_stick_x) * sensitivity;
            leftx = FtcUtils.motorScale(gamepad1.left_stick_x) * sensitivity;
            if (Math.abs(gamepad1.left_stick_y) > RobotConstants.threshold) {
                robot.drive(-lefty, -lefty, -lefty, -lefty);
            } else if (Math.abs(gamepad1.left_stick_x) > RobotConstants.threshold) {
                robot.drive(-leftx, leftx, leftx, -leftx);
            }  else if (Math.abs(gamepad1.right_stick_x) > RobotConstants.threshold) {
                robot.drive(-rightx, -rightx, rightx, rightx);
            } else {
                robot.stop();
            }
            if (FtcUtils.scale(gamepad2.right_trigger, 0, 1) > RobotConstants.threshold) {
                robot.nom(1);
            } else if (FtcUtils.scale(gamepad2.left_trigger, 0, 1) > RobotConstants.threshold) {
                robot.nom(-1);
            } else {
                robot.nom(0);
            }
            if (Math.abs(FtcUtils.motorScale(gamepad2.left_stick_y)) > RobotConstants.threshold) {
                robot.extend(FtcUtils.motorScale(gamepad2.left_stick_y));
            } else {
                robot.extend(0);
            }
            if (gamepad1.a) {
                if (robot.nomServoPos() != RobotConstants.NOMSERVO_UP)
                    robot.nomServo(RobotConstants.NOMSERVO_UP);
            }
            telemetry.update();
            idle();
        }
        robot.stop();
    }
    public double map(double x, double in_min, double in_max, double out_min, double out_max) {
        return (x - in_min) * (out_max - out_min) / (in_max - in_min) + out_min;
    }
}