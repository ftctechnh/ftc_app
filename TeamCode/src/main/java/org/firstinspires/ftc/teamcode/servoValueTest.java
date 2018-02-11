package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.ServoController;
import com.qualcomm.robotcore.hardware.ServoControllerEx;

/**
 * Created by anshnanda on 04/02/18.
 */

@TeleOp (name = "Servo motor value test", group = "test")

public class servoValueTest extends LinearOpMode{

    //GamePad 2
    //Grabber
    //private DcMotor grabMotor;
    //private Servo grabTopLeft;
    //private Servo grabTopRight;
    //private Servo grabBottomLeft;
    //private Servo grabBottomRight;

    //Relic
    private DcMotor relicDC;
    private Servo relicArm;
    private Servo relicGrab;

    //Ball
    private Servo jewelArm;
    private Servo jewelKnock;

    @Override
    public void runOpMode() throws InterruptedException{

        //Grabber
        //grabMotor = hardwareMap.get(DcMotor.class, "grabMotor");
        //grabTopLeft = hardwareMap.get(Servo.class, "GTL");
        //grabTopRight = hardwareMap.get(Servo.class, "GTR");
        //grabBottomLeft = hardwareMap.get(Servo.class, "GBL");
        //grabBottomRight = hardwareMap.get(Servo.class, "GBR");

        //Relic
        relicDC = hardwareMap.get(DcMotor.class,"RDC");
        relicArm = hardwareMap.get(Servo.class, "RA");
        relicGrab = hardwareMap.get(Servo.class, "RG");

        //JEWEL
        jewelArm = hardwareMap.get(Servo.class, "JA");
        jewelKnock = hardwareMap.get(Servo.class, "JK");

        telemetry.clear();

        relicGrab.setPosition(0.4);
        relicArm.setPosition(0.1);

        waitForStart();

        while (opModeIsActive()){

//            if(gamepad1.a)
//            {
//                currServo = 'a';
//            }
//            if(gamepad1.b)
//            {
//                currServo = 'b';
//            }
//            if(gamepad1.x)
//            {
//                currServo = 'x';
//            }
//            if(gamepad1.y)
//            {
//                currServo = 'y';
//            }
//
//            switch(currServo)
//            {
//                case 'a':
//                    grabTopLeft.setPosition(gamepad1.right_trigger);
//                    telemetry.addData("Servo in use : ", "grab top left");
//                    break;
//                case 'b':
//                    grabTopRight.setPosition(gamepad1.right_trigger);
//                    telemetry.addData("Servo in use : ", "grab top right");
//                    break;
//                case 'x':
//                    grabBottomLeft.setPosition(gamepad1.right_trigger);
//                    telemetry.addData("Servo in use : ", "Grab bottom left");
//                    break;
//                case 'y':
//                    grabBottomRight.setPosition(gamepad1.right_trigger);
//                    telemetry.addData("Servo in use : ", "Grab bottom right");
//                    break;
//
//                default:
//
//                    telemetry.addData("Servo in use : ", "none");
//            }

            if (gamepad1.a){
                relicArm.setPosition(relicArm.getPosition() + 0.1);
            }

            if (gamepad1.b){
                relicArm.setPosition(relicArm.getPosition() - 0.1);
            }

            if (gamepad1.x){
                relicGrab.setPosition(relicArm.getPosition() + 0.1);
            }

            if (gamepad1.y){
                relicGrab.setPosition(relicArm.getPosition() - 0.1);
            }

            if (gamepad1.dpad_up && !gamepad1.dpad_down){
                relicDC.setPower(gamepad1.right_trigger);
            }

            if (!gamepad1.dpad_up && gamepad1.dpad_down){
                relicDC.setPower(-gamepad1.right_trigger);
            }

            if (!gamepad1.dpad_up && !gamepad1.dpad_down){
                relicDC.setPower(0);
            }

            telemetry.addData("Relic arm: ", relicArm.getPosition());
            telemetry.addData("relic grabber: ", relicGrab.getPosition());
            telemetry.update();
            idle();

        }

    }

}

//grab servo values should be between 0.4 and 0.6
//for the right side it should be 0.4 for tight grip while for the left 0.6 for the same
