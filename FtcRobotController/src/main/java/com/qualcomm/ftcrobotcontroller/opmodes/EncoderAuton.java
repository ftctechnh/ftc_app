package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.ndhsb.ftc7593.tbc;

public class EncoderAuton extends OpMode {


    public EncoderAuton() {
    }
    public ElapsedTime mRuntime = new ElapsedTime();

    double time = mRuntime.time();

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

        if (tbc.sc != null) {
            tbc.sc.pwmEnable(); // enable servo controller PWM outputs
        }

        mRuntime.reset();
    }

    public void loop() {
        telemetry.addData("Text", "*** Robot Data***");
        // telemetry.addData("arm", "arm:  " + String.format("%.2f", armPosition));
        // telemetry.addData("claw", "claw:  " + String.format("%.2f", clawPosition));
        // telemetry.addData("left", "left:" + String.format("%.2f", tbc.motorFLeft));
        telemetry.addData("button servo", "button servo: " + String.format("%.2f", tbc.buttonServoSpeed));
        telemetry.addData("climber", "climber:  " + String.format("%.2f", tbc.climberPosition));
        telemetry.addData("sliderL", "sliderL: " + String.format("%.2f", tbc.sliderLPosition));
        telemetry.addData("sliderR", "sliderR:" + String.format("%.2f", tbc.sliderRPosition));
        telemetry.addData("mtape", "mtape: " + String.format("%.2f", tbc.mtapePosition));
        
        while (Math.abs(tbc.motorFLeft.getCurrentPosition()) < 13500) {
            tbc.setMotorFLeftPower(-1.0f);
            tbc.setMotorFRightPower(-1.0f);
            tbc.setMotorRRightPower(-1.0f);
            tbc.setMotorRLeftPower(-1.0f);
        }
        if (false) {


            tbc.setMotorFLeftPower(0.0f);
            tbc.setMotorFRightPower(0.0f);
            tbc.setMotorRRightPower(0.0f);
            tbc.setMotorRLeftPower(0.0f);

            while (Math.abs(tbc.motorFLeft.getCurrentPosition()) < 12300) {
                tbc.setMotorFLeftPower(1.0f);
                tbc.setMotorFRightPower(1.0f);
                tbc.setMotorRRightPower(-1.0f);
                tbc.setMotorRLeftPower(-1.0f);
            }

            tbc.setMotorFLeftPower(0.0f);
            tbc.setMotorFRightPower(0.0f);
            tbc.setMotorRRightPower(0.0f);
            tbc.setMotorRLeftPower(0.0f);
            tbc.setSnowplowPosition(0.4);

            while (Math.abs(tbc.motorFLeft.getCurrentPosition()) < 13700) {
                tbc.setMotorFLeftPower(-1.0f);
                tbc.setMotorFRightPower(-1.0f);
                tbc.setMotorRRightPower(-1.0f);
                tbc.setMotorRLeftPower(-1.0f);
            }

            tbc.setMotorFLeftPower(0.0f);
            tbc.setMotorFRightPower(0.0f);
            tbc.setMotorRRightPower(0.0f);
            tbc.setMotorRLeftPower(0.0f);

            mRuntime.reset();

            while (time <= 0.5) {
                tbc.setButtonServoSpeed(0.5);
            }

            tbc.setClimberPosition(0.0);

        }
    }
}
