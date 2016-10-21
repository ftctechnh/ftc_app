package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.ServoController;



import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.ServoController;
/**
 * Created by FTC Team 4799-4800 on 10/13/2016.
 */
@Autonomous(name = "DemoAuto", group = "")
public class DemoAuto extends OpMode {
    DcMotorController wheelControllerLeft;
    DcMotor motorBackLeft;
    DcMotor motorFrontLeft;
    DcMotorController wheelControllerRight;
    DcMotor motorBackRight;
    DcMotor motorFrontRight;
    DcMotor arm;
    DcMotorController armController;
    ServoController hand;
    Servo leftFinger;
    Servo rightFinger;



    public void init() {
        motorBackRight = hardwareMap.dcMotor.get("RightBack");
        motorFrontRight = hardwareMap.dcMotor.get("RightFront");
        motorBackLeft = hardwareMap.dcMotor.get("LeftBack");
        motorFrontLeft = hardwareMap.dcMotor.get("LeftFront");
        arm = hardwareMap.dcMotor.get("Arm1");
        arm.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        wheelControllerRight = hardwareMap.dcMotorController.get("Right");
        wheelControllerLeft = hardwareMap.dcMotorController.get("Left");
        armController = hardwareMap.dcMotorController.get("Arm");
        hand = hardwareMap.servoController.get("Hand");
        rightFinger = hardwareMap.servo.get("RightFinger");
        leftFinger = hardwareMap.servo.get("LeftFinger");
        arm.setMode(DcMotor.RunMode.RUN_TO_POSITION);
    }

    public void loop() {
        if (Math.abs(arm.getCurrentPosition()-1000)<50)
            arm.setPower(0);
        else
            arm.setPower(1);
        arm.setTargetPosition(1000);
    }
}

