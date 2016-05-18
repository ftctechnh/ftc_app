package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.ftcrobotcontroller.FreshClasses.FreshMethods;
import com.qualcomm.ftcrobotcontroller.FreshClasses.FreshMotors;
import com.qualcomm.ftcrobotcontroller.FreshClasses.FreshServos;
import com.qualcomm.ftcrobotcontroller.FreshClasses.FreshSensors;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;
import com.qualcomm.robotcore.hardware.IrSeekerSensor;
import com.qualcomm.robotcore.hardware.OpticalDistanceSensor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.TouchSensor;

/**
 * Created by Naisan on 4/13/2016.
 */
    public class TheFreshMenAuton extends LinearOpMode{

    DcMotor M_backLeft, //andy
            M_backRight, //barry
            M_frontLeft, //carl
            M_frontRight, //daniel
            M_rackPinion,
            M_sweeper;
    Servo S_rackRight,
            S_rackLeft,
            S_bucket;
    ColorSensor colorSensorLeft,
                colorSensorRight;
    OpticalDistanceSensor distanceSensor;
    IrSeekerSensor irSensor;
    TouchSensor T_Lift;
    FreshMethods autonMethods;
    FreshMotors motors = new FreshMotors(M_backLeft, M_backRight, M_frontLeft,M_frontRight,M_rackPinion, M_sweeper);
    FreshServos servos = new FreshServos(S_rackRight,S_rackLeft,S_bucket);
    FreshSensors sensors = new FreshSensors(T_Lift, colorSensorLeft, colorSensorRight, distanceSensor, irSensor);

    @Override
    public void runOpMode() throws InterruptedException {
        //DC Motors
        M_backLeft = hardwareMap.dcMotor.get("BackLeft");
        M_backRight = hardwareMap.dcMotor.get("BackRight");
        M_frontLeft = hardwareMap.dcMotor.get("FrontLeft");
        M_frontRight = hardwareMap.dcMotor.get("FrontLeft");
        M_rackPinion = hardwareMap.dcMotor.get("RackPinion");
        M_sweeper = hardwareMap.dcMotor.get("Sweeper");
        //Servos
        S_bucket = hardwareMap.servo.get("Bucket");
        S_rackLeft = hardwareMap.servo.get("RackLeft");
        S_rackRight = hardwareMap.servo.get("RackRight");
        colorSensorLeft = hardwareMap.colorSensor.get("colorSensorLeft");
        colorSensorRight = hardwareMap.colorSensor.get("colorSensorRight");
        distanceSensor = hardwareMap.opticalDistanceSensor.get("distancesSensor");
        irSensor = hardwareMap.irSeekerSensor.get("irSensor");
        
        /*
        M_backLeft.setChannelMode(DcMotorController.RunMode.RESET_ENCODERS);
        M_backRight.setChannelMode(DcMotorController.RunMode.RESET_ENCODERS);
        M_frontLeft.setChannelMode(DcMotorController.RunMode.RESET_ENCODERS);
        M_frontRight.setChannelMode(DcMotorController.RunMode.RESET_ENCODERS);
        */

        waitForStart();





    }

}



