package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.ServoController;

/**
 * Created by robotics on 10/20/2015.
 */
public class Team4800_Auto_Scrubtomonous extends OpMode {

    DcMotor leftBackMotor;
    DcMotor leftFrontMotor;
    DcMotor rightBackMotor;
    DcMotor rightFrontMotor;
    DcMotorController rightController;
    DcMotorController leftController;
    DcMotor scrubMotor;
    DcMotorController scrubcontroller;
    Servo scrubhook;
    ServoController scrubmind;
    final double SCRUB_HOOK_UP = 0.0;
    final double SCRUB_HOOK_DOWN = 1.0;


    @Override
    public void init() {
        //Get hardware references
        leftBackMotor = hardwareMap.dcMotor.get("back_left_drive");
        leftFrontMotor = hardwareMap.dcMotor.get("front_left_drive");
        rightBackMotor = hardwareMap.dcMotor.get("back_right_motor");
        rightFrontMotor = hardwareMap.dcMotor.get("front_right_motor");
        leftController = hardwareMap.dcMotorController.get("right_controller");
        rightController = hardwareMap.dcMotorController.get("left_controller");
        scrubMotor = hardwareMap.dcMotor.get("scrub_motor");
        scrubcontroller = hardwareMap.dcMotorController.get("scrub_controller");
        scrubhook = hardwareMap.servo.get("scrub_hook");
        scrubmind = hardwareMap.servoController.get("scrub_mind");
        scrubhook.setPosition(SCRUB_HOOK_DOWN);

        //Reverses right motors
        rightBackMotor.setDirection(DcMotor.Direction.REVERSE);
        leftFrontMotor.setDirection(DcMotor.Direction.REVERSE);
        //leftController.setMotorChannelMode(1, DcMotorController.RunMode.RUN_USING_ENCODERS);
        leftBackMotor.setMode(DcMotorController.RunMode.RUN_USING_ENCODERS);
        leftFrontMotor.setMode(DcMotorController.RunMode.RUN_USING_ENCODERS);
        rightBackMotor.setMode(DcMotorController.RunMode.RUN_USING_ENCODERS);
        rightFrontMotor.setMode(DcMotorController.RunMode.RUN_USING_ENCODERS);
        //scrubcontroller.setMotorChannelMode(1, DcMotorController.RunMode.RUN_WITHOUT_ENCODERS)
    }

    public void start() {
        resetStartTime();
    }

    @Override
    public void loop() {
        double leftY = 0;
        double rightY = 0;

        if (getRuntime() < 2.0){
            leftY = 1.0;
            rightY = 1.0;

        }
        else if (getRuntime() < 2.5){
            leftY = -.25;
            rightY = .25;
        }
        else if (getRuntime() < 3.0){
            leftY = 1.0;
            rightY = 1.0;
        }
        else if (getRuntime() < 5.0){
            leftY = -.15;
            rightY = .15;
        }
        //TODO: color sensor work
        else if (getRuntime() < 8.0){
            leftY = -.35;
            rightY = .35;
        }
        else if (getRuntime() < 10.0){
            leftY = .5;
            rightY = .5;
        }
        rightBackMotor.setPower(rightY);
        rightFrontMotor.setPower(rightY);
        leftBackMotor.setPower(leftY);
        leftFrontMotor.setPower(leftY);
        telemetry.addData("time: ", getRuntime());
    }
}
