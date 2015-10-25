package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.technicbots.MainRobot;
import android.hardware.Sensor;

public class _ResQAuto extends LinearOpMode {
   DcMotor rightWheel;
    DcMotor leftWheel;

    //opticalDistanceSensor = hardwareMap.opticalDistanceSensor.get("sensor_EOPD");
    //rightWheel.setDirection(DcMotor.Direction.REVERSE);
   // MainRobot MainRobot;

    @Override
    public void runOpMode() throws InterruptedException {
        rightWheel = hardwareMap.dcMotor.get("rightwheel");
        leftWheel = hardwareMap.dcMotor.get("leftwheel");
        rightWheel.setDirection(DcMotor.Direction.REVERSE);
        waitForStart();
        rightWheel.setChannelMode(DcMotorController.RunMode.RESET_ENCODERS);
        rightWheel.setChannelMode(DcMotorController.RunMode.RUN_USING_ENCODERS);
        rightWheel.setTargetPosition(5000);
        leftWheel.setPower(-0.9);
        rightWheel.setPower(-0.9);
        rightWheel.setChannelMode(DcMotorController.RunMode.RUN_TO_POSITION);

        while (rightWheel.getCurrentPosition()<rightWheel.getTargetPosition()) {
            telemetry.addData("Encoder Value", rightWheel.getCurrentPosition());
            waitForNextHardwareCycle();
    }
        leftWheel.setPower(0);
        rightWheel.setPower(0);
        //MainRobot.moveStraight(150.0, 0.9, true);
        //MainRobot.turn(60);
        //MainRobot.moveStraight(80.0, 0.9, true);
        //MainRobot.lineFollower(50);

    }
}