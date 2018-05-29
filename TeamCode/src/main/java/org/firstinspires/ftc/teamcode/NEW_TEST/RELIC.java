package org.firstinspires.ftc.teamcode.NEW_TEST;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.Range;


import static com.qualcomm.robotcore.hardware.DcMotor.RunMode.RUN_USING_ENCODER;
import static com.qualcomm.robotcore.hardware.DcMotor.RunMode.STOP_AND_RESET_ENCODER;
import static com.qualcomm.robotcore.hardware.DcMotorSimple.Direction.REVERSE;

/**
 * Created by shiva on 24-05-2018.
 */

@TeleOp(name = "Relic Test", group = "prototype")

public class RELIC extends LinearOpMode {

    //GRAB
    private DcMotor relicMotor;
    private Servo relicArm;
    private Servo relicGrab;

    private int upperLimit;
    private int lowerLimit;

    private double pos;
    private double pos2;

    @Override
    public void runOpMode() throws InterruptedException {

        relicMotor = hardwareMap.get(DcMotor.class, "RelicDC");
        relicArm = hardwareMap.get(Servo.class, "RA");
        relicGrab = hardwareMap.get(Servo.class, "RG");

        relicMotor.setMode(STOP_AND_RESET_ENCODER);
        relicMotor.setMode(RUN_USING_ENCODER);
        relicMotor.setDirection(REVERSE);

        upperLimit = 11100;
        lowerLimit = 650;

        relicArm.setPosition(0.5);
        relicGrab.setPosition(0.5);

        pos = 0;
        pos2 = 0;

        waitForStart();

        while (opModeIsActive()) {
//
//            if (gamepad1.x) {
//                relicMotor.setMode(STOP_AND_RESET_ENCODER);
//                relicMotor.setMode(RUN_USING_ENCODER);
//            }
//
//            if (gamepad1.dpad_up && !gamepad1.dpad_down) {
//                relicMotor.setPower(gamepad1.right_trigger);
//                telemetry.addData("up", upperLimit);
//                telemetry.update();
//            }
//            if (!gamepad1.dpad_up && gamepad1.dpad_down) {
//                relicMotor.setPower(-gamepad1.right_trigger);
//                telemetry.addData("down", upperLimit);
//                telemetry.update();
//            }
//            if (!gamepad1.dpad_up && !gamepad1.dpad_down) {
//                relicMotor.setPower(0);
//                telemetry.addData("a-stop", upperLimit);
//                telemetry.update();
//            }
//            if (gamepad1.a) {
//                relicMotor.setPower(0);
//                telemetry.addData("stop", upperLimit);
//                telemetry.update();
//            }

            //WITHIN LIMIT
            if (relicMotor.getCurrentPosition() < 11100 && relicMotor.getCurrentPosition() > 650) {
                if (gamepad1.dpad_up && !gamepad1.dpad_down) {
                    relicMotor.setPower(gamepad1.right_trigger);
                    telemetry.addData("up", upperLimit);
                    telemetry.update();
                }
                if (!gamepad1.dpad_up && gamepad1.dpad_down) {
                    relicMotor.setPower(-gamepad1.right_trigger);
                    telemetry.addData("down", upperLimit);
                    telemetry.update();
                }
                if (!gamepad1.dpad_up && !gamepad1.dpad_down) {
                    relicMotor.setPower(0);
                    telemetry.addData("a-stop", upperLimit);
                    telemetry.update();
                }
            }

            //UPPER LIMIT EXCEEDED
            if (relicMotor.getCurrentPosition() >= 11100) {
                if (gamepad1.dpad_up && !gamepad1.dpad_down) {
                    relicMotor.setPower(0.2);
                    telemetry.addData("up <upper limit exceeded>", upperLimit);
                    telemetry.update();
                }
                if (!gamepad1.dpad_up && gamepad1.dpad_down) {
                    relicMotor.setPower(-gamepad1.right_trigger);
                    telemetry.addData("down <upper limit exceeded>", upperLimit);
                    telemetry.update();
                }
                if (!gamepad1.dpad_up && !gamepad1.dpad_down) {
                    relicMotor.setPower(0);
                    telemetry.addData("a-stop <upper limit ecxeeded>", upperLimit);
                    telemetry.update();
                }
            }

            //LOWER LIMIT EXCEEDED
            if (relicMotor.getCurrentPosition() <= 750) {
                if (gamepad1.dpad_up && !gamepad1.dpad_down) {
                    relicMotor.setPower(gamepad1.right_trigger);
                    telemetry.addData("up <lower limit exceeded>", lowerLimit);
                    telemetry.update();
                }
                if (!gamepad1.dpad_up && gamepad1.dpad_down) {
                    relicMotor.setPower(-0.2);
                    telemetry.addData("down <lower limit exceeded>", lowerLimit);
                    telemetry.update();
                }
                if (!gamepad1.dpad_up && !gamepad1.dpad_down) {
                    relicMotor.setPower(0);
                    telemetry.addData("a-stop <lower limit exceeded>", lowerLimit);
                    telemetry.update();
                }
            }

            //AUTOMATIC STOP
            if (!gamepad1.dpad_up && !gamepad1.dpad_down) {
                relicMotor.setPower(0);
                telemetry.addData("a-stop", upperLimit);
                telemetry.update();
            }

//            if (gamepad1.left_trigger > 0.5) {
//                relicArm.setPosition(0.47);
//            }

            if (gamepad1.a && (gamepad1.left_trigger < 0.5)) {
                pos += 0.005;
            }

            if (gamepad1.b && (gamepad1.left_trigger < 0.5)) {
                pos -= 0.005;
            }

            pos = Range.clip(pos, 0, 1);
            relicArm.setPosition(pos);

            if (gamepad1.x) {
                pos2 += 0.01;
            }

            if (gamepad1.y) {
                pos2 -= 0.01;
            }

            pos2 = Range.clip(pos2,0.4,1);
            relicGrab.setPosition(pos2);

<<<<<<< HEAD
            if (gamepad1.left_trigger > 0.5) {
                relicArm.setPosition(0.47);
            }
=======

            if (gamepad1.left_trigger > 0.5) {
                relicArm.setPosition(0.47);
            }


>>>>>>> eeb2f56eec44da48ac0e68a9d7046ba9016b6d38

            telemetry.addData("RDC: ", relicMotor.getCurrentPosition());
            telemetry.addData("RDC power: ", relicMotor.getPower());
            telemetry.addData("RA: ", relicArm.getPosition());
            telemetry.addData("RG: ", relicGrab.getPosition());
            telemetry.update();

        }

    }
}
 // RG MAX = 0.41 MIN = 0.72
 // RA 0.47 or 0.465