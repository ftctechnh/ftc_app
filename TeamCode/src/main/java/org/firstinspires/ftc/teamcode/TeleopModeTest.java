package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import java.util.*;

@TeleOp(name = "TeleOpTest", group = "Tests")
public class TeleopModeTest extends LinearOpMode {

    //Initializes motors and servos
    private DcMotor frontLeft;
    private DcMotor frontRight;
    private DcMotor backLeft;
    private DcMotor backRight;

    private DcMotor Slide;

    private Servo claw;

    //Sets global variables.
    private static final double CLAW_DOWN_POSITION = 0.5;
    private static final double CLAW_UP_POSITION = 0;

    private int power;

    //Method sets motor powers to make the robot strafe left or right based on sign of input power.
    public void Strafe(int power) {
        frontLeft.setPower(-power);
        frontRight.setPower(power);
        backLeft.setPower(power);
        backRight.setPower(-power);
    }
    //Method sets all motors to 0 (makes robot stop).
    public void Stop() {
        frontLeft.setPower(0);
        frontRight.setPower(0);
        backLeft.setPower(0);
        backRight.setPower(0);
    }

    //Main runOpMode method.
    @Override
    public void runOpMode() throws InterruptedException {
//Sets previously initialized motors and servos to their proper match in the DriverStation config.
        frontLeft = hardwareMap.dcMotor.get("frontLeft");
        frontRight = hardwareMap.dcMotor.get("frontRight");
        backLeft = hardwareMap.dcMotor.get("backLeft");
        backRight = hardwareMap.dcMotor.get("backRight");

        Slide = hardwareMap.dcMotor.get("Slide");

        claw = hardwareMap.servo.get("claw");
//Reverses left side motors to make the robot go forward on positive power.
        frontRight.setDirection(DcMotor.Direction.REVERSE);
        backRight.setDirection(DcMotor.Direction.REVERSE);

        waitForStart();

        while (opModeIsActive()) {
//Sets the motor powers to the inputs given by the gamepad's analog sticks (gamepad y-values are inverted).
            frontLeft.setPower(gamepad1.left_stick_y);
            backLeft.setPower(gamepad1.left_stick_y);
            frontRight.setPower(gamepad1.right_stick_y);
            backRight.setPower(gamepad1.right_stick_y);

            Slide.setPower(gamepad2.right_stick_y);
//Uses gamepad button/bumper inputs to set servo positions.

            if (gamepad1.right_bumper) {
                Strafe(1);
            }
            else if (gamepad1.left_bumper) {
                Strafe(-1);
            }
//Stops the motors so that the robot only moves while the buttons are being pressed.
            else {
                Stop();
            }
//Allows the driver to drive slower for more precise movements when needed (by pressing the right trigger).
            if (gamepad1.right_trigger <= 0.5){
                frontLeft.setPower(-gamepad1.left_stick_y / 2);
                backLeft.setPower(-gamepad1.left_stick_y / 2);
                frontRight.setPower(-gamepad1.right_stick_y / 2);
                backRight.setPower(-gamepad1.right_stick_y / 2);
            }


            if (gamepad2.b) {
                claw.setPosition(CLAW_DOWN_POSITION);
            }
            else if (gamepad2.a) {
                claw.setPosition(CLAW_UP_POSITION);
            }

//Gives the hardware a small amount of time to catch up before looping again.
            idle();
        }
    }
}
