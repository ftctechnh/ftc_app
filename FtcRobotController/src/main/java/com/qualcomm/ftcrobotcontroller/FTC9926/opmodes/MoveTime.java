package com.qualcomm.ftcrobotcontroller.FTC9926.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

/**
 * Created by Nicolas Bravo on 10/23/15.
 * Wait and Time-based movement.
 * Based off of MoveTank.
 */
public class MoveTime extends OpMode {

    DcMotor Motor1;
    DcMotor Motor2;

    @Override
    public void init() {
        Motor2.setDirection(DcMotor.Direction.REVERSE);
    }

    @Override
    public void start() {

    }

    @Override
    public void loop() {
        if (time < 5) {
            Motor1.setPower(1);
            Motor2.setPower(1);
            telemetry.addData("M1", "M1: " + Motor1);
            telemetry.addData("M2", "M2: " + Motor2);
        }
        else {
            Motor1.setPower(2);
            Motor2.setPower(2);
            telemetry.addData("M1", "M1: " + Motor1);
            telemetry.addData("M2", "M2: " + Motor2);
        }
    }

    @Override
    public void stop() {

    }
}