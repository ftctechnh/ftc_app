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

        waitForStart();

        while (opModeIsActive()) {

            //Grabber
            if (gamepad2.right_stick_button){
                grabMotor.setPower(gamepad2.right_trigger);
            }

            if (gamepad2.left_stick_button){
                grabMotor.setPower(-gamepad2.right_trigger);
            }

            if (!gamepad2.right_stick_button && !gamepad2.left_stick_button){
                grabMotor.setPower(0);
            }

            //Outter grabber
            if (gamepad2.left_bumper){
                grabTopLeft.setPosition(1);
                grabTopRight.setPosition(1);
            }

            if (gamepad2.right_bumper){
                grabTopLeft.setPosition(0);
                grabTopRight.setPosition(0);
            }

            //Inner grabber






            //Ball
            if (gamepad2.dpad_down){
                jewelArm.setPosition(1);
            }

            if (gamepad2.dpad_up){
                jewelArm.setPosition(0);
            }


            //Relic
            if (gamepad2.a){
                relicMotor.setPower(gamepad2.left_trigger);
            }

            if (gamepad2.b){
                relicMotor.setPower(-gamepad2.left_trigger);
            }

            if (!gamepad2.a && !gamepad2.b){
                relicMotor.setPower(0);
            }

            if (gamepad2.x){
                relicArm.setPosition(1);
                relicGrab.setPosition(1);
            }

            if (gamepad2.y){
                relicArm.setPosition(0);
                relicGrab.setPosition(0);
            }



        }
    }
}
