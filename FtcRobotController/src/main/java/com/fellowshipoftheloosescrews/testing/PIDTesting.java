package com.fellowshipoftheloosescrews.testing;

import com.fellowshipoftheloosescrews.utilities.DcServo;
import com.fellowshipoftheloosescrews.utilities.PID;
import com.fellowshipoftheloosescrews.utilities.Util;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.Range;

/**
 * Created by Thomas on 8/14/2015.
 */
public class PIDTesting extends LinearOpMode {
    @Override
    public void runOpMode() throws InterruptedException {
        DcMotor motor = hardwareMap.dcMotor.get("motor1");
        DcServo servo = new DcServo(motor, 0);
        servo.setTarget(1);
        servo.start();
        sleep(1000);
        servo.stop();
    }
}
