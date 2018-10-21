package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

/**
 * Created by Wake Robotics Member on 11/2/2017.
 */


@TeleOp(name = "intakeTankDrive", group = "Tank")
public class intakeTankDrive extends OpMode {


    DcMotor intakeFrontLeft;
    DcMotor intakeFrontRight;
    DcMotor intakeBackLeft;
    DcMotor intakeBackRight;

    @Override
    public void init() {
        intakeFrontLeft = hardwareMap.dcMotor.get("intake_front_left");
        intakeFrontRight = hardwareMap.dcMotor.get("intake_front_right");
        intakeBackLeft = hardwareMap.dcMotor.get("intake_back_left");
        intakeBackRight = hardwareMap.dcMotor.get("intake_back_right");

        intakeBackLeft.setDirection(DcMotor.Direction.REVERSE);
        intakeFrontLeft.setDirection(DcMotor.Direction.REVERSE);

    }

    @Override
    public void loop() {
        float leftTrigger = gamepad2.left_trigger;
        float rightTrigger = gamepad2.right_trigger;


        if(rightTrigger>0){
            intakeFrontLeft.setPower(rightTrigger);
            intakeFrontRight.setPower(rightTrigger);
            intakeBackLeft.setPower(-rightTrigger);
            intakeBackRight.setPower(-rightTrigger);
        }

        if(leftTrigger>0){
            intakeFrontLeft.setPower(-rightTrigger);
            intakeFrontRight.setPower(-rightTrigger);
        }



    }
}
