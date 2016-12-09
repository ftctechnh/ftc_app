package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

import org.firstinspires.ftc.teamcode.modules.GamepadV2;
import org.firstinspires.ftc.teamcode.modules.MecanumDrive;

@TeleOp
public class FinalTeleop extends OpMode {
    DcMotor frontLeft, frontRight, backLeft, backRight;
    DcMotor intake, shoot, lift, flipper;

    GamepadV2 pad2 = new GamepadV2(), pad1 = new GamepadV2();

    boolean driveMec = true;
    boolean flipperHold = false;

    @Override
    public void init() {
        frontLeft = hardwareMap.dcMotor.get("FL");
        frontRight = hardwareMap.dcMotor.get("FR");
        backLeft = hardwareMap.dcMotor.get("BL");
        backRight = hardwareMap.dcMotor.get("BR");

        frontLeft.setDirection(DcMotorSimple.Direction.REVERSE);
        backLeft.setDirection(DcMotorSimple.Direction.REVERSE);

        intake = hardwareMap.dcMotor.get("intake");
        shoot = hardwareMap.dcMotor.get("shoot");
        lift = hardwareMap.dcMotor.get("lift");
        flipper = hardwareMap.dcMotor.get("flipper");

        shoot.setDirection(DcMotorSimple.Direction.REVERSE);
    }

    @Override public void start() {
        frontLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        backRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        backLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        backRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        frontLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        backRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        backLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        backRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }

    @Override
    public void loop() {
        pad2.update(gamepad2);
        pad1.update(gamepad1);

        if(driveMec) {
            MecanumDrive.loop(frontLeft, frontRight, backLeft, backRight, pad2);
        }
        else {
            frontLeft.setPower(pad2.left_stick_y_exponential(1));
            backLeft.setPower(pad2.left_stick_y_exponential(1));
            frontRight.setPower(pad2.right_stick_y_exponential(1));
            backRight.setPower(pad2.right_stick_y_exponential(1));
        }

        if(pad2.a_isReleased(true)) {
            driveMec = !driveMec;
        }

        if(pad2.b_isReleased(true)) {
            flipperHold = !flipperHold;
        }

        if(pad2.right_trigger()) {
            flipper.setPower(0.80);
        }
        else if(pad2.left_trigger()) {
            flipper.setPower(-0.10);
        }
        else if(flipperHold) {
            flipper.setPower(0.2);
        }
        else {
            flipper.setPower(0);
        }

        if(pad2.right_bumper) {
            lift.setPower(1);
        }
        else if(pad2.left_bumper) {
            lift.setPower(-1);
        }
        else {
            lift.setPower(0);
        }

        if(pad1.b) {
            shoot.setPower(0.85);
        }
        else if(pad1.x) {
            shoot.setPower(-0.85);
        }
        else {
            shoot.setPower(0);
        }

        if(pad1.right_bumper) {
            intake.setPower(0.75);
        }
        else if(pad1.left_bumper) {
            intake.setPower(-0.50);
        }
        else {
            intake.setPower(0);
        }

        telemetry();
    }

    public void telemetry() {
        telemetry.addData("Motors", "FL: %f FR: %f, BL: %f, BR %f", frontLeft.getPower(), frontRight.getPower(), backLeft.getPower(), backRight.getPower());
        telemetry.addData("Encoders", "FL: %d FR: %d BL %d BR %d", frontLeft.getCurrentPosition(), frontRight.getCurrentPosition(), backLeft.getCurrentPosition(), backRight.getCurrentPosition());
        telemetry.addData("Mtrs", "intake: %f shoot: %f lift: %f flipper: %f", intake.getPower(), shoot.getPower(), lift.getPower(), flipper.getPower());
    }
}
