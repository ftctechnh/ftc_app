package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.ServoController;

/**
 * Created by robotics on 10/20/2015.
 */
public class YaScrubOpMode extends OpMode {

    DcMotor leftBackMotor;
    DcMotor leftFrontMotor;
    DcMotor rightBackMotor;
    DcMotor rightFrontMotor;
    DcMotorController rightController;
    DcMotorController leftController;
    DcMotor scrubMotor;
    DcMotorController scrubController;
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
        scrubController = hardwareMap.dcMotorController.get("scrub_controller");
        scrubhook = hardwareMap.servo.get("scrub_hook");
        scrubmind = hardwareMap.servoController.get("scrub_mind");
        scrubhook.setPosition(SCRUB_HOOK_DOWN);

        //Reverses right motors
        rightBackMotor.setDirection(DcMotor.Direction.REVERSE);
        leftFrontMotor.setDirection(DcMotor.Direction.REVERSE);
        leftController.setMotorChannelMode(1, DcMotorController.RunMode.RUN_USING_ENCODERS);
        leftBackMotor.setChannelMode(DcMotorController.RunMode.RUN_USING_ENCODERS);
        leftFrontMotor.setChannelMode(DcMotorController.RunMode.RUN_USING_ENCODERS);
        rightBackMotor.setChannelMode(DcMotorController.RunMode.RUN_USING_ENCODERS);
        rightFrontMotor.setChannelMode(DcMotorController.RunMode.RUN_USING_ENCODERS);
        scrubController.setMotorChannelMode(1, DcMotorController.RunMode.RUN_WITHOUT_ENCODERS);
    }

    @Override
    public void loop() {
        float leftY = -gamepad1.left_stick_y;
        float rightY = -gamepad1.right_stick_y;
        float secondleftY = -gamepad2.left_stick_y * (float)0.25;

        rightBackMotor.setPower(rightY);
        rightFrontMotor.setPower(rightY);
        leftBackMotor.setPower(leftY);
        leftFrontMotor.setPower(leftY);
        scrubMotor.setPower(secondleftY);
        /*
        if (gamepad2.y){
            scrubhook.setPosition(SCRUB_HOOK_UP);
        }
        if (gamepad2.a) {
            scrubhook.setPosition(SCRUB_HOOK_DOWN);
        }
        */

        if (gamepad2.y){
            scrubhook.setPosition((SCRUB_HOOK_DOWN));
        }else{
            scrubhook.setPosition(SCRUB_HOOK_UP);
        }
        }
}
