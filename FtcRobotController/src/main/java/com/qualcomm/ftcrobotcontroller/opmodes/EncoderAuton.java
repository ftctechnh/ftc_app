package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotorController;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;

import org.ndhsb.ftc7593.tbc;

public class EncoderAuton extends OpMode {


    public EncoderAuton() {
    }
    public ElapsedTime mRuntime = new ElapsedTime();

    double time = mRuntime.time();
    double s3starttime;
    double s4starttime;
    double climberDelta = 0.005;
    int state = 0;

    public void init() {
        telemetry.addData("init", "init");

        tbc.hardwareMap = hardwareMap;
        tbc.initHardwareMap();

        tbc.initServoValues();

        tbc.setClimberPosition(tbc.climberPosition);
        tbc.setSliderLPosition(tbc.sliderLPosition);
        tbc.setSliderRPosition(tbc.sliderRPosition);
        tbc.setSnowplowPosition(tbc.snowplowPosition);
        tbc.setMtapePosition(tbc.mtapePosition);
        tbc.setButtonServoSpeed(tbc.buttonServoSpeed);
        //tbc.setMotorRRightPower(0.0f);
        //tbc.setMotorRLeftPower(0.0f);
        //tbc.setMotorFLeftPower(0.0f);
        // tbc.setMotorFRightPower(0.0f);
        tbc.setDriveMode(DcMotorController.RunMode.RESET_ENCODERS);
        //tbc.setDriveMode(DcMotorController.RunMode.RUN_WITHOUT_ENCODERS);
        state = 0;

        if (tbc.sc != null) {
            tbc.sc.pwmEnable(); // enable servo controller PWM outputs
        }

        mRuntime.reset();           // Zero game clock
    }

    public void loop() {


        tbc.setDriveMode(DcMotorController.RunMode.RUN_TO_POSITION);

        telemetry.addData("Text", "*** Robot Data***");
        // telemetry.addData("arm", "arm:  " + String.format("%.2f", armPosition));
        // telemetry.addData("claw", "claw:  " + String.format("%.2f", clawPosition));
        // telemetry.addData("left", "left:" + String.format("%.2f", tbc.motorFLeft));
        telemetry.addData("button servo", "button servo: " + String.format("%.2f", tbc.buttonServoSpeed));
        telemetry.addData("climber", "climber:  " + String.format("%.2f", tbc.climberPosition));
        telemetry.addData("sliderL", "sliderL: " + String.format("%.2f", tbc.sliderLPosition));
        telemetry.addData("sliderR", "sliderR:" + String.format("%.2f", tbc.sliderRPosition));
        telemetry.addData("mtape", "mtape: " + String.format("%.2f", tbc.mtapePosition));
        telemetry.addData("motorFLeft", "motorFLeft" + String.format("%.2f", tbc.motorFLeft.getPower()));
        telemetry.addData("motorRLeft", "motorRLeft" + String.format("%.2f", tbc.motorRLeft.getPower()));
        telemetry.addData("motorFRight", "motorFRight" + String.format("%.2f", tbc.motorFRight.getPower()));
        telemetry.addData("motorRRight", "motorRRight" + String.format("%.2f", tbc.motorRRight.getPower()));
        telemetry.addData("lf encoder", "lf encoder:" + String.format("%d", tbc.motorFLeft.getCurrentPosition()));
        telemetry.addData("rf encoder", "rf encoder:" + String.format("%d", tbc.motorFRight.getCurrentPosition()));
        telemetry.addData("lr encoder", "lr encoder:" + String.format("%d", tbc.motorRLeft.getCurrentPosition()));
        telemetry.addData("rr encoder", "rr encoder:" + String.format("%d", tbc.motorRRight.getCurrentPosition()));

        if (state == 0) {
            tbc.motorFRight.setTargetPosition(-12400);
            tbc.motorRLeft.setTargetPosition(-12000);
            tbc.motorFLeft.setTargetPosition(-12000);
            tbc.motorRRight.setTargetPosition(-12400);
            tbc.setMotorRLeftPower(-1.0f);
            tbc.setMotorFRightPower(-1.0f);
            tbc.setMotorFLeftPower(-1.0f);
            tbc.setMotorRRightPower(-1.0f);

            if ( (Math.abs(tbc.motorFRight.getCurrentPosition() - -12400) < 100)
                && (Math.abs(tbc.motorRRight.getCurrentPosition() - -12400) < 100)
                && (Math.abs(tbc.motorFLeft.getCurrentPosition() - -12000) < 100)
                && (Math.abs(tbc.motorRLeft.getCurrentPosition() - -12000) < 100))
            {
                state = 1;
            }
        }

        if (state == 1) {
            tbc.motorFRight.setTargetPosition(-134000);
            tbc.motorRLeft.setTargetPosition(-11000);
            tbc.motorFLeft.setTargetPosition(-11000);
            tbc.motorRRight.setTargetPosition(-13400);
            tbc.setMotorRLeftPower(0.5f);
            tbc.setMotorFRightPower(-0.5f);
            tbc.setMotorFLeftPower(0.5f);
            tbc.setMotorRRightPower(-0.5f);

            if ( (Math.abs(tbc.motorFRight.getCurrentPosition() - -13400) < 200)
                    && (Math.abs(tbc.motorRRight.getCurrentPosition() - -134000) < 200)
                    && (Math.abs(tbc.motorFLeft.getCurrentPosition() - -11000) < 200)
                    && (Math.abs(tbc.motorRLeft.getCurrentPosition() - -11000) < 200))
            {
                state = 2;
            }
        }

        if (state == 2) {
            tbc.motorFRight.setTargetPosition(-15500);
            tbc.motorRLeft.setTargetPosition(-13100);
            tbc.motorFLeft.setTargetPosition(-13100);
            tbc.motorRRight.setTargetPosition(-15500);
            tbc.setMotorRLeftPower(-0.5f);
            tbc.setMotorFRightPower(-0.5f);
            tbc.setMotorFLeftPower(-0.5f);
            tbc.setMotorRRightPower(-0.5f);

            if ( (Math.abs(tbc.motorFRight.getCurrentPosition() - -15500) < 100)
                    && (Math.abs(tbc.motorRRight.getCurrentPosition() - -15500) < 100)
                    && (Math.abs(tbc.motorFLeft.getCurrentPosition() - -13100) < 100)
                    && (Math.abs(tbc.motorRLeft.getCurrentPosition() - -13100) < 100))
            {
                state = 3;
                s3starttime = mRuntime.time();
            }
        }

        if (state == 3) {
            double s3elapsed = mRuntime.time() - s3starttime;

            tbc.buttonServoSpeed = 1.0f;

            if (s3elapsed > 2.0) {
                state = 4;
            }

        }

        if (state == 4) {
            double s4elapsed = mRuntime.time() - s4starttime;
            Double climberNewPos = tbc.climberPosition;

            climberNewPos = tbc.climberPosition + climberDelta;
            tbc.climberPosition = Range.clip(climberNewPos, tbc.CLIMBER_MIN_RANGE, tbc.CLIMBER_MAX_RANGE);
            tbc.setClimberPosition(tbc.climberPosition);

            if (s4elapsed > 5.0) {
                state = 5;
            }
        }

        if (state == 5) {
            tbc.setClimberPosition(0.0);
        }


            //mRuntime.reset();

           // while (time <= 0.5) {
               // tbc.setButtonServoSpeed(0.5);
           // }

            //tbc.setClimberPosition(0.0);


    }
}
