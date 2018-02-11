package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

/**
 * Created by anshnanda on 03/02/18.
 */

@TeleOp(name = "gamepad 2 test", group = "test")

public class gamepad2Test extends LinearOpMode {


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

    private int mode;
    private int grabberTop;
    private int grabberMiddle;
    private int grabberRest;

    @Override
    public void runOpMode() throws InterruptedException {

        //Grabber
        grabMotor = hardwareMap.get(DcMotor.class, "GDC");
        grabTopLeft = hardwareMap.get(Servo.class, "GTL");
        grabTopRight = hardwareMap.get(Servo.class, "GTR");
        grabBottomLeft = hardwareMap.get(Servo.class, "GBL");
        grabBottomRight = hardwareMap.get(Servo.class, "GBR");

        //Relic
        relicMotor = hardwareMap.get(DcMotor.class,"RDC");
        relicArm = hardwareMap.get(Servo.class, "RA");
        relicGrab = hardwareMap.get(Servo.class, "RG");

        //Ball
        jewelArm = hardwareMap.get(Servo.class, "JA");

        mode = 0;
        grabberTop = 3500;
        grabberMiddle = 1500;
        grabberRest= 0;

        waitForStart();

        while (opModeIsActive()) {


            if (gamepad2.x) {
                mode = 1;
            }
            if (gamepad2.y) {
                mode = 2;
            }



            if (mode == 1) {
                //Code for grabber only mapping here
                if (gamepad2.dpad_up) {
                    while(grabMotor.getCurrentPosition() < grabberTop){
                        grabMotor.setPower(0.7);
                    }

                }
                telemetry.addData("Encoder value: ", grabMotor.getCurrentPosition());
                if (gamepad2.dpad_down) {
                    while(grabMotor.getCurrentPosition() < grabberMiddle){
                        grabMotor.setPower(0.7);
                    }
                }
                telemetry.addData("Encoder value: ", grabMotor.getCurrentPosition());

                if (gamepad2.a) {
                    while(grabMotor.getCurrentPosition() < grabberRest){
                        grabMotor.setPower(0.7);
                    }
                }

                if (gamepad2.dpad_left) {
                    grabTopLeft.setPosition(0.4);
                    grabTopRight.setPosition(0.35);
                }
                if (gamepad2.dpad_right) {
                    grabTopLeft.setPosition(0.6);
                    grabTopRight.setPosition(0.35);
                }

                if (gamepad2.left_bumper) {
                    grabBottomLeft.setPosition(0.25);
                    grabBottomRight.setPosition(0.55);
                }
                if (gamepad2.right_bumper) {
                    grabBottomLeft.setPosition(0.6);
                    grabBottomRight.setPosition(0.4);
                }
            }

            if (mode == 2) {
                //Code for endgame here (relic and jewel)
                if (gamepad2.a) {
                    relicMotor.setPower(0);
                }

                if (gamepad2.dpad_up) {
                    relicMotor.setPower(gamepad2.right_trigger);
                }

                if (gamepad2.dpad_down) {
                    relicMotor.setPower(-gamepad2.right_trigger);
                }



                telemetry.addData("Encoder value (relic dc): ", grabMotor.getCurrentPosition());
            }

//            //Grabber
//            if (gamepad2.right_stick_button){
//                grabMotor.setPower(gamepad2.right_trigger);
//            }
//
//            if (gamepad2.left_stick_button){
//                grabMotor.setPower(-gamepad2.right_trigger);
//            }
//
//            if (!gamepad2.right_stick_button && !gamepad2.left_stick_button){
//                grabMotor.setPower(0);
//            }
//
//            //Outter grabber
//            if (gamepad2.left_bumper){
//                grabTopLeft.setPosition(1);
//                grabTopRight.setPosition(1);
//            }
//
//            if (gamepad2.right_bumper){
//                grabTopLeft.setPosition(0);
//                grabTopRight.setPosition(0);
//            }
//
//            //Inner grabber
//
//
//
//
//
//
//            //Ball
//            if (gamepad2.dpad_down){
//                jewelArm.setPosition(1);
//            }
//
//            if (gamepad2.dpad_up){
//                jewelArm.setPosition(0);
//            }
//
//
//            //Relic
//            if (gamepad2.a){
//                relicMotor.setPower(gamepad2.left_trigger);
//            }
//
//            if (gamepad2.b){
//                relicMotor.setPower(-gamepad2.left_trigger);
//            }
//
//            if (!gamepad2.a && !gamepad2.b){
//                relicMotor.setPower(0);
//            }
//
//            if (gamepad2.x){
//                relicArm.setPosition(1);
//                relicGrab.setPosition(1);
//            }
//
//            if (gamepad2.y){
//                relicArm.setPosition(0);
//                relicGrab.setPosition(0);
//            }

        telemetry.update();

        }
    }
}