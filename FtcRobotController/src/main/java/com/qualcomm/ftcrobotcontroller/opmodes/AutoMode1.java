package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.Range;

/**
 * Created by DKrakauer on 2/8/16.
 */
public class AutoMode1 extends AutoMode{

    @Override
    public void runOpMode() throws InterruptedException{
        super.runOpMode();
        waitForStart();

        checkEverything();
        startChildMode(100);


    }




}
