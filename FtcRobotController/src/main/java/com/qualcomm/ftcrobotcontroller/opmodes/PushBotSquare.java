package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;
import com.qualcomm.ftcrobotcontroller.opmodes.PushBotTelemetry;
import com.qualcomm.hardware.HiTechnicNxtDcMotorController;
/*
 * An example linear op mode where the pushbot
 * will drive forward
 */
public class PushBotSquare extends OpMode {
    DcMotor leftmotor;
    DcMotor rightmotor;


    @Override
    public void init()
    {
        leftmotor = hardwareMap.dcMotor.get("leftmotor");
        rightmotor = hardwareMap.dcMotor.get("rightmotor");
        leftmotor.setMode(DcMotorController.RunMode.RESET_ENCODERS);
        rightmotor.setMode(DcMotorController.RunMode.RESET_ENCODERS);

        leftmotor.setDirection(DcMotor.Direction.REVERSE);

    }
    @Override
    public void start() {
        leftmotor.setTargetPosition(2000);
        rightmotor.setTargetPosition(2000);

        leftmotor.setMode(DcMotorController.RunMode.RUN_TO_POSITION);
        rightmotor.setMode(DcMotorController.RunMode.RUN_TO_POSITION);

        leftmotor.setPower(0.5);
        rightmotor.setPower(0.5);

    }

    public void loop() {

    }

    public void move (int count)
    {


        leftmotor.setMode(DcMotorController.RunMode.RESET_ENCODERS);
        rightmotor.setMode(DcMotorController.RunMode.RESET_ENCODERS);
        rightmotor.setMode(DcMotorController.RunMode.RUN_TO_POSITION);
        leftmotor.setMode(DcMotorController.RunMode.RUN_TO_POSITION);
        leftmotor.setTargetPosition(200);
        rightmotor.setTargetPosition(200);
        rightmotor.setPower(.5);
        leftmotor.setPower(.5);
    }

}
