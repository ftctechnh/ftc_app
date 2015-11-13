package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.*;

/**
 * Created by Carlos on 11/12/2015.
 */
public class MotorTest extends LinearOpMode {
    DcMotor testMotor;

    @Override
    public void runOpMode() throws InterruptedException {

        testMotor = hardwareMap.dcMotor.get("motor");

        waitForStart();

        while(opModeIsActive()){

            testMotor.setPower(gamepad1.left_stick_y);


        }

    }
}
