package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;

/**
 * Created by Peter on 1/15/2016.
 */
public class LinearOpTest extends LinearOpMode
{
    DcMotor left;
    DcMotor right;
    public void runOpMode() throws InterruptedException
    {
        left = hardwareMap.dcMotor.get("leftDrive");
        right = hardwareMap.dcMotor.get("rightDrive");
        left.setDirection(DcMotor.Direction.REVERSE);
        // here is where distances will go in
        float forward1 = 1;
        waitForStart();
        //here is where the commands go
        left.setPower(1.0f);
        right.setPower(1.0f);
        wait((long) forward1 * 1000);
        left.setPower(0f);
        right.setPower(0f);
    }
}
