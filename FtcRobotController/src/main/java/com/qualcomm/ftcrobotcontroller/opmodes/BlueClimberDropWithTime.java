package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;

/**
 * Created by Kaitlin on 1/15/2016.
 */
public class BlueClimberDropWithTime extends AutonomousTime_T
{
    @Override
    public void runOpMode() throws InterruptedException
    {
        left = hardwareMap.dcMotor.get("left");
        right = hardwareMap.dcMotor.get("right");
        left.setDirection(DcMotor.Direction.REVERSE);
        right.setDirection(DcMotor.Direction.FORWARD);
        waitForStart();
        Forwards(24, true);
        turnOnCenter(48, false);
        Forwards((float) 76.25, true);
        turnOnCenter(48, false);
        Forwards(15, true);
        //try to do the flips

    }
}
