package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;

@TeleOp(name = "Drive v1")
public class TestDrive extends LinearOpMode {
    static final double threshold = 0.3;
    private ElapsedTime runtime = new ElapsedTime();
    private double rightx = 0;
    private double leftx = 0;
    private double lefty = 0;
    private Robot robot = new Robot();
    public void runOpMode() throws InterruptedException {
        robot.init(hardwareMap);
        telemetry.update();
        runtime.reset();
        robot.resetTicks();
        telemetry.addData("status", "initialized");
        telemetry.update();
        waitForStart();
        while (!isStopRequested() && opModeIsActive()) {
            lefty = Range.clip(gamepad1.left_stick_y, -1, 1);
            rightx = Range.clip(gamepad1.right_stick_x, -1, 1);
            leftx = Range.clip(gamepad1.left_stick_x, -1, 1);
            if (Math.abs(gamepad1.left_stick_y) > threshold) {
                robot.drive(-lefty, -lefty, -lefty, -lefty);
            } else if (Math.abs(gamepad1.left_stick_x) > threshold) {
                robot.drive(-leftx, leftx, leftx, -leftx);
            }  else if (Math.abs(gamepad1.right_stick_x) > threshold) {
                robot.drive(-rightx, -rightx, rightx, rightx);
            } else {
                robot.drive(0, 0, 0, 0);
            }
            if(gamepad1.a) {
                robot.nom(1);
            } else if (gamepad1.y) {
                robot.nom(-1);
            }
            idle();
        }
        robot.drive(0, 0, 0, 0);
    }
    public double map(double x, double in_min, double in_max, double out_min, double out_max) {
        return (x - in_min) * (out_max - out_min) / (in_max - in_min) + out_min;
    }
}