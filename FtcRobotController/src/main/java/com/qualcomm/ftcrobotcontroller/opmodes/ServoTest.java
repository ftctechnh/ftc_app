package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.*;
import com.qualcomm.ftcrobotcontroller.opmodes.robot.*;

/**
 * Created by Carlos on 11/12/2015.
 */
public class ServoTest extends LinearOpMode  {

    Servo testServo;

    @Override
    public void runOpMode() throws InterruptedException {
        testServo = hardwareMap.servo.get("servo");

        waitForStart();

        while(opModeIsActive()){

            testServo.setPosition((gamepad1.left_stick_y + 1) / 2.0);


        }
    }
}
