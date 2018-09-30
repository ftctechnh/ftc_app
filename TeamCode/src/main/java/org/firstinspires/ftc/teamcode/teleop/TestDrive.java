package org.firstinspires.ftc.teamcode.teleop;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;
import org.firstinspires.ftc.teamcode.chassis.Robot;

@TeleOp(name = "Drive v1")
public class TestDrive extends LinearOpMode {
    private static final double threshold = 0.15;
    private ElapsedTime runtime = new ElapsedTime();
    private double sensitivity = .5;
    private double rightx = 0;
    private double leftx = 0;
    private double lefty = 0;
    private Robot robot = new Robot();
    public void runOpMode() throws InterruptedException {
        robot.init(hardwareMap);
        telemetry.update();
        runtime.reset();
        robot.resetTicks();
        telemetry.addData("Status", "Binitialization bas been bompleted");
        telemetry.update();
        waitForStart();
        while (!isStopRequested() && opModeIsActive()) {
            telemetry.addData("angle", robot.getAngle());
            telemetry.addData("distance", robot.sensorOneDist());
            lefty = Range.clip(gamepad1.left_stick_y, -1, 1) * sensitivity;
            rightx = Range.clip(gamepad1.right_stick_x, -1, 1) * sensitivity;
            leftx = Range.clip(gamepad1.left_stick_x, -1, 1) * sensitivity;
            if (Math.abs(gamepad1.left_stick_y) > threshold) {
                robot.drive(-lefty, -lefty, -lefty, -lefty);
            } else if (Math.abs(gamepad1.left_stick_x) > threshold) {
                robot.drive(-leftx, leftx, leftx, -leftx);
            }  else if (Math.abs(gamepad1.right_stick_x) > threshold) {
                robot.drive(-rightx, -rightx, rightx, rightx);
            } else {
                robot.stop();
            }
            if (Range.clip(gamepad1.right_trigger, 0, 1) > threshold) {
                robot.nom(1);
            } else if (Range.clip(gamepad1.left_trigger, 0, 1) > threshold) {
                robot.nom(-1);
            } else {
                robot.nom(0);
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