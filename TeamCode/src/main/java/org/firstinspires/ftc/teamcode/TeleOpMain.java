/*
    Main robot teleop program
 */

package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

@TeleOp(name = "TeleOpMain", group = "Main")
//@Disabled
public class TeleOpMain extends OpMode {

    DcMotor frontLeftMotor;
    DcMotor frontRightMotor;
    DcMotor backLeftMotor;
    DcMotor backRightMotor;

    @Override
    public void init() {

        telemetry.addData("Status", "Initialized");

        // hardware maps
        frontLeftMotor = hardwareMap.dcMotor.get("front_left");
        frontRightMotor = hardwareMap.dcMotor.get("front_right");
        backLeftMotor = hardwareMap.dcMotor.get("back_left");
        backRightMotor = hardwareMap.dcMotor.get("back_right");

        // change directions if necessary
        frontLeftMotor.setDirection(DcMotor.Direction.REVERSE);
        frontRightMotor.setDirection(DcMotor.Direction.FORWARD);
        backLeftMotor.setDirection(DcMotor.Direction.REVERSE);
        backRightMotor.setDirection(DcMotor.Direction.FORWARD);
    }

    @Override
    public void start() {}

    @Override
    public void loop() {

        // run drivetrain motors
        if(gamepad1.dpad_left) {
            frontLeftMotor.setPower(-1.0);
            frontRightMotor.setPower(-1.0);
            backLeftMotor.setPower(1.0);
            backRightMotor.setPower(1.0);
        }
        else if(gamepad1.dpad_right) {
            frontLeftMotor.setPower(1.0);
            frontRightMotor.setPower(1.0);
            backLeftMotor.setPower(-1.0);
            backRightMotor.setPower(-1.0);
        }
        else {
            frontLeftMotor.setPower(gamepad1.left_stick_y);
            frontRightMotor.setPower(gamepad1.right_stick_y);
            backLeftMotor.setPower(gamepad1.left_stick_y);
            backRightMotor.setPower(gamepad1.right_stick_y);
        }
    }
}
