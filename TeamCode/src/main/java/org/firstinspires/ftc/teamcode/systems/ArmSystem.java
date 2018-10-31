package org.firstinspires.ftc.teamcode.systems;/* Copyright (c) 2017 FIRST. All rights reserved.
 *
 */


import com.qualcomm.robotcore.hardware.AnalogInput;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.qualcomm.robotcore.util.ElapsedTime;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.teamcode.systems.BaseSystems.System;

import java.util.HashSet;

public class ArmSystem extends System {

    /*
       TODO:
       - encoder values on slide to determine set positions
       - set those positions
       - ramp the encoder values
     */

    private DigitalChannel limitTop;
    private DigitalChannel limitMiddle;
    private DigitalChannel limitBottom;
    private DcMotor motor1;
    private DcMotor motor2;
    private DcMotor winch;
    private AnalogInput potentiometer;

    private final double PotentiometerMaximum = 0.8;
    private final double PotentiometerLatch = 0.7;
    private final double PotentiometerMinimum = 0.1;

    private final int EncoderMaximum = 100;
    private final int EncoderMinimum = 200;

    private boolean isAtTop = false;

    private boolean debouncing = false;
    private boolean middleIsPressed = false;
    private boolean topIsPressed = false;

    private HashSet<ArmState> states;

    public ArmSystem(OpMode opMode) {
        super(opMode, "ArmSystem");
        ElapsedTime time = new ElapsedTime();
        motor1 = hardwareMap.dcMotor.get( "parallelM1");
        motor2 = hardwareMap.dcMotor.get( "parallelM2");
        winch = hardwareMap.dcMotor.get("winch");
        limitTop = hardwareMap.get(DigitalChannel.class, "limitTop");
        limitMiddle = hardwareMap.get(DigitalChannel.class, "limitMiddle");
        potentiometer = hardwareMap.get(AnalogInput.class, "potentiometer");

        states = new HashSet<ArmState>();

        limitTop.setMode(DigitalChannel.Mode.INPUT);
        limitMiddle.setMode(DigitalChannel.Mode.INPUT);
    }

    public void addState(ArmState state) {
        states.add(state);
    }

    public void run() {
        if (states.contains(ArmState.WINCH_TOP)) {
            slideUp();
        }
        if (states.contains(ArmState.WINCH_BOTTOM)) {
            slideDown();
        }
        if (states.contains(ArmState.ROTATE_DOWN)) {
            robotDown();
        }
        if (states.contains(ArmState.ROTATE_LATCH)) {
            robotLatch();
        }
        if (states.contains(ArmState.ROTATE_UP)) {
            robotUp();
        }
    }

    public void slideUp() {
        motor1.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        motor2.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        if (limitTop.getState() || limitMiddle.getState()) {
            telemetry.log("Top", limitTop.getState());
            telemetry.log("Middle", limitMiddle.getState());
            telemetry.log("Winch", winch.getCurrentPosition());
            telemetry.write();
            winch.setPower(0.5);
        }
        if (!limitTop.getState() && !limitMiddle.getState())
        {
            states.remove(ArmState.WINCH_TOP);
            winch.setPower(0.0);
        }
    }

    public void slideDown() {
        boolean hitTop = limitTop.getState();
        boolean hitMiddle = limitMiddle.getState();
        motor1.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        motor2.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        if (!limitTop.getState() || !limitMiddle.getState()) {
            telemetry.log("Top", hitTop);
            telemetry.log("Middle", hitMiddle);
            telemetry.write();

            winch.setPower(-0.5);
            telemetry.log("Winch", winch.getCurrentPosition());
        }
        if (limitTop.getState() && limitMiddle.getState())
        {
            states.remove(ArmState.WINCH_BOTTOM);
            winch.setPower(0);
        }
    }
  
    public void robotDown() {
        motor1.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        motor2.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        if (potentiometer.getVoltage() > PotentiometerMinimum) {
            telemetry.log("Potentiometer", potentiometer.getVoltage());
            telemetry.log("Motor 1", "Encoder {0}", motor1.getCurrentPosition());
            telemetry.log("Motor 2", "Encoder {0}", motor2.getCurrentPosition());
            telemetry.write();

            motor1.setPower(0.03);
            motor2.setPower(-0.03);
        } else {
            states.remove(ArmState.ROTATE_DOWN);
            motor1.setPower(0.0);
            motor2.setPower(0.0);
        }
    }

    public void robotLatch(){
        motor1.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        motor2.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        int encoderMax = motor1.getCurrentPosition() - EncoderMinimum;
        if (potentiometer.getVoltage() < PotentiometerLatch) {
            telemetry.log("Potentiometer", potentiometer.getVoltage());
            telemetry.log("Motor 1", "Encoder {0}", motor1.getCurrentPosition());
            telemetry.log("Motor 2", "Encoder {0}", motor2.getCurrentPosition());
            telemetry.write();

            motor1.setPower(-0.03);
            motor2.setPower(0.03);
        } else {
            states.remove(ArmState.ROTATE_LATCH);
            motor1.setPower(0.0);
            motor2.setPower(0.0);
        }
    }

    public void robotUp() {
        motor1.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        motor2.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        int encoderMax = motor1.getCurrentPosition() - EncoderMinimum;
        if (potentiometer.getVoltage() < PotentiometerMaximum) {
            telemetry.log("Potentiometer", potentiometer.getVoltage());
            telemetry.log("Motor 1", "Encoder {0}", motor1.getCurrentPosition());
            telemetry.log("Motor 2", "Encoder {0}", motor2.getCurrentPosition());
            telemetry.write();

            motor1.setPower(-0.03);
            motor2.setPower(0.03);
        } else {
            states.remove(ArmState.ROTATE_UP);
            motor1.setPower(0.0);
            motor2.setPower(0.0);
        }
    }
}
