/*package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;

import org.ndhsb.ftc7593.tbc;
import org.ndhsb.ftc7593.AutonChoice;

public class EncoderAuton extends PushBotHardware {

    public ElapsedTime mRuntime = new ElapsedTime();

    public EncoderAuton() {

        tbc.setMotorFLeftPower();
        tbc.setMotorFRightPower();
        tbc.setMotorRLeftPower();
        tbc.setMotorRRightPower();

        tbc.setMotorFLeftPower();
        tbc.setMotorFRightPower();
        tbc.setMotorRLeftPower();
        tbc.setMotorRRightPower();

        tbc.setMotorFLeftPower();
        tbc.setMotorFRightPower();
        tbc.setMotorRLeftPower();
        tbc.setMotorRRightPower();
    }

    public void init() {
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
        telemetry.addData("button servo", "button servo: " + String.format("%.2f", tbc.buttonServoSpeed));
        telemetry.addData("climber", "climber:  " + String.format("%.2f", tbc.climberPosition));
        telemetry.addData("sliderL", "sliderL: " + String.format("%.2f", tbc.sliderLPosition));
        telemetry.addData("sliderR", "sliderR:" + String.format("%.2f", tbc.sliderRPosition));
        telemetry.addData("mtape", "mtape: " + String.format("%.2f", tbc.mtapePosition));
    }
}
*/