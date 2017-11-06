package org.firstinspires.ftc.teamcode.Tests;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

/**
 * Created by Stephen Ogden on 11/2/17.
 * FTC 6128 | 7935
 * FRC 1595
 */

@TeleOp(name = "7935 DriveTest", group = "Test")
@Disabled

public class testCoastDrive extends LinearOpMode{
    public void runOpMode() {

        telemetry.addData("Status", "Initializing...");
        telemetry.update();

        DcMotor left = hardwareMap.dcMotor.get("left");
        left.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        left.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        left.setDirection(DcMotorSimple.Direction.FORWARD);

        DcMotor right = hardwareMap.dcMotor.get("right");
        right.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        right.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        right.setDirection(DcMotorSimple.Direction.REVERSE);

        telemetry.addData("Status", "Done! Press play to start");
        telemetry.update();
        waitForStart();

        while (opModeIsActive()) {

            right.setPower(gamepad2.left_stick_y - (gamepad2.right_stick_x * 2));
            left.setPower(gamepad2.left_stick_y + (gamepad2.right_stick_x * 2));

        }

    }

}
