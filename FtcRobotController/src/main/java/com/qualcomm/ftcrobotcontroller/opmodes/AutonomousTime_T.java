package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

/**
 * Created by Peter on 1/31/2016.
 */
public class AutonomousTime_T extends LinearOpMode
{
    DcMotor left;
    DcMotor right;
    public void runOpMode() throws InterruptedException
    {

    }
    public void Forwards (float inches, boolean isForward) throws InterruptedException {
        if (isForward) {
            left.setPower(1.0f);
            right.setPower(1.0f);
            sleep((long) (inches * 50.9554));
            left.setPower(0f);
            right.setPower(0f);
        }
        else
        {
            left.setPower(-1.0f);
            right.setPower(-1.0f);
            sleep((long) (inches * 50.9554));
            left.setPower(0f);
            right.setPower(0f);
        }
    }
}
