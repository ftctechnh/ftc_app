package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.technicbots.MainRobot;
import android.hardware.Sensor;

public class _ResQAuto extends LinearOpMode {
    @Override
    public void runOpMode() throws InterruptedException {
        DcMotor rightWheel;
        DcMotor leftWheel;
        rightWheel = hardwareMap.dcMotor.get("rightwheel");
        leftWheel = hardwareMap.dcMotor.get("leftwheel");
        rightWheel.setDirection(DcMotor.Direction.REVERSE);

        ColorSensor colorSensor;
        colorSensor = hardwareMap.colorSensor.get("color");
        waitForStart();

        MainRobot.moveStraight(150.0, 0.9, true);
        MainRobot.turn(60);//needs to be written(imported from justin)
        MainRobot.moveStraight(80.0, 0.9, true);
        MainRobot.lineFollower(50);

        colorSensor.enableLed(false);
        if (colorSensor.blue()>100){
            //press this one
        } else if (colorSensor.red()>100){
            //move servo, press other
        }
        //drive out of way
        MainRobot.turn(90);
        MainRobot.moveStraight(40.0,0.9,true);

    }
}