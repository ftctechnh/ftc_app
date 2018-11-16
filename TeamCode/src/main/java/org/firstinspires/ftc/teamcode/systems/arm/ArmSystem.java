package org.firstinspires.ftc.teamcode.systems.arm;/* Copyright (c) 2017 FIRST. All rights reserved.
 *
 */


import com.qualcomm.robotcore.hardware.AnalogInput;
import com.qualcomm.robotcore.hardware.DcMotor;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.teamcode.components.scale.LogarithmicRamp;
import org.firstinspires.ftc.teamcode.components.scale.Point;
import org.firstinspires.ftc.teamcode.components.scale.Ramp;
import org.firstinspires.ftc.teamcode.systems.base.System;

public class ArmSystem extends System {
    private DcMotor motor1;
    private DcMotor motor2;
    private AnalogInput potentiometer;
    private Ramp rampUp;
    private Ramp rampDown;

    private final double PotentiometerMaximum = 1.1;
    private final double PotentiometerMiddle = 0.55;
    private final double PotentiometerLatch = 0.86;
    private final double PotentiometerRange = 0.01;
    private final double PotentiometerMinimum = 0.08;
    private final double MaxPower = 0.3;
    private final double MinPower = 0.01;

    private ArmState currentState;

    public ArmSystem(OpMode opMode) {
        super(opMode, "ArmSystem");
        motor1 = hardwareMap.dcMotor.get( "parallelM1");
        motor2 = hardwareMap.dcMotor.get( "parallelM2");
        potentiometer = hardwareMap.get(AnalogInput.class, "potentiometer");
        setState(ArmState.IDLE);
        rampUp = new LogarithmicRamp(
                new Point(PotentiometerMiddle, MaxPower),
                new Point(PotentiometerMaximum, MinPower)
        );
        rampDown = new LogarithmicRamp(
                new Point(PotentiometerMinimum, MinPower),
                new Point(PotentiometerMaximum, MaxPower)
        );
    }

    public void setState(ArmState state) {
        currentState = state;
    }

    public void run() {
        switch (currentState) {
            case ROTATING_DROP:
                rotateDrop();
                break;
            case ROTATING_LATCH:
                rotateLatch();
                break;
            case ROTATING_PICKUP:
                rotatePickup();
                break;
        }
    }
  
    public void rotatePickup() {
        motor1.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        motor2.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        if (!isAtPickupPosition()) {
            telemetry.log("power", rampDown.scaleX(potentiometer.getVoltage()));
            motor1.setPower(0.2);
            motor2.setPower(-0.2);
            telemetry.write();
        } else {
            setState(ArmState.IDLE);
            motor1.setPower(0.0);
            motor2.setPower(0.0);
        }
    }

    private boolean isAtPickupPosition() {
        return potentiometer.getVoltage() <= PotentiometerMinimum;
    }

    public void rotateLatch(){
        motor1.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        motor2.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        if (!isAtRotateLatch()) {
            motor1.setPower(-getPower());
            motor2.setPower(getPower());
        } else {
            setState(ArmState.IDLE);
            motor1.setPower(0.0);
            motor2.setPower(0.0);
        }
    }

    private boolean isAtRotateLatch() {
        return potentiometer.getVoltage() >= PotentiometerLatch - PotentiometerRange &&
                potentiometer.getVoltage() <= PotentiometerLatch + PotentiometerRange;
    }

    private double getPower() {
        return potentiometer.getVoltage() > PotentiometerLatch + PotentiometerRange ?
                -MaxPower :
                MaxPower;
    }

    public void rotateDrop() {
        motor1.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        motor2.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        if (!isAtDropPosition()) {
            motor1.setPower(-0.2);
            motor2.setPower(0.2);
        } else {
            setState(ArmState.IDLE);
            motor1.setPower(0.0);
            motor2.setPower(0.0);
        }
    }

    private boolean isAtDropPosition() {
        return potentiometer.getVoltage() >= PotentiometerMaximum;
    }
}
