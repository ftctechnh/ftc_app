package org.firstinspires.ftc.teamcode.autonomous;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.autonomous.controllers.SixWheelPurePursuitController;
import org.firstinspires.ftc.teamcode.common.math.Point;
import org.firstinspires.ftc.teamcode.robot.sixwheel.SixWheelHardware;
import org.firstinspires.ftc.teamcode.robot.sixwheel.SixWheelPowers;

import static org.firstinspires.ftc.teamcode.common.math.MathUtil.clamp;
import static org.firstinspires.ftc.teamcode.common.math.MathUtil.deadZone;

@TeleOp
public class TestPurePursuitTracking extends LinearOpMode {
    SixWheelHardware robot;

    @Override
    public void runOpMode() {
        robot = new SixWheelHardware(this);
        waitForStart();

        telemetry.clearAll();
        robot.initBulkReadTelemetry();

        while (opModeIsActive()) {
            robot.performBulkRead();

            if (gamepad1.left_stick_button) {
                ElapsedTime elapsed = new ElapsedTime();
                while (elapsed.seconds() < 5) {
                    robot.performBulkRead();
                    SixWheelPowers p = SixWheelPurePursuitController.goToPosition(robot.pose(), new Point(0, 0));
                    robot.setWheelPowers(new SixWheelPowers(-p.left, -p.right));
                    sleep(20);
                }
            }

            double left = deadZone(clamp(gamepad1.left_stick_y - gamepad1.right_stick_x), 0.15);
            double right = deadZone(clamp(gamepad1.left_stick_y + gamepad1.right_stick_x), 0.15);
            robot.driveLeft.setPower(left);
            robot.driveRight.setPower(right);
            robot.PTOLeft.setPower(left);
            robot.PTORight.setPower(right);

            sleep(20);
        }
    }
}
