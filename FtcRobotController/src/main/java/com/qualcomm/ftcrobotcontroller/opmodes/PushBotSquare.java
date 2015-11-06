package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;

/*
 * An example linear op mode where the pushbot
 * will drive in a square pattern using sleep() 
 * and a for loop.
 */
public class PushBotSquare extends LinearOpMode {
    DcMotor leftmotor;
    DcMotor rightmotor;

    @Override
    public void runOpMode() throws InterruptedException {
        leftmotor = hardwareMap.dcMotor.get("leftmotor");
        rightmotor = hardwareMap.dcMotor.get("rightmotor");
        rightmotor.setDirection(DcMotor.Direction.REVERSE);
        rightmotor.setMode(DcMotorController.RunMode.RESET_ENCODERS);
        leftmotor.setMode(DcMotorController.RunMode.RESET_ENCODERS);
        waitForStart();
        rightmotor.setMode(DcMotorController.RunMode.RUN_USING_ENCODERS);
        leftmotor.setMode(DcMotorController.RunMode.RUN_USING_ENCODERS);

       while (rightmotor.getCurrentPosition()< 1000)
       {
           rightmotor.setPower(.5);
           leftmotor.setPower(.5);


        }

        leftmotor.setPower(0);
        rightmotor.setPower(0);

    }
}
