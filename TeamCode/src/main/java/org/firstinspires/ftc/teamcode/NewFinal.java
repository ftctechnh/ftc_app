package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.Range;

/**
 * Created by shiva on 22-05-2018.
 */

@TeleOp(name = "final tele op", group = "final")

public class NewFinal extends LinearOpMode {

    //Driving
    private DcMotor motorFrontLeft;
    private DcMotor motorBackLeft;
    private DcMotor motorFrontRight;
    private DcMotor motorBackRight;

    //Grabber
    private DcMotor grabMotor;
    private Servo grabTopLeft;
    private Servo grabBottomLeft;
    private Servo grabTopRight;
    private Servo grabBottomRight;
    private int grabberTop;
    private int grabberMiddle;
    private int grabberRest;

    //Relic
    private DcMotor relicMotor;
    private Servo relicArm;
    private Servo relicGrab;
    private double relicArmPosition;
    private double relicArmDelta;
    private double relicGrabPosition;
    private double relicGrabDelta;

    //Jewel
    private Servo jewelArm;
    private Servo jewelKnock;

    private int mode;


    @Override
    public void runOpMode() throws InterruptedException {
        //Driver
        motorFrontLeft = hardwareMap.dcMotor.get("MC1M1");
        motorBackLeft = hardwareMap.dcMotor.get("MC1M2");
        motorFrontRight = hardwareMap.dcMotor.get("MC2M1");
        motorBackRight = hardwareMap.dcMotor.get("MC2M2");

        //Grabber
        grabMotor = hardwareMap.get(DcMotor.class, "GrabDC");
        grabTopLeft = hardwareMap.get(Servo.class, "GTL");
        grabBottomLeft = hardwareMap.get(Servo.class, "GBL");
        grabTopRight = hardwareMap.get(Servo.class, "GTR");
        grabBottomRight = hardwareMap.get(Servo.class, "GBR");
        grabberTop = 3500;
        grabberMiddle = 1500;
        grabberRest = 0;

        //Relic
        relicMotor = hardwareMap.get(DcMotor.class, "RelicDC");
        relicArm = hardwareMap.get(Servo.class, "RA");
        relicGrab = hardwareMap.get(Servo.class, "RG");
        relicArmDelta = 0.01;
        relicArmPosition = 0.1;
        relicGrabDelta = 0.01;

        //Jewel
        jewelArm = hardwareMap.get(Servo.class, "JA");
        jewelKnock = hardwareMap.get(Servo.class, "JK");

        mode = 1;

        waitForStart();

        while(opModeIsActive()) {

            //----------DRIVING----------
            double r = Math.hypot(gamepad1.left_stick_x, gamepad1.left_stick_y);
            telemetry.addData("r: ", r);
            double robotAngle = Math.atan2(-gamepad1.left_stick_y, gamepad1.left_stick_x);
            telemetry.addData("robotAngl: ", robotAngle);
            double rightX = gamepad1.right_stick_x;
            telemetry.addData("rightX: ", rightX);
            final double v1 = r * Math.cos(robotAngle) + rightX;
            telemetry.addData("FL: ", v1);
            final double v2 = r * Math.sin(robotAngle) - rightX;
            telemetry.addData("FR: ", v2);
            final double v3 = r * Math.sin(robotAngle) + rightX;
            telemetry.addData("BL: ", v3);
            final double v4 = r * Math.cos(robotAngle) - rightX;
            telemetry.addData("BR: ", v4);

            motorFrontLeft.setPower(v1);
            motorFrontRight.setPower(v2);
            motorBackRight.setPower(v4);
            motorBackLeft.setPower(v3);

            //----------SECONDARY----------

            //MODE SWITCH
            if (gamepad1.x) {
                mode = 1;
            }
            if (gamepad1.x) {
                mode = 2;
            }

            //MODE-1
            if (mode == 1) {
                //GRABBER <UP>
                if (gamepad2.dpad_up && !gamepad2.dpad_down) {
                    grabMotor.setPower(gamepad2.right_trigger);
                }
                //GRABBER <DOWN>
                if (!gamepad2.dpad_up && gamepad2.dpad_down) {
                    grabMotor.setPower(-gamepad2.right_trigger);
                }
                //GRABBER <STOP>
                if (!gamepad2.dpad_up && !gamepad2.dpad_down) {
                    grabMotor.setPower(0);
                }
                //GRABBER UPPER LIMIT
                if (gamepad2.dpad_up) {
                    while (grabMotor.getCurrentPosition() > grabberTop) {
                        grabMotor.setPower(0.7);
                    }
                    grabMotor.setPower(0);
                }
                //GRABBER LOWER LIMIT
                if (gamepad2.dpad_down) {
                    while (grabMotor.getCurrentPosition() > grabberMiddle) {
                        grabMotor.setPower(0.7);
                    }
                    grabMotor.setPower(0);
                }
//                if (gamepad2.a) {
//                    while (grabMotor.getCurrentPosition() >grabberRest) {
//                        grabMotor.setPower(0.7);
//                    }
//                    grabMotor.setPower(0);
//                }
                //GRABBER POSITION
                telemetry.addData("Grab Motor Encoder: ", grabMotor.getCurrentPosition());

                //TOP GRAB <CLOSE>
                if (gamepad2.a) {

                }
                //TOP GRAB <OPEN>
                if (gamepad2.b) {

                }
                //BOTTOM GRAB <CLOSE>
                if (gamepad2.left_bumper) {

                }
                //BOTTOM GRAB <EXTREME CLOSE>
                if (gamepad2.left_stick_button) {

                }
                //BOTTOM GRAB <OPEN>
                if (gamepad2.right_bumper) {

                }
                //BOTTOM GRAB <EXTREME OPEN>
                if (gamepad2.right_stick_button) {

                }
            }

            //MODE-2
            if (mode == 2) {
                
            }

        }
    }

}
