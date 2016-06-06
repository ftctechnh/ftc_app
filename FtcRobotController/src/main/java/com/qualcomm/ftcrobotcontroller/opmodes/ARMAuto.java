package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

public class ARMAuto extends LinearOpMode {
    DcMotor leftMotor;
    DcMotor rightMotor;

    @Override
    public void runOpMode() throws InterruptedException {
        leftMotor = hardwareMap.dcMotor.get("left_drive");
        rightMotor = hardwareMap.dcMotor.get("right_drive");
        rightMotor.setDirection(DcMotor.Direction.REVERSE);

        waitForStart();

        for(int i=0; i<4; i++) {
            leftMotor.setPower(1.0);
            rightMotor.setPower(1.0);
            sleep(1000);

            leftMotor.setPower(0.5);
            rightMotor.setPower(-0.5);
            sleep(1000);
        }

        leftMotor.setPowerFloat();
        rightMotor.setPowerFloat();
    }
}

