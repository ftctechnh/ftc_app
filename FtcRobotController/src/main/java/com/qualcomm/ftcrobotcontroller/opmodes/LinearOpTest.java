package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;

/**
 * Created by Peter&Kaitlin on 1/15/2016.
 */
public class LinearOpTest extends AutonomousTime_T
{

    @Override
    public void runOpMode() throws InterruptedException
    {
        initialize();
        waitForStart();
        Forwards(24, true);
        turnOnCenter(48, true);
        Forwards((float) 76.25, true);
        turnOnCenter(48, true);
        Forwards(15, true);
        //Forwards(6,true);
        //turnOnCenter(90, false);
    }
}
