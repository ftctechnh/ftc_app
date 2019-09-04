package org.firstinspires.ftc.teamcode.robot.mecanum;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;

public class FieldCentricMecanum extends LinearOpMode {

    DcMotorEx frontLeft, backLeft, frontRight, backRight;
    BNO055IMU imu;

    static final BNO055IMU.Parameters parameters = new BNO055IMU.Parameters();
    static {
        parameters.angleUnit = BNO055IMU.AngleUnit.DEGREES;
        parameters.accelUnit = BNO055IMU.AccelUnit.METERS_PERSEC_PERSEC;
    }

    @Override
    public void runOpMode() {
        frontLeft = hardwareMap.get(DcMotorEx.class, "LeftFront");
        frontRight = hardwareMap.get(DcMotorEx.class, "RightFront");
        backLeft = hardwareMap.get(DcMotorEx.class, "LeftBack");
        backRight = hardwareMap.get(DcMotorEx.class, "RightBack");

        frontLeft.setDirection(DcMotorSimple.Direction.REVERSE);
        backLeft.setDirection(DcMotorSimple.Direction.REVERSE);

        imu = hardwareMap.get(BNO055IMU.class, "imu");
        imu.initialize(parameters);

        waitForStart();
        while (opModeIsActive()) {
            Orientation angles = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.RADIANS);

            double angle = Math.atan2(gamepad1.left_stick_y, gamepad1.left_stick_x) + angles.firstAngle;
            double forward = gamepad1.left_stick_y * Math.cos(angle) + gamepad1.left_stick_x * Math.sin(angle);
            double strafe = -gamepad1.left_stick_y * Math.sin(angle) + gamepad1.left_stick_x * Math.cos(angle);
            double turn = gamepad1.right_stick_x;

            frontLeft.setPower(Range.clip(forward - strafe - turn, -1, 1));
            frontRight.setPower(Range.clip(forward + strafe + turn, -1, 1));
            backLeft.setPower(Range.clip(forward + strafe - turn, -1, 1));
            backRight.setPower(Range.clip(forward - strafe + turn, -1, 1));

        }
    }
}