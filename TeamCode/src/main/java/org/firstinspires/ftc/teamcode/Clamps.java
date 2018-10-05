package org.firstinspires.ftc.teamcode;

/**
 * Set up a class for the left and right org.firstinspires.ftc.teamcode.Clamps for the KP robot
 * Defines all of the necessary data for the clamp.
 * Created by Howard on 12/27/2017.
 */

class Clamps {
    static final double LEFT_CLAMPED = 45;
    static final double LEFT_UNCLAMPED = -5;
    static final double RIGHT_CLAMPED = 5;
    static final double RIGHT_UNCLAMPED = -45;
    static final double SERVO_TWEAK    = 2;
    static final double LEFT_MOSTLY_CLAMPED = 42;
    static final double RIGHT_MOSTLY_CLAMPED= -42;
    static final double LEFT_TIGHT_CLAMPED = 48;
    static final double RIGHT_TIGHT_CLAMPED= -48;

    double leftClamp;
    double rightClamp;
    float liftMotor;
    boolean status;
}