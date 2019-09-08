
package org.firstinspires.ftc.teamcode.robot.mecanum;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.openftc.revextensions2.RevBulkData;

@TeleOp(name="Robot centric teleop")
public class MecanumRobotCentric extends LinearOpMode {
    @Override
    public void runOpMode() {
        MecanumHardware robot = new MecanumHardware(this);
        //robot.initBNO055IMU(hardwareMap);

        waitForStart();
        //robot.initBulkReadTelemetry();

        while (opModeIsActive()) {
            //RevBulkData data = robot.performBulkRead();

            double leftX = MecanumUtil.deadZone(gamepad1.left_stick_x, 0.05);
            double leftY = MecanumUtil.deadZone(gamepad1.left_stick_y, 0.05);
            double angle = Math.atan2(leftY, leftX) - Math.PI/4;
            double driveScale = Math.sqrt(Math.pow(leftX, 2) + Math.pow(leftY, 2));
            driveScale = Range.clip(driveScale, 0, 1);

            double turn = MecanumUtil.deadZone(gamepad1.right_stick_x, 0.05);
            turn = turn * turn * turn;

            robot.setPowers(MecanumUtil.powersFromAngle(angle, driveScale, turn));
        }
    }
}