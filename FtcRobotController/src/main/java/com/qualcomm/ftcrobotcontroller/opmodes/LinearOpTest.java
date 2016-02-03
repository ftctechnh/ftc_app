package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;

/**
 * Created by Peter on 1/15/2016.
 */
public class LinearOpTest extends AutonomousTime_T
{
    @Override
    public void runOpMode() throws InterruptedException
    {
        left = hardwareMap.dcMotor.get("left");
        right = hardwareMap.dcMotor.get("right");
        left.setDirection(DcMotor.Direction.REVERSE);
        right.setDirection(DcMotor.Direction.FORWARD);
        waitForStart();
        Forwards(12, true);
        sleep(100);
        Forwards(12, false);
    }
}
