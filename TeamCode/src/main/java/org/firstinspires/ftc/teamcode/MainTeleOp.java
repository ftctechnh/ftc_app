package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.lynx.LynxI2cColorRangeSensor;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

@TeleOp

public class MainTeleOp extends LinearOpMode{
    // Motors
    protected DcMotor motor0;
    protected DcMotor motor1;

    // Servos

    // Color sensors
    protected LynxI2cColorRangeSensor color0;

    private void initOpMode() {
        //initialize all the motors
        motor0 = hardwareMap.get(DcMotor.class, "motor0");

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

    double slow=2;
//driver 1
    private void drive() {
        if (gamepad1.right_bumper) {
            slow = 1;
        }
        if (gamepad1.left_bumper) {
            slow = 1.66;
        }

        motor0.setPower(gamepad1.left_stick_y / slow);
        motor1.setPower(-1 * gamepad1.left_stick_y / slow);

        if (gamepad1.right_stick_x != 0) {
            motor0.setPower(gamepad1.right_stick_x / slow * 2);
            motor1.setPower(gamepad1.right_stick_x / slow * 2);
        }
    }
//driver 2
    private void intake() {

    }
//driver 2
    private void lift() {

    }
//driver 2
    private void deposit() {

    }
}
