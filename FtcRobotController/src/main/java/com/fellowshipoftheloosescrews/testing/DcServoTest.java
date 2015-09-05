package com.fellowshipoftheloosescrews.testing;

import com.fellowshipoftheloosescrews.utilities.DcServo;
//import com.fellowshipoftheloosescrews.utilities.Util;
import com.fellowshipoftheloosescrews.utilities.MotorUtils;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

/**
 * Created by Thomas on 8/28/2015.
 */
public class DcServoTest extends LinearOpMode{

    DcMotor motor;
    DcServo servo;

    @Override
    public void runOpMode()
    {
        motor = hardwareMap.dcMotor.get("motor1");
        servo = new DcServo(motor, MotorUtils.ENCODER_NEVEREST_CPR);

        servo.setTarget(1);
        servo.start();

        try {
            sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        servo.setTarget(2);

        try {
            sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        servo.stop();
    }
}
