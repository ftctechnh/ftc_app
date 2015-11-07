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

        waitForStart();

        move(500);


    }
    public void move (int count)
    {
        leftmotor.setTargetPosition(count);
        rightmotor.setTargetPosition(count);
        rightmotor.setMode(DcMotorController.RunMode.RUN_TO_POSITION);
        leftmotor.setMode(DcMotorController.RunMode.RUN_TO_POSITION);
        rightmotor.setPower(50);
        leftmotor.setPower(50);

    }
    public void data()
    {
        telemetry.addData("rightmotor",rightmotor.getCurrentPosition());
    }
}
