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
        left.setMode(DcMotorController.RunMode.RESET_ENCODERS);
        right.setMode(DcMotorController.RunMode.RESET_ENCODERS);
        while ( left.getCurrentPosition() != 0 | right.getCurrentPosition() != 0){}
        left.setMode(DcMotorController.RunMode.RUN_USING_ENCODERS);
        right.setMode(DcMotorController.RunMode.RUN_USING_ENCODERS);
        waitForStart();
        //move forward a distance
            left.setPower(1.0f);
            right.setPower(1.0f);
            while(left.getCurrentPosition() < 1000){}
            while(right.getCurrentPosition() < 1000){}
            left.setPower(0f);
            right.setPower(0f);

       /* {//rotate right a distance
            left.setPower(1.0f);
            right.setPower(-1.0f);
            while(left.getCurrentPosition() < 1000 | right.getCurrentPosition() > -1000){}
            left.setPower(0f);
            right.setPower(0f);
        }
        {//rotate left a distance
            left.setPower(-1.0f);
            right.setPower(1.0f);
            while(right.getCurrentPosition() < 1000 | left.getCurrentPosition() > -1000){}
            left.setPower(0f);
            right.setPower(0f);
        }
        {//move backwards a distance
            left.setPower(-1.0f);
            right.setPower(-1.0f);
            while(left.getCurrentPosition() > -1000 | right.getCurrentPosition() > -1000){}
            left.setPower(0f);
            right.setPower(0f);
        }*/
    }
}
