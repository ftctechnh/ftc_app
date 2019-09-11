
package org.firstinspires.ftc.teamcode.robot.mecanum;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.teamcode.common.SimulatableMecanumOpMode;

@TeleOp(name="Robot centric teleop")
public class MecanumRobotCentric extends SimulatableMecanumOpMode {
    MecanumHardware robot;

    @Override
    public void init() {
        this.robot = this.getRobot();
        robot.initBNO055IMU(hardwareMap);
    }

    @Override
    public void start() {
        //robot.initBulkReadTelemetry();
    }

    @Override
    public void loop() {
        //RevBulkData data = robot.performBulkRead();
        double leftX = MecanumUtil.deadZone(gamepad1.left_stick_x, 0.05);
        double leftY = MecanumUtil.deadZone(gamepad1.left_stick_y, 0.05);
        double angle = -Math.atan2(leftY, leftX) + Math.PI/2;
        double driveScale = Math.sqrt(Math.pow(leftX, 2) + Math.pow(leftY, 2));
        driveScale = Range.clip(driveScale, 0, 1);

        // Exponentiate our turn
        double turn = Math.pow(MecanumUtil.deadZone(gamepad1.right_stick_x, 0.05), 3);

        MecanumPowers powers = MecanumUtil.powersFromAngle(angle, driveScale, turn);
        robot.setPowers(powers);
    }
}