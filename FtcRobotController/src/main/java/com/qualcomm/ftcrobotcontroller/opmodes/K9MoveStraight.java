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



        DC_left.setPower(0.5);
        DC_right.setPower(0.5);



    }




    }
