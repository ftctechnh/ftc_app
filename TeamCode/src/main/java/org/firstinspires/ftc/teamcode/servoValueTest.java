package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Servo;

/**
 * Created by anshnanda on 04/02/18.
 */

@TeleOp (name = "Servo motor value test", group = "z tests")

public class servoValueTest extends LinearOpMode{

    //GamePad 2
    //Grabber
//    private DcMotor grabMotor;
    private Servo grabTopLeft;
    private Servo grabTopRight;
    private Servo grabBottomLeft;
    private Servo grabBottomRight;

    //Relic
//    private DcMotor relicMotor;
//    private Servo relicServo1;
//    private Servo relicServo2;
//
//    //Ball
//    private Servo knockerServo;
//    private Servo armServo;

    @Override
    public void runOpMode() throws InterruptedException{

        //Grabber
//        grabMotor = hardwareMap.get(DcMotor.class, "grabMotor");
        grabTopLeft = hardwareMap.get(Servo.class, "grabTopLeft");
        grabTopRight = hardwareMap.get(Servo.class, "grabTopRight");
        grabBottomLeft = hardwareMap.get(Servo.class, "grabBottomLeft");
        grabBottomRight = hardwareMap.get(Servo.class, "grabBottomRight");

//        //Relic
//        relicMotor = hardwareMap.get(DcMotor.class,"relic");
//        relicServo1 = hardwareMap.get(Servo.class, "relicServo1");
//        relicServo2 = hardwareMap.get(Servo.class, "relicServo2");
//
//        //Ball
//        knockerServo = hardwareMap.get(Servo.class, "knockerServo");
//        armServo = hardwareMap.get(Servo.class, "armServo");

        char currServo = ' ';

        waitForStart();

        while (opModeIsActive()){

            if(gamepad1.a)
            {
                currServo = 'a';
            }
            if(gamepad1.b)
            {
                currServo = 'b';
            }
            if(gamepad1.x)
            {
                currServo = 'x';
            }
            if(gamepad1.y)
            {
                currServo = 'y';
            }

            switch(currServo)
            {
                case 'a':
                    grabTopLeft.setPosition(gamepad1.right_trigger);
                    telemetry.addData("Servo in use : ", "grab top left");
                    break;
                case 'b':
                    grabTopRight.setPosition(gamepad1.right_trigger);
                    telemetry.addData("Servo in use : ", "grab top right");
                    break;
                case 'x':
                    grabBottomLeft.setPosition(gamepad1.right_trigger);
                    telemetry.addData("Servo in use : ", "Grab bottom left");
                    break;
                case 'y':
                    grabBottomRight.setPosition(gamepad1.right_trigger);
                    telemetry.addData("Servo in use : ", "Grab bottom right");
                    break;

                default:

                    telemetry.addData("Servo in use : ", "none");
            }

            telemetry.addData("Position : " , gamepad1.right_trigger);
            telemetry.update();
            idle();

        }

    }

}