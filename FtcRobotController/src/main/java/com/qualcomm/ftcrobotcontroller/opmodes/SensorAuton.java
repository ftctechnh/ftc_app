package com.qualcomm.ftcrobotcontroller.opmodes;

import android.util.Log;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotorController;
import com.qualcomm.robotcore.hardware.LightSensor;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;

import org.ndhsb.ftc7593.tbc;

public class SensorAuton extends OpMode {


    public SensorAuton() {
    }
    public ElapsedTime mRuntime = new ElapsedTime();
    //public SoundPool mySound;
    //public int beepID;

    double time = mRuntime.time();
    double s2starttime;
    double s3starttime;
    double s4starttime;
    double s5starttime;

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

       // mySound = new SoundPool(1, AudioManager.STREAM_MUSIC, 0); // PSM
        //beepID = mySound.load(hardwareMap.appContext, R.raw.beep, 1); // PSM

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
        //telemetry.addData("state", "state:" + String.format("%.d", state));

        if (state == 0) {
            tbc.motorRLeft.setTargetPosition(-12400); // -12000
            tbc.motorFLeft.setTargetPosition(-12400);
            tbc.motorRRight.setTargetPosition(-12900); // -12400
            tbc.motorFRight.setTargetPosition(-12900); // -10600

            tbc.setMotorRLeftPower(-1.0f);
            tbc.setMotorFLeftPower(-1.0f);
            tbc.setMotorRRightPower(-1.0f);
            tbc.setMotorFRightPower(-1.0f);


/*
                    (Math.abs(tbc.motorRLeft.getCurrentPosition() - tbc.motorRLeft.getTargetPosition()) < 100) &&
                    (Math.abs(tbc.motorFLeft.getCurrentPosition() - tbc.motorFLeft.getTargetPosition()) < 100) &&
                    (Math.abs(tbc.motorRRight.getCurrentPosition() - tbc.motorRRight.getTargetPosition()) < 100) &&
                    (Math.abs(tbc.motorFRight.getCurrentPosition() - tbc.motorFRight.getTargetPosition()) < 100)

*/

            if  ((tbc.motorRLeft.getPower() == 0.0) &&
                    (tbc.motorFLeft.getPower() == 0.0) &&
                    (tbc.motorRRight.getPower() == 0.0) &&
                    (tbc.motorFRight.getPower() == 0.0))
            {
                Log.d("EncoderAuton", "state 0; next state is 1");
                state = 1; // should be 1, but just a test.
                //Log.d("EncoderAuton", "rl:" + String.format("%.2f", tbc.motorRLeft.getPower()));
                //Log.d("EncoderAuton", "fl:" + String.format("%.2f", tbc.motorFLeft.getPower()));
                //Log.d("EncoderAuton", "rr:" + String.format("%.2f", tbc.motorRRight.getPower()));
                //Log.d("EncoderAuton", "fr:" + String.format("%.2f", tbc.motorFRight.getPower()));
            }
        }

        if (state == 1) {
            tbc.motorRLeft.setTargetPosition(-12400);
            tbc.motorFLeft.setTargetPosition(-12400);
            tbc.motorRRight.setTargetPosition(-14800);
            tbc.motorFRight.setTargetPosition(-14800);

            tbc.setMotorRLeftPower(0.5f);
            tbc.setMotorFLeftPower(0.5f);
            tbc.setMotorRRightPower(-0.5f);
            tbc.setMotorFRightPower(-0.5f);

           /* (Math.abs(tbc.motorFRight.getCurrentPosition() - -13400) < 200) &&
                    (Math.abs(tbc.motorRRight.getCurrentPosition() - -134000) < 200) &&
                    (Math.abs(tbc.motorFLeft.getCurrentPosition() - -11000) < 200) &&
                    (Math.abs(tbc.motorRLeft.getCurrentPosition() - -11000) < 200))
                    */

            if  ( (tbc.motorRLeft.getPower() == 0.0) &&
                    (tbc.motorFLeft.getPower() == 0.0) &&
                    (tbc.motorRRight.getPower() == 0.0) &&
                    (tbc.motorFRight.getPower() == 0.0))

            {
                Log.d("EncoderAuton", "state 1; next state is 2");
                state = 2; // should be 2
                s2starttime = mRuntime.time();
            }
        }

        if (state == 2) {
            double s2elapsed = mRuntime.time() - s2starttime;

            if (s2elapsed > 10.0) {
                Log.d("EncoderAuton", "state 2; next state is 3");
                state = 3;
                s3starttime = mRuntime.time();
            }
        }

        if (state == 3) {
            double s3elapsed = mRuntime.time() - s3starttime;

            tbc.motorRLeft.setTargetPosition(-13900);
            tbc.motorFLeft.setTargetPosition(-13900);
            tbc.motorRRight.setTargetPosition(-16300);
            tbc.motorFRight.setTargetPosition(-16300);

            tbc.setMotorRLeftPower(-0.5f);
            tbc.setMotorFLeftPower(-0.5f);
            tbc.setMotorRRightPower(-0.5f);
            tbc.setMotorFRightPower(-0.5f);

            /*(Math.abs(tbc.motorFRight.getCurrentPosition() - -15500) < 100)
                    && (Math.abs(tbc.motorRRight.getCurrentPosition() - -15500) < 100)
                    && (Math.abs(tbc.motorFLeft.getCurrentPosition() - -13100) < 100)
                    && (Math.abs(tbc.motorRLeft.getCurrentPosition() - -13100) < 100)*/


            if (s3elapsed > 2) {
                Log.d("EncoderAuton", "rl:" + String.format("%.2f", tbc.motorRLeft.getPower()));
                Log.d("EncoderAuton", "fl:" + String.format("%.2f", tbc.motorFLeft.getPower()));
                Log.d("EncoderAuton", "rr:" + String.format("%.2f", tbc.motorRRight.getPower()));
                Log.d("EncoderAuton", "fr:" + String.format("%.2f", tbc.motorFRight.getPower()));

                Log.d("EncoderAuton", "timeout state 3; next state is 4");
                state = 4; //should be 4
                s4starttime = mRuntime.time();
            }

            if ((tbc.motorRLeft.getPower() == 0.0) &&
                    (tbc.motorFLeft.getPower() == 0.0) &&
                    (tbc.motorRRight.getPower() == 0.0) &&
                    (tbc.motorFRight.getPower() == 0.0)
                    )
            {
                Log.d("EncoderAuton", "state 3; next state is 4");
                state = 4; //should be 4
                s4starttime = mRuntime.time();
            }
        }


        if (state == 4) {
            double reflection = 0.0;

            reflection = tbc.light1.alpha();

            if (reflection < 0.3) {
                tbc.setMotorRLeftPower(0.5f);
                tbc.setMotorFLeftPower(0.5f);
                tbc.setMotorRRightPower(0.5f);
                tbc.setMotorFRightPower(0.5f);
            } else if (reflection == 0.3) {
                state = 5;
            }
        }

        if (state == 5) {
            double reflection = 0.0;
            double reflection2 = 0.0;

            reflection = tbc.light1.alpha();
            reflection2 = tbc.light2.alpha();

            if (reflection < 0.3 && reflection2 < 0.3) {
                tbc.setMotorRLeftPower(-0.5f);
                tbc.setMotorFLeftPower(-0.5f);
                tbc.setMotorRRightPower(0.5f);
                tbc.setMotorFRightPower(0.5f);
            } else if (reflection == 0.3) {
                state = 6;
            }
        }

        if (state == 6) {

            double s4elapsed = mRuntime.time() - s4starttime;

            tbc.buttonServoSpeed = 0.0f;
            tbc.setButtonServoSpeed(tbc.buttonServoSpeed);

            if (s4elapsed > 2.0) {
                tbc.buttonServoSpeed = 0.5f;
                tbc.setButtonServoSpeed(tbc.buttonServoSpeed);

                Log.d("EncoderAuton", "state 4; next state is 5");
                state = 7;
                s5starttime = mRuntime.time();
            }
        }

        Double climberNewPos = tbc.climberPosition;
        if (state == 7) {
            double s5elapsed = mRuntime.time() - s5starttime; // [MPH - where does s4starttime get set! ?]

            climberNewPos = tbc.climberPosition - climberDelta;
            tbc.climberPosition = Range.clip(climberNewPos, tbc.CLIMBER_MIN_RANGE, tbc.CLIMBER_MAX_RANGE);
            tbc.setClimberPosition(tbc.climberPosition);

            if (s5elapsed > 10.0) {
                Log.d("EncoderAuton", "state 5; next state is 6");
                state = 8;
            }
        }

        if (state == 8) {
            tbc.setClimberPosition(0.0);
        }


        //mRuntime.reset();

        // while (time <= 0.5) {
        // tbc.setButtonServoSpeed(0.5);
        // }

        //tbc.setClimberPosition(0.0);


    }
}
