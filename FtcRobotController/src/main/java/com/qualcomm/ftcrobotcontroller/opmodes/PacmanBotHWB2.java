package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;


/**
 * Created by tdoylend on 2015-12-10.
 *
 * Change log:
 * 1.0.0 - First version
 */
public class PacmanBotHWB2 extends OpMode {

    final static VersionNumber hwbVersion = new VersionNumber(1,0,0);

    public void init() {}
    public void loop() {}

    //Motor section

    DcMotor frontLeft;
    DcMotor rearLeft;
    DcMotor rearRight;
    DcMotor frontRight;

    DcMotor winch;
    DcMotor tail;

    DcMotor bucket;

    //Servo section

    Servo tripper;
    Servo slideRelease;

    Servo hook;
    Servo aim;

    //Configuration section

    double driveFinalMultiplier = 1.0;

    public double limit(double value, double lower, double upper) {
        if (value < lower) return lower;
        if (value > upper) return upper;
        return value;
    }

    public void drive(double driveRate,double turnRate) {
        /* Standard (non-mountain) drive. */
        driveRate = limit(driveRate, -1, 1);
        turnRate  = limit(turnRate, -1, 1);
        telemetry.addData("DR",driveRate);
        double left;
        double right;
        left  = limit(driveRate + turnRate,-1,1);
        right = limit(driveRate - turnRate,-1,1);
        left  *= driveFinalMultiplier;
        right *= driveFinalMultiplier;
        telemetry.addData("Left",left);
        telemetry.addData("Right",right);
        rearLeft.setPower(left);
        frontLeft.setPower(-left);
        rearRight.setPower(right);
        frontRight.setPower(-right);
    }

    public void setupHardware() {

        frontLeft = hardwareMap.dcMotor.get("frontleft");
        frontRight = hardwareMap.dcMotor.get("fronright");
        rearLeft = hardwareMap.dcMotor.get("rearleft");
        rearRight = hardwareMap.dcMotor.get("rearright");

        winch = hardwareMap.dcMotor.get("winch");

        //slideRelease = hardwareMap.servo.get("sliderelease");

        //hook = hardwareMap.servo.get("hook");
        //aim = hardwareMap.servo.get("aim");
    }

    public void update() {

    }
}
