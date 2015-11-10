package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;

/*
 * An example linear op mode where the pushbot
 * will drive forward
 */
public class PushBotSquare extends LinearOpMode {
    DcMotor leftmotor;
    DcMotor rightmotor;

    @Override
    public void runOpMode() throws InterruptedException {
        leftmotor = hardwareMap.dcMotor.get("leftmotor");
        rightmotor = hardwareMap.dcMotor.get("rightmotor");


        waitForStart();

        move(500);


    }
    public void move (int count)
    {
        leftmotor.setTargetPosition(count);
        rightmotor.setTargetPosition(count);
        rightmotor.setMode(DcMotorController.RunMode.RUN_TO_POSITION);
        leftmotor.setMode(DcMotorController.RunMode.RUN_TO_POSITION);
        rightmotor.setPower(.5);
        leftmotor.setPower(.5);

    }
    public void data()
    {
        telemetry.addData("rightmotor",rightmotor.getCurrentPosition());
    }
}
