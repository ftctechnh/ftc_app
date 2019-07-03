package org.firstinspires.ftc.teamcode.robot.sixwheel;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.openftc.revextensions2.RevBulkData;

public class SixWheelTeleop extends LinearOpMode {

    SixWheelHardware robot;

    @Override
    public void runOpMode() {
        robot = new SixWheelHardware(this);
        waitForStart();

        robot.initBulkReadTelemetry();

        while (opModeIsActive()) {
            RevBulkData data = robot.performBulkRead();
            
            double left = clamp(gamepad1.left_stick_y + gamepad1.right_stick_x);
            double right = clamp(gamepad1.left_stick_y - gamepad1.right_stick_x);
            robot.driveLeft.setPower(left);
            robot.driveRight.setPower(right);
            robot.PTOLeft.setPower(left);
            robot.PTORight.setPower(right);
        }
    }

    private double clamp(double d) {
        return Math.min(Math.max(d, -1), 1);
    }
}
