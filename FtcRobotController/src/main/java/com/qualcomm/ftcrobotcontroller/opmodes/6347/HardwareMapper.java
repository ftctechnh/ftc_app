package com.qualcomm.ftcrobotcontroller.opmodes.ftc6347;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;
import com.qualcomm.robotcore.hardware.GyroSensor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.LightSensor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.TouchSensor;
import com.qualcomm.robotcore.hardware.UltrasonicSensor;

/**
 * Created by FTCGearedUP on 3/3/2016.
 */
public class HardwareMapper {
    HardwareMap hardwareMap;

    DcMotor motor1;
    DcMotor motor2;
    DcMotor motor3;
    DcMotor motor4;
    DcMotor motor5;
    DcMotor motor6;
    DcMotor motor7;
    DcMotorController winch;
    DcMotorController wheelie;
    Servo red;
    Servo blue;
    Servo arm1;
    Servo climberExtend;
    Servo climberDump;
    UltrasonicSensor ultrasonicLeft;
    UltrasonicSensor ultrasonicRight;
    LightSensor reflectedLight;
    TouchSensor touch1;
    GyroSensor xAxis;
    GyroSensor zAxis;

    public HardwareMapper(HardwareMap _hardwareMap) {
        this.hardwareMap = _hardwareMap;

        ///////////////////////Motors////////////////////////////
        motor1 = hardwareMap.dcMotor.get("1");
        motor2 = hardwareMap.dcMotor.get("2");
        motor3 = hardwareMap.dcMotor.get("3");
        motor4 = hardwareMap.dcMotor.get("4");
        motor5 = hardwareMap.dcMotor.get("5");
        motor6 = hardwareMap.dcMotor.get("6");
        motor7 = hardwareMap.dcMotor.get("7");

        winch = hardwareMap.dcMotorController.get("c1");
        wheelie = hardwareMap.dcMotorController.get("c2");

        motor2.setDirection(DcMotor.Direction.REVERSE);

        motor1.setMode(DcMotorController.RunMode.RUN_USING_ENCODERS);
        motor2.setMode(DcMotorController.RunMode.RUN_USING_ENCODERS);

        /////////////////////////////Servos////////////////////////////
        red = hardwareMap.servo.get("sr3");// 3
        blue = hardwareMap.servo.get("sr1");// 1
        arm1 = hardwareMap.servo.get("sr5");// 6

        red.setPosition(0.91); //initialize red arm
        blue.setPosition(0.02); //initialize blue arm
        arm1.setPosition(0.85); //initialize hang arm1, higher servo

        //////////////////////////////Sensors///////////////////////////
        reflectedLight = hardwareMap.lightSensor.get("s3"); //s3
        ultrasonicLeft = hardwareMap.ultrasonicSensor.get("s4"); //s4
        ultrasonicRight = hardwareMap.ultrasonicSensor.get("s5"); //s5
        touch1 = hardwareMap.touchSensor.get("t1"); //D 0
    }
}
