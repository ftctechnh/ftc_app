package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;


public class ResQAuto extends LinearOpMode {
    DcMotor leftMotor;
    DcMotor rightMotor;

    @Override
    public void runOpMode() throws InterruptedException {
        leftMotor = hardwareMap.dcMotor.get("left_drive");
        rightMotor = hardwareMap.dcMotor.get("right_drive");
        leftMotor.setDirection(DcMotor.Direction.REVERSE);
        //rightMotor.setDirection(DcMotor.Direction.REVERSE);

        waitForStart();

        rightMotor.setChannelMode(DcMotorController.RunMode.RUN_USING_ENCODERS);
//        rightMotor.setChannelMode(DcMotorController.RunMode.RUN_USING_ENCODERS);



        rightMotor.setTargetPosition((int) 1000);;
        rightMotor.setChannelMode(DcMotorController.RunMode.RUN_TO_POSITION);

        leftMotor.setPower(0.1);
        rightMotor.setPower(0.1);
        while (rightMotor.getCurrentPosition()<rightMotor.getTargetPosition()) {
            telemetry.addData("test", rightMotor.getCurrentPosition());
            waitForNextHardwareCycle();

        }
        leftMotor.setPower(0);
        rightMotor.setPower(0);
    }
}
