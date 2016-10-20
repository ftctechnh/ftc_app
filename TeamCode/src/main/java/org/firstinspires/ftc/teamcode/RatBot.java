/*
    Basic rat bot program for testing
 */

package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

@TeleOp(name = "RatBot", group = "Misc.")
//@Disabled
public class RatBot extends OpMode {

    DcMotor frontLeftMotor;
    DcMotor frontRightMotor;

    @Override
    public void init() {

        telemetry.addData("Status", "Initialized");

        // hardware maps
        frontLeftMotor = hardwareMap.dcMotor.get("front_left");
        frontRightMotor = hardwareMap.dcMotor.get("front_right");

        // change directions if necessary
        frontLeftMotor.setDirection(DcMotor.Direction.REVERSE);
        frontRightMotor.setDirection(DcMotor.Direction.FORWARD);
    }

    @Override
    public void start() {}

    @Override
    public void loop() {

        // run the drive train motors
        frontLeftMotor.setPower(gamepad1.left_stick_y);
        frontRightMotor.setPower(gamepad1.right_stick_y);
    }
}
