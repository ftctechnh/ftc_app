package com.powerstackers.resq.opmodes.common;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.OpticalDistanceSensor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.TouchSensor;

/**
 * Created by Jonathan on 9/26/2015.
 */
public abstract class Robot extends LinearOpMode{
    protected DcMotor motorLeft;
    protected DcMotor motorRight;

    protected Servo servoArm;
    protected Servo servoClaw;

    protected TouchSensor touchSensor;
    protected OpticalDistanceSensor opticalSensor;

    public Robot() {
    }
}
