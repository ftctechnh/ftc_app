package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.LightSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.hardware.HiTechnicNxtLightSensor;

//import com.qualcomm.ftcrobotcontroller.opmodes.MC;
//import com.qualcomm.robotcore.hardware.DcMotorController;
//import com.qualcomm.robotcore.hardware.Servo;
//import com.qualcomm.robotcore.util.Range;

public class Move extends LinearOpMode{

    public Move(){
    }

    final static double speed = .5;
    static DcMotor armLowerMotor;
    DcMotor armUpperMotor;
    boolean peopleAndLight = true;
    boolean climbing = false;
    static boolean isBlue = true;
    final static int distance = 1000;
    DcMotor rightMotor;
    DcMotor leftMotor;
    HiTechnicNxtLightSensor light;


    public void runOpMode(){
        //waitForStart();
        armUpperMotor = hardwareMap.dcMotor.get("armUpperMotor");
        armLowerMotor = hardwareMap.dcMotor.get("armLowerMotor");
        light = (HiTechnicNxtLightSensor)hardwareMap.lightSensor.get("lightSensor");
        //HiTechnicNxtLightSensor.get("lightSensor");
        rightMotor = hardwareMap.dcMotor.get("motor_right");
        rightMotor.setDirection(DcMotor.Direction.REVERSE);
        leftMotor = hardwareMap.dcMotor.get("motor_left");
        double lightVal;

        light.enableLed(true);
        /*//Code to test the light sensor
        while(true) {
            lightVal = light.getLightDetected();
            telemetry.addData("Text", "*** Robot Data***");
            telemetry.addData("Light", "Light: " + String.format("%.2f", (float) lightVal));
        }*/
        /*
        for(int i = 0; i <= 10000; i++){
            rightMotor.setPower(.666);
            leftMotor.setPower(.666);
            telemetry.addData("Count", "i: " + String.format("%.2f", (float) i));
        }

        rightMotor.setPower(0);
        leftMotor.setPower(0);
        */
        //move for distance (1000) at speed using motors right and left
        int rightPos = rightMotor.getCurrentPosition();
        int leftPos = leftMotor.getCurrentPosition();

        rightMotor.setTargetPosition(distance + rightPos);
        leftMotor.setTargetPosition(distance + leftPos);
        rightMotor.setPower(speed);
        leftMotor.setPower(speed);
        while(leftMotor.getTargetPosition() > leftMotor.getCurrentPosition()) {

            telemetry.addData("Current Position", "Pos: " + String.format("%.2f", (float) leftMotor.getCurrentPosition()));
        }

        rightMotor.setPower(0);
        leftMotor.setPower(0);
       // waitOneHardwareCycle();
    }
}