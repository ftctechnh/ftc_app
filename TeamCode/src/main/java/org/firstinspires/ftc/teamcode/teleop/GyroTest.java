package org.firstinspires.ftc.teamcode.teleop;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.chassis.Robot;
import org.firstinspires.ftc.teamcode.misc.FtcUtils;
import org.firstinspires.ftc.teamcode.misc.RobotConstants;

@TeleOp(name = "Gyro test")
public class GyroTest extends LinearOpMode {
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
        robot.init(hardwareMap, this, true);
        telemetry.addData("Status", "Initialization bas been completed");
        telemetry.update();
        waitForStart();
        while (!isStopRequested() && opModeIsActive()) {
            lefty = FtcUtils.motorScale(gamepad1.left_stick_y) * RobotConstants.sensitivity;
            rightx = FtcUtils.motorScale(gamepad1.right_stick_x) * RobotConstants.sensitivity;
            leftx = FtcUtils.motorScale(gamepad1.left_stick_x) * RobotConstants.sensitivity;
            robot.updateAngle();
            telemetry.addData("gyro", robot.getAngle());
            if (FtcUtils.abs(lefty) > RobotConstants.threshold) {
                robot.drive(-lefty, -lefty, -lefty, -lefty);
            } else if (FtcUtils.abs(leftx) > RobotConstants.threshold) {
                robot.drive(leftx, -leftx, leftx, -leftx);
            }  else if (FtcUtils.abs(rightx) > RobotConstants.threshold) {
                robot.drive(-rightx, -rightx, rightx, rightx);
            } else {
                robot.stop();
            }

            telemetry.update();
            idle();
        }
        robot.stop();
    }
}