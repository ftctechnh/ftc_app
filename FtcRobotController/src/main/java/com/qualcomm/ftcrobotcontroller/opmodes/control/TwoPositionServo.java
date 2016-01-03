package com.qualcomm.ftcrobotcontroller.opmodes.control;

import com.qualcomm.robotcore.hardware.Servo;

/**
 * Created by tdoylend on 2016-01-02.
 *
 * This class provides a rudimentary two-position servo. A two-position
 * servo is initialized with two positions, off and on. It defaults to
 * off. A single method set() allows you to set the servo to either of its
 * two positions with a boolean.
 *
 */
public class TwoPositionServo {

    Servo servo;
    double off;
    double on;

    public TwoPositionServo(Servo servo, double off, double on) {
        this.servo = servo;
        this.off = off;
        this.on = on;
    }

    public void set(boolean position) {
        //Set the servo's position to one of its two options.
        this.servo.setPosition(position ? this.on : this.off);
    }
}
