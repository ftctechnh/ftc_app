package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.modules.GamepadV2;

/**
 * Created by Kit Caldwell on 10/4/2017.
 */

@TeleOp
public class fourMotorTest extends OpMode {

    DcMotor motorLeft;
    DcMotor motorRight;
    DcMotor grabLeft;
    DcMotor grabRight;

    Servo rotate1;
    Servo arm;

    public void init(){
        motorLeft = hardwareMap.dcMotor.get("left");
        motorRight = hardwareMap.dcMotor.get("right");
        grabLeft = hardwareMap.dcMotor.get("grab1");
        grabRight = hardwareMap.dcMotor.get("grab2");
        rotate1 = hardwareMap.servo.get("rotate1");
        arm = hardwareMap.servo.get("arm");

        motorLeft.setDirection(DcMotorSimple.Direction.REVERSE);
    }
    public void loop(){

        motorLeft.setPower(gamepad1.left_stick_y);
        motorRight.setPower(gamepad1.right_stick_y);

        if (gamepad1.x == true){
            grabLeft.setPower(.5);
            grabRight.setPower(-.5);
        }
        else if (gamepad1.y == true){
            grabLeft.setPower(-.5);
            grabRight.setPower(.5);
        }
        else{
            grabLeft.setPower(0);
            grabRight.setPower(0);
        }
        if (gamepad1.a == true) {
            rotate1.setPosition(1);
        }
        if (gamepad1.b == true) {
            rotate1.setPosition(0);
        }
        if (gamepad1.left_bumper == true){
            arm.setPosition(1);
        }
        if (gamepad1.right_bumper == true){
            arm.setPosition(0);
        }

        telemetry.addData("right bumper",gamepad1.right_bumper);
        telemetry.addData("left bumper",gamepad1.left_bumper);
        telemetry.addData("a",gamepad1.a);
        telemetry.addData("b",gamepad1.b);
        telemetry.addData("x",gamepad1.x);
        telemetry.addData("y",gamepad1.y);
        telemetry.addData("right stick", gamepad1.right_stick_y);
        telemetry.addData("left stick", gamepad1.left_stick_y);


    }
}
