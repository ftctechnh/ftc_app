package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

/**
 * Created by Peter on 12/3/2015.
 */
public class CompBotTest extends LinearOpMode
{
    @Override public void runOpMode() throws InterruptedException
   {
       DriverInterface robotDrive = new CompBot(hardwareMap);
       waitForStart();
       robotDrive.spinOnCenter(-90, 1.0f);
       robotDrive.stop();
   }
}
