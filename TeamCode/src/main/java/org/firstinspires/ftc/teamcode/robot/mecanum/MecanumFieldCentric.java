
package org.firstinspires.ftc.teamcode.robot.mecanum;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.openftc.revextensions2.RevBulkData;

@TeleOp(name="Field centric teleop")
public class MecanumFieldCentric extends LinearOpMode {
    @Override
    public void runOpMode() {
        MecanumHardware robot = new MecanumHardware(this);
        robot.initBNO055IMU(hardwareMap);

        waitForStart();
        //robot.initBulkReadTelemetry();

        while (opModeIsActive()) {
            //RevBulkData data = robot.performBulkRead();
            Orientation angles = robot.imu.getAngularOrientation(
                    AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.RADIANS);

            double leftX = gamepad1.left_stick_x;
            double leftY = gamepad1.left_stick_y;
            double angle = Math.atan2(leftY, leftX) + angles.firstAngle;
            double driveScale = Math.sqrt(Math.pow(leftX, 2) + Math.pow(leftY, 2));
            driveScale = Range.clip(driveScale, 0, 1);
            robot.setPowers(MecanumUtil.powersFromAngle(angle, driveScale, gamepad1.right_stick_x));
        }
    }
}