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
 * Created by shiva on 25-05-2018.
 */

@TeleOp(name = "compiled", group = "prototype")

public class COMPILED extends LinearOpMode {

    //DRIVING
    private DcMotor motorFrontLeft;
    private DcMotor motorBackLeft;
    private DcMotor motorFrontRight;
    private DcMotor motorBackRight;

    //GRAB
    private DcMotor grabMotor;
    private Servo grabTopLeft;
    private Servo grabTopRight;
    private Servo grabBottomLeft;
    private Servo grabBottomRight;

    private double gtlOPEN = 0.71;
    private double gtlGRAB = 0.31;

    private double gtrOPEN = 0.02;
    private double gtrGRAB = 0.37;

    private double gblOPEN = 0.04;
    private double gblGRAB = 0.53;

    private double gbrOPEN = 0.74;
    private double gbrGRAB = 0.27;

    //RELIC
    private DcMotor relicMotor;
    private Servo relicArm;
    private Servo relicGrab;

    private double pos;
    private double pos2;

    private int mode;

    @Override
    public void runOpMode() throws InterruptedException {

        //DRIVING
        motorFrontLeft = hardwareMap.dcMotor.get("MC1M1");
        motorBackLeft = hardwareMap.dcMotor.get("MC1M2");
        motorFrontRight = hardwareMap.dcMotor.get("MC2M1");
        motorBackRight = hardwareMap.dcMotor.get("MC2M2");

        motorFrontLeft.setDirection(DcMotor.Direction.REVERSE);
        motorBackLeft.setDirection(DcMotor.Direction.REVERSE);

        //GRAB
        grabMotor = hardwareMap.get(DcMotor.class, "GrabDC");
        grabTopLeft = hardwareMap.get(Servo.class, "GTL");
        grabBottomLeft = hardwareMap.get(Servo.class, "GBL");
        grabTopRight = hardwareMap.get(Servo.class, "GTR");
        grabBottomRight = hardwareMap.get(Servo.class, "GBR");

        grabMotor.setMode(STOP_AND_RESET_ENCODER);
        grabMotor.setMode(RUN_USING_ENCODER);
        grabMotor.setDirection(REVERSE);

        grabTopLeft.setPosition(gtlOPEN);
        grabTopRight.setPosition(gtrOPEN);
        grabBottomLeft.setPosition(gblOPEN);
        grabBottomRight.setPosition(gbrOPEN);

        //RELIC
        relicMotor = hardwareMap.get(DcMotor.class, "RelicDC");
        relicArm = hardwareMap.get(Servo.class, "RA");
        relicGrab = hardwareMap.get(Servo.class, "RG");

        relicMotor.setMode(STOP_AND_RESET_ENCODER);
        relicMotor.setMode(RUN_USING_ENCODER);
        relicMotor.setDirection(REVERSE);

        relicArm.setPosition(0.5);
        relicGrab.setPosition(0.5);

        pos = 0;
        pos2 = 0;

        mode = 1;

        waitForStart();

        while (opModeIsActive()) {

            /**GAMEPAD-1*/

            //--------------------DRIVING---------------------//
            double r = Math.hypot(gamepad1.left_stick_x, gamepad1.left_stick_y);
            telemetry.addData("r = ", r);
            double robotAngle = Math.atan2(-gamepad1.left_stick_y, gamepad1.left_stick_x) - Math.PI / 4;
            telemetry.addData("robotAngle = ", robotAngle);
            double rightX = gamepad1.right_stick_x;
            //telemetry.addData("rightX = ", rightX);
            final double v1 = r * Math.cos(robotAngle) + rightX;
            telemetry.addData("front left power = ", v1);
            final double v2 = r * Math.sin(robotAngle) - rightX;
            telemetry.addData("front right power = ", v2);
            final double v3 = r * Math.sin(robotAngle) + rightX;
            telemetry.addData("back left power = ", v3);
            final double v4 = r * Math.cos(robotAngle) - rightX;
            telemetry.addData("back right power = ", v4);

            telemetry.update();

            motorFrontLeft.setPower(v1);
            motorFrontRight.setPower(v2);
            motorBackLeft.setPower(v3);
            motorBackRight.setPower(v4);


            /**GAMEPAD-2*/

            if (gamepad2.x) {
                mode = 1;
            }
            if (gamepad2.y) {
                mode = 2;
            }

            if (mode == 1) {
                //--------------------GRABBING--------------------//
                telemetry.addData("Position: ", grabMotor.getCurrentPosition());
                telemetry.update();

                    //GDC WITHIN LIMIT
                if (grabMotor.getCurrentPosition() < 5050 && grabMotor.getCurrentPosition() > 750) {
                    if (gamepad2.dpad_up && !gamepad2.dpad_down) {
                        grabMotor.setPower(gamepad2.right_trigger);
                        telemetry.addLine("up");
                        telemetry.update();
                    }
                    if (!gamepad2.dpad_up && gamepad2.dpad_down) {
                        grabMotor.setPower(-gamepad2.right_trigger);
                        telemetry.addLine("down");
                        telemetry.update();
                    }
                    if (!gamepad2.dpad_up && !gamepad2.dpad_down) {
                        grabMotor.setPower(0);
                        telemetry.addLine("a-stop");
                        telemetry.update();
                    }
                }

                    //UPPER LIMIT EXCEEDED
                if (grabMotor.getCurrentPosition() >= 5050) {
                    if (gamepad2.dpad_up && !gamepad2.dpad_down) {
                        grabMotor.setPower(0.2);
                        telemetry.addLine("up <upper limit exceeded>");
                        telemetry.update();
                    }
                    if (!gamepad2.dpad_up && gamepad2.dpad_down) {
                        grabMotor.setPower(-gamepad2.right_trigger);
                        telemetry.addLine("down <upper limit exceeded>");
                        telemetry.update();
                    }
                    if (!gamepad2.dpad_up && !gamepad2.dpad_down) {
                        grabMotor.setPower(0);
                        telemetry.addLine("a-stop <upper limit ecxeeded>");
                        telemetry.update();
                    }
                }

                    //LOWER LIMIT EXCEEDED
                if (grabMotor.getCurrentPosition() <= 750) {
                    if (gamepad2.dpad_up && !gamepad2.dpad_down) {
                        grabMotor.setPower(gamepad2.right_trigger);
                        telemetry.addLine("up <lower limit exceeded>");
                        telemetry.update();
                    }
                    if (!gamepad2.dpad_up && gamepad2.dpad_down) {
                        grabMotor.setPower(-0.2);
                        telemetry.addLine("down <lower limit exceeded>");
                        telemetry.update();
                    }
                    if (!gamepad2.dpad_up && !gamepad2.dpad_down) {
                        grabMotor.setPower(0);
                        telemetry.addLine("a-stop <lower limit exceeded>");
                        telemetry.update();
                    }
                }

                    //AUTOMATIC STOP
                if (!gamepad2.dpad_up && !gamepad2.dpad_down) {
                    grabMotor.setPower(0);
                    telemetry.addLine("a-stop");
                    telemetry.update();
                }

                    //TOP SERVOS OPEN
                if (gamepad2.a) {
                    grabTopLeft.setPosition(gtlOPEN);
                    grabTopRight.setPosition(gtrOPEN);
                }
                    //TOP SERVOS GRAB
                if (gamepad2.b) {
                    grabTopLeft.setPosition(gtlGRAB);
                    grabTopRight.setPosition(gtrGRAB);
                }
                    //BOTTOM SERVOS OPEN
                if (gamepad2.left_bumper) {
                    grabBottomLeft.setPosition(gblOPEN);
                    grabBottomRight.setPosition(gbrOPEN);
                }
                    //BOTTOM SERVOS GRAB
                if (gamepad2.right_bumper) {
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


            if (mode == 2) {
                //--------------------RELIC--------------------//
                    //WITHIN LIMIT
                if (relicMotor.getCurrentPosition() < 11100 && relicMotor.getCurrentPosition() > 650) {
                    if (gamepad2.dpad_up && !gamepad2.dpad_down) {
                        relicMotor.setPower(gamepad2.right_trigger);
                        telemetry.addLine("up");
                        telemetry.update();
                    }
                    if (!gamepad2.dpad_up && gamepad2.dpad_down) {
                        relicMotor.setPower(-gamepad2.right_trigger);
                        telemetry.addLine("down");
                        telemetry.update();
                    }
                    if (!gamepad2.dpad_up && !gamepad2.dpad_down) {
                        relicMotor.setPower(0);
                        telemetry.addLine("a-stop");
                        telemetry.update();
                    }
                }

                    //UPPER LIMIT EXCEEDED
                if (relicMotor.getCurrentPosition() >= 11100) {
                    if (gamepad2.dpad_up && !gamepad2.dpad_down) {
                        relicMotor.setPower(0.2);
                        telemetry.addLine("up <upper limit exceeded>");
                        telemetry.update();
                    }
                    if (!gamepad2.dpad_up && gamepad2.dpad_down) {
                        relicMotor.setPower(-gamepad2.right_trigger);
                        telemetry.addLine("down <upper limit exceeded>");
                        telemetry.update();
                    }
                    if (!gamepad2.dpad_up && !gamepad2.dpad_down) {
                        relicMotor.setPower(0);
                        telemetry.addLine("a-stop <upper limit ecxeeded>");
                        telemetry.update();
                    }
                }

                    //LOWER LIMIT EXCEEDED
                if (relicMotor.getCurrentPosition() <= 750) {
                    if (gamepad2.dpad_up && !gamepad2.dpad_down) {
                        relicMotor.setPower(gamepad2.right_trigger);
                        telemetry.addLine("up <lower limit exceeded>");
                        telemetry.update();
                    }
                    if (!gamepad2.dpad_up && gamepad2.dpad_down) {
                        relicMotor.setPower(-0.2);
                        telemetry.addLine("down <lower limit exceeded>");
                        telemetry.update();
                    }
                    if (!gamepad2.dpad_up && !gamepad2.dpad_down) {
                        relicMotor.setPower(0);
                        telemetry.addLine("a-stop <lower limit exceeded>");
                        telemetry.update();
                    }
                }

                    //AUTOMATIC STOP
                if (!gamepad2.dpad_up && !gamepad2.dpad_down) {
                    relicMotor.setPower(0);
                    telemetry.addLine("a-stop");
                    telemetry.update();
                }

                    //Relic Arm
                if (gamepad2.a){
                    pos += 0.005;
                }

                if (gamepad2.b){
                    pos -= 0.005;
                }

                relicArm.setPosition(pos);

                    //Relic Grab
                if (gamepad2.left_bumper){
                    pos2 += 0.01;
                }

                if (gamepad2.right_bumper){
                    pos2 -= 0.01;
                }

                relicGrab.setPosition(pos2);

                telemetry.addData("RDC: ", relicMotor.getCurrentPosition());
                telemetry.addData("RDC power: ", relicMotor.getPower());
                telemetry.addData("RA: ", relicArm.getPosition());
                telemetry.addData("RG: ", relicGrab.getPosition());
                telemetry.update();
            }
        }
    }
}
