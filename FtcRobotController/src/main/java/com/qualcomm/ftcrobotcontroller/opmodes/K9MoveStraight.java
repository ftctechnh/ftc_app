package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;


public class K9MoveStraight extends LinearOpMode {

    @Override
    public void runOpMode() throws InterruptedException {
    DcMotor DC_left;
    DcMotor DC_right;

        DC_left = hardwareMap.dcMotor.get("DC_left");
        DC_right = hardwareMap.dcMotor.get("DC_right");
        DC_right.setDirection(DcMotor.Direction.REVERSE);


        DC_left.setPower(0.5);
        DC_right.setPower(0.5);

        sleep(5000);


        DC_left.setPower(0.8);
        DC_right.setPower(0.1);

        sleep(3000);


    }




    }
