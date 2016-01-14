package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotorController;
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
        //tbc.setMotorRRightPower(0.0f);
        //tbc.setMotorRLeftPower(0.0f);
        //tbc.setMotorFLeftPower(0.0f);
        // tbc.setMotorFRightPower(0.0f);
        tbc.setDriveMode(DcMotorController.RunMode.RESET_ENCODERS);
        //tbc.setDriveMode(DcMotorController.RunMode.RUN_WITHOUT_ENCODERS);


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

        tbc.motorFRight.setTargetPosition(-13500);
        tbc.motorRLeft.setTargetPosition(-13500);
        tbc.motorFLeft.setTargetPosition(-13500);
        tbc.motorRRight.setTargetPosition(-13500);
        tbc.setMotorRLeftPower(-1.0f);
        tbc.setMotorFRightPower(-1.0f);
        tbc.setMotorFLeftPower(-1.0f);
        tbc.setMotorRRightPower(-1.0f);

        /*
        tbc.motorFRight.setTargetPosition(-15000);
        tbc.motorRLeft.setTargetPosition(-12200);
        tbc.motorFLeft.setTargetPosition(-12200);
        tbc.motorRRight.setTargetPosition(-15000);
        tbc.setMotorRLeftPower(0.5f);
        tbc.setMotorFRightPower(-0.5f);
        tbc.setMotorFLeftPower(0.5f);
        tbc.setMotorRRightPower(-0.5f);

        tbc.motorFRight.setTargetPosition(-16200);
        tbc.motorRLeft.setTargetPosition(-13600);
        tbc.motorFLeft.setTargetPosition(-13600);
        tbc.motorRRight.setTargetPosition(-16200);
        tbc.setMotorRLeftPower(-0.5f);
        tbc.setMotorFRightPower(-0.5f);
        tbc.setMotorFLeftPower(-0.5f);
        tbc.setMotorRRightPower(-0.5f);

        */

            //mRuntime.reset();

           // while (time <= 0.5) {
               // tbc.setButtonServoSpeed(0.5);
           // }

            //tbc.setClimberPosition(0.0);


    }
}
