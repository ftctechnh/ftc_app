package org.firstinspires.ftc.teamcode.NEW_TEST;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;

import static com.qualcomm.robotcore.hardware.DcMotor.RunMode.RUN_USING_ENCODER;
import static com.qualcomm.robotcore.hardware.DcMotor.RunMode.STOP_AND_RESET_ENCODER;
import static com.qualcomm.robotcore.hardware.DcMotorSimple.Direction.REVERSE;

/**
 * Created by shiva on 24-05-2018.
 */

@TeleOp(name = "Grab Test", group = "prototype")

public class GRABBER extends LinearOpMode {

    //GRAB
    private DcMotor grabMotor;
    private Servo grabTopLeft;
    private Servo grabBottomLeft;
    private Servo grabTopRight;
    private Servo grabBottomRight;

    private int upperLimit;
    private int lowerLimit;

    private double gtlOPEN = 0.71;
    private double gtlGRAB = 0.31;

    private double gtrOPEN = 0.02;
    private double gtrGRAB = 0.37;

    private double gblOPEN = 0.04;
    private double gblGRAB = 0.53;

    private double gbrOPEN = 0.74;
    private double gbrGRAB = 0.27;

    private int mode;

    @Override
    public void runOpMode() throws InterruptedException {

        grabMotor = hardwareMap.get(DcMotor.class, "GrabDC");
        grabTopLeft = hardwareMap.get(Servo.class, "GTL");
        grabBottomLeft = hardwareMap.get(Servo.class, "GBL");
        grabTopRight = hardwareMap.get(Servo.class, "GTR");
        grabBottomRight = hardwareMap.get(Servo.class, "GBR");

        grabMotor.setMode(STOP_AND_RESET_ENCODER);
        grabMotor.setMode(RUN_USING_ENCODER);
        grabMotor.setDirection(REVERSE);

        upperLimit = 5000;
        lowerLimit = 800;

        grabTopLeft.setPosition(gtlOPEN);
        grabTopRight.setPosition(gtrOPEN);
        grabBottomLeft.setPosition(gblOPEN);
        grabBottomRight.setPosition(gbrOPEN);

        mode = 1;

        waitForStart();

        while (opModeIsActive()) {

            telemetry.addData("Position: ", grabMotor.getCurrentPosition());
            telemetry.update();

            //GDC ENCODER RESET
//            if (gamepad1.right_stick_button) {
//                grabMotor.setMode(STOP_AND_RESET_ENCODER);
//                grabMotor.setMode(RUN_USING_ENCODER);
//            }

            //GDC WITHIN LIMIT
            if (grabMotor.getCurrentPosition() < 5050 && grabMotor.getCurrentPosition() > 750) {
                if (gamepad1.dpad_up && !gamepad1.dpad_down) {
                    grabMotor.setPower(gamepad1.right_trigger);
                    telemetry.addData("up", upperLimit);
                    telemetry.update();
                }
                if (!gamepad1.dpad_up && gamepad1.dpad_down) {
                    grabMotor.setPower(-gamepad1.right_trigger);
                    telemetry.addData("down", upperLimit);
                    telemetry.update();
                }
                if (!gamepad1.dpad_up && !gamepad1.dpad_down) {
                    grabMotor.setPower(0);
                    telemetry.addData("a-stop", upperLimit);
                    telemetry.update();
                }
            }

            //UPPER LIMIT EXCEEDED
            if (grabMotor.getCurrentPosition() >= 5050) {
                if (gamepad1.dpad_up && !gamepad1.dpad_down) {
                    grabMotor.setPower(0.2);
                    telemetry.addData("up <upper limit exceeded>", upperLimit);
                    telemetry.update();
                }
                if (!gamepad1.dpad_up && gamepad1.dpad_down) {
                    grabMotor.setPower(-gamepad1.right_trigger);
                    telemetry.addData("down <upper limit exceeded>", upperLimit);
                    telemetry.update();
                }
                if (!gamepad1.dpad_up && !gamepad1.dpad_down) {
                    grabMotor.setPower(0);
                    telemetry.addData("a-stop <upper limit ecxeeded>", upperLimit);
                    telemetry.update();
                }
            }

            //LOWER LIMIT EXCEEDED
            if (grabMotor.getCurrentPosition() <= 750) {
                if (gamepad1.dpad_up && !gamepad1.dpad_down) {
                    grabMotor.setPower(gamepad1.right_trigger);
                    telemetry.addData("up <lower limit exceeded>", lowerLimit);
                    telemetry.update();
                }
                if (!gamepad1.dpad_up && gamepad1.dpad_down) {
                    grabMotor.setPower(-0.2);
                    telemetry.addData("down <lower limit exceeded>", lowerLimit);
                    telemetry.update();
                }
                if (!gamepad1.dpad_up && !gamepad1.dpad_down) {
                    grabMotor.setPower(0);
                    telemetry.addData("a-stop <lower limit exceeded>", lowerLimit);
                    telemetry.update();
                }
            }

            //AUTOMATIC STOP
            if (!gamepad1.dpad_up && !gamepad1.dpad_down) {
                grabMotor.setPower(0);
                telemetry.addData("a-stop", upperLimit);
                telemetry.update();
            }

            //TOP SERVOS OPEN
            if (gamepad1.a) {
                grabTopLeft.setPosition(gtlOPEN);
                grabTopRight.setPosition(gtrOPEN);
            }
            //TOP SERVOS GRAB
            if (gamepad1.b) {
                grabTopLeft.setPosition(gtlGRAB);
                grabTopRight.setPosition(gtrGRAB);
            }
            //BOTTOM SERVOS OPEN
            if (gamepad1.x) {
                grabBottomLeft.setPosition(gblOPEN);
                grabBottomRight.setPosition(gbrOPEN);
            }
            //BOTTOM SERVOS GRAB
            if (gamepad1.y) {
                grabBottomLeft.setPosition(gblGRAB);
                grabBottomRight.setPosition(gbrGRAB);
            }

            telemetry.addData("GDC: ", grabMotor.getCurrentPosition());
            telemetry.addData("GTL: ", grabTopLeft.getPosition());
            telemetry.addData("GTR: ", grabTopRight.getPosition());
            telemetry.addData("GBL: ", grabBottomLeft.getPosition());
            telemetry.addData("GBR: ", grabBottomRight.getPosition());
            telemetry.update();

        }

    }
}
