package org.firstinspires.ftc.teamcode.Avocado;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

import junit.framework.Test;

@TeleOp(name = "Avacado Mecanum TeleOp", group = "Test")

public class TeleOpMecanum extends OpMode {

    DcMotor topLeftMotor;
    DcMotor bottomLeftMotor;
    DcMotor topRightMotor;
    DcMotor bottomRightMotor;

    @Override
    public void loop() {
        FourWheelDrive_fast();
        straffe();
        FourWheelDrive_slow();
    }

    public void init(){
        topLeftMotor = hardwareMap.dcMotor.get("topLeftMotor");
        bottomLeftMotor = hardwareMap.dcMotor.get("bottomLeftMotor");
        topRightMotor = hardwareMap.dcMotor.get("topRightMotor");
        bottomRightMotor = hardwareMap.dcMotor.get("bottomRightMotor");
    }

    public void FourWheelDrive_fast(){
        float leftY_gp1 = (-gamepad1.left_stick_y);
        float rightY_gp1 = (-gamepad1.right_stick_y);

        if (gamepad1.left_trigger > 0) {
            topLeftMotor.setPower(1);
            bottomLeftMotor.setPower(1);
            topRightMotor.setPower(0);
            bottomRightMotor.setPower(0);
        }
        if (gamepad1.right_trigger > 0) {
            topLeftMotor.setPower(0);
            bottomLeftMotor.setPower(0);
            topRightMotor.setPower(1);
            bottomRightMotor.setPower(1);
        } else {
            topLeftMotor.setPower(0);
            bottomLeftMotor.setPower(0);
            topRightMotor.setPower(0);
            bottomRightMotor.setPower(0);
        }
    }

    public void FourWheelDrive_slow(){
        if (gamepad1.dpad_up){
            topLeftMotor.setPower(0.5);
            bottomLeftMotor.setPower(0.5);
            topRightMotor.setPower(0);
            bottomRightMotor.setPower(0);
        }
        if (gamepad1.dpad_down){
            topLeftMotor.setPower(0);
            bottomLeftMotor.setPower(0);
            topRightMotor.setPower(0.5);
            bottomRightMotor.setPower(0.5);
        }
    }

    public void straffe(){
        if (gamepad1.dpad_left){
            topLeftMotor.setPower(-1);
            bottomLeftMotor.setPower(1);
            topRightMotor.setPower(1);
            bottomRightMotor.setPower(-1);
        }
        else if (gamepad1.dpad_right){
            topLeftMotor.setPower(1);
            bottomLeftMotor.setPower(-1);
            topRightMotor.setPower(-1);
            bottomRightMotor.setPower(1);
        }
    }

    public void stop(){

    }

}