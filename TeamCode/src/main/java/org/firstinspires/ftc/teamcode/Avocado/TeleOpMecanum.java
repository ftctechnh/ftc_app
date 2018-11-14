package org.firstinspires.ftc.teamcode.Avocado;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

import junit.framework.Test;

@TeleOp(name = "Avocado Mecanum TeleOp", group = "Test")

public class TeleOpMecanum extends OpMode {

    DcMotor topLeftMotor;
    DcMotor bottomLeftMotor;
    DcMotor topRightMotor;
    DcMotor bottomRightMotor;
    byte posleft = -1;

    @Override
    public void loop() {
        dpad();
        TankDrive();
    }

    @Override
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

    public void stop(){

    }

}