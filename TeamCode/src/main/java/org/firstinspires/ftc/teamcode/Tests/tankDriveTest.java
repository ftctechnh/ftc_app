package org.firstinspires.ftc.teamcode.Tests;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

/**
 * Created by Travis Morris on 9/23/17.
 * FTC 6128 | 7935
 */
@Disabled
@TeleOp(name = "tankDriveTest", group = "Test")
public class tankDriveTest extends LinearOpMode {
    public void runOpMode() {
        DcMotor right = hardwareMap.dcMotor.get("bob");
        DcMotor left = hardwareMap.dcMotor.get("jeff");
        right.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        left.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        right.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        left.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        right.setDirection(DcMotorSimple.Direction.FORWARD);
        left.setDirection(DcMotorSimple.Direction.FORWARD);
        waitForStart();
        while (opModeIsActive() && !gamepad1.x) {
            //if gamepad1.left_stick_y{
            right.setPower(gamepad1.right_stick_y);
            left.setPower(gamepad1.left_stick_y);
        }
    }
}
