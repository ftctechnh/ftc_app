package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;

/**
 * Created by Peter on 12/3/2015.
 */
public class CompBotTest extends LinearOpMode
{
    @Override public void runOpMode() throws InterruptedException
   {
       DriverInterface robotDrive = new CombBot(hardwareMap);
       waitForStart();
       robotDrive.moveStraightEncoders(24, 1.0f);//3ft-96.5% 2ft 95.3% need to recollect on a mat
       stop();
   }
}
