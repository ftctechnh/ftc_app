package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple.Direction;
import com.qualcomm.robotcore.hardware.Servo;


/**
 * Created by Shreyas on 9/30/17.
 */

@TeleOp

public class Teleop extends LinearOpMode {
    private DcMotor Motor1;
    private DcMotor Motor2;
    private DcMotor Motor3;
    private DcMotor Motor4;



    private DcMotor Winch1;

    private Servo JewelServo;

    private DcMotor GlyphMotor1;
    private DcMotor GlyphMotor2;

    private Servo GlyphFlipper;
    private Servo GlyphFlipper2;

    private DcMotor relic;

    @Override
    public void runOpMode() throws InterruptedException {

        Motor1 = hardwareMap.dcMotor.get("Motor1");
        Motor2 = hardwareMap.dcMotor.get("Motor2");
        Motor3 = hardwareMap.dcMotor.get("Motor3");
        Motor4 = hardwareMap.dcMotor.get("Motor4");
        Motor2.setDirection(Direction.REVERSE);


        Winch1 = hardwareMap.dcMotor.get("Winch1");

        JewelServo = hardwareMap.servo.get("JewelServo");

        GlyphMotor1 = hardwareMap.dcMotor.get("GlyphMotor1");
        GlyphMotor2 = hardwareMap.dcMotor.get("GlyphMotor2");

        GlyphFlipper = hardwareMap.servo.get("GlyphFlipper");
        GlyphFlipper2 = hardwareMap.servo.get("GlyphFlipper2");
        relic = hardwareMap.dcMotor.get("relic");




        ///colorSensor = hardwareMap.colorSensor.get("sensor_color");

        JewelServo.setPosition(0.03);
        GlyphFlipper.setPosition(0.20);
        GlyphFlipper2.setPosition(0.78);

        boolean meme = false;
        waitForStart();
        while (opModeIsActive()) {





            Motor1.setPower(-gamepad1.right_stick_y);
            Motor3.setPower(-gamepad1.right_stick_y);
            Motor2.setPower(-gamepad1.left_stick_y);
            Motor4.setPower(gamepad1.left_stick_y);





            if (gamepad1.left_bumper) {
                GlyphMotor1.setPower(0.65);
                GlyphMotor2.setPower(-0.65);
            }
            else if (gamepad1.right_bumper){
                    GlyphMotor1.setPower(-0.65);
                    GlyphMotor2.setPower(0.65);
                }
            else{
                GlyphMotor1.setPower(0);
                GlyphMotor2.setPower(0);
            }
            relic.setPower(gamepad2.left_stick_y);




            if (gamepad2.dpad_up == true) {
//                MoveLift(1000,0.5);
                Winch1.setPower(-0.6);

            }
            else if (gamepad2.dpad_down == true) {
//                MoveLift(-1000, 0.5);
                Winch1.setPower(0.4);
            }
            else {
                Winch1.setPower(0.01);
            }

            if(gamepad2.y){
                GlyphFlipper.setPosition(8);
                GlyphFlipper2.setPosition(0.08);

            }
            else{
                GlyphFlipper.setPosition(0.2);
                GlyphFlipper2.setPosition(0.78);
            }




        }


    }
}
