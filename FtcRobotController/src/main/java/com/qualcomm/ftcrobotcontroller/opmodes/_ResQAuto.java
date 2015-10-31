package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
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
        waitForStart();
        MainRobot.moveStraight(150.0, 0.9, true);
        MainRobot.turn(60);
        MainRobot.moveStraight(80.0, 0.9, true);
        MainRobot.lineFollower(50);

    }
}