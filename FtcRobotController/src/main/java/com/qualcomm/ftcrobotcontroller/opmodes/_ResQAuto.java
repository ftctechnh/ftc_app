package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.OpticalDistanceSensor;
import com.technicbots.MainRobot;
import android.hardware.Sensor;

public class _ResQAuto extends LinearOpMode {
    @Override
    public void runOpMode() throws InterruptedException {
        DcMotor rightWheel;
        DcMotor leftWheel;
        OpticalDistanceSensor opticalDistanceSensor;
        double reflectance = 0;
        final double TARGET_REFLECTANCE = 0.2;
        final double BLACKVALUE = 0.02;
        final double WHITEVALUE = 0.64;
        final double EOPDThreshold = 0.5 * (BLACKVALUE + WHITEVALUE);
        final double POWER = 0.3;
        final double BASEPOWER = 0.2;
        double value;


        rightWheel = hardwareMap.dcMotor.get("rightwheel");
        leftWheel = hardwareMap.dcMotor.get("leftwheel");
        opticalDistanceSensor = hardwareMap.opticalDistanceSensor.get("sensor_EOPD");
        leftWheel.setDirection(DcMotor.Direction.REVERSE);

        waitForStart();
        //rightWheel.setChannelMode(DcMotorController.RunMode.RESET_ENCODERS);
        //rightWheel.setChannelMode(DcMotorController.RunMode.RUN_USING_ENCODERS);
       // leftWheel.setTargetPosition(5000);
        // rightWheel.setChannelMode(DcMotorController.RunMode.RUN_TO_POSITION);

        reflectance = opticalDistanceSensor.getLightDetected();
        telemetry.addData("Reflectance Value", reflectance);
        while(reflectance < TARGET_REFLECTANCE) {
            telemetry.addData("Reflectance Value", reflectance);
            reflectance = opticalDistanceSensor.getLightDetected();
            leftWheel.setPower(0.5);
            rightWheel.setPower(0.5);
            waitForNextHardwareCycle();
        }

        if (reflectance > EOPDThreshold) {
            value = reflectance-EOPDThreshold;
            leftWheel.setPower ((BASEPOWER+POWER*value));
            rightWheel.setPower((BASEPOWER-POWER*value));
        } else {
            value = EOPDThreshold-reflectance;
            leftWheel.setPower((BASEPOWER-POWER*value));
            rightWheel.setPower((BASEPOWER+POWER*value));
        }


        //  while (rightWheel.getCurrentPosition()<rightWheel.getTargetPosition()) {
           // telemetry.addData("Encoder Value", rightWheel.getCurrentPosition());
           // waitForNextHardwareCycle();
  //  }
      //  leftWheel.setPower(0);
      //  rightWheel.setPower(0);


       // rightWheel.setChannelMode(DcMotorController.RunMode.RESET_ENCODERS);
       // rightWheel.setChannelMode(DcMotorController.RunMode.RUN_USING_ENCODERS);
      //  leftWheel.setTargetPosition(1000);

      //  rightWheel.setChannelMode(DcMotorController.RunMode.RUN_TO_POSITION);
      //  while (rightWheel.getCurrentPosition()<rightWheel.getTargetPosition()) {
        //    telemetry.addData("Encoder Value", rightWheel.getCurrentPosition());
          //  waitForNextHardwareCycle();
      //  }

        //MainRobot.moveStraight(150.0, 0.9, true);
        //MainRobot.turn(60);
        //MainRobot.moveStraight(80.0, 0.9, true);
        //MainRobot.lineFollower(50);

    }
}