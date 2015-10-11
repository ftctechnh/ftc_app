package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.OpticalDistanceSensor;
import com.qualcomm.robotcore.hardware.*;

//Replace with robot we are using
import com.technicbots.SparringBot;

public class SparringRobotODS extends OpMode {
    DcMotor rightMotor;
    DcMotor leftMotor;
    OpticalDistanceSensor opticalDistanceSensor;

    double EOPDThreshold = 0.5;

    final static int ENCODER_CPR = 1440;
    final static double GEAR_RATIO = 1;  //ie: if the wheel turns three times in 1 motor rotation, this value is 1/3
    final static int WHEEL_DIAMETER = 3; //Diameter in cm
    final static int DISTANCE = 20;      //Distance in cm

    final static double CIRCUMFERENCE = Math.PI * WHEEL_DIAMETER;
    final static double ROTATIONS = DISTANCE / CIRCUMFERENCE;
    final static double COUNTS = ENCODER_CPR * ROTATIONS * GEAR_RATIO;


    @Override
    public void init() {
        leftMotor = hardwareMap.dcMotor.get("left_drive");
        rightMotor = hardwareMap.dcMotor.get("right_drive");
        opticalDistanceSensor = hardwareMap.opticalDistanceSensor.get("sensor_EOPD");
        leftMotor.setChannelMode(DcMotorController.RunMode.RESET_ENCODERS);
    }

    @Override
    public void start() {

        leftMotor.setTargetPosition((int) COUNTS);
        leftMotor.setChannelMode(DcMotorController.RunMode.RUN_TO_POSITION);
    }

    public void loop() {
        double reflectance = opticalDistanceSensor.getLightDetected();

        if (reflectance >= EOPDThreshold) {
            leftMotor.setPower(0.3);
            rightMotor.setPower(0.5);
        } else {
            leftMotor.setPower(0.5);
            rightMotor.setPower(0.3);
        }

        telemetry.addData("Reflectance Value", reflectance);
    }
}