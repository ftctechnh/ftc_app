package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.ServoController;
/**
 * Created by Ian Ramsey on 11/30/2015.
 */
public class Team4799Autonomous extends PushBotTelemetrySensors {

    DcMotorController wheelControllerLeft;
    DcMotor motorBackLeft;
    DcMotor motorFrontLeft;
    DcMotorController wheelControllerRight;
    DcMotor motorBackRight;
    DcMotor motorFrontRight;
    //Servo servoClimber1;
    //Servo servoClimber2;
    Servo hookServo;
    ServoController Servocontroller;
    DcMotorController winchController;
    DcMotor hookMotor;
    DcMotor liftMotor;

    public void init() {
        motorBackRight = hardwareMap.dcMotor.get("RightBack");
        motorFrontRight = hardwareMap.dcMotor.get("RightFront");
        motorBackLeft = hardwareMap.dcMotor.get("LeftBack");
        motorFrontLeft = hardwareMap.dcMotor.get("LeftFront");
        wheelControllerRight = hardwareMap.dcMotorController.get("Right");
        wheelControllerLeft = hardwareMap.dcMotorController.get("Left");
        hookServo = hardwareMap.servo.get("hookServo");
        Servocontroller = hardwareMap.servoController.get("ServoController");
        winchController = hardwareMap.dcMotorController.get("winchController");
        hookMotor = hardwareMap.dcMotor.get("hookMotor");
        liftMotor = hardwareMap.dcMotor.get("liftMotor");
        wheelControllerRight.setMotorChannelMode(1, DcMotorController.RunMode.RUN_USING_ENCODERS);
        wheelControllerRight.setMotorChannelMode(2, DcMotorController.RunMode.RUN_USING_ENCODERS);
        wheelControllerLeft.setMotorChannelMode(1, DcMotorController.RunMode.RUN_USING_ENCODERS);
        wheelControllerLeft.setMotorChannelMode(2, DcMotorController.RunMode.RUN_USING_ENCODERS);
        winchController.setMotorChannelMode(1, DcMotorController.RunMode.RUN_WITHOUT_ENCODERS);
        winchController.setMotorChannelMode(2, DcMotorController.RunMode.RUN_WITHOUT_ENCODERS);

    }

    public void loop() {
        if (getRuntime() > 1 && getRuntime() < 3) {
            motorBackLeft.setPower(-1);
            motorFrontLeft.setPower(-1);
            motorBackRight.setPower(1);
            motorFrontRight.setPower(1);

        }
    }
}