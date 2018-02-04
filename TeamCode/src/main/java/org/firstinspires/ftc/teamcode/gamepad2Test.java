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
    private Servo relicServo1;
    private Servo relicServo2;

    //Ball
    private Servo knockerServo;
    private Servo armServo;


    @Override
    public void runOpMode() throws InterruptedException {

        //Grabber
        grabMotor = hardwareMap.get(DcMotor.class, "grabMotor");
        grabTopLeft = hardwareMap.get(Servo.class, "grabTopLeft");
        grabTopRight = hardwareMap.get(Servo.class, "grabTopRight");
        grabBottomLeft = hardwareMap.get(Servo.class, "grabBottomLeft");
        grabBottomRight = hardwareMap.get(Servo.class, "grabBottomRight");

        //Relic
        relicMotor = hardwareMap.get(DcMotor.class,"relic");
        relicServo1 = hardwareMap.get(Servo.class, "relicServo1");
        relicServo2 = hardwareMap.get(Servo.class, "relicServo2");

        //Ball
        knockerServo = hardwareMap.get(Servo.class, "knockerServo");
        armServo = hardwareMap.get(Servo.class, "armServo");

        waitForStart();

        while (opModeIsActive()) {

            //Grabber
            if (gamepad2.right_stick_button){
                grabMotor.setPower(gamepad2.right_trigger);
            }

            if (gamepad2.left_stick_button){
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
                armServo.setPosition(1);
            }

            if (gamepad2.dpad_up){
                armServo.setPosition(0);
            }

            if (gamepad2.dpad_right){
                knockerServo.setPosition(1);
            }

            if (gamepad2.dpad_left){
                knockerServo.setPosition(0);
            }

            //Relic
            if (gamepad2.a){
                relicMotor.setPower(gamepad2.left_trigger);
            }

            if (gamepad2.b){
                relicMotor.setPower(0);
            }

            if (gamepad2.x){
                relicServo1.setPosition(1);
                relicServo2.setPosition(1);
            }

            if (gamepad2.y){
                relicServo1.setPosition(0);
                relicServo2.setPosition(0);
            }



        }
    }
}
