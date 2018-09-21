package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.lynx.LynxI2cColorRangeSensor;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

@TeleOp

public class MainTeleOp extends LinearOpMode{
    // Motors
    protected DcMotor rightDriveMotor;
    protected DcMotor leftDriveMotor;

    // Servos

    // Color sensors
    protected LynxI2cColorRangeSensor color0;

    private void initOpMode() {
        //initialize all the motors
        rightDriveMotor = hardwareMap.get(DcMotor.class, "rightDriveMotor");
        leftDriveMotor = hardwareMap.get(DcMotor.class, "leftDriveMotor");

        // Sensors initialization
        color0 = hardwareMap.get(LynxI2cColorRangeSensor.class, "color0");


    }

    public void runOpMode() {
        initOpMode();
        waitForStart();
        while(opModeIsActive()) {
            drive();
            intake();
            lift();
            deposit();

        }
    }

    // slow variable to allow for 'slowmode' - allowing the robot to go slower.
    double slow = 1.66;

//controller 1
    private void drive() {
        if (gamepad1.right_bumper) {
            slow = 1;
        }
        if (gamepad1.left_bumper) {
            slow = 1.66;
        }

        rightDriveMotor.setPower(gamepad1.left_stick_y / slow);
        leftDriveMotor.setPower(-1 * gamepad1.left_stick_y / slow);

        if (gamepad1.right_stick_x != 0) {
            rightDriveMotor.setPower(gamepad1.right_stick_x / slow * 2);
            leftDriveMotor.setPower(gamepad1.right_stick_x / slow * 2);
        }
    }

//controller 2
    private void intake() {

    }

//controller 2
    private void lift() {

    }

//controller 2
    private void deposit() {

    }
}
