package org.firstinspires.ftc.teamcode.Avocado;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

import junit.framework.Test;

@TeleOp(name = "Avacado Mecanum TeleOp", group = "Final")

public class TeleOpMecanum extends OpMode {

    // Gamepad 1
    DcMotor topLeftMotor;
    DcMotor bottomLeftMotor;
    DcMotor topRightMotor;
    DcMotor bottomRightMotor;
    byte posleft = -1;


    //Gamepad 2

    DcMotor hanger;
    DcMotor hanger2;
    DcMotor claw;
    DcMotor tiltMotor;



    @Override
    public void loop() {
        dpad();
        TankDrive();
        //angles();
        //collect();
        //lift();
    }

    public void init(){
        topLeftMotor = hardwareMap.dcMotor.get("topLeftMotor");
        bottomLeftMotor = hardwareMap.dcMotor.get("bottomLeftMotor");
        topRightMotor = hardwareMap.dcMotor.get("topRightMotor");
        bottomRightMotor = hardwareMap.dcMotor.get("bottomRightMotor");
    }

    public void TankDrive() {

        float leftY_gp1 = (-gamepad1.left_stick_y);
        float rightY_gp1 = (gamepad1.right_stick_y);

        topLeftMotor.setPower(leftY_gp1);
        bottomLeftMotor.setPower(leftY_gp1);
        topRightMotor.setPower(rightY_gp1);
        bottomRightMotor.setPower(rightY_gp1);

    }

    public void dpad(){
        if (gamepad1.dpad_left){
            topLeftMotor.setPower(1 * posleft);
            bottomLeftMotor.setPower(-1 * posleft);
            topRightMotor.setPower(-1);
            bottomRightMotor.setPower(1);
        }
        else if (gamepad1.dpad_right){
            topLeftMotor.setPower(-1 * posleft);
            bottomLeftMotor.setPower(1 * posleft);
            topRightMotor.setPower(1);
            bottomRightMotor.setPower(-1);
        }
        else if (gamepad1.dpad_up) {
            topLeftMotor.setPower(0.5 * posleft);
            bottomLeftMotor.setPower(0.5 * posleft);
            topRightMotor.setPower(0.5);
            bottomRightMotor.setPower(0.5);


        }
        else if (gamepad1.dpad_down) {

            topLeftMotor.setPower(-0.5 * posleft);
            bottomLeftMotor.setPower(-0.5 * posleft);
            topRightMotor.setPower(-0.5);
            bottomRightMotor.setPower(-0.5);

        }
    }

/*    public void collect() {




    } */

/* Note that x, y, and z serve as placeholder values for the position of the motor: uncommenting
this will cause syntax errors as the placeholder variables are undefined.

    public void angles() {

        if(gamepad2.dpad_up) {

            while(tiltMotor.getCurrentPosition() < x) {

                tiltMotor.setPower(0.75);

            }

            while(tiltMotor.getCurrentPosition() > x) {

                tiltMotor.setPower(-0.75);

            }

        }

        if(gamepad2.dpad_left) {

            while(tiltMotor.getCurrentPosition() > y) {

                tiltMotor.setPower(-0.75);

            }

            while(tiltMotor.getCurrentPosition() < y) {

                tiltMotor.setPower(0.75);

            }

        }

        if(gamepad2.dpad_down) {

            while(tiltMotor.getCurrentPosition() > z) {

                tiltMotor.setPower(-0.75);

            }

            while(tiltMotor.getCurrentPosition() < z) {

                tiltMotor.setPower(0.75);

            }

        }

        }
*/
/*
    public void lift() {

        float leftY_gp2 = (-gamepad2.left_stick_y);
        float rightY_gp2 = (-gamepad2.right_stick_y);

    }
*/
    public void stop(){

    }

}