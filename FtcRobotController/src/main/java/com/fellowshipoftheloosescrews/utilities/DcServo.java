package com.fellowshipoftheloosescrews.utilities;

import com.qualcomm.robotcore.hardware.DcMotor;

/**
 * Created by Thomas on 8/13/2015.
 *
 * Makes a Dc motor act like a servo, so we can set its position.
 * This will constantly track the position of the motor.
 *
 * TODO: See if it works with the old controllers?
 * TODO: add PID controller
 */
public class DcServo {
    private DcMotor motor;
    private PID pidController;

    private double motorEncoderCPR;

    public DcServo(DcMotor motor, double cpr)
    {
        this.motor = motor;
        motorEncoderCPR = cpr;
    }

    public void release()
    {

    }
}
