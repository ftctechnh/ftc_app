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

public class TeleopSingleAlternate extends LinearOpMode {
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
        Motor4.setDirection(Direction.REVERSE);


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
        double x = 0;
        double forward = 0;
        double backward =0;

        boolean meme = false;
        waitForStart();
        while (opModeIsActive()) {

            x = gamepad1.left_stick_x;
            forward = gamepad1.right_trigger*0.75;
            backward = gamepad1.left_trigger*0.75;
            if(gamepad1.right_trigger== 0 && gamepad1.left_trigger ==0){

                Motor2.setPower(0.6*x);
                Motor4.setPower(0.6*x);
                Motor1.setPower(0.6*-x);
                Motor3.setPower(0.6*-x);

            }
            else if(forward != 0){
                if (x>=0){
                    Motor2.setPower(forward);
                    Motor4.setPower(forward);
                    Motor1.setPower(forward*(1-x));
                    Motor3.setPower(forward*(1-x));

                }
                else{
                    Motor1.setPower(forward);
                    Motor3.setPower(forward);
                    Motor2.setPower(forward*(1+x));
                    Motor4.setPower(forward*(1+x));
                }

            }
            else{
                if (x>=0){
                    Motor2.setPower(-backward);
                    Motor4.setPower(-backward);
                    Motor1.setPower(-backward*(1-x));
                    Motor3.setPower(-backward*(1-x));

                }
                else{
                    Motor1.setPower(-backward);
                    Motor3.setPower(-backward);
                    Motor2.setPower(-backward*(1+x));
                    Motor4.setPower(-backward*(1+x));
                }

            }




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





            if (gamepad1.dpad_up == true) {
//                MoveLift(1000,0.5);
                Winch1.setPower(-0.6);

            }
            else if (gamepad1.dpad_down == true) {
//                MoveLift(-1000, 0.5);
                Winch1.setPower(0.4);
            }
            else {
                Winch1.setPower(0.01);
            }

            if(gamepad1.y){
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
