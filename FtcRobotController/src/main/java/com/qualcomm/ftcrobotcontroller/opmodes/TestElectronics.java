package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

/**
 * Created by Peter on 12/22/2015.
 */
public class TestElectronics extends LinearOpMode
{
    DcMotor test1;
    DcMotor test2;
    DcMotor test3;
    public void runOpMode() throws InterruptedException
    {
        test1 = hardwareMap.dcMotor.get("test1");
        test2 = hardwareMap.dcMotor.get("test2");
        test3= hardwareMap.dcMotor.get("test3");
        waitForStart();
        test1.setPower(1);
        test2.setPower(1);
        test3.setPower(1);
    }
}
