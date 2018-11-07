package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

/**
 * Created by Wake Robotics Member on 11/2/2017.
 */


@TeleOp(name = "tankDrive", group = "Tank")
public class tankDrive extends OpMode {

    DcMotor frontLeft;
    DcMotor frontRight;
    DcMotor backLeft;
    DcMotor backRight;
    DcMotor intakeFrontLeft;
    DcMotor intakeFrontRight;
    DcMotor intakeBackLeft;
    DcMotor intakeBackRight;

    @Override
    public void init() {
        backRight = hardwareMap.dcMotor.get("back_right");
        backLeft = hardwareMap.dcMotor.get("back_left");
        frontRight = hardwareMap.dcMotor.get("front_right");
        frontLeft = hardwareMap.dcMotor.get("front_left");
        intakeFrontLeft = hardwareMap.dcMotor.get("intake_front_left");
        intakeFrontRight = hardwareMap.dcMotor.get("intake_front_right");
        intakeBackLeft = hardwareMap.dcMotor.get("intake_back_left");
        intakeBackRight = hardwareMap.dcMotor.get("intake_back_right");

        frontLeft.setDirection(DcMotor.Direction.REVERSE);

    }

    @Override
    public void loop() {
        float leftTrigger = gamepad2.left_trigger;
        float rightTrigger = gamepad2.right_trigger;

        frontLeft.setPower(-gamepad1.left_stick_y); //Moves robot forward.
        backLeft.setPower(-gamepad1.left_stick_y);
        frontRight.setPower(-gamepad1.right_stick_y);
        backRight.setPower(-gamepad1.right_stick_y);

        if(rightTrigger>0){
            intakeFrontLeft.setPower(1.0);
            intakeFrontRight.setPower(1.0);
            intakeBackLeft.setPower(-1.0);
            intakeBackRight.setPower(-1.0);
        }

        if(leftTrigger>0){
            intakeFrontLeft.setPower(-1.0);
            intakeFrontRight.setPower(-1.0);
        }




    }
}
