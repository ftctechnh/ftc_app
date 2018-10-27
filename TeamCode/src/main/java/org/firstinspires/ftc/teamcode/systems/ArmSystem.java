package org.firstinspires.ftc.teamcode.systems;/* Copyright (c) 2017 FIRST. All rights reserved.
 *
 */


import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.AnalogInput;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.opmodes.debuggers.LinearOpModeDebugger;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

import org.firstinspires.ftc.teamcode.components.Motors.DriveMotor;
import org.firstinspires.ftc.teamcode.systems.BaseSystems.System;

public class ArmSystem extends System {

    DigitalChannel limitTop;
    DigitalChannel limitMiddle;
    DigitalChannel limitBottom;
    DcMotor motor1;
    DcMotor motor2;
    DcMotor winch;
    AnalogInput potentiometer;

    double potenMax = 3.3;
    double potenMin = 2.43;

    boolean isAtTop = false;

    boolean debouncing = false;
    boolean middleIsPressed = false;
    boolean topIsPressed = false;


    public ArmSystem(OpMode opMode) {
        super(opMode, "ArmSystem");
        ElapsedTime time = new ElapsedTime();
        motor1 = hardwareMap.dcMotor.get( "parallelM1");
        motor2 = hardwareMap.dcMotor.get( "parallelM2");
        winch = hardwareMap.dcMotor.get( "winch");
        limitTop = hardwareMap.get(DigitalChannel.class, "limitTop");
        limitMiddle = hardwareMap.get(DigitalChannel.class, "limitMiddle");
        //limitBottom = hardwareMap.get(DigitalChannel.class, "limitBot");

        potentiometer = hardwareMap.get(AnalogInput.class, "potentiometer");

        limitTop.setMode(DigitalChannel.Mode.INPUT);
        limitMiddle.setMode(DigitalChannel.Mode.INPUT);
        //limitBottom.setMode(DigitalChannel.Mode.INPUT);
    }



    public void slideUp() {


        motor1.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        motor2.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        while(!limitTop.getState() && !limitMiddle.getState()) {
            boolean hitTop = limitTop.getState();
            String strTop = String.valueOf(hitTop);
            boolean hitMiddle = limitMiddle.getState();
            String strMiddle = String.valueOf(hitMiddle);

            telemetry.log("Top", strTop);
            telemetry.log("Middle", strMiddle);
            telemetry.write();

            motor1.setPower(0.8);
            motor2.setPower(-0.8);
        }
        motor1.setPower(0.0);
        motor2.setPower(0.0);

    }

    public void slideDown() {

        boolean hitTop = limitTop.getState();
        String strTop = String.valueOf(hitTop);
        boolean hitMiddle = limitMiddle.getState();
        String strMiddle = String.valueOf(hitMiddle);

        motor1.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        motor2.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        while(limitTop.getState() && limitMiddle.getState()) {

            telemetry.log("Top", strTop);
            telemetry.log("Middle", strMiddle);
            telemetry.write();

            motor1.setPower(0.1);
            motor2.setPower(-0.1);
        }
        motor1.setPower(0.0);
        motor2.setPower(0.0);
    }
  
    public void robotUp() {

        motor1.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        motor2.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        int encoderMax = motor1.getCurrentPosition();
        while(potentiometer.getVoltage() > potenMin || motor1.getCurrentPosition() < encoderMax + 1000) {
            telemetry.log("Motor 1,2 power","yes");
            motor1.setPower(-0.05);
            motor2.setPower(0.05);
        }
        motor1.setPower(0.0);
        motor2.setPower(0.0);

    }

    public void robotDown(){

        motor1.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        motor2.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

            motor1.setPower(0.1);
            motor2.setPower(-0.1);

    }



    public void robotDown(int encoderTicks){
        if (!limitMiddle.getState()) {
            motor1.setTargetPosition(encoderTicks);
            motor2.setTargetPosition(encoderTicks);
            motor1.setPower(-0.05);
            motor2.setPower(0.05);
        }
    }
}
