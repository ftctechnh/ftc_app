package org.firstinspires.ftc.teamcode.OLD;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

/**
 * Created by anshnanda and shivaan on 11/02/18.
 */

@TeleOp(name = "final tele op", group = "final")

public class finalTeleOp extends LinearOpMode{

    //Gamepad 1
    private DcMotor motorFrontLeft;
    private DcMotor motorBackLeft;
    private DcMotor motorFrontRight;
    private DcMotor motorBackRight;

    //GamePad 2
    //Grabber
    private DcMotor grabMotor;
    private Servo grabTopLeft;
    private Servo grabTopRight;
    private Servo grabBottomLeft;
    private Servo grabBottomRight;

    //Relic
    private DcMotor relicMotor;
    private Servo relicArm;
    private Servo relicGrab;

    //Jewel
    private Servo jewelArm;
    private Servo jewelKnock;

    //Variables
    private int mode;
    private int grabberTop;
    private int grabberMiddle;
    private int grabberRest;
    private double relicArmPosition;
    private double relicArmDelta;
    private double relicGrabPosition;
    private double relicGrabDelta;




    @Override
    public void runOpMode() throws InterruptedException {
        motorFrontLeft = hardwareMap.dcMotor.get("MC1M1");
        motorBackLeft = hardwareMap.dcMotor.get("MC1M2");
        motorFrontRight = hardwareMap.dcMotor.get("MC2M1");
        motorBackRight = hardwareMap.dcMotor.get("MC2M2");
        motorFrontLeft.setDirection(DcMotor.Direction.REVERSE);
        motorBackLeft.setDirection(DcMotor.Direction.REVERSE);


        //Grabber
        grabMotor = hardwareMap.get(DcMotor.class, "GrabDC");
//        grabTopLeft = hardwareMap.get(Servo.class, "GTL");
//        grabTopRight = hardwareMap.get(Servo.class, "GTR");
//        grabBottomLeft = hardwareMap.get(Servo.class, "GBL");
//        grabBottomRight = hardwareMap.get(Servo.class, "GBR");

        //Relic
        relicMotor = hardwareMap.get(DcMotor.class,"RelicDC");
//        relicArm = hardwareMap.get(Servo.class, "RA");
//        relicGrab = hardwareMap.get(Servo.class, "RG");

        //Jewewl
//        jewelArm = hardwareMap.get(Servo.class, "JA");
//        jewelKnock = hardwareMap.get(Servo.class, "JK");

        //Varibales
        mode = 1;
        grabberTop = 3500;
        grabberMiddle = 1500;
        grabberRest= 0;
        relicArmDelta = 0.01;
        relicArmPosition = 0.1;
        relicGrabDelta = 0.01;
//        relicArmPosition = 0;

        //Initial positions
//        jewelArm.setPosition(0.77);
//        jewelKnock.setPosition(0);
//        relicArm.setPosition(0);

        waitForStart();

        while (opModeIsActive()){

            gamepad1.setJoystickDeadzone(0.3f);

            //GamePad 1
            double r = Math.hypot(gamepad1.left_stick_x, gamepad1.left_stick_y);
            telemetry.addData("r = ", r);
            double robotAngle = Math.atan2(-gamepad1.left_stick_y, gamepad1.left_stick_x) - Math.PI / 4;
            telemetry.addData("robotAngle = ", robotAngle);
            double rightX = gamepad1.right_stick_x;
            //telemetry.addData("rightX = ", rightX);
            final double v1 = r * Math.cos(robotAngle) + rightX;
            //telemetry.addData("front left power = ", v1);
            final double v2 = r * Math.sin(robotAngle) - rightX;
            //telemetry.addData("front right power = ", v2);
            final double v3 = r * Math.sin(robotAngle) + rightX;
            //telemetry.addData("back left power = ", v3);
            final double v4 = r * Math.cos(robotAngle) - rightX;
            //telemetry.addData("back right power = ", v4);

            motorFrontLeft.setPower(v1);
            motorFrontRight.setPower(v2);
            motorBackLeft.setPower(v3);
            motorBackRight.setPower(v4);

            //Degrees travlled at this point
            telemetry.addData("front left degrees = ", motorFrontLeft.getCurrentPosition());
            telemetry.addData("front right degrees = ",motorFrontRight.getCurrentPosition());
            telemetry.addData("back left degrees = ", motorBackLeft.getCurrentPosition());
            telemetry.addData("back right degrees = ", motorBackRight.getCurrentPosition());
            telemetry.update();

            if (gamepad1.a){
                motorBackLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                motorFrontLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                motorBackRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                motorFrontRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

                motorFrontLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
                motorFrontRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
                motorBackLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
                motorBackRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            }


            if (gamepad2.x) {
                mode = 1;
            }
            if (gamepad2.y) {
                mode = 2;
            }


            //Gamepad 2
            if (mode == 1) {
                //Code for grabber only mapping here
                if (gamepad2.dpad_up && !gamepad2.dpad_down){
                    grabMotor.setPower(gamepad2.right_trigger);
                }

                if (!gamepad2.dpad_up && gamepad2.dpad_down) {
                    grabMotor.setPower(-gamepad2.right_trigger);
                }

                if (!gamepad2.dpad_up && !gamepad2.dpad_down) {
                    grabMotor.setPower(0);
                }
                if (gamepad2.dpad_up) {
                    while(grabMotor.getCurrentPosition() < grabberTop){
                        grabMotor.setPower(0.7);
                    }
                    grabMotor.setPower(0);
                }

                if (gamepad2.dpad_down) {
                    while(grabMotor.getCurrentPosition() > grabberMiddle){
                        grabMotor.setPower(0.7);
                    }
                    grabMotor.setPower(0);
                }


                if (gamepad2.a) {

                    while(grabMotor.getCurrentPosition() > grabberRest){
                        grabMotor.setPower(0.7);
                    }
                    grabMotor.setPower(0);
                }

                //GT CLOSE
                if (gamepad2.a) {
                    grabTopLeft.setPosition(0.3);
                    grabTopRight.setPosition(0.4);
                }
                //GT OPEN
                if (gamepad2.b) {
                    grabTopLeft.setPosition(0.395);
                    grabTopRight.setPosition(0.305);
                }
                //GB OPEN
                if (gamepad2.left_bumper) {
                    grabBottomLeft.setPosition(0.25);
                    grabBottomRight.setPosition(0.25); //0.5
                }
                //GB CLOSE
                if (gamepad2.right_bumper) {
                    grabBottomLeft.setPosition(0.38);
                    grabBottomRight.setPosition(0.35); //0.4
                }
                //EXTREME CLOSE
                if (gamepad2.left_stick_button){
                    grabBottomLeft.setPosition(0.8);
                }
                if (gamepad2.right_stick_button){
                    grabBottomRight.setPosition(0.7); //0.1
                }


                telemetry.addData("Grab Motor Encoder: ", grabMotor.getCurrentPosition());
            }

            if (mode == 2) {
                //Code for endgame here (relic and jewel)

                //Relic DC Motor

                if (gamepad2.dpad_up) {
                    relicMotor.setPower(0.7);
                }

                if (gamepad2.dpad_down) {
                    relicMotor.setPower(-0.7);
                }

                if (!gamepad2.dpad_up && !gamepad2.dpad_down) {
                    relicMotor.setPower(0);
                }

//
//                //Relic Arm Servo Manual Mode
//                if (gamepad2.a) {
//                    relicArmPosition += relicArmDelta;
//                }
//                if (gamepad2.b) {
//                    relicArmPosition -= relicArmDelta;
//                }
//                if (gamepad2.left_stick_button) {
//                    relicArmPosition = 0.27;
//                }
//                relicArmPosition = Range.clip(relicArmPosition, 0, 0.9);
                relicArm.setPosition(relicArmPosition);
                telemetry.addData("Relic Arm Servo: ", relicArm.getPosition());



                //Relic Grab Servo Manual Mode
//                if (gamepad2.left_bumper) {
//                    relicGrabPosition += relicGrabDelta;
//                }
//                if (gamepad2.right_bumper) {
//                    relicGrabPosition -= relicGrabDelta;
//                }
//                if (gamepad2.right_stick_button) {
//                    relicGrabPosition = 0.38;
//                }
//                relicGrabPosition = Range.clip(relicGrabPosition, 0, 1);
//                relicGrab.setPosition(relicGrabPosition);
                telemetry.addData("Relic Grab Servo: ", relicGrab.getPosition());


                telemetry.addData("Relic DC Encoder: ", grabMotor.getCurrentPosition());
            }

            telemetry.update();

        }
    }
}
//arm closed = 0.26