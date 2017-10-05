/*
    Basic rat bot program for testing
 */

package org.firstinspires.ftc.teamcode.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;

@TeleOp(name = "RatBot", group = "Misc.")
//@Disabled
public class RatBot extends OpMode {

    DcMotorEx frontLeftMotor;
    DcMotorEx backLeftMotor;
    DcMotorEx frontRightMotor;
    DcMotorEx backRightMotor;

    @Override
    public void init() {

        telemetry.addData("Status", "Initialized");

        // hardware maps
        frontLeftMotor = (DcMotorEx) hardwareMap.dcMotor.get("front_left");
        frontRightMotor = (DcMotorEx) hardwareMap.dcMotor.get("front_right");
        backLeftMotor = (DcMotorEx) hardwareMap.dcMotor.get("back_left");
        backRightMotor = (DcMotorEx) hardwareMap.dcMotor.get("back_right");

        // change directions if necessary
        frontLeftMotor.setDirection(DcMotor.Direction.REVERSE);
        frontRightMotor.setDirection(DcMotor.Direction.FORWARD);
        backLeftMotor.setDirection(DcMotorSimple.Direction.REVERSE);
        backRightMotor.setDirection(DcMotorSimple.Direction.REVERSE);
    }

    @Override
    public void start() {}

    @Override
    public void loop() {

        // run the drive train motors
        frontLeftMotor.setPower(gamepad1.left_stick_y);
        frontRightMotor.setPower(gamepad1.right_stick_y);
        backLeftMotor.setPower(gamepad1.left_stick_y);
        backRightMotor.setPower(gamepad1.right_stick_y);

        telemetry.addData("Veloctiy front right", "%f.2", frontRightMotor.getVelocity(AngleUnit.DEGREES));
    }
}
